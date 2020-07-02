package com.moefilm.web.service;

import java.util.List;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Relation;

public interface EventService extends BaseService<Event> {
	
	Integer newEvent(Integer objectType, Object obj);
	
	List<Event> getEvents(Integer start, Integer step);
	
	/*
	 * 根据relation关系(object_type, object_id)查询event
	 */
	List<Event> getEventsWithRelations(List<Relation> relations);
	
	/**
	 * 获取含有图片的Event
	 * @param start 
	 * @param step
	 * @return 
	 */
	List<Event> getEventsHasPhoto(Integer start, Integer step);
	
	Event getEvent(Integer eventId);
	
	Event getEvent(Integer objectType, Integer objectId);
	
	/*
	 * 根据event id查询event
	 */
	List<Event> getEventsWithIDs(List<Integer> eventIds);
	
	List<Event> getEventsOfUser(Integer userId, Integer count);
	
	Integer delete(Integer id);
	
	Integer delete(Integer objectType, Integer objectId);
}
