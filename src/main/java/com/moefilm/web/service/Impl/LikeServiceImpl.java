package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.LikeMapper;
import com.moefilm.web.model.Like;
import com.moefilm.web.service.LikeService;
import com.moefilm.web.util.DictUtil;

@Service("likeService")
public class LikeServiceImpl implements LikeService{
	
	private static final String LIKE_KEY = "like:";
	
	@Autowired
	private LikeMapper likeMapper;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Resource(name="redisTemplate")
	private SetOperations<String, Integer> setOps;
	
	public Integer like(Integer userId, Integer objectType, Integer objectId) {
		Like like = new Like();
		like.setUserId(userId);
		like.setObjectType(objectType);
		like.setObjectId(objectId);
		Integer res = likeMapper.insert(like);
		if (res > 0) {
			cacheLikers(objectType, objectId);
			setOps.add(LIKE_KEY + DictUtil.checkType(objectType) + ":" + objectId,userId);
			return res;
		}
		return 0;
	}
	
	public Integer undoLike(Integer userId, Integer objectType, Integer objectId) {
		EntityWrapper<Like> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.eq("object_type", objectType);
		wrapper.eq("object_id", objectId);
		Integer res = likeMapper.delete(wrapper);
		if (res > 0) {
			cacheLikers(objectType, objectId);
			setOps.remove(LIKE_KEY + DictUtil.checkType(objectType) + ":" + objectId,userId);
			return res;
		}
		return 0;
	}
	
	private List<Integer> cacheLikers(Integer objectType, Integer objectId) {
		String key = LIKE_KEY + DictUtil.checkType(objectType) + ":" + objectId;
		List<Integer> likers = new ArrayList<>();
		if (!redisTemplate.hasKey(key)) {
			EntityWrapper<Like> wrapper = new EntityWrapper<>();
			wrapper.eq("object_type", objectType);
			wrapper.eq("object_id", objectId);
			List<Like> likes = likeMapper.selectList(wrapper);
			if (CollectionUtils.isNotEmpty(likes)) {
				for (Like like : likes) {
					setOps.add(key, like.getUserId());
					likers.add(like.getUserId());
				}
			}
		} 
		return likers;
	}
	
	/**
	 * 判断用户是否喜欢某个对象
	 * @param user_id
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	public Boolean isLike(Integer userId, Integer objectType, Integer objectId) {
		cacheLikers(objectType, objectId);
		return setOps.isMember(LIKE_KEY + DictUtil.checkType(objectType) + ":" + objectId, userId);
	}
	
	/**
	 * 返回喜欢某个对象的用户数量
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	public long likersCount(Integer objectType, Integer objectId) {
		cacheLikers(objectType, objectId);
		return setOps.size(LIKE_KEY + DictUtil.checkType(objectType) + ":" + objectId);
	}
	
	/**
	 * 返回喜欢某个对象的用户ID列表
	 * @param object_type
	 * @param object_id
	 * @return
	 */
	public List<Integer> likers(Integer objectType, Integer objectId) {
		return cacheLikers(objectType, objectId);
	}
	
	/**
	 * 用户喜欢的对象数量(部分对象类型)
	 * @param userId
	 * @return
	 */
	public Integer likeCount(Integer userId) {
		EntityWrapper<Like> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		return likeMapper.selectCount(wrapper);
	}
}
