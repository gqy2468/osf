package com.moefilm.web.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.moefilm.web.mapper.PostMapper;
import com.moefilm.web.model.Event;
import com.moefilm.web.model.Post;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.service.EventService;
import com.moefilm.web.service.FeedService;
import com.moefilm.web.service.PostService;
import com.moefilm.web.service.RelationService;
import com.moefilm.web.service.TagService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Service("postService")
public class PostServiceImpl implements PostService {

	public static final int POST_STATUS_PUB = 0;	//公开
	public static final int POST_STATUS_PRV = 1;	//私密
	public static final int POST_STATUS_SAVED = 2;	//保存
	public static final int POST_STATUS_EDIT = 3;	//编辑
	
	public static final int COMMENT_STATUS_ALLOWED = 0;		//允许评论
	public static final int COMMENT_STATUS_NOTALLOWED = 1;	//不允许评论
	
	public static final int POST_SUMMARY_LENGTH = 200;
	
	@Autowired
	private RelationService relationService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private PostMapper postMapper;
	
	@SuppressWarnings({ "unchecked", "static-access" })
	@Transactional
	public Map<String, Object> newPost(Integer author, String title, String content, 
						Integer postStatus, Integer commentStatus, String paramTags, String postCover) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(author == null || 
		   title == null || title.length() == 0 ||
		   content == null || content.length() == 0) {
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}
		
		if (postStatus == null)
			postStatus = POST_STATUS_PUB;
		if (postStatus < 0 || postStatus > 3) {
			map.put("status", Property.ERROR_POST_STATUS);
			return map;
		}
		
		if (commentStatus == null)
			commentStatus = COMMENT_STATUS_ALLOWED;
		if(commentStatus != 0 && commentStatus != 1) {
			map.put("status", Property.ERROR_COMMENT_STATUS);
			return map;
		}
		
		Post post = new Post();
		post.setPostAuthor(author);
		post.setPostTitle(title);
		post.setPostExcerpt(getSummary(content));
		post.setPostContent(content);
		post.setPostStatus(postStatus);
		post.setCommentStatus(commentStatus);
		post.setLikeCount(0);
		post.setShareCount(0);
		post.setCommentCount(0);
		post.setPostCover(postCover);
		
		if(paramTags != null && paramTags.length() != 0) {				
			Map<String, Object> tagsmap = tagService.newTags(TagService.toList(paramTags));
			
			post.setPostTags(TagService.toString((List<Tag>)tagsmap.get("tags")));
			int id = savePost(post);
			post.setId(id);
			
			for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
				relationService.newRelation(
					RelationService.RELATION_TYPE_POST, 
					post.getId(), 
					tag.getId()
				);
			}
			
			map.put("tags", tagsmap.get("tags"));
		} else {
			int id = savePost(post);
			post.setId(id);
			map.put("tags", new ArrayList<Tag>());
		}
				
		map.put("post", post);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}
	
	protected int savePost(Post post){
		return postMapper.insert(post);
	}
	
	public Post findPostByID(Integer id) {
		return postMapper.selectById(id);
	}
	
	public List<Post> findPostsByIDs(List<Integer> ids) {
		return postMapper.selectBatchIds(ids);
	}
	
	public List<Post> findPostsOfUser(Integer userId) {
		return findPostsOfUser(userId, 0, 10);
	}
	
	public List<Post> findPostsOfUser(Integer userId, Integer page, Integer size) {
		EntityWrapper<Post> wrapper = new EntityWrapper<>();
		wrapper.eq("post_author", userId);
		wrapper.isNotNull("post_title");
		Integer offset = (page - 1) * size;
		wrapper.last(" limit " + offset + "," + size);
		return postMapper.selectList(wrapper);
	}
	
	public List<Post> findPostsOfUser(Integer id, Object[] fromto) {
		return null;
	}
	
	public List<Post> findPostsOfUser(Integer id, String orderby, Object[] fromto) {
		return null;
	}
	
	public static String getSummary(String postContent) {
		if (postContent == null || postContent.length() == 0) {
			return null;
		}
		Document doc = Jsoup.parse(postContent);
		String text = doc.text();
		return text.substring(0, text.length() > POST_SUMMARY_LENGTH ? POST_SUMMARY_LENGTH : text.length());
	}
	
	public User getAuthorOfPost(Integer id) {
		Post post = findPostByID(id);
		return post == null ? null : userService.findById(post.getPostAuthor());
	}
	
	public long count(Integer userId) {
		EntityWrapper<Post> wrapper = new EntityWrapper<>();
		wrapper.eq("post_author", userId);
		wrapper.isNotNull("post_title");
		return postMapper.selectCount(wrapper);
	}
	
	public void deletePost(Integer id) {
		if (postMapper.deleteById(id) > 0) {
			Event event = eventService.getEvent(DictUtil.OBJECT_TYPE_POST, id);
			if(event != null){
				eventService.delete(DictUtil.OBJECT_TYPE_POST, id);
				feedService.delete(event.getUserId(), event.getId());
			}
		}
	}
}
