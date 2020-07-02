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
 * 评论信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-01-21
 */
@Setter
@Getter
@ToString
@TableName("osf_comments")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 评论对象类型（post, album,...）
     */
	@TableField("comment_object_type")
	private Integer commentObjectType;
    /**
     * 评论对象ID
     */
	@TableField("comment_object_id")
	private Integer commentObjectId;
    /**
     * 评论用户ID
     */
	@TableField("comment_author")
	private Integer commentAuthor;
    /**
     * 评论用户名
     */
	@TableField("comment_author_name")
	private String commentAuthorName;
    /**
     * 评论内容
     */
	@TableField("comment_content")
	private String commentContent;
    /**
     * 父级评论ID
     */
	@TableField("comment_parent")
	private Integer commentParent;
    /**
     * 父级评论用户名
     */
	@TableField("comment_parent_author_name")
	private String commentParentAuthorName;
    /**
     * 父级评论用户ID
     */
	@TableField("comment_parent_author")
	private Integer commentParentAuthor;
    /**
     * 评论时间戳
     */
	@TableField("comment_ts")
	private Date commentTs;

	@TableField(exist = false)
	private String commentAuthorAvatar;
	
}
