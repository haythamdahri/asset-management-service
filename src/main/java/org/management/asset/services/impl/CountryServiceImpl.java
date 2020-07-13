package org.management.asset.services.impl;

import org.management.asset.bo.Country;
import org.management.asset.dao.CountryRepository;
import org.management.asset.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Country saveCountry(Country country) {
        return this.countryRepository.save(country);
    }

    @Override
    public boolean deleteCountry(Long id) {
        this.countryRepository.deleteById(id);
        return !this.countryRepository.findById(id).isPresent();
    }

    @Override
    public Country getCountry(Long id) {
        return this.countryRepository.findById(id).orElse(null);
    }

    @Override
    public Country getCountry(String name) {
        return this.countryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Country> getCountries() {
        return this.countryRepository.findAll();
    }
}
