package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Setting;

public interface SettingService {

	public int saveSetting(Setting setting);

	public Setting getSettingByID(Integer id);

	public List<Setting> getSettings(Integer mediaId);

	public Map<String, String> getSettings(Integer mediaId, String objectType, String objectKey);

	public List<Setting> getSettings(List<Integer> ids);

	public int deleteSetting(Integer id);

	public int updateSetting(Setting setting);
}
