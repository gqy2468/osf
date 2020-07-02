package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.RelationMapper;
import com.moefilm.web.model.Relation;
import com.moefilm.web.model.Tag;
import com.moefilm.web.service.RelationService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.util.Property;

@Service("relationService")
public class RelationServiceImpl implements RelationService {
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private RelationMapper relationMapper;
	
	@Transactional
	public Map<String, Object> newRelation(Integer objectType, Integer objectId, Integer tagId) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Relation entity = new Relation();
		entity.setObjectType(objectType);
		entity.setObjectId(objectId);
		entity.setTagId(tagId);
		Integer id = relationMapper.insert(entity);
		if(id != 0){
			Relation relation = new Relation();
			relation.setId(id);
			relation.setObjectType(objectType);
			relation.setObjectId(objectId);
			relation.setTagId(tagId);
			ret.put("relation", relation);
			ret.put("status", Property.SUCCESS_RELATION_CREATE);
		} else {
			ret.put("status", Property.ERROR_RELATION_CREATE);
		}
		return ret;
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	public List<Relation> getRelationsWithTag(String tag){
		List<Relation> relations = new ArrayList<Relation>();
		Integer tagId = tagService.getID(tag);
		if (tagId != 0) {
			EntityWrapper<Relation> wrapper = new EntityWrapper<>();
			wrapper.eq("tag_id", tagId);
			wrapper.orderBy("add_ts", true);
			return relationMapper.selectList(wrapper);
		}
		return relations;
	}
	
	/**
	 * 获取有列表中tag的关联关系
	 * 
	 * @param tags
	 * @return List
	 */
	public List<Relation> getRelationsInTags(List<Tag> tags) {
		if (tags == null || tags.size() == 0) {
			return new ArrayList<Relation>();
		}
		
		List<Integer> tagIds = new ArrayList<Integer>();
		for (Tag tag : tags) {
			tagIds.add(tag.getId());
		}

		EntityWrapper<Relation> wrapper = new EntityWrapper<>();
		wrapper.in("tag_id", tagIds);
		return relationMapper.selectList(wrapper);
	}
	
}
