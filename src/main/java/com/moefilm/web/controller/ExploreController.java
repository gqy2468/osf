package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.InterestService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;

@Controller
@RequestMapping("/explore")
public class ExploreController {
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FollowService followService;
	
	@RequestMapping("")
	public ModelAndView explore(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("explore");
		User user = (User) session.getAttribute("user");
		
		mav.addObject("events", feedService.getRecommendFeeds(user==null?0:user.getId()));
		
		List<Tag> tags_recommend = tagService.getRecommendTags(user==null?0:user.getId());
		mav.addObject("tags", tags_recommend);
		mav.addObject("isInterests", interestService.hasInterestInTags(user==null?0:user.getId(), tags_recommend));
		
		List<User> rec_users = userService.getRecommendUsers(user==null?0:user.getId(), 4);
		Map<Integer, Boolean> isFollowings = followService.isFollowing(user==null?0:user.getId(), rec_users);
		Map<String, Boolean> newIsFollowings = new HashMap<>();
		if (isFollowings != null && isFollowings.size() > 0) {
			for(Map.Entry<Integer, Boolean> entry : isFollowings.entrySet()){
				newIsFollowings.put(entry.getKey().toString(), entry.getValue());
			}
		}
		mav.addObject("isFollowings", newIsFollowings);
		
		Map<User, List<Event>> feeds = new HashMap<User, List<Event>>();
		for(User rec_user: rec_users){
			feeds.put(rec_user, eventService.getEventsOfUser(rec_user.getId(), 4));
		}
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
}
