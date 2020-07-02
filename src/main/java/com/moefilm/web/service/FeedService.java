package com.moefilm.web.service;

import java.util.List;

import com.moefilm.web.model.Event;

public interface FeedService {

	public static final Integer FEED_COUNT_PER_PAGE = 2;
	public static final Integer FEED_COUNT = 200;//feed缓存量
	
	void push(Integer userId, Integer eventId);
	
	/**
	 * 缓存feed到对应标签列表序列中
	 * 
	 * @param tagId
	 * @param eventId
	 */
	void cacheFeed2Tag(Integer tagId, Integer eventId);
	
	void cacheFeeds2Tag(Integer tagId, List<Integer> eventIds);
	
	List<Event> getFeeds(Integer userId);
	
	List<Event> getFeeds(Integer userId, Integer count);
	
	List<Event> getFeedsOfPage(Integer userId, Integer num);
	
	List<Event> addUserInfo(List<Event> events);
	
	void updLikeCount(Integer userId, List<Event> events);
	
	void addCommentCount(List<Event> events);
	
	void delete(Integer userId, Integer eventId);
	
	boolean deleteAll(Integer eventId);

	/**
	 * 获取tag标签的feed
	 * 
	 * @param userId
	 * @param tagId
	 * @return
	 */
	List<Event> getFeedsByTag(Integer userId, Integer tagId);
	
	List<Event> getFeedsByTag(Integer userId, Integer tagId, Integer count);
	
	List<Event> getFeedsByTagOfPage(Integer userId, Integer tagId, Integer num);
	
	/**
	 * feed推荐算法
	 * 这里只是简单实现, 可自己扩充
	 * @param userId
	 * @return 推荐feed列表 - List<Event>
	 */
	List<Event> getRecommendFeeds(Integer userId);
	
	void codeStart(Integer userId);
}
