package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moefilm.web.model.*;
import com.moefilm.web.service.AlbumService;
import com.moefilm.web.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.EventMapper;
import com.moefilm.web.util.DictUtil;

@Service("eventService")
public class EventServiceImpl extends BaseServiceImpl<EventMapper, Event> implements EventService{
	
	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private AlbumService albumService;
	
	private Event toEvent(Integer objectType, Object obj) {
		Event event = new Event();
		if(DictUtil.OBJECT_TYPE_POST == objectType) {
			Post post = (Post)obj;
			event.setObjectType(DictUtil.OBJECT_TYPE_POST);
			event.setObjectId(post.getId());
			event.setUserId(post.getPostAuthor());
			event.setTitle(post.getPostTitle());
			event.setSummary(post.getPostExcerpt());
			event.setContent(post.getPostCover());
			event.setLikeCount(post.getLikeCount());
			event.setShareCount(post.getShareCount());
			event.setCommentCount(post.getCommentCount());
			event.setTags(post.getPostTags());
		} else if(DictUtil.OBJECT_TYPE_ALBUM == objectType) {
			Album album = (Album)obj;
			event.setObjectType(DictUtil.OBJECT_TYPE_ALBUM);
			event.setObjectId(album.getId());
			event.setUserId(album.getUserId());
			event.setTitle(album.getCover());
			event.setSummary(album.getAlbumDesc());
			
			List<Photo> photos = album.getPhotos();
			List<Integer> ids = new ArrayList<Integer>();
			for(Photo photo:photos) {
				ids.add(photo.getId());
			}
			List<String> keys = albumService.getKeys(ids);
			StringBuffer buffer = new StringBuffer();
			for(String key: keys) {
				buffer.append(key+":");
			}
			event.setContent(buffer.toString());
			event.setLikeCount(0);
			event.setShareCount(0);
			event.setCommentCount(0);
			event.setTags(album.getAlbumTags());
		} else if(DictUtil.OBJECT_TYPE_PHOTO == objectType) {
		} else if(DictUtil.OBJECT_TYPE_SHORTPOST == objectType){
			ShortPost spost = (ShortPost) obj;
			event.setObjectType(DictUtil.OBJECT_TYPE_SHORTPOST);
			event.setObjectId(spost.getId());
			event.setSummary(spost.getPostContent());
			event.setUserId(spost.getPostAuthor());
			event.setLikeCount(spost.getLikeCount());
			event.setShareCount(spost.getShareCount());
			event.setCommentCount(spost.getCommentCount());
		}
		return event;
	}
	
	public Integer newEvent(Integer objectType, Object obj) {
		Integer event_id = eventMapper.insert(toEvent(objectType, obj));
		return event_id;
	}
	
	/**
	 * 
	 * @param start
	 * @param step
	 * @return
	 */
	public List<Event> getEvents(Integer start, Integer step) {
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		wrapper.last(" limit " + start + "," + step);
		return eventMapper.selectList(wrapper);
	}
	
	
	/*
	 * 根据relation关系(object_type, object_id)查询event
	 */
	public List<Event> getEventsWithRelations(List<Relation> relations) {
		List<Event> events = new ArrayList<Event>();
		if(relations != null && relations.size() != 0) {
			Map<Integer, List<Integer>> category = new HashMap<Integer, List<Integer>>();
			for(Relation relation : relations) {
				if(!category.containsKey(relation.getObjectType())) {
					category.put(relation.getObjectType(), new ArrayList<Integer>());
				}
				category.get(relation.getObjectType()).add(relation.getObjectId());
			}
			events = getEventsWithRelations(category);
		}
		return events;
	}
	
	private List<Event> getEventsWithRelations(Map<Integer, List<Integer>> relationsCategory) {
		Integer i = 0;
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		for(Integer type: relationsCategory.keySet()) {		
			if (i != 0) {
				wrapper.or();
			}
			wrapper.eq("object_type", type);
			wrapper.in("object_id", relationsCategory.get(type));
			i++;
		}
		return eventMapper.selectList(wrapper);
	}
	
	/**
	 * 获取含有图片的Event
	 * @param start 
	 * @param step
	 * @return 
	 */
	public List<Event> getEventsHasPhoto(Integer start, Integer step) {
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		wrapper.where("(object_type={0} and content is not null) "
					 + "or (object_type={1} and title is not null)", 
					 DictUtil.OBJECT_TYPE_POST, 
					 DictUtil.OBJECT_TYPE_ALBUM);
		wrapper.last(" limit " + start + "," + step);
		return eventMapper.selectList(wrapper);
	}
	
	public Event getEvent(Integer eventId){
		return eventMapper.selectById(eventId);
	}
	
	public Event getEvent(Integer objectType, Integer objectId){
		Event entity = new Event();
		entity.setObjectType(objectType);
		entity.setObjectId(objectId);
		return eventMapper.selectOne(entity);
	}
	
	/*
	 * 根据event id查询event
	 */
	public List<Event> getEventsWithIDs(List<Integer> eventIds) {
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		wrapper.in("id", eventIds);
		wrapper.orderBy("ts", false);
		return eventMapper.selectList(wrapper);
	}
	
	public List<Event> getEventsOfUser(Integer userId, Integer count){
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.orderBy("ts", false);
		wrapper.last(" limit " + count);
		return eventMapper.selectList(wrapper);
	}
	
	public Integer delete(Integer id){
		return eventMapper.deleteById(id);
	}
	
	public Integer delete(Integer objectType, Integer objectId){
		EntityWrapper<Event> wrapper = new EntityWrapper<>();
		wrapper.eq("object_type", objectType);
		wrapper.eq("object_id", objectId);
		return eventMapper.delete(wrapper);
	}
}
