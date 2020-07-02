package com.moefilm.web.service;

import java.util.List;

public interface LikeService {
	
	Integer like(Integer userId, Integer objectType, Integer objectId);
	
	Integer undoLike(Integer userId, Integer objectType, Integer objectId);
	
	/**
	 * 判断用户是否喜欢某个对象
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	Boolean isLike(Integer userId, Integer objectType, Integer objectId);
	
	/**
	 * 返回喜欢某个对象的用户数量
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	long likersCount(Integer objectType, Integer objectId);
	
	/**
	 * 返回喜欢某个对象的用户ID列表
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	List<Integer> likers(Integer objectType, Integer objectId);
	
	/**
	 * 用户喜欢的对象数量(部分对象类型)
	 * @param userId
	 * @return
	 */
	Integer likeCount(Integer userId);
}
