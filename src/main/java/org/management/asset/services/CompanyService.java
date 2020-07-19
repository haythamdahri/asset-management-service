package org.management.asset.services;

import org.management.asset.bo.Company;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface CompanyService {

    Company saveCompany(Company company);

    boolean deleteCompany(String id);

    Company getCompany(String id);

    Company getCompanyByName(String name);

    List<Company> getCompanies();

}
