package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.TagMapper;
import com.moefilm.web.model.Event;
import com.moefilm.web.model.Tag;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.RelationService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.util.Property;

@Service("tagService")
public class TagServiceImpl implements TagService {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RelationService relationService;

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private TagMapper tagMapper;
	
	private String check(String tag) {
		if(tag == null || tag.length() == 0) {
			return Property.ERROR_TAG_EMPTY;
		}
		return Property.SUCCESS_TAG_FORMAT;
	}
	
	public Map<String, Object> newTag(String tag) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String status = check(tag);
		ret.put("status", status);
		if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
			return ret;
		}
		
		Integer id = getID(tag);
		if (id != 0) {
			Tag tg = new Tag();
			tg.setId(id);
			tg.setTag(tag);
			ret.put("tag", tg);
			return ret;
		}

		Tag tg = new Tag();
		tg.setTag(tag);
		id = tagMapper.insert(tg);
		if(id != 0) {
			tg = new Tag();
			tg.setId(id);
			tg.setTag(tag);
			ret.put("tag", tg);
		}
		return ret;
	}
	
	@Transactional
	public Map<String, Object> newTags(List<Tag> tags) {
				
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Tag> taglist = new ArrayList<Tag>();
		ret.put("tags", taglist);
		
		if(tags == null || tags.size() == 0) {
			ret.put("status", Property.SUCCESS_TAG_CREATE);
			return ret;
		}
		
		for(Tag tag: tags) {
			String status = check(tag.getTag());
			if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
				return ret;
			}

			Integer id = getID(tag.getTag());
			if(id != 0) {
				Tag tg = new Tag();				
				tg.setId(id);
				tg.setTag(tag.getTag());
				taglist.add(tg);
				continue;
			}
			
			Tag tg = new Tag();
			tg.setTag(tag.getTag());
			id = tagMapper.insert(tg);
			if(id != 0) {
				tg = new Tag();	
				tg.setId(id);
				tg.setTag(tag.getTag());
				taglist.add(tg);
			}
		}
		ret.put("tags", taglist);
		ret.put("status", Property.SUCCESS_TAG_CREATE);
		return ret;
	}
	
	public Integer getID(String tag) {
		Tag entity = new Tag();
		entity.setTag(tag);
		Tag tg = tagMapper.selectOne(entity);
		return tg != null ? tg.getId() : 0;
	}
	
	/**
	 * 需重构，迁移到feed或event
	 * @param tag
	 * @return
	 */
	public List<Event> getWithTag(String tag) {
		List<Event> events = eventService.getEventsWithRelations(relationService.getRelationsWithTag(tag));
		feedService.addUserInfo(events);
		return events;
	}
	
	
	/**
	 * 获取推荐tag
	 * 简单实现，获取有cover的tag
	 * @param userId
	 * @return
	 */
	public List<Tag> getRecommendTags(Integer userId) {
		EntityWrapper<Tag> wrapper = new EntityWrapper<>();
		wrapper.isNotNull("cover");
		wrapper.last(" limit 12");
		return tagMapper.selectList(wrapper);
	}
	
	public Tag getTagByID(Integer id) {
		return tagMapper.selectById(id);
	}
	
	public Tag getTagByName(String tag) {
		Tag entity = new Tag();
		entity.setTag(tag);
		return tagMapper.selectOne(entity);
	}
}
