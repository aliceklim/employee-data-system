package EmployeeDataSystem.dto;

import EmployeeDataSystem.constants.DepartmentType;
import EmployeeDataSystem.constants.EmployeeType;
import EmployeeDataSystem.constants.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdEmployeeRequest {

    private DepartmentType department;

    private EmployeeType employeeType;

    private Double salary;

    private Set<RoleType> roles;
}
