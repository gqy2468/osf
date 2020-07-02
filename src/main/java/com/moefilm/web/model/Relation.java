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
 * 标签关联关系表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_relations")
public class Relation implements Serializable {

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
     * 标签ID
     */
	@TableField("tag_id")
	private Integer tagId;
    /**
     * 创建时间戳
     */
	@TableField("add_ts")
	private Date addTs;


	
}
