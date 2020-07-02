package com.moefilm.web.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.moefilm.web.service.TagService;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 事件信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_events")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 对象类型
     */
	@TableField("object_type")
	private Integer objectType;
    /**
     * 对象ID
     */
	@TableField("object_id")
	private Integer objectId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 用户名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 用户头像
     */
	@TableField("user_avatar")
	private String userAvatar;
    /**
     * 点赞数
     */
	@TableField("like_count")
	private Integer likeCount;
    /**
     * 分享数
     */
	@TableField("share_count")
	private Integer shareCount;
    /**
     * 评论数
     */
	@TableField("comment_count")
	private Integer commentCount;
    /**
     * 标题
     */
	private String title;
    /**
     * 概述
     */
	private String summary;
    /**
     * 内容
     */
	private String content;
    /**
     * 标签
     */
	private String tags;
    /**
     * 关注用户ID
     */
	@TableField("following_user_id")
	private Integer followingUserId;
    /**
     * 关注用户名
     */
	@TableField("following_user_name")
	private String followingUserName;
    /**
     * 粉丝用户ID
     */
	@TableField("follower_user_id")
	private Integer followerUserId;
    /**
     * 粉丝用户名
     */
	@TableField("follower_user_name")
	private String followerUserName;
    /**
     * 创建时间戳
     */
	private Date ts;

	@TableField(exist = false)
	private boolean isLike;

	@TableField(exist = false)
	private List<Tag> tagList;
	public List<Tag> getTagList() {
		return TagService.toList(tags);
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
		tags = TagService.toString(tagList);
	}
	
}
