package com.moefilm.web.service.Impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.RedisService;

public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate; 
	
	@Resource(name="redisTemplate")
	private ListOperations<String, Integer> listOps;
	
	public void saveCache(String key, int eventId) {
		listOps.leftPush(key, eventId);
	}

	public void deleteCache(String key, int eventId) {
		listOps.remove(key, 0, eventId);
	}

	public Long countCache(String key) {
		return listOps.size(key);
	}
	
	public List<Integer> fetchCache(String key) {
		return listOps.range(key, 0, listOps.size(key) - 1);
	}
	
	public List<Integer> fetchCache(String key, long start, long step) {
		return listOps.range(key, start, start+step);
	}

	public void saveCacheAll(String key, List<Integer> eventIds) {
		Iterator<Integer> eventId = eventIds.iterator();
		int count = 0;
		while (eventId.hasNext() && count < FeedService.FEED_COUNT) {
			saveCache(key, eventId.next());
			count++;
		}
	}

}
