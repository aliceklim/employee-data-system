package EmployeeDataSystem.controllers;

import EmployeeDataSystem.dto.EmployeeDTO;
import EmployeeDataSystem.dto.UpdEmployeeRequest;
import EmployeeDataSystem.dto.UpdMyDataRequest;
import EmployeeDataSystem.security.TokenAuthentication;
import EmployeeDataSystem.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public EmployeeDTO editMyData(@PathVariable Long id, @Valid @RequestBody UpdMyDataRequest updMyDataRequest,
                                  TokenAuthentication token){
        return employeeService.editMyData(id, updMyDataRequest, token.getTokenData().getEmail());
    }

    @PutMapping("/{id}")
    public EmployeeDTO editEmployee(@PathVariable Long id, @RequestBody UpdEmployeeRequest updEmployeeDTO){
        return employeeService.editEmployee(id, updEmployeeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDTO setAdminRole(@PathVariable Long id){
        return employeeService.setAdminRole(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDTO deleteAdminRole(@PathVariable Long id){
        return employeeService.deleteAdminRole(id);
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/myaccount")
    public EmployeeDTO getMyInfo(TokenAuthentication token){
        return employeeService.getMyInfo(token.getTokenData().getEmail(), token.getTokenData().getId());
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees(){
        System.out.println("getAllEmployees");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return employeeService.getAllEmployees();
    }

}
