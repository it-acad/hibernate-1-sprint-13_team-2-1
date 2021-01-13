package com.softserve.itacademy.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "todos")
public class ToDo {
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(generator = "todo-sequence-generator")
	@GenericGenerator(
			name = "todo-sequence-generator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "todo_sequence"),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@Positive(message = "ToDo id must be positive")
	private Long id;

	@Column(nullable = false)
	@NotBlank(message = "ToDo title cannot be blank")
	private String title;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_on_todo"))
	private User owner;

	@ToString.Exclude
	@OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "todo_collaborator",
			joinColumns = @JoinColumn(name = "todo_id", foreignKey = @ForeignKey(name = "FK_todo_on_todo_collaborator")),
			inverseJoinColumns = @JoinColumn(name = "collaborator_id", foreignKey = @ForeignKey(name = "FK_user_on_todo_collaborator"))
	)
	private List<User> collaborators = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToDo toDo = (ToDo) o;
		return Objects.equals(id, toDo.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
