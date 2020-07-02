package com.moefilm.web.service;

import java.util.List;
import java.util.Map;
import com.moefilm.web.model.Follower;
import com.moefilm.web.model.Following;
import com.moefilm.web.model.User;

public interface FollowService {
	
	public Map<String, Object> newFollowing(Integer userId, String userName, Integer followingUserId, String followingUserName);
	
	public Map<String, Object> undoFollow(Integer userId, Integer followingUserId);
	
	public long followersCount(Integer userId);
	
	public long followingsCount(Integer userId);
	
	public List<Integer> getFollowerIDs(Integer userId);
	
	public List<Integer> getFollowingIDs(Integer userId);
	
	public List<Follower> getFollowers(Integer userId);
	
	public List<Following> getFollowings(Integer userId);
	
	public boolean isFollowing(Integer user_a, Integer user_b);
	
	public Map<Integer, Boolean> isFollowing(Integer userId, List<User> users);
	
}
