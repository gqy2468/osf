package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moefilm.web.model.Event;
import com.moefilm.web.model.Relation;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.AlbumService;
import com.moefilm.web.service.CommentService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.InterestService;
import com.moefilm.web.service.LikeService;
import com.moefilm.web.service.PostService;
import com.moefilm.web.service.RelationService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;

@Service("feedService")
public class FeedServiceImpl extends RedisServiceImpl implements FeedService {
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private EventServiceImpl eventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService likeService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private RelationService relationService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private AlbumService albumService;
	
	public void push(Integer userId, Integer eventId) {
		List<Integer> followers = followService.getFollowerIDs(userId);
		followers.add(userId);	//add self
		if(followers != null && followers.size()!=0) {
			for(Integer follower: followers) {
				saveCache("feed:user:"+follower, eventId);
			}
		}
	}
	
	/**
	 * 缓存feed到对应标签列表序列中
	 * 
	 * @param tagId
	 * @param eventId
	 */
	public void cacheFeed2Tag(Integer tagId, Integer eventId) {
		saveCache("feed:tag:"+tagId, eventId);
	}
	
	public void cacheFeeds2Tag(Integer tagId, List<Integer> eventIds) {
		saveCacheAll("feed:tag:"+tagId, eventIds);
	}
	
	private List<Integer> getEventIDs(Integer userId, Integer start, Integer count) {
		return fetchCache("feed:user:"+userId, start, count);
	}
	
	public List<Event> getFeeds(Integer userId) {
		return getFeeds(userId, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeeds(Integer userId, Integer count){
		List<Integer> eventIds = getEventIDs(userId, 0, count-1);
		return decorateFeeds(userId, eventIds);
	}
	
	private List<Event> decorateFeeds(Integer userId, List<Integer> eventIds){
		List<Event> events = new ArrayList<Event>();
		if(eventIds != null && eventIds.size()!=0 ) {
			events = eventService.getEventsWithIDs(eventIds);
			addUserInfo(events);
			updLikeCount(userId, events);
			addCommentCount(events);
		}
		return events;
	}
	
	public List<Event> getFeedsOfPage(Integer userId, Integer num) {
		List<Integer> eventIds = fetchCache("feed:user:"+userId, 
											FEED_COUNT_PER_PAGE*(num-1), 
											FEED_COUNT_PER_PAGE-1);
		return decorateFeeds(userId, eventIds);
		
	}
	
	public List<Event> addUserInfo(List<Event> events) {
		if(events == null || events.size() == 0)
			return events;
		for(Event event : events) {
			User user = userService.findById(event.getUserId());
			if (user == null) continue;
			event.setUserName(user.getUserName());
			event.setUserAvatar(user.getUserAvatar());
		}
		return events;
	}
	
	public void updLikeCount(Integer userId, List<Event> events){
		if(events == null || events.size() == 0)
			return;
		for(Event event : events) {
			event.setLikeCount((int)likeService.likersCount(event.getObjectType(), 
															 event.getObjectId()));
			event.setLike(likeService.isLike(userId, 
												event.getObjectType(), 
												event.getObjectId()));
		}
	}
	
	public void addCommentCount(List<Event> events){
		if(events == null || events.size() == 0)
			return;
		for(Event event : events) {
			event.setCommentCount(commentService.getCommentsCount(event.getObjectType(), 
															 	   event.getObjectId()));
		}
	}
	
	public void delete(Integer userId, Integer eventId) {
		deleteCache("feed:user:"+userId, eventId);
	}
	
	public boolean deleteAll(Integer eventId) {
		Event event = eventService.getEvent(eventId);
		if (event == null) return false;
		if (eventService.delete(eventId) > 0) {
			deleteCache("feed:user:"+event.getUserId(), eventId);
			if(DictUtil.OBJECT_TYPE_POST == event.getObjectType()) {
				postService.deletePost(event.getObjectId());
			} else if(DictUtil.OBJECT_TYPE_ALBUM == event.getObjectType()) {
				albumService.delAlbum(event.getObjectId());
			} else if(DictUtil.OBJECT_TYPE_PHOTO == event.getObjectType()) {
			} else if(DictUtil.OBJECT_TYPE_SHORTPOST == event.getObjectType()){
				postService.deletePost(event.getObjectId());
			}
		}
		return true;
	}

	/**
	 * 获取tag标签的feed
	 * 
	 * @param userId
	 * @param tagId
	 * @return
	 */
	public List<Event> getFeedsByTag(Integer userId, Integer tagId) {
		return getFeedsByTag(userId, tagId, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeedsByTag(Integer userId, Integer tagId, Integer count){
		List<Integer> eventIds = getEventIDsByTag(tagId, 0, count-1);
		return decorateFeeds(userId, eventIds);
	}
	
	public List<Event> getFeedsByTagOfPage(Integer userId, Integer tagId, Integer num) {
		List<Integer> eventIds = fetchCache("feed:tag:"+tagId, 
				FEED_COUNT_PER_PAGE*(num-1), 
				FEED_COUNT_PER_PAGE-1);
		return decorateFeeds(userId, eventIds);
		
	}
	
	private List<Integer> getEventIDsByTag(Integer tagId, Integer start, Integer count) {
		return fetchCache("feed:tag:"+tagId, start, count);
	}
	
	/**
	 * feed推荐算法
	 * 这里只是简单实现, 可自己扩充
	 * @param userId
	 * @return 推荐feed列表 - List<Event>
	 */
	public List<Event> getRecommendFeeds(Integer userId) {
		return addUserInfo(eventService.getEventsHasPhoto(0, 20));
	}
	
	public void codeStart(Integer userId) {
		if (countCache("feed:user:"+userId) != 0) {
			return ;
		}
		
		List<Tag> tags = interestService.getTagsUserInterestedIn(userId);
		List<Relation> relations = relationService.getRelationsInTags(tags);
		List<Event> events = eventService.getEventsWithRelations(relations);
		
		if (events == null || events.size() == 0) {
			events = eventService.getEvents(0, FEED_COUNT_PER_PAGE);
		}
			
		List<Integer> eventIds = new ArrayList<Integer>();
		for (Event event : events) {
			eventIds.add(event.getId());
		}
		saveCacheAll("feed:user:"+userId, eventIds);

	}
}
