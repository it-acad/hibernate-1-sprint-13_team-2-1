package com.softserve.itacademy.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "states", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "UQ_state_name"))
public class State {
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(generator = "state-sequence-generator")
	@GenericGenerator(
			name = "state-sequence-generator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = "state_sequence"),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@Positive(message = "State id must be positive")
	private Long id;

	@Column(nullable = false)
	@NaturalId
	@Pattern(regexp = "^[ _A-Za-z0-9-]{1,20}", message = "State name con contain only latin letters, digits, spaces, dashes and underscores")
	@NotNull(message = "State name cannot be null")
	private String name;

	@ToString.Exclude
	@OneToMany(mappedBy = "state", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		State state = (State) o;
		return Objects.equals(name, state.name);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
