package com.moefilm.web.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 日志信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_posts")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 作者ID
     */
	@TableField("post_author")
	private Integer postAuthor;
    /**
     * 内容
     */
	@TableField("post_content")
	private String postContent;
    /**
     * 标题
     */
	@TableField("post_title")
	private String postTitle;
    /**
     * 摘要
     */
	@TableField("post_excerpt")
	private String postExcerpt;
    /**
     * 状态
     */
	@TableField("post_status")
	private Integer postStatus;
    /**
     * 评论状态
     */
	@TableField("comment_status")
	private Integer commentStatus;
	@TableField("post_pwd")
	private String postPwd;
    /**
     * 评论数
     */
	@TableField("comment_count")
	private Integer commentCount;
    /**
     * 点赞数
     */
	@TableField("like_count")
	private Integer likeCount;
    /**
     * 分享次数
     */
	@TableField("share_count")
	private Integer shareCount;
    /**
     * 链接
     */
	@TableField("post_url")
	private String postUrl;
    /**
     * 标签
     */
	@TableField("post_tags")
	private String postTags;
    /**
     * 日志相册
     */
	@TableField("post_album")
	private Integer postAlbum;
    /**
     * 日志封面
     */
	@TableField("post_cover")
	private String postCover;
    /**
     * 发布时间戳
     */
	@TableField("post_ts")
	private Date postTs;
    /**
     * 最新发布时间戳
     */
	@TableField("post_lastts")
	private Date postLastts;


	
}
