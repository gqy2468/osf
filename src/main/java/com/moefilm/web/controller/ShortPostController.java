package com.moefilm.web.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moefilm.web.model.ShortPost;
import com.moefilm.web.model.User;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.ShortPostService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;

@Controller
@RequestMapping("/spost")
public class ShortPostController {

	@Autowired
	private ShortPostService shortPostService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Map<String, Object> createPost(@RequestParam("content") String content,
										  HttpSession session){
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = shortPostService.newPost(user.getId(), content);
		
		ShortPost spost = (ShortPost) map.get("spost");	
		int event_id = eventService.newEvent(DictUtil.OBJECT_TYPE_SHORTPOST, spost);
		feedService.push(user.getId(), event_id);
		
		map.put("avatar", userService.findById(user.getId()).getUserAvatar());
		map.put("author_name", user.getUserName());
		
		return map;
	}
}
