package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Comment;
import com.moefilm.web.model.User;

public interface CommentService {
	
	public Map<String, String> newComment(Integer commentObjectType, Integer commentObjectId,
							 Integer commentAuthor, String commentAuthorName, 
							 String commentContent, Integer commentParent, 
							 Integer commentParentAuthor, String commentParentAuthorName);
	
	public Comment findCommentByID(Integer id);
	
	public List<Comment> getComments(String type, Integer id);
	
	public List<Comment> getComments(String type, Integer id, Integer offset, Integer count);
	
	public User getCommentAuthor(Integer commentId);
	
	public Integer getCommentsCount(Integer objectType, Integer objectId);
}
