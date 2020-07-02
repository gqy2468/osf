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
 * 媒体设置信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_settings")
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 媒体编号
     */
	@TableField("media_id")
	private Integer mediaId;
    /**
     * 设置对象类型
     */
	@TableField("object_type")
	private String objectType;
    /**
     * 设置对象键
     */
	@TableField("object_key")
	private String objectKey;
    /**
     * 设置字段名
     */
	@TableField("setting_name")
	private String settingName;
    /**
     * 设置字段值
     */
	@TableField("setting_value")
	private String settingValue;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Date updateTime;


	
}
