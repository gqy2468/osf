package com.moefilm.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.moefilm.web.service.TagService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("osf_albums")
public class Album implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	@TableField("user_id")
	private Integer userId;
	@TableField("album_title")
	private String albumTitle;
	@TableField("album_desc")
	private String albumDesc;
	@TableField("photos_count")
	private Integer photosCount;
	private Integer status;
	private String cover;
	@TableField("album_tags")
	private String albumTags;
	@TableField("create_ts")
	private Date createTs;
	@TableField("last_add_ts")
	private Date lastAddTs;

	@TableField(exist = false)
	private Integer likeCount;
	@TableField(exist = false)
	private Integer shareCount;
	@TableField(exist = false)
	private Integer commentCount;
	@TableField(exist = false)
	private List<Photo> photos;
	@TableField(exist = false)
	private List<Tag> albumTagList;
	public List<Tag> getAlbumTagList() {
		return TagService.toList(albumTags);
	}
	public void setAlbumTagList(List<Tag> albumTagList) {
		this.albumTagList = albumTagList;
		albumTags = TagService.toString(albumTagList);
	}
}
