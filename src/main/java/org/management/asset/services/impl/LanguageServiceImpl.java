package org.management.asset.services.impl;

import org.management.asset.bo.Language;
import org.management.asset.dao.LanguageRepository;
import org.management.asset.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public Language saveLanguage(Language language) {
        return this.languageRepository.save(language);
    }

    @Override
    public boolean deleteLanguage(String id) {
        this.languageRepository.deleteById(id);
        return !this.languageRepository.findById(id).isPresent();
    }

    @Override
    public Language getLanguage(String id) {
        return this.languageRepository.findById(id).orElse(null);
    }

    @Override
    public Language getLanguageByName(String name) {
        return this.languageRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Language> getLanguages() {
        return this.languageRepository.findAll();
    }
}
