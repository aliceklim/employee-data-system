package EmployeeDataSystem.constants;

import EmployeeDataSystem.exceptions.EdsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EmployeeType {
    MANAGER("MANAGER"), SPECIALIST("SPECIALIST");

    private final String employeeName;

    public static EmployeeType getEmployeeName(String name){
        return Arrays.stream(values()).filter(employeeType -> employeeType.employeeName.equals(name)).findFirst()
                .orElseThrow(() -> new EdsException("Invalid employee type", HttpStatus.BAD_REQUEST));
    }
}
