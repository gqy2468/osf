package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.InterestService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;


@Controller
public class HomePage {

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private InterestService interestService;
	
	@RequestMapping("/")
	public ModelAndView showHomePage(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		
		User user = (User)session.getAttribute("user");
		if (user == null) {
			return mav;
		}
		mav.addObject("user", user);
		mav.addObject("counter", userService.getCounterOfFollowAndShortPost(user.getId()));
		List<Event> feeds = feedService.getFeeds(user.getId());
		mav.addObject("feeds", feeds);
		
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
	@RequestMapping("/popup_usercard/{user_id}")
	public ModelAndView getPopupUserCard(@PathVariable("user_id") String user_id){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("popup_usercard");
		User user = userService.findById(Integer.valueOf(user_id));
		if(user != null) {
			mav.addObject("u", user);
			mav.addObject("counter", userService.getCounterOfFollowAndShortPost(Integer.valueOf(user_id)));
		}
		
		return mav;
	}

	@RequestMapping("/page/{num}")
	public ModelAndView nextPage(@PathVariable("num") String num_str, HttpSession session) {
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return null;
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nextpage");
		
		int num = Integer.parseInt(num_str);
		List<Event> feeds = feedService.getFeedsOfPage(user.getId(), num);
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
	@RequestMapping("/welcome")
	public ModelAndView welcome(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user") != null){
			mav.setViewName("redirect:/");
			return mav;
		}
		
		mav.setViewName("welcome");
		List<Tag> tags_recommend = tagService.getRecommendTags(0);
		mav.addObject("tags", tags_recommend);
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
	@RequestMapping("/sidebar")
	public ModelAndView sideBar(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sidebar");
		User user = (User)session.getAttribute("user");
		if(user == null){
			return mav;
		}
		
		List<User> rec_users = userService.getRecommendUsers(user==null?0:user.getId(), 4);
		mav.addObject("isFollowings", followService.isFollowing(user==null?0:user.getId(), rec_users));
		mav.addObject("popusers", rec_users);
				
		List<Tag> tags_recommend = tagService.getRecommendTags(user==null?0:user.getId());
		mav.addObject("poptags", tags_recommend);
		
		return mav;
	}
	
	@RequestMapping("/guide")
	public ModelAndView guide(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("guide");
		
		User user = (User) session.getAttribute("user");
		
		List<Tag> tags_recommend = tagService.getRecommendTags(user==null?0:user.getId());
		mav.addObject("tags", tags_recommend);
		mav.addObject("isInterests", interestService.hasInterestInTags(user==null?0:user.getId(), tags_recommend));
		
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
	/**
	 * 新用户兴趣选择之后 feed初始化
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/guide/ok")
	public Map<String, Object> guideOk(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		feedService.codeStart(user.getId());
		
		return map;
	}
	
	@RequestMapping("/followers")
	public ModelAndView getFollowers(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followers", userService.findAllbyIDs(
								   followService.getFollowerIDs(
										   ((User)session.getAttribute("user")).getId())
								   ));
		
		mav.setViewName("follower");
		return mav;
	}
	
	@RequestMapping("/followers/{uid}")
	public ModelAndView getFollowers(@PathVariable("uid") int uid, HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followers", userService.findAllbyIDs(followService.getFollowerIDs(uid)));
		mav.addObject("uid", uid);
		User me = (User) session.getAttribute("user");
		mav.addObject("me", me);
		mav.setViewName("follower");
		return mav;
	}
	
	@RequestMapping("/followings")
	public ModelAndView getFollowings(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followings", userService.findAllbyIDs(
									followService.getFollowingIDs(
											((User)session.getAttribute("user")).getId())
									));
		
		mav.setViewName("following");
		return mav;
	}
	
	@RequestMapping("/followings/{uid}")
	public ModelAndView getFollowings(@PathVariable("uid") int uid, HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followings", userService.findAllbyIDs(followService.getFollowingIDs(uid)));
		mav.addObject("uid", uid);
		User me = (User) session.getAttribute("user");
		mav.addObject("me", me);
		mav.setViewName("following");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/feed/delete", method=RequestMethod.POST)
	public Map<String, Object> deleteFeed(@RequestParam("id") int id){
		Map<String, Object> map = new HashMap<String, Object>();
		feedService.deleteAll(id);
		map.put("status", Property.SUCCESS_FEED_DELETE);
		return map;
	}
	
	@RequestMapping("/404")
	public ModelAndView pageNotFound() {
		return new ModelAndView("404");
	}
	
	@RequestMapping("/500")
	public ModelAndView error500() {
		return new ModelAndView("500");
	}
}
