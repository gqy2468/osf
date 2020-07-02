package com.moefilm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Tag;

public interface TagService {
	
	public static List<Tag> toList(String tags) {
		if(tags == null || tags.length() == 0)
			return new ArrayList<Tag>();
		String[] tag_and_id_strs = tags.split(" ");
		List<Tag> tag_list = new ArrayList<Tag>();
		for(String tag : tag_and_id_strs) {
			String[] tag_and_id = tag.split(":");
			Tag t = new Tag();
			if(tag_and_id.length > 1) {
				t.setId(Integer.valueOf(tag.split(":")[1]) );
			}
			t.setTag(tag.split(":")[0]);
			
			tag_list.add(t);
		}
		
		return tag_list;
	}

	public static String toString(List<Tag> tags) {
		if(tags == null || tags.size() == 0)
			return "";
		StringBuffer buffer = new StringBuffer();
		for(Tag tag: tags) {
			buffer.append(tag.getTag()+":"+tag.getId()+" ");
		}
		return buffer.toString();
	}
	
	Map<String, Object> newTag(String tag);
	
	Map<String, Object> newTags(List<Tag> tags);
	
	Integer getID(String tag);
	
	/**
	 * 需重构，迁移到feed或event
	 * @param tag
	 * @return
	 */
	List<Event> getWithTag(String tag);
	
	/**
	 * 获取推荐tag
	 * 简单实现，获取有cover的tag
	 * @param userId
	 * @return
	 */
	List<Tag> getRecommendTags(Integer userId);
	
	Tag getTagByID(Integer id);
	
	Tag getTagByName(String tag);
}
