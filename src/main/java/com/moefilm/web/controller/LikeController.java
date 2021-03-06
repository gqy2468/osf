package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moefilm.web.model.Notification;
import com.moefilm.web.model.User;
import com.moefilm.web.service.LikeService;
import com.moefilm.web.service.NotificationService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/like")
public class LikeController {

	@Autowired
	private LikeService likeService;
	
	@Autowired
	private NotificationService notificationService;
	
	@ResponseBody
	@RequestMapping("/do")
	public Map<String, String> like(@RequestParam("author") int author,
									@RequestParam("object_type") int object_type,
					  				@RequestParam("object_id") int object_id,
					  				HttpSession session){
		User me = (User) session.getAttribute("user");
				
		likeService.like(me.getId(), object_type, object_id);
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("status", Property.SUCCESS_LIKE);
		
		Notification notification = new Notification(DictUtil.NOTIFY_TYPE_LIKE,
													 0, 
													 object_type, 
													 object_id, 
													 author, 
													 me.getId()
													 );
		notificationService.doNotify(notification);
		return ret;
	}
	
	@ResponseBody
	@RequestMapping("/undo")
	public Map<String, String> undolike(@RequestParam("object_type") int object_type,
										@RequestParam("object_id") int object_id,
										HttpSession session){
		User me = (User) session.getAttribute("user");
		likeService.undoLike(me.getId(), object_type, object_id);
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("status", Property.SUCCESS_LIKE_UNDO);
		return ret;
	}
	
}
