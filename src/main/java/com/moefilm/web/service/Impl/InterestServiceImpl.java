package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.InterestMapper;
import com.moefilm.web.mapper.TagMapper;
import com.moefilm.web.model.Interest;
import com.moefilm.web.model.Tag;
import com.moefilm.web.service.InterestService;

@Service("interestService")
public class InterestServiceImpl implements InterestService {
	
	@Autowired
	private InterestMapper interestMapper;
	
	@Autowired
	private TagMapper tagMapper;
	
	/**
	 * 关注tag
	 * 
	 * @param userId
	 * @param tagId
	 */
	public Integer interestInTag(Integer userId, Integer tagId) {
		Interest interest = new Interest();
		interest.setUserId(userId);
		interest.setTagId(tagId);
		return interestMapper.insert(interest);
	}
	
	
	/**
	 * 撤销关注tag
	 * 
	 * @param userId
	 * @param tagId
	 */
	public Integer undoInterestInTag(Integer userId, Integer tagId) {
		EntityWrapper<Interest> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.eq("tag_id", tagId);
		return interestMapper.delete(wrapper);
	}
	
	/**
	 * 获取关注tagId的用户列表
	 * 
	 * @param tagId
	 * @return
	 */
	public List<Integer> getUsersInterestedInTag(Integer tagId) {
		EntityWrapper<Interest> wrapper = new EntityWrapper<>();
		wrapper.eq("tag_id", tagId);
		List<Integer> users = new ArrayList<>();
		List<Interest> interests = interestMapper.selectList(wrapper);
		if (CollectionUtils.isNotEmpty(interests)) {
			for (Interest interest : interests) {
				users.add(interest.getUserId());
			}
		}
		return users;
	}
	
	/**
	 * 判断用户对tag是否已经关注
	 * 
	 * @param userId
	 * @param tagId
	 * @return
	 */
	public Boolean hasInterestInTag(Integer userId, Integer tagId) {
		EntityWrapper<Interest> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.eq("tag_id", tagId);
		Integer count = interestMapper.selectCount(wrapper);
		return count > 0 ? true : false;
	}
	

	/**
	 * 判断用户对列表中的tag是否已经关注
	 * 
	 * @param userId
	 * @param tags
	 * @return
	 */
	public Map<Integer, Boolean> hasInterestInTags(Integer userId, List<Tag> tags){
		if (tags == null || tags.size() == 0 ) {
			return null;
		}
		List<Integer> tagIds = new ArrayList<Integer>();
		for (Tag tag: tags) {
			tagIds.add(tag.getId());
		}

		EntityWrapper<Interest> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.in("tag_id", tagIds);
		List<Interest> interests = interestMapper.selectList(wrapper);
		Map<Integer, Boolean> result = new HashMap<Integer, Boolean>();
		if (CollectionUtils.isNotEmpty(interests)) {
			for (Interest interest : interests) {
				result.put(interest.getTagId(), true);
			}
		}
		return result;
		
	}
	
	/**
	 * 获取用户关注的tag列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Tag> getTagsUserInterestedIn(Integer userId){
		List<Integer> tagIds = new ArrayList<Integer>();
		EntityWrapper<Interest> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		List<Interest> interests = interestMapper.selectList(wrapper);
		if (CollectionUtils.isNotEmpty(interests)) {
			for (Interest interest : interests) {
				tagIds.add(interest.getTagId());
			}
		}
		return tagMapper.selectBatchIds(tagIds);
	}
}
