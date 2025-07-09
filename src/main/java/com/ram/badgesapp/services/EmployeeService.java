package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Employee;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final BadgeRepo badgeRepo;
    private final CompanyService companyService;
    public EmployeeService(EmployeeRepo employeeRepo, BadgeRepo badgeRepo, CompanyService companyService) {
        this.employeeRepo = employeeRepo;
        this.badgeRepo = badgeRepo;
        this.companyService = companyService;
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }

    public Employee updateEmployee(Long id, Employee employee){
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        if(employee.getFirstName() != null){
            emp.setFirstName(employee.getFirstName());
        }
        if(employee.getLastName() != null){
            emp.setLastName(employee.getLastName());
        }
        if(employee.getEmail() != null){
            emp.setEmail(employee.getEmail());
        }
        if(employee.getPhone() != null){
            emp.setPhone(employee.getPhone());
        }
        if(employee.getMatricule() != null){
            emp.setMatricule(employee.getMatricule());
        }
        if(employee.getStatus() != null){
            emp.setStatus(employee.getStatus());
        }

        employeeRepo.save(emp);
        return emp;


    }

    public Employee updateEmployeeStatus(Long id, Employee employee){
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        if(employee.getStatus() != null){
            emp.setStatus(employee.getStatus());
        }
        employeeRepo.save(emp);
        return emp;
    }


    public Employee removeBadgeFromEmployee(Long id){
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        emp.setBadge(null);
        return employeeRepo.save(emp);
    }
    public Employee addOrUpdateEmployeeBadge(Long id, Long idBadge){
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        Badge badge = badgeRepo.findById(idBadge)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + idBadge));
        emp.setBadge(badge);
        return employeeRepo.save(emp);
    }

    public Employee addOrUpdateEmployeeCompany(Long id, Long idComp){
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        Company company = companyService.getCompanyById(idComp);
        emp.setCompany(company);
        return employeeRepo.save(emp);
    }


}
