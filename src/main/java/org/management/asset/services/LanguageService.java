package org.management.asset.services;

import org.management.asset.bo.Language;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface LanguageService {

    Language saveLanguage(Language language);

    boolean deleteLanguage(Long id);

    Language getLanguage(Long id);

    Language getLanguage(String name);

    List<Language> getLanguages();

}
