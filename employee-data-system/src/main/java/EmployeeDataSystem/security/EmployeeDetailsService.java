package EmployeeDataSystem.security;

import EmployeeDataSystem.mappers.EmployeeMapper;
import EmployeeDataSystem.model.Employee;
import EmployeeDataSystem.repository.EmployeeRepository;
import EmployeeDataSystem.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        return employee.map(employeeMapper::toDTO)
                .map(EmployeeDetails::build)
                .orElseThrow(() -> new UsernameNotFoundException("Error! Customer not found!"));
    }
}
