package com.moefilm.web.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moefilm.web.model.Notification;
import com.moefilm.web.model.User;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.NotificationService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;

@Controller
@RequestMapping("/follow")
public class FollowController {
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	
	@ResponseBody
	@RequestMapping("/{following_user_id}")
	public Map<String, Object> follow(@PathVariable("following_user_id") int following_user_id,
									  HttpSession session) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = followService.newFollowing(user.getId(), 
															 user.getUserName(), 
															 following_user_id, 
															 userService.findById(following_user_id).getUserName());
		Notification notification = new Notification(DictUtil.NOTIFY_TYPE_FOLLOW,
												     0, 
												     DictUtil.OBJECT_TYPE_USER,
												     following_user_id, 
												     following_user_id, 
												     user.getId());
		notificationService.doNotify(notification);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/undo/{following_user_id}")
	public Map<String, Object> undoFollow(@PathVariable("following_user_id") int following_user_id,
							   HttpSession session) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = followService.undoFollow(user.getId(),following_user_id);
		return map;
	}
}
