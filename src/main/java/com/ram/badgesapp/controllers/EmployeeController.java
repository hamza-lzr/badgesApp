package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.EmployeeDTO;
import com.ram.badgesapp.entities.Employee;
import com.ram.badgesapp.mapper.EmployeeMapper;
import com.ram.badgesapp.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees().stream()
                .map(employeeMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeMapper.toDTO(employeeService.getEmployeeById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully with id: " + id);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee saved = employeeService.createEmployee(employee);
        return ResponseEntity.ok(employeeMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee updated = employeeService.updateEmployee(id,employee);
        return ResponseEntity.ok(employeeMapper.toDTO(updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeeStatusById(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee updated = employeeService.updateEmployeeStatus(id,employee);
        return ResponseEntity.ok(employeeMapper.toDTO(updated));
    }
    @DeleteMapping("/{id}/badge")
    public ResponseEntity<EmployeeDTO> removeBadgeFromEmployeeById(@PathVariable Long id) {
        Employee updated = employeeService.removeBadgeFromEmployee(id);
        return ResponseEntity.ok(employeeMapper.toDTO(updated));
    }
    @PutMapping("/{id}/badge")
    public ResponseEntity<EmployeeDTO> addOrUpdateEmployeeBadgeById(@PathVariable Long id, @RequestBody Long idBadge) {
        Employee updated = employeeService.addOrUpdateEmployeeBadge(id,idBadge);
        return ResponseEntity.ok(employeeMapper.toDTO(updated));
    }

    @PutMapping("/{id}/company")
    public ResponseEntity<EmployeeDTO> addOrUpdateEmployeeCompanyById(@PathVariable Long id, @RequestBody Long idBadge){
        Employee updated = employeeService.addOrUpdateEmployeeCompany(id,idBadge);
        return ResponseEntity.ok(employeeMapper.toDTO(updated));
    }
}
