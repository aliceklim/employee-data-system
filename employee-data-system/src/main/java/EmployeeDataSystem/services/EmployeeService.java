package EmployeeDataSystem.services;

import EmployeeDataSystem.constants.RoleType;
import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.dto.UpdEmployeeRequest;
import EmployeeDataSystem.dto.UpdMyDataRequest;
import EmployeeDataSystem.exceptions.EdsException;
import EmployeeDataSystem.mappers.EmployeeMapper;
import EmployeeDataSystem.mappers.RoleSetMapper;
import EmployeeDataSystem.model.Employee;
import EmployeeDataSystem.repository.EmployeeRepository;
import EmployeeDataSystem.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleSetMapper roleSetMapper;

    @Transactional
    public EmployeeDTO editMyData(Long id, UpdMyDataRequest updMyDataRequest, String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.get().getId() == id){
            log.info("Employee " + id + " updated");
            return employee.map(empl -> updateMyInfo(empl, updMyDataRequest))
                    .map(employeeRepository::save)
                    .map(employeeMapper::toDTO)
                    .get();
        } else {
            throw new EdsException("You can't edit other employee data");
        }
    }

    public EmployeeDTO getMyInfo(String email, Long id){
        log.info("Employee " + id + " gets own info");
        return employeeRepository.findByEmail(email)
                .map(employeeMapper::toDTO)
                .orElseThrow(() -> new EdsException("Employee not found"));
    }

    @Transactional
    public EmployeeDTO editEmployee(Long id, UpdEmployeeRequest updEmployeeDTO) {
        log.info("Employee " + id + " is updated");
        return employeeRepository.findById(id)
                .map(employee -> updateEmployeeInfo(employee, updEmployeeDTO))
                .map(employeeRepository::save)
                .map(employeeMapper::toDTO)
                .orElseThrow(() -> new EdsException("Employee with the id " + id + " not found"));
    }

    public void deleteEmployee(Long id) {
        log.info("Employee " + " " + id + " is deleted");
        Optional<Employee> employee = employeeRepository.findById(id);
        employee.ifPresent(employeeRepository::delete);
    }

    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Employee " + id + " is returned by id");
        return employeeMapper.toDTO(employeeRepository.findById(id).get());
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Employee updateEmployeeInfo(Employee employee, UpdEmployeeRequest updEmployeeDTO) {
        employee.setEmployeeType(updEmployeeDTO.getEmployeeType());
        employee.setDepartment(updEmployeeDTO.getDepartment());
        employee.setSalary(updEmployeeDTO.getSalary());
        employee.setRoles(roleSetMapper.toRoleSet(updEmployeeDTO.getRoles()));
        return employee;
    }

    private Employee updateMyInfo(Employee employee, UpdMyDataRequest updMyDataRequest){
        employee.setFirstName(updMyDataRequest.getFirstName());
        employee.setLastName(updMyDataRequest.getLastName());
        employee.setBirthDate(updMyDataRequest.getBirthDate());
        return employee;
    }

    @Transactional
    public EmployeeDTO setAdminRole(Long id){
        log.info("Employee " + id + " is given ADMIN role");
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EdsException("Employee with the id " + id + " not found", HttpStatus.BAD_REQUEST));
        employee.getRoles().add(roleRepository.findByRole(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new EdsException("Role not found")));
        EmployeeDTO employeeDTO = employeeMapper.toDTO(employeeRepository.save(employee));
        return employeeDTO;
    }

    public EmployeeDTO deleteAdminRole(Long id){
        log.info("Employee " + id + " now has only USER role");
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EdsException("Employee with the id " + id + " not found", HttpStatus.BAD_REQUEST));

        employee.setRoles(employee.getRoles().stream()
                .filter(role -> !role.equals(RoleType.ROLE_ADMIN)).collect(Collectors.toSet()));
        EmployeeDTO employeeDTO = employeeMapper.toDTO(employeeRepository.save(employee));
        return employeeDTO;
    }
}

