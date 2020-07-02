package com.moefilm.web.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.CommentMapper;
import com.moefilm.web.model.User;
import com.moefilm.web.service.CommentService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Service("commentService")
public class CommentServiceImpl implements CommentService {
	
	public static final String TYPE_POST = "post";
	public static final String TYPE_PHOTO = "photo";
	public static final String TYPE_ALBUM = "album";
	public static final String TYPE_SPOST = "spost";
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	public Map<String, String> newComment(Integer commentObjectType, Integer commentObjectId,
							 Integer commentAuthor, String commentAuthorName, 
							 String commentContent, Integer commentParent, 
							 Integer commentParentAuthor, String commentParentAuthorName){
		
		Map<String, String> ret = new HashMap<String, String>();
		if(commentContent == null || commentContent.length() == 0) {
			ret.put("status", Property.ERROR_COMMENT_EMPTY);
			return ret;
		}
		//不支持的评论类型
		if(DictUtil.checkType(commentObjectType) == null){
			ret.put("status", Property.ERROR_COMMENT_TYPE);
			return ret;
		}

		Comment comment = new Comment();
		comment.setCommentObjectType(commentObjectType);
		comment.setCommentObjectId(commentObjectId);
		comment.setCommentAuthor(commentAuthor);
		comment.setCommentAuthorName(commentAuthorName);
		comment.setCommentContent(commentContent);
		comment.setCommentParent(commentParent);
		comment.setCommentParentAuthor(commentParentAuthor);
		comment.setCommentParentAuthorName(commentParentAuthorName);
		Integer id = commentMapper.insert(comment);
		ret.put("status", Property.SUCCESS_COMMENT_CREATE);
		ret.put("id", String.valueOf(id));
		return ret;
		
	}
	
	public Comment findCommentByID(Integer id) {
		return commentMapper.selectById(id);
	}
	
	public List<Comment> getComments(String type, Integer id) {
		return getComments(type, id, 0, 10);
	}
	
	public List<Comment> getComments(String type, Integer id, Integer offset, Integer count) {
		if(type == null || type.length() == 0)
			return null;
		List<Comment> comments = null;
		EntityWrapper<Comment> wrapper = new EntityWrapper<>();
		wrapper.eq("comment_object_id", id);
		if(type.equals(TYPE_POST)) {
			wrapper.eq("comment_object_type", DictUtil.OBJECT_TYPE_POST);
		} else if(type.equals(TYPE_PHOTO)) {
			wrapper.eq("comment_object_type", DictUtil.OBJECT_TYPE_PHOTO);
		} else if(type.equals(TYPE_ALBUM)){
			wrapper.eq("comment_object_type", DictUtil.OBJECT_TYPE_ALBUM);
		} else if(type.equals(TYPE_SPOST)){
			wrapper.eq("comment_object_type", DictUtil.OBJECT_TYPE_SHORTPOST);
		}
		wrapper.orderBy("comment_ts", false);
		wrapper.last(" limit " + offset + "," + count);
		comments = commentMapper.selectList(wrapper);
		
		//add avatars;
		if(comments != null && comments.size() !=0) {
			for(Comment comment: comments) {
				comment.setCommentAuthorAvatar(userService.findById(comment.getCommentAuthor()).getUserAvatar());
			}
		}
		return comments;
	}
	
	public User getCommentAuthor(Integer commentId){
		Comment comment = commentMapper.selectById(commentId);
		if (comment == null) return null;
		User user = new User();
		user.setId(comment.getCommentAuthor());
		user.setUserName(comment.getCommentAuthorName());
		return user;
	}
	
	public Integer getCommentsCount(Integer objectType, Integer objectId) {
		EntityWrapper<Comment> wrapper = new EntityWrapper<>();
		wrapper.eq("comment_object_id", objectId);
		wrapper.eq("comment_object_type", objectType);
		return commentMapper.selectCount(wrapper);
	}
}
