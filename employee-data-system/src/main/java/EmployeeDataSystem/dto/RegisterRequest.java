package EmployeeDataSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    @Email
    @NotBlank
    private String email;

    @JsonProperty(value = "password1", required = true)
    private String password;

    @JsonProperty(value = "password2", required = true)
    private String confirmPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate birthdate;
}
