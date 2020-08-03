package org.management.asset.services;

import org.management.asset.bo.Language;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface LanguageService {

    Language saveLanguage(Language language);

    boolean deleteLanguage(String id);

    Language getLanguage(String id);

    Language getLanguageByName(String name);

    List<Language> getLanguages();

    Integer getLanguagesCounter();

}
