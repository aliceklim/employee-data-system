package EmployeeDataSystem.security;

import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class EmployeeDetails implements UserDetails {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String password;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    private List<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return lastName + " " + firstName;
    }

    public static EmployeeDetails build(EmployeeDTO employeeDTO) {
        return EmployeeDetails.builder()
                .id(employeeDTO.getId())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .password(employeeDTO.getPassword())
                .authorities(employeeDTO.getRoles().stream()
                        .map(RoleType::getRoleName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
