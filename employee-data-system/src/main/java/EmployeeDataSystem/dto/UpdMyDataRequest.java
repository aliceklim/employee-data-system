package EmployeeDataSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdMyDataRequest {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

}
