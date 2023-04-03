package EmployeeDataSystem.services;

import EmployeeDataSystem.constants.DepartmentType;
import EmployeeDataSystem.constants.EmployeeType;
import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.dto.UpdEmployeeRequest;
import EmployeeDataSystem.dto.UpdMyDataRequest;
import EmployeeDataSystem.exceptions.EdsException;
import EmployeeDataSystem.mappers.EmployeeMapper;
import EmployeeDataSystem.mappers.RoleSetMapper;
import EmployeeDataSystem.model.Employee;
import EmployeeDataSystem.model.Role;
import EmployeeDataSystem.repository.EmployeeRepository;
import EmployeeDataSystem.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private EmployeeMapper employeeMapper;
    private RoleSetMapper roleSetMapper;
    private  EmployeeService employeeService;

    @BeforeEach
    void setUp(){
        employeeRepository = mock(EmployeeRepository.class);
        roleRepository = mock(RoleRepository.class);
        employeeMapper = mock(EmployeeMapper.class);
        roleSetMapper = mock(RoleSetMapper.class);
        employeeService = new EmployeeService(employeeRepository, roleRepository, employeeMapper, roleSetMapper);
    }

    @Test
    void getsAllEmployees() {
        employeeService.getAllEmployees();
        verify(employeeRepository).findAll();
    }
    @Test
    public void returnsMyInfo() {
        // Given
        String email = "test@test.com";
        Long id = 1L;
        Employee employee = new Employee();
        employee.setEmail(email);
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail(email);
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        // When
        EmployeeDTO result = employeeService.getMyInfo(email, id);

        // Then
        assertEquals(employeeDTO, result);
        verify(employeeRepository).findByEmail(email);
    }

    @Test
    void throwsExceptionForEditMyData() {
        // Given
        Long id = 1L;
        String email = "test@test.com";
        UpdMyDataRequest updMyDataRequest = new UpdMyDataRequest("John", "Doe", null);
        Employee employee = new Employee(id, "Doe", "John",  null,
                null, null, 100, null, null, null, email);

        // When
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        // Then
        Assertions.assertThrows(EdsException.class, () -> employeeService.editMyData(2L, updMyDataRequest, email));
    }

    @Test
    public void deletsEmployee() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // When
        employeeService.deleteEmployee(id);

        // Then
        verify(employeeRepository).findById(id);
        verify(employeeRepository).delete(employee);
    }

    @Test
    public void getsMyInfo() {
        // Given
        String email = "test@test.com";
        Long id = 1L;
        Employee employee = new Employee();
        employee.setEmail(email);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO();
        expectedEmployeeDTO.setEmail(email);

        // When
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(expectedEmployeeDTO);
        EmployeeDTO actualEmployeeDTO = employeeService.getMyInfo(email, id);

        // Then
        assertEquals(expectedEmployeeDTO, actualEmployeeDTO);
    }

    @Test
    public void deletsAdminRole() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setRoles(roleSetMapper.toRoleSet(Collections.singleton(RoleType.ROLE_ADMIN)));
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(id);
        updatedEmployee.setRoles(roleSetMapper.toRoleSet(Collections.singleton(RoleType.ROLE_USER)));
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO();
        expectedEmployeeDTO.setId(id);
        expectedEmployeeDTO.setRoles(Collections.singleton(RoleType.ROLE_USER));

        // When
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(employeeMapper.toDTO(updatedEmployee)).thenReturn(expectedEmployeeDTO);
        EmployeeDTO actualEmployeeDTO = employeeService.deleteAdminRole(id);

        // Then
        assertEquals(expectedEmployeeDTO, actualEmployeeDTO);
    }
    @Test
    public void setsAdminRole() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setRoles(roleSetMapper.toRoleSet(Collections.singleton(RoleType.ROLE_USER)));
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(id);
        updatedEmployee.setRoles(roleSetMapper.toRoleSet(new HashSet<>(Arrays.asList(RoleType.ROLE_USER, RoleType.ROLE_ADMIN))));
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO();
        expectedEmployeeDTO.setId(id);
        expectedEmployeeDTO.setRoles(new HashSet<>(Arrays.asList(RoleType.ROLE_USER, RoleType.ROLE_ADMIN)));

        // When
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(roleRepository.findByRole(RoleType.ROLE_ADMIN)).thenReturn(Optional.of(new Role(RoleType.ROLE_ADMIN)));
        willReturn(updatedEmployee).given(employeeRepository).save(any(Employee.class));
        when(employeeMapper.toDTO(updatedEmployee)).thenReturn(expectedEmployeeDTO);
        EmployeeDTO actualEmployeeDTO = employeeService.setAdminRole(id);

        // Then
        assertEquals(expectedEmployeeDTO, actualEmployeeDTO);
    }

    @Test
    public void testGetEmployeeById() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO();
        expectedEmployeeDTO.setId(id);

        // When
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(expectedEmployeeDTO);
        EmployeeDTO actualEmployeeDTO = employeeService.getEmployeeById(id);

        // Then
        assertEquals(expectedEmployeeDTO, actualEmployeeDTO);
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeMapper, times(1)).toDTO(employee);
    }

}