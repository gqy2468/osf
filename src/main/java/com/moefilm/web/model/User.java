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
 * 用户信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 用户邮箱
     */
	@TableField("user_email")
	private String userEmail;
    /**
     * 用户密码
     */
	@TableField("user_pwd")
	private String userPwd;
    /**
     * 注册日期
     */
	@TableField("user_registered_date")
	private Date userRegisteredDate;
    /**
     * 用户状态
     */
	@TableField("user_status")
	private Integer userStatus;
	@TableField("user_activationKey")
	private String userActivationKey;
    /**
     * 用户头像
     */
	@TableField("user_avatar")
	private String userAvatar;
    /**
     * 用户描述
     */
	@TableField("user_desc")
	private String userDesc;
	@TableField("resetpwd_key")
	private String resetpwdKey;


	
}
