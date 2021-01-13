package com.softserve.itacademy.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "UQ_user_email"))
public class User  {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    @GenericGenerator(
            name = "user-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Positive(message = "User id must be positive")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Email name cannot be blank")
    @Email(message = "Please provide valid email address")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "First name cannot be blank")
    @Pattern.List({
            @Pattern(regexp = "(^[A-Z][A-Za-z\\-]+)", message = "First name can contain only letters and dashes ('-')"),
            @Pattern(regexp = "\\b(\\w+)-*(?!\\1\\b)\\w+", message = "First name cannot contain repeating parts", flags = Pattern.Flag.CASE_INSENSITIVE)
    })
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name cannot be blank")
    @Pattern.List({
            @Pattern(regexp = "(^[A-Z][A-Za-z\\-]+)", message = "Last name can contain only letters and dashes ('-')"),
            @Pattern(regexp = "\\b(\\w+)-*(?!\\1\\b)\\w+", message = "Last name cannot contain repeating parts", flags = Pattern.Flag.CASE_INSENSITIVE)
    })
    private String lastName;

    @Column(nullable = false)
    @Size(min = 6, max = 255, message = "Password length must be between 6 and 255 chars")
    @NotNull(message = "Password cannot be null")
    private String password;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "FK_role_on_user"))
    @NotNull(message = "User role cannot be null")
    private Role role;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDo> todos = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "collaborators", fetch = FetchType.LAZY)
    private List<ToDo> collaborations = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
