package EmployeeDataSystem.services;

import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.dto.LoginRequest;
import EmployeeDataSystem.dto.RegisterRequest;
import EmployeeDataSystem.mappers.EmployeeMapper;
import EmployeeDataSystem.model.Employee;
import EmployeeDataSystem.repository.EmployeeRepository;
import EmployeeDataSystem.security.EmployeeDetails;
import EmployeeDataSystem.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void returnsTokenWhenLogsIn() {
        // Given
        LoginRequest request = new LoginRequest("test@test.com", "password");
        Authentication authentication = mock(Authentication.class);
        EmployeeDetails employeeDetails = EmployeeDetails.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .password("password")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.getRoleName())))
                .build();

        // When
        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(employeeDetails);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        when(jwtService.generateJwtToken(any(EmployeeDetails.class))).thenReturn(token);
        String result = authService.login(request);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    @Test
    public void createsEmployeeWhenRegisters() {
        // Given
        LocalDateTime creationTime = LocalDateTime.now();
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setConfirmPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthdate(LocalDate.of(1990, 1, 1));

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthdate())
                .creationDate(creationTime)
                .roles(Collections.singleton(RoleType.ROLE_USER))
                .build();

        // When
        authService.createEmployee(request);
        ArgumentCaptor<Employee> argumentCaptor =  ArgumentCaptor.forClass(Employee.class);
        Employee employee = employeeMapper.toEmployee(employeeDTO);

        verify(employeeRepository).save(argumentCaptor.capture());

        Employee capturedEmployee = argumentCaptor.getValue();

        authService.createEmployee(request);

        // Then
        assertThat(capturedEmployee).isEqualTo(employeeDTO);
        assertThat(capturedEmployee.getRoles().contains(RoleType.ROLE_USER));
        assertThat(capturedEmployee.getCreationDate().equals(creationTime));
        assertEquals(request.getFirstName(), employeeDTO.getFirstName());
        assertEquals(request.getLastName(), employeeDTO.getLastName());
        assertEquals(request.getEmail(), employeeDTO.getEmail());
        assertEquals(request.getBirthdate(), employeeDTO.getBirthDate());
        assertTrue(passwordEncoder.matches(request.getPassword(), employeeDTO.getPassword()));
    }
}