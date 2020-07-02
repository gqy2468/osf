package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.User;

public interface UserService {
	
	User findByUsername(String userName);
	
	User findByEmail(String email);
	
	User findById(Integer id);
	
	List<User> findAllbyIDs(List<Integer> ids);
	
	Map<String, Object> login(String email, String password);
	
	String register(String userName, String email, String password, String conformPwd, Map<String, String> map);
	
	Map<String, Object> updateActivationKey(String email);
	
	String activateUser(String email, String key);
	
	String findActivationKeyOfUser(Integer id);
	
	User findByActivationKey(String key);
	
	int getStatus(String email);
	
	/**
	 * 推荐用户
	 * @param count
	 * @return
	 */
	List<User> getRecommendUsers(Integer userId, Integer count);
	
	List<Integer> getRecommendUsersID(Integer userId, Integer count);
	
	Map<String, Long> getCounterOfFollowAndShortPost(Integer userId);
	
	String changeAvatar(Integer userId, String avatar);
	
	void updateUsernameAndDesc(Integer userId, String userName, String desc);
	
	/**
	 * return reset password key
	 * @param email
	 * @return
	 */
	String updateResetPwdKey(String email);
	
	/**
	 * 检查是否有权限重置密码
	 * @param email
	 * @param key
	 */
	boolean isAllowedResetPwd(String email, String key);

	/**
	 * 重置密码
	 * @param email
	 * @param password
	 * @return
	 */
	String resetPassword(String email, String password, String cfmPwd);
	
	/**
	 * 修改密码
	 * @param email
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	String changePassword(String email, String oldPwd, String newPwd);
	
	User getAuthor(Integer objectType, Integer objectId);
}
