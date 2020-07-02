package com.moefilm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.moefilm.web.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.service.AlbumService;
import com.moefilm.web.service.FollowService;
import com.moefilm.web.service.PostService;
import com.moefilm.web.service.UserService;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/user")
public class UserController {
	
    protected final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final static int DEFAULT_PAGE_SIZE = 4;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Object> valOps;
	
	@RequestMapping("/{id}")
	public ModelAndView collection(@PathVariable("id") int id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = userService.findById(id);
		mav.addObject("u", user);
		User me = (User) session.getAttribute("user");
		mav.addObject("follow", followService.isFollowing(me==null ? 0 : me.getId(), id));
		mav.addObject("counter", userService.getCounterOfFollowAndShortPost(id));
		
		List<Album> albums = albumService.getAlbumsOfUser(id, 1, DEFAULT_PAGE_SIZE);
		mav.addObject("albums", albums);
		mav.setViewName("user/index");
		return mav;
	}
	
	@RequestMapping({"/{id}/{type}", "/{id}/{type}/{page}", "/{id}/{type}/{page}/{size}"})
	public ModelAndView collection(@PathVariable("id") Integer id, @PathVariable("type") String type, 
			@PathVariable(required = false) Integer page, @PathVariable(required = false) Integer size, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		page = page == null ? 1 : page;
		size = size == null ? DEFAULT_PAGE_SIZE : size;
		
		if ("post".equals(type)) {
			List<Post> posts = postService.findPostsOfUser(id, page, size);
			mav.addObject("posts", posts);
		}
		if ("album".equals(type)) {
			List<Album> albums = albumService.getAlbumsOfUser(id, page, size);
			mav.addObject("albums", albums);
		}
		mav.setViewName("user/index");
		return mav;
	}
	
	/*
	 * 还原成原图片
	 */
	@ResponseBody
	@RequestMapping(value="/album/restore/{album_id}",  method=RequestMethod.POST)
	public Map<String, Object> albumRestore(@PathVariable("album_id") int albumId, 
			   HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		//检查相册是否属于用户
		if(!Property.SUCCESS_ALBUM_ALLOWED.equals(albumService.checkUserOfAlbum(albumId, user.getId()))) {
			map.put("status", Property.ERROR_ALBUM_PERMISSIONDENIED);
			return map;
		}
		Album album = albumService.getAlbum(albumId);
		List<Photo> photos = album.getPhotos();
		int count = 0;
		for (Photo photo : photos) {
			if (StringUtils.isEmpty(photo.getSrc())) continue;
			String key = photo.getKey();
			String src = photo.getSrc();
			photo.setKey(src);
			photo.setSrc("");
			if (albumService.updatePhotoInfo(photo) <= 0) {
				logger.error("Update photo info error!");
			} else {
				if (album != null && album.getCover().equals(key)) {
					if (albumService.updateAlbumCover(albumId, src).equals(Property.ERROR_ALBUM_UPDCOVER)) {
						logger.error("Update album cover error!");
					} else {
						albumService.delPhotoInBucket(key);
					}
	            } else {
	            	logger.error("Album cover not exist!");
				}
			}
			count++;
		}
		if (count <= 0) {
			map.put("status", Property.ERROR_ALBUM_RESTORE);
			return map;
		}
		map.put("status", Property.SUCCESS);
		return map;
	}
}
