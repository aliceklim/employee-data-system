package EmployeeDataSystem.services;

import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.dto.LoginRequest;
import EmployeeDataSystem.dto.RegisterRequest;
import EmployeeDataSystem.mappers.EmployeeMapper;
import EmployeeDataSystem.repository.EmployeeRepository;
import EmployeeDataSystem.security.EmployeeDetails;
import EmployeeDataSystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public String login(LoginRequest request) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication =
                authenticationProvider.authenticate(token);

        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Employee " + employeeDetails.getId() + " has logged in");
        return buildJwtToken(employeeDetails);
    }

    private String buildJwtToken(EmployeeDetails employeeDetails) {
        return jwtService.generateJwtToken(employeeDetails);
    }

    public void createEmployee(RegisterRequest request) {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .birthDate(request.getBirthdate())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(RoleType.ROLE_USER))
                .creationDate(LocalDateTime.now())
                .build();
        employeeRepository.save(employeeMapper.toEmployee(employeeDTO));
        log.info("Employee " + employeeDTO.getFirstName() + " " + employeeDTO.getLastName() + " is created");
    }
}
