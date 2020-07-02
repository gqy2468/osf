package com.moefilm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.moefilm.web.constant.AlbumStatus;
import com.moefilm.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.moefilm.web.model.Album;
import com.moefilm.web.model.Photo;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;

@Controller
@RequestMapping("/album")
public class AlbumController {
	
	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private InterestService interestService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;
	
	@RequestMapping("/{id}/photos")
	public ModelAndView album(@PathVariable("id") int id, HttpSession session) {
		User me = (User) session.getAttribute("user");
		
		ModelAndView mav = new ModelAndView();
		Album album = albumService.getAlbum(id);
		mav.addObject("album", album);
		
		User author = albumService.getAuthorOfALbum(id); 
		if (author == null) {
			mav.setViewName("404");
			return mav;
		}
		mav.addObject("u", author);
		
		mav.addObject("follow", followService.isFollowing(me==null?0:me.getId(), author.getId()));

		List<Photo> photos = album.getPhotos();
		if (!CollectionUtils.isEmpty(photos)) {
			Photo photo = photos.get(0);
			mav.addObject("comments", commentService.getComments("photo", photo.getId()));
		}
		
		mav.setViewName("album/index");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/{id}")
	public Album getAlbumInfo(@PathVariable("id") int id) {
		return albumService.getAlbum(id);
	}
	
	/*
	 * 相册上传页面
	 * 指定album
	 */
	@RequestMapping(value="/{album_id}/upload", method=RequestMethod.GET)
	public String albumUploadPage(@PathVariable("album_id") int id) {
		return "album/upload";
	}
	
	/*
	 * 相册上传页面
	 * 未指定album
	 */
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public ModelAndView albumUploadPage(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("user");
		int album_id = albumService.getToBeReleasedAlbum(user.getId());
		List<Photo> photos = albumService.getPhotosOfAlbum(album_id);
		session.setAttribute("album_id", album_id);
		mav.addObject("photos", photos);
		mav.setViewName("album/upload");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
	public Map<String, Object> deleteAlbum(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<String, Object>();
		albumService.delAlbum(id);
		map.put("status", Property.SUCCESS_ALBUM_DELETE);
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/photo/{id}", method=RequestMethod.POST)
	public Map<String, Object> deletePhoto(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<String, Object>();
		albumService.deletePhoto(id);
		map.put("status", Property.SUCCESS_PHOTO_DELETE);
		return map;
	}
	
	/*
	 * 上传图片到相册
	 */
	@ResponseBody
	@RequestMapping(value="/{album_id}/upload/photo",  method=RequestMethod.POST)
	public Map<String, Object> albumUpload(@PathVariable("album_id") int album_id, 
										   @RequestParam("uploader_input") MultipartFile img, 
										   HttpSession session) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}
		
		User user = (User) session.getAttribute("user");
		//检查相册是否属于用户
		if(!Property.SUCCESS_ALBUM_ALLOWED.equals(albumService.checkUserOfAlbum(album_id, user.getId()))) {
			map.put("status", Property.ERROR_ALBUM_PERMISSIONDENIED);
			return map;
		}
		//上传图片
		Map<String, Object> photoMap = albumService.newPhoto(album_id, img, null);
		map.put("status", photoMap.get("status"));
		map.put("photo", photoMap.get("photo"));
		
		return map;
	}
	
	private Album toAlbum(String params) {
		Album album = new Album();
		JSONObject root = JSONObject.parseObject(params);
		album.setAlbumDesc(root.getString("album_desc"));

		JSONArray photos = root.getJSONArray("photos");
		if(photos.size() > 0) {
			JSONObject object = (JSONObject) photos.get(0);
			album.setCover(albumService.getKeyofPhoto(Integer.parseInt(object.getString("id"))));

			List<Photo> photos2upd = new ArrayList<Photo>();
			album.setPhotos(photos2upd);
			for(int i=0; i<photos.size(); i++) {
				object = (JSONObject) photos.get(i);
				int photo_id = Integer.parseInt(object.getString("id"));
				String photo_desc = object.getString("desc");
				Photo photo = new Photo();
				photo.setId(photo_id);
				photo.setDesc(photo_desc);
				photos2upd.add(photo);
			}
			album.setPhotosCount(photos2upd.size());
		}

		JSONArray tags = root.getJSONArray("tags");
		if (tags.size() > 0) {
			List<Tag> tag_list = new ArrayList<Tag>();
			for(int i=0; i<tags.size(); i++) {
				Tag t = new Tag();
				t.setTag(tags.getString(i));
				tag_list.add(t);
			}
			album.setAlbumTagList(tag_list);
		}

		return album;
	}
	
	/*
	 * 创建相册
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST) 
	public Map<String, Object> createAlbum(@RequestBody String params, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Album album = toAlbum(params);
		if (album.getPhotosCount() <= 0) {
			map.put("album", album);
			map.put("status", Property.ERROR_ALBUM_CREATE);
			return map;
		}
		album.setId((Integer)session.getAttribute("album_id"));
		User user = (User)session.getAttribute("user");
		album.setUserId(user.getId());
		
		List<Tag> tags = albumService.updateAlbum(album);
		
		int eventId = eventService.newEvent(DictUtil.OBJECT_TYPE_ALBUM, album);
		if(eventId !=0 ) {
			feedService.push(user.getId(), eventId);
		}
		
		for(Tag tag : tags) {
			List<Integer> i_users = interestService.getUsersInterestedInTag(tag.getId());
			for(int u : i_users) {
				feedService.push(u, eventId);
			}
			feedService.cacheFeed2Tag(tag.getId(), eventId);
		}
		
		map.put("album", album);
		map.put("status", Property.SUCCESS_ALBUM_UPDATE);
		return map;
	}
	
	/*
	 * 未指定相册
	 * 临时创建相册
	 */
	@ResponseBody
	@RequestMapping(value="/upload/photo", method=RequestMethod.POST)
	public Map<String, Object> uploadPhoto(@RequestParam("uploader_input") MultipartFile file,
										    HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (file.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "图片文件内容不能为空！");
			return map;
		}
		String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!"jpg".equals(suffix) && !"jpeg".endsWith(suffix) && !"png".endsWith(suffix)) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "请上传支持jpg,jpeg,png文件！");
			return map;
		}
		if (file.getSize() > 5 * 1024 * 1024) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "最大可上传5M！");
			return map;
		}
		
		User user = (User) session.getAttribute("user");
		Integer album_id = (Integer)session.getAttribute("album_id");
		
		//创建临时相册
		if (album_id == null || album_id == 0) {
			Map<String, Object> albumMap = albumService.newAlbum(user.getId(), null, null, AlbumStatus.ALBUM_STAUS_TOBERELEASED,null);
			if(!Property.SUCCESS_ALBUM_CREATE.equals(albumMap.get("status")) ) {
				map.put("status", albumMap.get("status"));
				return map;
			}
			album_id = ((Album)albumMap.get("album")).getId();
			session.setAttribute("album_id", album_id);
		}
		
		//上传图片
		Map<String, Object> photoMap = albumService.newPhoto(album_id, file, "");
		map.put("status", photoMap.get("status"));
		map.put("warn", photoMap.get("warn") == null ? "" : photoMap.get("warn"));
		map.put("error", photoMap.get("error") == null ? "" : photoMap.get("error"));
		Photo photo = (Photo)photoMap.get("photo");
		map.put("id", photo == null ? 0 : photo.getId());
		map.put("key", photo == null ? "" : photo.getKey());
		return map;
	}
	
	/*
	 * post 中图片上传
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/upload/postphoto", method=RequestMethod.POST)
	public Map<String, Object> postPhotoUpload(@RequestParam("uploader_input") MultipartFile file,
										    HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (file.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "文件内容不能为空！");
			return map;
		}
		String imgName = file.getOriginalFilename();
        String suffix = imgName.substring(imgName.lastIndexOf(".") + 1);
		if (!"jpg".equals(suffix) && !"jpeg".endsWith(suffix) && !"png".endsWith(suffix)) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "请上传jpg,jpeg,png文件！");
			return map;
		}
		if (file.getSize() > 5 * 1024 * 1024) {
			map.put("status", Property.ERROR_PHOTO_UPLOAD);
			map.put("error", "最大可上传5M文件！");
			return map;
		}
		
		map = albumService.uploadPhoto(file);
		session.setAttribute("post_cover", map.get("key"));
		return map;
	}
	
	/**
	 * 上传头像 
	 * @param img
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload/avatar", method=RequestMethod.POST)
	public Map<String, Object> avatarUpload(@RequestParam("avatar_file") MultipartFile img,
										    HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(img.isEmpty()) {
			map.put("status", Property.ERROR_PHOTO_EMPTY);
			return map;
		}
		
		map = albumService.uploadPhoto(img);
		
		albumService.saveImgToLocal(img, (String)map.get("key"));
		
		session.setAttribute("temp_avatar", map.get("key"));
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/cropavatar", method=RequestMethod.POST)
	public Map<String, Object> cropAvatar(@RequestParam("x") int x,
										 @RequestParam("y") int y,
										 @RequestParam("width") int width,
										 @RequestParam("height") int height,
									     HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		String key = (String) session.getAttribute("temp_avatar");
		if(key == null || key.length() == 0 || width <= 0 || height <= 0){
			map.put("status", Property.ERROR_AVATAR_CHANGE);
			return map;
		}
		
		String avatar_img = albumService.cropAvatar(key, x, y, width, height);
		String status = userService.changeAvatar(((User)session.getAttribute("user")).getId(), avatar_img);
		if(Property.SUCCESS_AVATAR_CHANGE.equals(status)) {
			((User)session.getAttribute("user")).setUserAvatar(avatar_img);			
		}
		
		map.put("status", status);
		return map;
	}
	
	/**
	 * 上传转换后的3d图片或视频
	 * @param fileData
	 * @param fileName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload/media", method=RequestMethod.POST)
	public Map<String, Object> uploadMedia(@RequestParam("file_data") MultipartFile fileData, 
										  @RequestParam("file_name") String fileName) {
		Map<String, Object> map = new HashMap<String, Object>();
    	Photo photo = albumService.getPhotoByKey(fileName);
    	if (photo != null && photo.getId() > 0) {
    		byte[] fileBytes = null;
			try {
				fileBytes = fileData.getBytes();
			} catch (IOException e) {
				map.put("error", "File data empty!");
				return map;
			}
    		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			Map<String, Object> uploadMap = albumService.uploadPhoto(fileBytes, fileType);
			if (uploadMap.get("status").toString().equals(Property.SUCCESS_PHOTO_CREATE)) {
				String key = uploadMap.get("key").toString();
				photo.setKey(key);
				photo.setSrc(fileName);
				if (albumService.updatePhotoInfo(photo) <= 0) {
					map.put("error", "Update photo info error!");
				} else {
					int albumId = photo.getAlbumId();
					Album album = albumService.getAlbum(albumId);
					if (album != null && album.getCover().equals(fileName)) {
						if (albumService.updateAlbumCover(albumId, key).equals(Property.ERROR_ALBUM_UPDCOVER)) {
							map.put("error", "Update album cover error!");
						}
		            } else {
		            	map.put("error", "Album cover not exist!");
					}
				}
			}
        } else {
			map.put("error", "Photo not exist!");
        }
		return map;
	}
}
