package com.moefilm.web.service.Impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.UserMapper;
import com.moefilm.web.model.User;
import com.moefilm.web.service.AlbumService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.PostService;
import com.moefilm.web.service.ShortPostService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.CipherUtil;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Service("userService")
public class UserServiceImpl implements UserService {

	public static final int STATUS_USER_NORMAL = 0;				//正常
	public static final int STATUS_USER_INACTIVE = 1;			//待激活
	public static final int STATUS_USER_LOCK = 2;				//锁定
	public static final int STATUS_USER_CANCELLED = 3;			//注销
	
	public static final String DEFAULT_USER_AVATAR = "default-avatar.jpg";
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private ShortPostService shortPostService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private AlbumService albumService;
	
	private boolean ValidateEmail(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException e) {
			result = false;
		}
		return result;
	}
	
//	public String confirmPwd(String userName, String userPwd) {
//		if(userPwd == null || userName.length() == 0)
//			return Property.ERROR_PWD_EMPTY;
//		String pwd = userDao.getPwdByUsername(userName);
//		if(pwd.equals(userPwd)) 
//			return null;
//		else
//			return Property.ERROR_PWD_DIFF;
//			
//	}
	
	public User findByUsername(String userName) {
		User entity = new User();
		entity.setUserName(userName);
		User user = userMapper.selectOne(entity);
//		if(user != null){
//			addAvatar(user);
//		}
		return user;
	}
	
	public User findByEmail(String email) {
		User entity = new User();
		entity.setUserEmail(email);
		User user = userMapper.selectOne(entity);
//		if(user != null) {
//			addAvatar(user);
//		}
		return user;
	}
	
	public User findById(Integer id) {
		User user = userMapper.selectById(id);
//		if(user != null) {
//			addAvatar(user);
//		}
		return user;
	}
	
	public List<User> findAllbyIDs(List<Integer> ids) {
		return CollectionUtils.isEmpty(ids) ? new ArrayList<User>() : userMapper.selectBatchIds(ids);
	}
	
	@SuppressWarnings("unused")
	private void addAvatar(User user) {
		user.setUserAvatar(user.getUserAvatar());
	}
	
	public Map<String, Object> login(String email, String password) {
		Map<String, Object> ret = new HashMap<String, Object>();
		//1 empty check
		if (email == null || email.length() <= 0) {
			ret.put("status", Property.ERROR_EMAIL_EMPTY);
			return ret;
		}
			
		if (password == null || password.length() <= 0){
			ret.put("status", Property.ERROR_PWD_EMPTY);
			return ret;
		}
			
		
		//2 ValidateEmail 
		if (!ValidateEmail(email)) {
			ret.put("status", Property.ERROR_EMAIL_FORMAT);
			return ret;
		}

		//3 email exist?
		User user = findByEmail(email);
		if (user == null) {
			ret.put("status", Property.ERROR_EMAIL_NOT_REG);
			return ret;
		} else {
			//4 check user status
			if(STATUS_USER_NORMAL != user.getUserStatus()) {
				ret.put("status", user.getUserStatus());
				return ret;
			}
		}
		
		//5 password validate
		if (!CipherUtil.validatePassword(user.getUserPwd(), password)) {
			ret.put("status", Property.ERROR_PWD_DIFF);
			return ret;
		}
		ret.put("status", Property.SUCCESS_ACCOUNT_LOGIN);
		ret.put("user", user);
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	public String register(String userName, String email, String password, String conformPwd, Map<String, String> map) {
		//1 empty check
		if(email == null || email.length() <= 0)
			return Property.ERROR_EMAIL_EMPTY;
		else{
			//4 ValidateEmail
			if(!ValidateEmail(email))
				return Property.ERROR_EMAIL_FORMAT;
			
			//5 email exist?
			User user = findByEmail(email);
			if(user != null) {
							
				//6 user status check
				if(STATUS_USER_NORMAL == user.getUserStatus())
					return Property.ERROR_ACCOUNT_EXIST;
				else if(STATUS_USER_INACTIVE == user.getUserStatus()){
					map.put("activationKey", URLEncoder.encode(user.getUserActivationKey()));
					return Property.ERROR_ACCOUNT_INACTIVE;
				}
				else if(STATUS_USER_LOCK == user.getUserStatus())
					return Property.ERROR_ACCOUNT_LOCK;
				else if(STATUS_USER_CANCELLED == user.getUserStatus()) 
					return Property.ERROR_ACCOUNT_CANCELLED;
			}			
		}
		
		if(userName == null || userName.length() == 0) 
			return Property.ERROR_USERNAME_EMPTY;
		else {
			//userName exist check
			if(findByUsername(userName) != null) {
				return Property.ERROR_USERNAME_EXIST;
			}
		}
		
		
		if(password == null || password.length() <= 0)
			return Property.ERROR_PWD_EMPTY;
		else {
			//3 password format validate
			String vpf_rs = CipherUtil.validatePasswordFormat(password);
			if(vpf_rs != Property.SUCCESS_PWD_FORMAT)
				return vpf_rs;
		}
		if(conformPwd == null || conformPwd.length() <= 0)
			return Property.ERROR_CFMPWD_EMPTY;
				
		//2 pwd == conformPwd ?
		if(!password.equals(conformPwd))
			return Property.ERROR_CFMPWD_NOTAGREE;
					
		
		User user = new User();
		user.setUserName(userName);
		user.setUserPwd(CipherUtil.generatePassword(password));
		user.setUserEmail(email);
		user.setUserStatus(STATUS_USER_INACTIVE);
		user.setUserAvatar(DEFAULT_USER_AVATAR);
		String activationKey = CipherUtil.generateActivationUrl(email, password);
		user.setUserActivationKey(activationKey);
		int id = userMapper.insert(user);
		
		map.put("id", String.valueOf(id));
		map.put("activationKey", URLEncoder.encode(activationKey));
		return Property.SUCCESS_ACCOUNT_REG;
		
	}
	
	public Map<String, Object> updateActivationKey(String email) {
		//1 check user status
		User user = findByEmail(email);
		String status = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if(user == null){
			status = Property.ERROR_EMAIL_NOT_REG;
		}
		
		if (STATUS_USER_INACTIVE == user.getUserStatus()) {
			//2 gen activation key
			String activationKey = CipherUtil.generateActivationUrl(email, new Date().toString());
			User entity = new User();
			entity.setId(user.getId());
			entity.setUserActivationKey(activationKey);
			userMapper.updateById(entity);
			status = Property.SUCCESS_ACCOUNT_ACTIVATION_KEY_UPD;
			map.put("activationKey", activationKey);
		} else {
			if(STATUS_USER_NORMAL == user.getUserStatus())
				status = Property.ERROR_ACCOUNT_EXIST; //已激活
			else if(STATUS_USER_CANCELLED == user.getUserStatus()) 
				status = Property.ERROR_ACCOUNT_CANCELLED;
			
			//status = Property.ERROR_ACCOUNT_ACTIVATION;
		}
		map.put("status", status);
		return map;
	}
	
	public String activateUser(String email, String key) {
		User user = findByEmail(email);
		if (user == null) {
			return Property.ERROR_ACCOUNT_ACTIVATION_NOTEXIST;
		} else {
			if (user.getUserStatus() == STATUS_USER_INACTIVE ) {
				if (user.getUserActivationKey().equals(key)) {
					User entity = new User();
					entity.setId(user.getId());
					entity.setUserActivationKey(null);
					entity.setUserStatus(STATUS_USER_NORMAL);
					userMapper.updateById(entity);
				} else {
					return Property.ERROR_ACCOUNT_ACTIVATION_EXPIRED;
				}
			} else {
				if (user.getUserStatus() == STATUS_USER_NORMAL) {
					return Property.ERROR_ACCOUNT_EXIST;
				} else {
					return Property.ERROR_ACCOUNT_ACTIVATION;
				}
				
			}
		}
		return Property.SUCCESS_ACCOUNT_ACTIVATION;
	}
	
	public String findActivationKeyOfUser(Integer id) {
		return null;
	}
	
	public User findByActivationKey(String key) {
		User entity = new User();
		entity.setUserActivationKey(key);
		return userMapper.selectOne(entity);
	}
	
	public int getStatus(String email) {
		User user = findByEmail(email);
		return user.getUserStatus();
	}
	
	/**
	 * 推荐用户
	 * @param count
	 * @return
	 */
	public List<User> getRecommendUsers(Integer userId, Integer count) {
		//to do recommend logic
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.last(" limit " + count);
		List<User> users = userMapper.selectList(wrapper);
		Iterator<User> it = users.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (user.getId() == userId) {
				it.remove();
				continue;
			}
			//addAvatar(user);
		}

		return users;
	}
	
	public List<Integer> getRecommendUsersID(Integer userId, Integer count) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.last(" limit " + count);
		List<User> users = userMapper.selectList(wrapper);
		List<Integer> ids =  new ArrayList<Integer>();
		Iterator<User> it = users.iterator();
		while(it.hasNext()){
			User user = it.next();
			if(user.getId() == userId) {
				it.remove();
				continue;
			}
			ids.add(user.getId());
		}
		return ids;
	}
	
	public Map<String, Long> getCounterOfFollowAndShortPost(Integer userId) {
		Map<String, Long> counter = new HashMap<String, Long>();
		counter.put("follower", followService.followersCount(userId));
		counter.put("following", followService.followingsCount(userId));
		counter.put("spost", shortPostService.count(userId));
		return counter;
	}
	
	public String changeAvatar(Integer userId, String avatar) {
		User entity = new User();
		entity.setId(userId);
		entity.setUserAvatar(avatar);
		userMapper.updateById(entity);
		return Property.SUCCESS_AVATAR_CHANGE;
	}
	
	public void updateUsernameAndDesc(Integer userId, String userName, String desc) {
		User entity = new User();
		entity.setId(userId);
		entity.setUserName(userName);
		entity.setUserDesc(desc);
		userMapper.updateById(entity);
	}
	
	/**
	 * return reset password key
	 * @param email
	 * @return
	 */
	public String updateResetPwdKey(String email) {
		String key = CipherUtil.generateRandomLinkUseEmail(email);
		User entity = new User();
		entity.setResetpwdKey(key);
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.eq("user_email", email);
		userMapper.update(entity, wrapper);
		return key;
	}
	
	/**
	 * 检查是否有权限重置密码
	 * @param email
	 * @param key
	 */
	public boolean isAllowedResetPwd(String email, String key) {
		if( (email==null) || (key==null)) {
			return false;
		}
		
		User user = findByEmail(email);
		String resetpwdKey = user == null ? null : user.getResetpwdKey();
		boolean result = false;
		if (resetpwdKey == null || resetpwdKey.length() ==0) {
			result = false;
		} else {
			result = resetpwdKey.equals(key) ? true : false;
		}
		
		return result;
	}

	/**
	 * 重置密码
	 * @param email
	 * @param password
	 * @return
	 */
	public String resetPassword(String email, String password, String cfmPwd) {
		if ( password == null || password.length() == 0) {
			return Property.ERROR_PWD_EMPTY;
		}
		if (cfmPwd == null || cfmPwd.length()==0) {
			return Property.ERROR_CFMPWD_EMPTY;
		}
			 
		if (!password.equals(cfmPwd)) {
			return Property.ERROR_CFMPWD_NOTAGREE;
		}
		
		String vpf_rs = CipherUtil.validatePasswordFormat(password);
		if (vpf_rs != Property.SUCCESS_PWD_FORMAT) {
			return vpf_rs;
		}
		
		User entity = new User();
		entity.setUserPwd(CipherUtil.generatePassword(password));
		entity.setResetpwdKey(null);
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.eq("user_email", email);
		userMapper.update(entity, wrapper);
		return Property.SUCCESS_PWD_RESET;
	}
	
	/**
	 * 修改密码
	 * @param email
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public String changePassword(String email, String oldPwd, String newPwd) {
		if (oldPwd == null || oldPwd.length() == 0) {
			return Property.ERROR_PWD_EMPTY;
		}
		if ( newPwd == null || newPwd.length() == 0) {
			return Property.ERROR_PWD_EMPTY;
		}
		if (newPwd.equals(oldPwd)){
			return Property.ERROR_CFMPWD_SAME;
		}
				
		String vpf_rs = CipherUtil.validatePasswordFormat(newPwd);
		if (vpf_rs != Property.SUCCESS_PWD_FORMAT) {
			return vpf_rs;
		}
		
		User user = findByEmail(email);
		String currentPwd = user == null ? "" : user.getUserPwd();
		if (user == null || !currentPwd.equals(CipherUtil.generatePassword(oldPwd))) {
			return Property.ERROR_PWD_NOTAGREE;
		}

		User entity = new User();
		entity.setId(user.getId());
		entity.setUserPwd(CipherUtil.generatePassword(newPwd));
		userMapper.updateById(entity);
		return Property.SUCCESS_PWD_CHANGE;
	}
	
	public User getAuthor(Integer objectType, Integer objectId) {
		if (objectType == DictUtil.OBJECT_TYPE_POST) {
			return postService.getAuthorOfPost(objectId);
		} else if(objectType == DictUtil.OBJECT_TYPE_ALBUM) {
			return albumService.getAuthorOfALbum(objectId);
		} else if(objectType == DictUtil.OBJECT_TYPE_SHORTPOST) {
			return shortPostService.getAuthorOfPost(objectId);
		} else if(objectType == DictUtil.OBJECT_TYPE_PHOTO) {
			return albumService.getAuthorOfPhoto(objectId);
		} else {
			return new User();
		}
	}
}
