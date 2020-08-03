package org.management.asset.services;

import org.management.asset.bo.Setting;

import java.util.List;

public interface SettingService {

    Setting saveSetting(Setting setting);

    boolean deleteSetting(String id);

    Setting getSetting(String id);

    List<Setting> getSettings();

}