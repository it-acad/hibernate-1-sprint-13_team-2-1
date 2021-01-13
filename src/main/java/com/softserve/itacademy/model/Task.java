package com.softserve.itacademy.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tasks")
public class Task {
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(generator = "task-sequence-generator")
	@GenericGenerator(
			name = "task-sequence-generator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "task_sequence"),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@Positive(message = "Task id must be positive")
	private Long id;

	@Column(nullable = false)
	@Size(min = 3, max = 200)
	@NotNull(message = "Task name cannot be null")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(length = 6, nullable = false)
	@NotNull(message = "Task priority cannot be null")
	private Priority priority;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "todo_id", nullable = false, foreignKey = @ForeignKey(name = "FK_todo_on_task"))
	private ToDo todo;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", nullable = false, foreignKey = @ForeignKey(name = "FK_state_on_task"))
	private State state;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return Objects.equals(id, task.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
