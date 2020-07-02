package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Notification;

public interface NotificationService {
	
	public Integer doNotify(Notification notification);

	public Map<String, Integer> getNotificationsCount(Integer userId);
	
	public void refreshNotifications(Integer userId);
	
	public List<Notification> getNotifications(Integer userId, Integer notifyType);
	
}
