package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.FollowerMapper;
import com.moefilm.web.mapper.FollowingMapper;
import com.moefilm.web.model.Follower;
import com.moefilm.web.model.Following;
import com.moefilm.web.model.User;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.util.Property;

@Service("followService")
public class FollowServiceImpl implements FollowService {
	
	private static final String FOLLOWING_KEY = "following:user:";
	private static final String FOLLOWER_KEY = "follower:user:";
	
	@Autowired
	private FollowingMapper followingMapper;
	
	@Autowired
	private FollowerMapper followerMapper;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Resource(name="redisTemplate")
	private ListOperations<String, Integer> listOps;
	
	@Resource(name="redisTemplate")
	private SetOperations<String, Integer> setOps;
	
	public Map<String, Object> newFollowing(Integer userId, String userName, Integer followingUserId, String followingUserName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Following following = new Following();
		
		following.setUserId(userId);
		following.setUserName(userName);
		following.setFollowingUserId(followingUserId);
		following.setFollowingUserName(followingUserName);
		Integer id = followingMapper.insert(following);
		if(id == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("following", following);
		setOps.add(FOLLOWING_KEY+following.getUserId(), following.getFollowingUserId());
		
		Follower follower = new Follower();
		follower.setUserId(followingUserId);
		follower.setUserName(followingUserName);
		follower.setFollowerUserId(userId);
		follower.setFollowerUserName(userName);
		followerMapper.insert(follower);
		if(id == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("follower", follower);
		setOps.add(FOLLOWER_KEY+follower.getUserId() ,follower.getFollowerUserId());
		map.put("status", Property.SUCCESS_FOLLOW);
		return map;
	}
	
	public Map<String, Object> undoFollow(Integer userId, Integer followingUserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		EntityWrapper<Following> followingWrapper = new EntityWrapper<>();
		followingWrapper.eq("user_id", userId);
		followingWrapper.eq("following_user_id", followingUserId);
		if(followingMapper.delete(followingWrapper) <= 0) {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
			return map;
		}
		Following following = new Following();
		following.setUserId(userId);
		following.setFollowingUserId(followingUserId);
		map.put("following", following);
		setOps.remove(FOLLOWING_KEY+following.getUserId(), following.getFollowingUserId());
		
		EntityWrapper<Follower> followerWrapper = new EntityWrapper<>();
		followerWrapper.eq("user_id", followingUserId);
		followerWrapper.eq("following_user_id", userId);
		if(followerMapper.delete(followerWrapper) > 0) {
			map.put("status", Property.SUCCESS_FOLLOW_UNDO);
			Follower follower = new Follower();
			follower.setUserId(followingUserId);
			follower.setFollowerUserId(userId);
			map.put("follower", follower);
			setOps.remove(FOLLOWER_KEY+follower.getUserId() ,follower.getFollowerUserId());
		} else {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
		}
		return map;
	}
	
	public long followersCount(Integer userId) {
		return setOps.size(FOLLOWER_KEY+userId);
	}
	public long followingsCount(Integer userId) {
		return setOps.size(FOLLOWING_KEY+userId);
	}
	
	public List<Integer> getFollowerIDs(Integer userId) {
		Set<Integer> members = setOps.members(FOLLOWER_KEY+userId);
		return members != null ? new ArrayList<Integer>(members) : new ArrayList<Integer>();
	}
	
	public List<Integer> getFollowingIDs(Integer userId) {
		Set<Integer> members = setOps.members(FOLLOWING_KEY+userId);
		return members != null ? new ArrayList<Integer>(members) : new ArrayList<Integer>();
	}
	
	public List<Follower> getFollowers(Integer userId) {
		EntityWrapper<Follower> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		return followerMapper.selectList(wrapper);
	}
	
	public List<Following> getFollowings(Integer userId) {
		EntityWrapper<Following> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		return followingMapper.selectList(wrapper);
	}
	
	public boolean isFollowing(Integer user_a, Integer user_b){
		return setOps.isMember(FOLLOWING_KEY+user_a, user_b);
	}
	
	public Map<Integer, Boolean> isFollowing(Integer userId, List<User> users){
		if(users == null || users.size() == 0) {
			return null;
		}
		
		List<Integer> userIds = new ArrayList<Integer>();
		for(User user: users) {
			userIds.add(user.getId());
		}
		return isFollowingUsers(userId, userIds);
	}
	
	private Map<Integer, Boolean> isFollowingUsers(Integer userId, List<Integer> followingIds) {
		final Map<Integer, Boolean> result = new HashMap<Integer, Boolean>();
		for (Integer id: followingIds) {
			result.put(id, false);
		}
		EntityWrapper<Following> wrapper = new EntityWrapper<>();
		wrapper.eq("user_id", userId);
		wrapper.in("following_user_id", followingIds);
		List<Following> followings = followingMapper.selectList(wrapper);
		for (Following following : followings) {
			Integer followingUserId = following.getFollowingUserId();
			result.put(followingUserId, true);
			//修复缓存不一致问题
			if (!isFollowing(userId, followingUserId)) {
				setOps.add(FOLLOWING_KEY+userId, followingUserId);
			}
		}
		return result;
	}
}
