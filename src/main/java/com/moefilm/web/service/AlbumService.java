package com.moefilm.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.moefilm.web.model.Album;
import com.moefilm.web.model.Photo;
import com.moefilm.web.model.Tag;
import com.moefilm.web.model.User;

public interface AlbumService extends BaseService<Album> {

	String getMediaType(MultipartFile file);

	Map<String, Object> newAlbum(Integer userId, String title, String desc, Integer status, String cover);

	void saveImgToLocal(MultipartFile img, String key);

	Map<String, Object> uploadPhoto(byte[] img, String type);

	Map<String, Object> uploadPhoto(MultipartFile file);

	Map<String, Object> newPhoto(Integer albumId, MultipartFile file, String desc);

	String checkUserOfAlbum(Integer albumId, Integer userId);

	//获取用户的待发布相册
	Integer getToBeReleasedAlbum(Integer userId);

	List<Photo> getPhotosOfAlbum(Integer albumId);
	
	String getKey(Integer id);
	
	List<String> getKeys(List<Integer> ids);

	String updateAlbumDesc(Integer albumId, String albumDesc);

	String updatePhotoDesc(List<Photo> photos);

	String updateAlbumCover(Integer albumId, String cover);

	String updatePhotosCount(Integer albumId, Integer count);

	String updateAlbumInfo(Album album);

	List<Tag> updateAlbum(Album album);

	List<Album> getAlbumsOfUser(Integer uid);

	List<Album> getAlbumsOfUser(Integer uid, Integer page, Integer size);

	Album getAlbum(Integer id);

	String getKeyofPhoto(Integer id);

	User getAuthorOfALbum(Integer id);

	User getAuthorOfPhoto(Integer id);

	String cropAvatar(String key, Integer x, Integer y, Integer width, Integer height);

	void delAlbum(Integer id);

	void deletePhoto(Integer id);

	void delPhotoInBucket(String key);

	Photo getPhotoByKey(String key);

	Integer updatePhotoInfo(Photo photo);
	
}
