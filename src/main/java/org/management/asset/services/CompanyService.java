package org.management.asset.services;

import org.management.asset.bo.Company;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface CompanyService {

    Company saveCompany(Company company);

    boolean deleteCompany(Long id);

    Company getCompany(Long id);

    Company getCompany(String name);

    List<Company> getCompanies();

}
