package org.management.asset.services;

import org.management.asset.bo.ClassificationDictSettings;

import java.util.List;

public interface ClassificationDictSettingsService {

    ClassificationDictSettings saveClassificationDictSettings(ClassificationDictSettings classificationDictSettings);

    boolean deleteClassificationDictSettings(String id);

    ClassificationDictSettings getClassificationDictSettings(String id);

    List<ClassificationDictSettings> getClassificationDictSettingsList();

}