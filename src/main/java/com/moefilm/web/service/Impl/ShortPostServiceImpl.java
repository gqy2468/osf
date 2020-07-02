package com.moefilm.web.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.PostMapper;
import com.moefilm.web.model.Post;
import com.moefilm.web.model.ShortPost;
import com.moefilm.web.service.ShortPostService;
import com.moefilm.web.util.Property;

@Service("shortPostService")
public class ShortPostServiceImpl extends PostServiceImpl implements ShortPostService {

	@Autowired
	private PostMapper postMapper;
	
	public Map<String, Object> newPost(Integer author, String content){		
		Map<String, Object> map = new HashMap<String, Object>();
		if(content == null || content.length() == 0){
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}
		ShortPost spost = new ShortPost();
		spost.setPostAuthor(author);
		spost.setPostContent(content);
		spost.setId(savePost(spost));
		map.put("spost", spost);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}
	
	@Override
	public long count(Integer userId){
		EntityWrapper<Post> wrapper = new EntityWrapper<>();
		wrapper.eq("post_author", userId);
		wrapper.isNull("post_title");
		return postMapper.selectCount(wrapper);
	}
}
