package org.management.asset.services;

import org.management.asset.bo.Country;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface CountryService {

    Country saveCountry(Country country);

    boolean deleteCountry(Long id);

    Country getCountry(Long id);

    Country getCountry(String name);

    List<Country> getCountries();

}
