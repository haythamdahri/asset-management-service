package org.management.asset.services.impl;

import org.management.asset.bo.Setting;
import org.management.asset.dao.SettingRepository;
import org.management.asset.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Setting saveSetting(Setting setting) {
        return this.settingRepository.save(setting);
    }

    @Override
    public boolean deleteSetting(String id) {
        this.settingRepository.deleteById(id);
        return !this.settingRepository.findById(id).isPresent();
    }

    @Override
    public Setting getSetting(String id) {
        return this.settingRepository.findById(id).orElse(null);
    }

    @Override
    public Setting getActiveSetting() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "identificationDate"));
        return this.mongoOperations.findOne(query, Setting.class);
    }

    @Override
    public List<Setting> getSettings() {
        return this.settingRepository.findAll();
    }
}
