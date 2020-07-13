package org.management.asset.services.impl;

import org.management.asset.bo.Company;
import org.management.asset.dao.CompanyRepository;
import org.management.asset.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    @Override
    public boolean deleteCompany(Long id) {
        this.companyRepository.deleteById(id);
        return !this.companyRepository.findById(id).isPresent();
    }

    @Override
    public Company getCompany(Long id) {
        return this.companyRepository.findById(id).orElse(null);
    }

    @Override
    public Company getCompany(String name) {
        return this.companyRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Company> getCompanies() {
        return this.companyRepository.findAll();
    }
}
