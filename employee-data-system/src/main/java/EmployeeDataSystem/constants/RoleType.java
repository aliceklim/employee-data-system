package EmployeeDataSystem.constants;

import EmployeeDataSystem.exceptions.EdsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_ADMIN("ADMIN"), ROLE_USER("USER");

    private final String roleName;

    public static RoleType getRole(String name){
        return Arrays.stream(values()).filter(roleType -> roleType.roleName.equals(name)).findFirst()
                .orElseThrow(() -> new EdsException("Invalid role", HttpStatus.BAD_REQUEST));
    }
}
