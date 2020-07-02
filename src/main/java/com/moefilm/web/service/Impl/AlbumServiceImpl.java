package com.moefilm.web.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.moefilm.web.constant.AlbumStatus;
import com.moefilm.web.mapper.AlbumMapper;
import com.moefilm.web.mapper.PhotoMapper;
import com.moefilm.web.model.*;
import com.moefilm.web.service.*;
import com.moefilm.web.util.DictUtil;
import com.moefilm.web.util.Property;
import com.moefilm.web.util.UploadUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("albumService")
public class AlbumServiceImpl extends BaseServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    @Qualifier("userService")
    private UserServiceImpl userService;

    @Autowired
    @Qualifier("tagService")
    private TagServiceImpl tagService;

    @Autowired
    @Qualifier("relationService")
    private RelationServiceImpl relationService;

    @Autowired
    @Qualifier("eventService")
    private EventServiceImpl eventService;

    @Autowired
    @Qualifier("feedService")
    private FeedServiceImpl feedService;

    public String getMediaType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.substring(contentType.indexOf('/') + 1);
    }

    public Map<String, Object> newAlbum(Integer userId, String title, String desc, Integer status, String cover) {
        Map<String, Object> map = new HashMap<String, Object>();
        Album album = new Album();
        album.setUserId(userId);
        album.setAlbumTitle(title);
        album.setAlbumDesc(desc);
        album.setStatus(status);
        album.setCover(cover);
        Integer id = albumMapper.insert(album);
        if (id != 0) {
            album.setId(id);
            map.put("album", album);
            map.put("status", Property.SUCCESS_ALBUM_CREATE);
        } else {
            map.put("status", Property.ERROR_ALBUM_CREATE);
        }
        return map;
    }

    public void saveImgToLocal(MultipartFile img, String key) {
        try {
            BufferedImage imgBuf = ImageIO.read(img.getInputStream());
            String classpath = AlbumService.class.getClassLoader().getResource("").getPath();
            ImageIO.write(imgBuf, getMediaType(img), new File(classpath + "/tmp/" + key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> uploadPhoto(byte[] img, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (img == null || img.length <= 0 || StringUtils.isEmpty(type)) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            return map;
        }
        String md5 = new String(Hex.encodeHex(DigestUtils.md5Digest(img)));
        Photo entity = new Photo();
        entity.setMd5(md5);
        Photo exist = photoMapper.selectOne(entity);
        if (exist != null && exist.getId() > 0) {
            map.put("status", Property.SUCCESS_PHOTO_CREATE);
            map.put("warn", "不能重复上传相同文件");
            map.put("key", exist.getKey());
            map.put("md5", md5);
            map.put("id", exist.getId());
            map.put("link", Property.IMG_BASE_URL + exist.getKey());
            return map;
        }
        String key = UUID.randomUUID().toString() + "." + type;
        String etag = UploadUtil.uploadFile(img, key);
        if (etag == null || etag.length() == 0) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            return map;
        } else {
            map.put("key", key);
            map.put("md5", md5);
            map.put("link", Property.IMG_BASE_URL + key);
            map.put("status", Property.SUCCESS_PHOTO_CREATE);
        }
        return map;
    }

    public Map<String, Object> uploadPhoto(MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
			String type = getMediaType(file);
			int angle = getRotateAngle(file);
			if (angle != 0) {
				map = uploadPhoto(rotatePhonePhoto(file, type, angle), type);
			} else {
				map = uploadPhoto(file.getBytes(), type);
			}
        } catch (IOException e) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            map.put("error", e.getMessage());
        }
        return map;
    }

    public Map<String, Object> newPhoto(Integer albumId, MultipartFile file, String desc) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
			String type = getMediaType(file);
			int angle = getRotateAngle(file);
			if (angle != 0) {
				map = uploadPhoto(rotatePhonePhoto(file, type, angle), type);
			} else {
				map = uploadPhoto(file.getBytes(), type);
			}
        } catch (IOException e) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            map.put("error", e.getMessage());
        }
        if(!Property.SUCCESS_PHOTO_CREATE.equals(map.get("status")) || map.get("warn") != null) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            return map;
        }
        Photo photo = new Photo();
        photo.setKey(map.get("key").toString());
        photo.setAlbumId(albumId);
        photo.setDesc(desc);
        photo.setMd5(map.get("md5").toString());
        Integer photo_id = photoMapper.insert(photo);
        if (photo_id == 0) {
            map.put("status", Property.ERROR_PHOTO_CREATE);
            return map;
        }
        photo.setId(photo_id);
        map.put("photo", photo);
        return map;
    }
	
	/**
     * 获取图片正确显示需要旋转的角度（顺时针）
     */
    private int getRotateAngle(MultipartFile file) {
        int angle = 0;
        Metadata metadata = null;
        
        try {
            metadata = JpegMetadataReader.readMetadata(file.getInputStream());
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {
                // Exif信息中方向　　
                int orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
                // 原图片的方向信息
                if (6 == orientation ) {
                    //6旋转90
                	angle = 90;
                } else if( 3 == orientation) {
                    //3旋转180
                	angle = 180;
                } else if( 8 == orientation) {
                    //8旋转90
                	angle = 270;
                }
            }
        } catch(JpegProcessingException e) {
            e.printStackTrace();
        } catch(MetadataException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
       
        return angle;
    }
    
    /**
     * 旋转手机照片
     */
    private byte[] rotatePhonePhoto(MultipartFile file, String type, int angle) {
        BufferedImage src;
        try {
            src = ImageIO.read(file.getInputStream());
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angle);
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angle), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(res, type, bos);
			return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算旋转参数
     */
    private Rectangle calcRotatedSize(Rectangle src,int angle) {
        if (angle > 90) {
            if (angle / 9%2 ==1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angle = angle % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width ) / 2 ;
        double len = 2 * Math.sin(Math.toRadians(angle) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angle)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

    public String checkUserOfAlbum(Integer albumId, Integer userId) {
        Album album = albumMapper.selectById(albumId);
        if(album == null || album.getUserId() != userId) {
            return Property.ERROR_ALBUM_PERMISSIONDENIED;
        } else {
            return Property.SUCCESS_ALBUM_ALLOWED;
        }
    }

    //获取用户的待发布相册
    public Integer getToBeReleasedAlbum(Integer userId) {
        Album entity = new Album();
        entity.setUserId(userId);
        entity.setStatus(AlbumStatus.ALBUM_STAUS_TOBERELEASED);
        Album album = albumMapper.selectOne(entity);
        return album == null ? 0 : album.getId();
    }

    public List<Photo> getPhotosOfAlbum(Integer albumId) {
        EntityWrapper<Photo> wrapper = new EntityWrapper<>();
        wrapper.eq("album_id", albumId);
        wrapper.orderBy("ts", true);
        return photoMapper.selectList(wrapper);
    }
	
	public String getKey(Integer id) {
		Photo photo = photoMapper.selectById(id);
		return photo == null ? null : photo.getKey();
	}
	
	public List<String> getKeys(List<Integer> ids) {
		List<Photo> photoes = photoMapper.selectBatchIds(ids);
		List<String> keys = new ArrayList<>();
		if (!CollectionUtils.isEmpty(photoes)) {
			for (Photo photo : photoes) {
				keys.add(photo.getKey());
			}
		}
		return keys;
	}

    public String updateAlbumDesc(Integer albumId, String albumDesc) {
        Album entity = new Album();
        entity.setId(albumId);
        entity.setAlbumDesc(albumDesc);
        entity.setStatus(AlbumStatus.ALBUM_STAUS_NORMAL);
        Integer effRows = albumMapper.updateById(entity);
        if(effRows == 1) {
            return Property.SUCCESS_ALBUM_UPDATE;
        } else {
            return Property.ERROR_ALBUM_UPDDESC;
        }
    }

    public String updatePhotoDesc(List<Photo> photos) {
        for (Photo photo: photos) {
            photoMapper.updateById(photo);
        }
        return Property.SUCCESS_ALBUM_UPDATE;
    }

    public String updateAlbumCover(Integer albumId, String cover) {
        Album entity = new Album();
        entity.setId(albumId);
        entity.setCover(cover);
        Integer effRows = albumMapper.updateById(entity);
        if(effRows==1) {
            return Property.SUCCESS_ALBUM_UPDATE;
        } else {
            return Property.ERROR_ALBUM_UPDCOVER;
        }
    }

    public String updatePhotosCount(Integer albumId, Integer count) {
        Album entity = new Album();
        entity.setId(albumId);
        entity.setPhotosCount(count);
        Integer effRows = albumMapper.updateById(entity);
        if(effRows==1) {
            return Property.SUCCESS_ALBUM_UPDATE;
        } else {
            return Property.ERROR_ALBUM_UPDCOVER;
        }
    }

    public String updateAlbumInfo(Album album) {
        albumMapper.updateById(album);
        return Property.SUCCESS_ALBUM_UPDATE;
    }

    @SuppressWarnings("unchecked")
    public List<Tag> updateAlbum(Album album) {
        //save tag
        Map<String, Object> tagsmap = tagService.newTags(album.getAlbumTagList());
        album.setAlbumTagList((List<Tag>)tagsmap.get("tags"));

        updateAlbumInfo(album);
        updatePhotoDesc(album.getPhotos());

        //save relation
        for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
            relationService.newRelation(
                    RelationServiceImpl.RELATION_TYPE_ALBUM,
                    album.getId(),
                    tag.getId()
            );
        }
        return (List<Tag>)tagsmap.get("tags");
    }

    public List<Album> getAlbumsOfUser(Integer uid) {
        EntityWrapper<Album> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", uid);
        wrapper.last(" limit 0,10");
        return albumMapper.selectList(wrapper);
    }

    public List<Album> getAlbumsOfUser(Integer uid, Integer page, Integer size) {
        EntityWrapper<Album> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", uid);
        Integer offset = (page - 1) * size;
        wrapper.last(" limit " + offset + "," + size);
        return albumMapper.selectList(wrapper);
    }

    public Album getAlbum(Integer id) {
        Album album = albumMapper.selectById(id);
        List<Photo> photos = getPhotosOfAlbum(id);
        album.setPhotos(photos);
        return album;
    }

    public String getKeyofPhoto(Integer id) {
        Photo photo = photoMapper.selectById(id);
        return photo == null ? "" : photo.getKey();
    }

    public User getAuthorOfALbum(Integer id) {
        Album album = albumMapper.selectById(id);
        Integer user_id = album == null ? 0 : album.getUserId();
        return userService.findById(user_id);
    }

    public User getAuthorOfPhoto(Integer id) {
        Album album = albumMapper.selectById(id);
        User user = new User();
        if(album != null){
            user.setId(album.getUserId());
        }
        return user;
    }

    public String cropAvatar(String key, Integer x, Integer y, Integer width, Integer height) {
        String classpath = AlbumService.class.getClassLoader().getResource("").getPath();
        try {
            File ori_img = new File(classpath+"/tmp/"+key);

            BufferedImage croped_img = Thumbnails.of(ImageIO.read(ori_img))
                    .sourceRegion(x, y, width, height)
                    .size(200, 200).asBufferedImage();
            String img_type = key.split("\\.")[1];
            //convert bufferedimage to inputstream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(croped_img, img_type, bos);

            UploadUtil.delFileInBucket(key);

            String new_key = UUID.randomUUID().toString()+"."+img_type;
            if( UploadUtil.uploadFile(bos.toByteArray(), new_key) != null ){
                if(ori_img.exists()){
                    ori_img.delete();
                }
                return new_key;
            } else {
                return key;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }

    public void delAlbum(Integer id) {
        List<Photo> photos = getPhotosOfAlbum(id);
        if (albumMapper.deleteById(id) > 0) {
            EntityWrapper<Photo> wrapper = new EntityWrapper<>();
            wrapper.eq("album_id", id);
            photoMapper.delete(wrapper);
            if (!CollectionUtils.isEmpty(photos)) {
                for (Photo photo : photos) {
                    delPhotoInBucket(photo.getKey());
                }
            }
            Event event = eventService.getEvent(DictUtil.OBJECT_TYPE_ALBUM, id);
            if(event != null){
                eventService.delete(DictUtil.OBJECT_TYPE_ALBUM, id);
                feedService.delete(event.getUserId(), event.getId());
            }
        }
    }

    public void deletePhoto(Integer id) {
        photoMapper.deleteById(id);
    }

    public void delPhotoInBucket(String key) {
        UploadUtil.delFileInBucket(key);
    }

    public Photo getPhotoByKey(String key) {
        Photo photo = new Photo();
        photo.setKey(key);
        return photoMapper.selectOne(photo);
    }

    public Integer updatePhotoInfo(Photo photo) {
        return photoMapper.updateById(photo);
    }
}
