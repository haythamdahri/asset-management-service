package org.management.asset.services;

import org.management.asset.bo.Setting;
import org.management.asset.dto.SettingRequestDTO;

import java.util.List;

public interface SettingService {

    Setting saveSetting(Setting setting);

    Setting saveSetting(SettingRequestDTO settingRequest);

    boolean deleteSetting(String id);

    Setting getSetting(String id);

    Setting getActiveSetting();

    List<Setting> getSettings();

}