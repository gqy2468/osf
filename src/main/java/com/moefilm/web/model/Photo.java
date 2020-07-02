package com.moefilm.web.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("osf_photos")
public class Photo {
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String key;
	private String src;
	@TableField("album_id")
	private Integer albumId;
	private String desc;
	private String md5;
	private Date ts;
}
