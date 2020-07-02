package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Tag;

public interface InterestService {
	
	/**
	 * 关注tag
	 * 
	 * @param userId
	 * @param tagId
	 */
	Integer interestInTag(Integer userId, Integer tagId);
	
	
	/**
	 * 撤销关注tag
	 * 
	 * @param userId
	 * @param tagId
	 */
	Integer undoInterestInTag(Integer userId, Integer tagId);
	
	/**
	 * 获取关注tagId的用户列表
	 * 
	 * @param tagId
	 * @return
	 */
	List<Integer> getUsersInterestedInTag(Integer tagId);
	
	/**
	 * 判断用户对tag是否已经关注
	 * 
	 * @param userId
	 * @param tagId
	 * @return
	 */
	Boolean hasInterestInTag(Integer userId, Integer tagId);
	

	/**
	 * 判断用户对列表中的tag是否已经关注
	 * 
	 * @param userId
	 * @param tags
	 * @return
	 */
	Map<Integer, Boolean> hasInterestInTags(Integer userId, List<Tag> tags);
	
	/**
	 * 获取用户关注的tag列表
	 * 
	 * @param userId
	 * @return
	 */
	List<Tag> getTagsUserInterestedIn(Integer userId);
}
