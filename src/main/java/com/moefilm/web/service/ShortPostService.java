package com.moefilm.web.service;

import java.util.Map;

public interface ShortPostService extends PostService {
	
	Map<String, Object> newPost(Integer author, String content);
	
	long count(Integer userId);
}
