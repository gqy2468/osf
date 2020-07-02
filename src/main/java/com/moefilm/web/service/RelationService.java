package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Relation;
import com.moefilm.web.model.Tag;

public interface RelationService {
	
	public static final Integer RELATION_TYPE_POST = 0;
	public static final Integer RELATION_TYPE_PHOTO = 1;
	public static final Integer RELATION_TYPE_ALBUM = 2;
	public static final Integer RELATION_TYPE_VIDEO = 3;
	public static final Integer RELATION_TYPE_AUDIO = 4;
	
	public Map<String, Object> newRelation(Integer objectType, Integer objectId, Integer tagId);
	
	public List<Relation> getRelationsWithTag(String tag);
	
	/**
	 * 获取有列表中tag的关联关系
	 * 
	 * @param tags
	 * @return List
	 */
	public List<Relation> getRelationsInTags(List<Tag> tags);
	
}
