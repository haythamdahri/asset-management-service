package org.management.asset.services.impl;

import org.management.asset.bo.ClassificationDictSettings;
import org.management.asset.dao.ClassificationDictSettingsRepository;
import org.management.asset.services.ClassificationDictSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class ClassificationDictSettingsServiceImpl implements ClassificationDictSettingsService {

    @Autowired
    private ClassificationDictSettingsRepository classificationDictSettingsRepository;

    @Override
    public ClassificationDictSettings saveClassificationDictSettings(ClassificationDictSettings classificationDictSettings) {
        return this.classificationDictSettingsRepository.save(classificationDictSettings);
    }

    @Override
    public boolean deleteClassificationDictSettings(String id) {
        this.classificationDictSettingsRepository.deleteById(id);
        return !this.classificationDictSettingsRepository.findById(id).isPresent();
    }

    @Override
    public ClassificationDictSettings getClassificationDictSettings(String id) {
        return this.classificationDictSettingsRepository.findById(id).orElse(null);
    }

    @Override
    public List<ClassificationDictSettings> getClassificationDictSettingsList() {
        return this.classificationDictSettingsRepository.findAll();
    }
}
