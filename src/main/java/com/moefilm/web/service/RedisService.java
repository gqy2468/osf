package com.moefilm.web.service;

import java.util.List;

public interface RedisService {
	
	public void saveCache(String key, int eventId);

	public void deleteCache(String key, int eventId);

	public Long countCache(String key);
	
	public List<Integer> fetchCache(String key);
	
	public List<Integer> fetchCache(String key, long start, long step);

	public void saveCacheAll(String key, List<Integer> eventIds);

}
