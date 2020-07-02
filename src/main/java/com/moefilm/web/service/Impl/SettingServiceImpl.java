package com.moefilm.web.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.SettingMapper;
import com.moefilm.web.model.Setting;
import com.moefilm.web.service.SettingService;

@Service("settingService")
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingMapper settingMapper;

	public int saveSetting(Setting setting) {
		Setting entity = new Setting();
		entity.setMediaId(setting.getMediaId());
		entity.setObjectType(setting.getObjectType());
		entity.setObjectKey(setting.getObjectKey());
		entity.setSettingName(setting.getSettingName());
		Setting exist = settingMapper.selectOne(entity);
		if (exist != null && exist.getId() > 0) {
			setting.setUpdateTime(new Date());
			return settingMapper.updateById(setting);
		} else {
			return settingMapper.insert(setting);
		}
	}

	public Setting getSettingByID(Integer id) {
		return settingMapper.selectById(id);
	}

	public List<Setting> getSettings(Integer mediaId) {
		EntityWrapper<Setting> wrapper = new EntityWrapper<>();
		wrapper.eq("media_id", mediaId);
		return settingMapper.selectList(wrapper);
	}

	public Map<String, String> getSettings(Integer mediaId, String objectType, String objectKey) {
		EntityWrapper<Setting> wrapper = new EntityWrapper<>();
		wrapper.eq("media_id", mediaId);
		wrapper.eq("object_type", objectType);
		wrapper.eq("object_key",objectKey);
		List<Setting> settings = settingMapper.selectList(wrapper);
		Map<String, String> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(settings)) {
			for (Setting setting : settings) {
				map.put(setting.getSettingName(), setting.getSettingValue());
			}
		}
		return map;
	}

	public List<Setting> getSettings(List<Integer> ids) {
		EntityWrapper<Setting> wrapper = new EntityWrapper<>();
		wrapper.in("id", ids);
		wrapper.orderBy("ts", false);
		return settingMapper.selectList(wrapper);
	}

	public int deleteSetting(Integer id) {
		return settingMapper.deleteById(id);
	}

	public int updateSetting(Setting setting) {
		Setting entity = new Setting();
		entity.setSettingValue(setting.getSettingValue());
		EntityWrapper<Setting> wrapper = new EntityWrapper<>();
		wrapper.eq("media_id", setting.getMediaId());
		wrapper.eq("object_type", setting.getObjectType());
		wrapper.eq("object_key", setting.getObjectKey());
		wrapper.eq("setting_name", setting.getSettingName());
		return settingMapper.update(entity, wrapper);
	}
}
