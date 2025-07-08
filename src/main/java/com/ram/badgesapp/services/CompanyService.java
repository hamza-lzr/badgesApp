package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.repos.CompanyRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;
    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Company not found with id: "+ id));
    }

    public Company createCompany(Company company) {
        return companyRepo.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

    public Company updateCompany(Long id, Company company) {
        Company comp = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        if (company.getName() != null) {
            comp.setName(company.getName());
        }
        if (company.getAddress() != null) {
            comp.setAddress(company.getAddress());
        }
        if (company.getPhone() != null) {
            comp.setPhone(company.getPhone());
        }
        if (company.getDescription() != null) {
            comp.setDescription(company.getDescription());
        }

        companyRepo.save(comp);
        return comp;
    }

}
