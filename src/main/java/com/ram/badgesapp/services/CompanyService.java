package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.repos.CompanyRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;
    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company getCompany(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Company not found with id: "+ id));
    }

    public Company createCompany(Company company) {
        return companyRepo.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

    public  Company updateCompany(Long id, Company company) {
        Optional<Company> currentCompany = companyRepo.findById(id);
        if (currentCompany.isPresent()) {
            Company currentCompanyEntity = currentCompany.get();
            currentCompanyEntity.setName(company.getName());
            currentCompanyEntity.setDescription(company.getDescription());
            currentCompanyEntity.setAddress(company.getAddress());
            currentCompanyEntity.setPhone(company.getPhone());
            companyRepo.save(currentCompanyEntity);
        }
        return company;
    }
}
