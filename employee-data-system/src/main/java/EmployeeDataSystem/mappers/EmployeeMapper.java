package EmployeeDataSystem.mappers;

import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.model.Employee;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        uses = {RoleSetMapper.class})
public interface EmployeeMapper {
   // @Mapping(target = "password", ignore = true)
    EmployeeDTO toDTO (Employee employee);
    Employee toEmployee (EmployeeDTO employeeDTO);

    EmployeeDTO employeeDTO(Employee employee);
}
