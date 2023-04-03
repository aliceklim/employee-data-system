package EmployeeDataSystem.mappers;

import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.exceptions.EdsException;
import EmployeeDataSystem.model.Role;
import EmployeeDataSystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository repository;

    public Role toRole(RoleType roleType) {
        return repository.findByRole(roleType)
                .orElseThrow(() -> new EdsException("Error! Invalid role!"));
    }

    public RoleType toRoleType(Role role) {
        return role.getRole();
    }
}
