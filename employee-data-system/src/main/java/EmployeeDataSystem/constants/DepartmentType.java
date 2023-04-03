package EmployeeDataSystem.constants;

import EmployeeDataSystem.exceptions.EdsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DepartmentType {
    HR("HR"), SALES("SALES"), MARKETING("MARKETING"),
    DEV("DEV"), LEGAL("LEGAL");

    private final String departmentName;

    public static DepartmentType getDepartment(String name){
        return Arrays.stream(values()).filter(department -> department.departmentName.equals(name)).findFirst()
                .orElseThrow(() -> new EdsException("Invalid department name", HttpStatus.BAD_REQUEST));
    }
}
