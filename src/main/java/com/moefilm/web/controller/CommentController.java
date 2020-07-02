package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.moefilm.web.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.Notification;
import com.moefilm.web.model.User;
import com.moefilm.web.service.CommentService;
import com.moefilm.web.service.NotificationService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	 
	@ResponseBody
	@RequestMapping("/{id}")
	public Map<String, Object> comment(@PathVariable("id") int id) {
		Comment comment = commentService.findCommentByID(id);
		Map<String, Object> ret = new HashMap<String, Object>();
		if(comment == null) {
			ret.put("status", Property.ERROR);
		}else {
			ret.put("status", Property.SUCCESS);
			ret.put("comment", comment);
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	public Map<String, String> createComment(@RequestParam("comment_object_type") int commentObjectType,
											 @RequestParam("comment_object_id") int commentObjectId,
											 @RequestParam("comment_content") String commentContent,
											 @RequestParam("comment_parent") int commentParent,
											 HttpSession session) {
		User user = (User)session.getAttribute("user");
		User comment_parent_author = new User();
		if(commentParent !=0 ){
			comment_parent_author = commentService.getCommentAuthor(commentParent);
		}
		
		Map<String, String> ret = commentService.newComment(commentObjectType, 
				commentObjectId, 
															user.getId(), 
															user.getUserName(), 
															commentContent, 
															commentParent,
															comment_parent_author.getId(),
															comment_parent_author.getUserName());
		Notification notification = new Notification(DictUtil.NOTIFY_TYPE_COMMENT,
													  Integer.parseInt(ret.get("id")),
													  commentObjectType,
													  commentObjectId,
													  userService.getAuthor(commentObjectType, commentObjectId).getId(),
													  user.getId()
													  );
		
		if(commentParent!=0) {
			//reply notification
			notification.setNotifyType(DictUtil.NOTIFY_TYPE_COMMENT_REPLY);
			notification.setNotifiedUser(comment_parent_author.getId());
			notificationService.doNotify(notification);
		} else {
			//comment notification
			notificationService.doNotify(notification);
		}
		
		
		ret.put("avatar", userService.findById(user.getId()).getUserAvatar());
		ret.put("author_id", String.valueOf(user.getId()));
		ret.put("author_name", user.getUserName());
		ret.put("reply_to_author", String.valueOf(comment_parent_author.getId()));
		ret.put("reply_to_authorname", comment_parent_author.getUserName());
		return ret;
	}
	
	@RequestMapping(value="/{type}/{id}")
	public ModelAndView getComments(@PathVariable("type") String type, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("comment/index");
		mav.addObject("comments", commentService.getComments(type, id));
		return mav;
	}
	
	/**
	 * feed附属的comments
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/attach/{type}/{id}")
	public ModelAndView getAttachComments(@PathVariable("type") String type, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("comment/attach_comments");
		mav.addObject("comments", commentService.getComments(type, id, 0, 5));
		return mav;
	}
	
}
