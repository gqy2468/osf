package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.InterestService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/tag")
public class TagController {

	@Autowired
	private TagService tagService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private FeedService feedService;
	
	@RequestMapping("/{tag_id}")
	public ModelAndView getFeedsByTag(@PathVariable("tag_id") int tag_id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tag/index");
		
		Tag tag = tagService.getTagByID(tag_id);
		if(tag == null) {
			mav.setViewName("404");
			return mav;
		}
		
		mav.addObject("tag", tag.getTag());
		mav.addObject("id", tag.getId());
		
		User user = (User)session.getAttribute("user");
		if(user != null) {
			mav.addObject("isInterest", 
						  interestService.hasInterestInTag(user.getId(), tag_id));
			
		} else {
			mav.addObject("isInterest", false);
		}
		
		List<Event> feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag_id, 1);
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new DictUtil());
		return mav;
	}
	
	@RequestMapping("/{tag_id}/page/{page}")
	public ModelAndView getFeedsByTagOfPage(@PathVariable("tag_id") int tag_id, 
											@PathVariable("page") int page,
											HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nextpage");
		
		Tag tag = tagService.getTagByID(tag_id);
		if (tag == null) {
			mav.setViewName("404");
			return mav;
		}
		
		User user = (User) session.getAttribute("user");
		
		List<Event> feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag_id, page);
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new DictUtil());
		return mav;
	}

	@ResponseBody
	@RequestMapping("/feeds")
	public Map<String, Object> getFeedsByTagOfPage(@RequestParam("tag_name") String tag_name, 
											@RequestParam(defaultValue = "1", value = "page") int page,
											HttpSession session) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		Tag tag = tagService.getTagByName(tag_name);
		if (tag == null) {
			return ret;
		}
		
		User user = (User) session.getAttribute("user");
		
		List<Event> feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag.getId(), page);
		ret.put("feeds", feeds);
		ret.put("dic", new DictUtil());
		return ret;
	}

	@RequestMapping("/feeds/page")
	public ModelAndView getFeedsByTagOfPage(@RequestParam("tag_name") String tag_name, 
											@RequestParam("refer") String refer, 
											@RequestParam(defaultValue = "1", value = "page") int page,
											HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("feedpage");
		
		Tag tag = tagService.getTagByName(tag_name);
		if (tag == null) {
			mav.setViewName("404");
			return mav;
		}
		
		User user = (User) session.getAttribute("user");
		
		List<Event> feeds = feedService.getFeedsByTagOfPage(user!=null?user.getId():0, tag.getId(), page);
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new DictUtil());
		mav.addObject("refer", refer);
		return mav;
	}
	
	/**
	 * 对某个标签感兴趣
	 */
	@ResponseBody
	@RequestMapping("/{tag_id}/interest")
	public Map<String, Object> interest(@PathVariable("tag_id") int tag_id, HttpSession session) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		User user = (User) session.getAttribute("user");
		interestService.interestInTag(user.getId(), tag_id);
				
		ret.put("status", Property.SUCCESS_INTEREST);
		return ret;
	}
	
	
	/**
	 * 
	 */
	@ResponseBody
	@RequestMapping("/{tag_id}/undointerest")
	public Map<String, Object> undoInterest(@PathVariable("tag_id") int tag_id, HttpSession session) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		User user = (User) session.getAttribute("user");
		interestService.undoInterestInTag(user.getId(), tag_id);
		
		ret.put("status", Property.SUCCESS_INTEREST_UNDO);
		return ret;
	}
	
	
	
}
