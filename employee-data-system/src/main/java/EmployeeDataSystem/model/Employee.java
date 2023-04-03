package EmployeeDataSystem.model;

import EmployeeDataSystem.constants.DepartmentType;
import EmployeeDataSystem.constants.EmployeeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "employee", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_gen")
    @SequenceGenerator(name = "employee_id_gen", sequenceName = "employee_id_seq", allocationSize = 1)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    private DepartmentType department;

    @Column(name = "employee")
    private EmployeeType employeeType;

    private double salary;

    @NotNull
    private String email;

    private LocalDateTime creationDate = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee2role",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @NotNull
    private String password;
}
