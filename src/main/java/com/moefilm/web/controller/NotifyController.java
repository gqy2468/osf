package com.moefilm.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.User;
import com.moefilm.web.service.NotificationService;
import com.moefilm.web.util.DictUtil;

@Controller
@RequestMapping("/notifications")
public class NotifyController {
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping("/comment")
	public ModelAndView comment(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("notifications/comment");
		
		User user = (User) session.getAttribute("user");
		mav.addObject("dic", new DictUtil());
		mav.addObject("notis", notificationService.getNotifications(user.getId(), DictUtil.NOTIFY_TYPE_COMMENT));
		return mav;
	}
	
	@RequestMapping("/like")
	public ModelAndView like(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("notifications/like");
		User user = (User) session.getAttribute("user");
		mav.addObject("dic", new DictUtil());
		mav.addObject("notis", notificationService.getNotifications(user.getId(), DictUtil.NOTIFY_TYPE_LIKE));
		return mav;
	}
	
	@RequestMapping("/follow")
	public ModelAndView follow(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("notifications/follow");
		User user = (User) session.getAttribute("user");
		mav.addObject("dic", new DictUtil());
		mav.addObject("notis", notificationService.getNotifications(user.getId(), DictUtil.NOTIFY_TYPE_FOLLOW));
		return mav;
	}

	@RequestMapping("/system")
	public ModelAndView system(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("notifications/system");
		User user = (User) session.getAttribute("user");
		mav.addObject("dic", new DictUtil());
		mav.addObject("notis", notificationService.getNotifications(user.getId(), DictUtil.NOTIFY_TYPE_SYSTEM));
		return mav;
	}
	
}
