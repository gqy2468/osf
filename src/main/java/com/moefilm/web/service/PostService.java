package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import com.moefilm.web.model.Post;
import com.moefilm.web.model.User;

public interface PostService {

	Map<String, Object> newPost(Integer author, String title, String content, 
						Integer postStatus, Integer commentStatus, String paramTags, String postCover);
	
	Post findPostByID(Integer id);
	
	List<Post> findPostsByIDs(List<Integer> ids);
	
	List<Post> findPostsOfUser(Integer userId);
	
	List<Post> findPostsOfUser(Integer userId, Integer page, Integer size);
	
	List<Post> findPostsOfUser(Integer id, Object[] fromto);
	
	List<Post> findPostsOfUser(Integer id, String orderby, Object[] fromto);
	
	User getAuthorOfPost(Integer id);
	
	long count(Integer userId);
	
	void deletePost(Integer id);
}
