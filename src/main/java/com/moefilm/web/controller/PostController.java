package com.moefilm.web.controller;

import java.util.ArrayList;
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

import com.moefilm.web.model.Post;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.InterestService;
import com.moefilm.web.service.LikeService;
import com.moefilm.web.service.PostService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private LikeService likeService;
	
	@RequestMapping("/{id}")
	public ModelAndView post(@PathVariable("id") int id, HttpSession session) {
		User me = (User) session.getAttribute("user");
		
		ModelAndView mav = new ModelAndView();
		User author = postService.getAuthorOfPost(id);
		mav.addObject("u", author);
		if (author == null) {
			mav.setViewName("404");
			return mav;
		}
		mav.addObject("follow", followService.isFollowing(me==null?0:me.getId(), author.getId()));
		mav.addObject("is_like", likeService.isLike(me==null?0:me.getId(), DictUtil.OBJECT_TYPE_POST, id));
		mav.addObject("post", postService.findPostByID(id));
		mav.setViewName("post/index");
		return mav;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createPost() {
		return "post/create";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Map<String, Object> createPost(					
						@RequestParam("title") String title,
						@RequestParam("content") String content,
						@RequestParam("post_status") int post_status,
						@RequestParam("comment_status") int comment_status,
						@RequestParam("tags") String param_tags,
						HttpSession session) {
				
		User user = (User)session.getAttribute("user");
		String post_cover = (String) session.getAttribute("post_cover");
		session.removeAttribute("post_cover");
		//1 save post
		Map<String, Object> map = postService.newPost(user.getId(), 
													  title, 
													  content, 
													  post_status, 
													  comment_status,
													  param_tags,
													  post_cover);
		String status = (String)map.get("status");
		Post post = (Post)map.get("post");
		
		//2 add event 
		if(Property.SUCCESS_POST_CREATE.equals(status)) {
			int event_id = eventService.newEvent(DictUtil.OBJECT_TYPE_POST, post);
			
			//3 push to followers
			if(event_id !=0 ) {
				feedService.push(user.getId(), event_id);
			}
			
			//4 push to users who follow the tags in the post
			List<Tag> tags = (ArrayList<Tag>)map.get("tags");
			for(Tag tag : tags) {
				List<Integer> i_users = interestService.getUsersInterestedInTag(tag.getId());
				for(int u : i_users) {
					feedService.push(u, event_id);
				}
				//5 cache feeds to tag list
				feedService.cacheFeed2Tag(tag.getId(), event_id);
			}
			
		}
		return map;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public Map<String, Object> deletePost(@RequestParam("id") int id){
		Map<String, Object> map = new HashMap<String, Object>();
		postService.deletePost(id);
		map.put("status", Property.SUCCESS_POST_DELETE);
		return map;
	}
}
