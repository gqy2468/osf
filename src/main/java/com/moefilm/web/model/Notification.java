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
 * 通知信息表
 * </p>
 *
 * @author moefilm.com
 * @since 2020-02-13
 */
@Setter
@Getter
@ToString
@TableName("osf_notifications")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 通知类型
     */
	@TableField("notify_type")
	private Integer notifyType;
    /**
     * 通知ID
     */
	@TableField("notify_id")
	private Integer notifyId;
    /**
     * 通知对象类型
     */
	@TableField("object_type")
	private Integer objectType;
    /**
     * 通知对象ID
     */
	@TableField("object_id")
	private Integer objectId;
	@TableField("notified_user")
	private Integer notifiedUser;
    /**
     * 通知人
     */
	private Integer notifier;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 创建时间戳
     */
	private Date ts;
	
	//以下属性用于通知展现
	@TableField(exist = false)
	private String notifierName;
	@TableField(exist = false)
	private String notifierAvatar;
	@TableField(exist = false)
	private String objectTitle;
	
	public Notification() {
	}

	public Notification(int notifyType, int notifyId, int objectType, int objectId, int notifiedUser, int notifier) {
		this.notifiedUser = notifiedUser;
		this.notifyType = notifyType;
		this.notifyId = notifyId;
		this.objectId = objectId;
		this.objectType = objectType;
		this.notifier = notifier;
	}
	
}
