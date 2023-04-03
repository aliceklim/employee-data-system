package EmployeeDataSystem.dto;

import EmployeeDataSystem.constants.DepartmentType;
import EmployeeDataSystem.constants.EmployeeType;
import EmployeeDataSystem.constants.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotNull
    @JsonProperty(
            required = true
    )
    private String firstName;

    @NotNull
    @JsonProperty(
            required = true
    )
    private String lastName;

    private LocalDate birthDate;

    private DepartmentType department;

    private EmployeeType employeeType;

    private Double salary;

    @JsonIgnore
    private String password;

    @Email
    @NotNull
    @JsonProperty(required = true)
    private String email;

    private LocalDateTime creationDate;
    @NotNull
    @JsonProperty(required = true)
    private Set<RoleType> roles;

}
