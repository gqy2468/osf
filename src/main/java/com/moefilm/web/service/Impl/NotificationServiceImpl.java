package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.NotificationMapper;
import com.moefilm.web.model.Event;
import com.moefilm.web.model.Notification;
import com.moefilm.web.model.User;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.NotificationService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
	
	private static final String NOTIFY_KEY = "notification:";
	
	@Autowired
	private NotificationMapper notificationMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Integer> hashOps;
	
	public Integer doNotify(Notification notification){
		Integer id = notificationMapper.insert(notification);
		refreshNotifications(notification.getNotifiedUser());
		return id;
	}
	
	private void initNotification(Map<String, Integer> notifications){
		notifications.put("comment", 0);
		notifications.put("comment_reply", 0);
		notifications.put("follow", 0);
		notifications.put("like", 0);
		notifications.put("system", 0);
	}
	
	private Map<String, Integer> getNotifications(Map<String, Integer> notifications, Integer user_id){
		initNotification(notifications);
		return notifications;
	}

	public Map<String, Integer> getNotificationsCount(Integer userId) {
		Map<String, Integer> notifications = new HashMap<String, Integer>();
		if (!redisTemplate.hasKey(NOTIFY_KEY + userId)) {
			hashOps.putAll(NOTIFY_KEY+userId, getNotifications(notifications, userId));
		} else {
			for (String key: hashOps.keys(NOTIFY_KEY+userId)) {
				notifications.put(key, hashOps.get(NOTIFY_KEY+userId, key));
			}
		}
		return notifications;
	}
	
	public void refreshNotifications(Integer userId){
		Map<String, Integer> notifications = new HashMap<String, Integer>();
		hashOps.putAll(NOTIFY_KEY+userId, getNotifications(notifications, userId));
	}
	
	public List<Notification> getNotifications(Integer userId, Integer notifyType) {
		EntityWrapper<Notification> wrapper = new EntityWrapper<>();
		wrapper.eq("notified_user", userId);
		if (notifyType == DictUtil.NOTIFY_TYPE_COMMENT) {
			List<Integer> notifyTypes = new ArrayList<>();
			notifyTypes.add(DictUtil.NOTIFY_TYPE_COMMENT);
			notifyTypes.add(DictUtil.NOTIFY_TYPE_COMMENT_REPLY);
			wrapper.in("notify_type", notifyTypes);
		} else {
			wrapper.eq("notify_type", notifyType);
		}
		wrapper.orderBy("ts", false);
		List<Notification> notifications = notificationMapper.selectList(wrapper);
		
		if (notifications != null) {
			for (Notification notification : notifications) {
				User user = userService.findById(notification.getNotifier());
				notification.setNotifierName(user.getUserName());
				notification.setNotifierAvatar(user.getUserAvatar());
				
				Event event = eventService.getEvent(notification.getObjectType(), notification.getObjectId());
				
				String objectTitle = null;
				if(DictUtil.OBJECT_TYPE_POST == notification.getObjectType()) {
					objectTitle = event.getTitle();
				} else if(DictUtil.OBJECT_TYPE_SHORTPOST == notification.getObjectType()) {
					objectTitle = event.getSummary();
				} else if(DictUtil.OBJECT_TYPE_ALBUM == notification.getObjectType()){
					objectTitle = event.getSummary();
				} 
				if (objectTitle != null) {
					Integer len = objectTitle.length();
					if(len > 20) {
						objectTitle = objectTitle.substring(0, 20);
					}
				}
				notification.setObjectTitle(objectTitle);
			}
		}
		return notifications;
	}
}
