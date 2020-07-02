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
 * 关注信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_followings")
public class Following implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
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
     * 创建时间戳
     */
	private Date ts;


	
}
