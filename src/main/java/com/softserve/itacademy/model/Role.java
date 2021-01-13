package com.softserve.itacademy.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "UQ_role_name"))
public class Role {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(generator = "role-sequence-generator")
    @GenericGenerator(
            name = "role-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "role_sequence"),
                    @Parameter(name = "initial_value", value = "10"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Positive(message = "Role id must be positive") // or zero when not persisted
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false)
    @NaturalId
    @NotBlank(message = "Role's name cannot be blank")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();


}

