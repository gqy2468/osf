package com.moefilm.web.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DictUtil {
	public static final String REDIS_MEDIA_GENERATE = "media:generate:";
	
	public static final int OBJECT_TYPE_POST = 0;
	public static final int OBJECT_TYPE_PHOTO = 1;
	public static final int OBJECT_TYPE_ALBUM = 2;
	public static final int OBJECT_TYPE_FOLLOWING = 3;
	public static final int OBJECT_TYPE_SHORTPOST = 4;
	public static final int OBJECT_TYPE_USER = 5;
	public static final int OBJECT_TYPE_VIDEO = 6;
	public static final int OBJECT_TYPE_AUDIO = 7;
	
	public static final int NOTIFY_TYPE_SYSTEM = 0;
	public static final int NOTIFY_TYPE_COMMENT = 1;
	public static final int NOTIFY_TYPE_COMMENT_REPLY = 2;
	public static final int NOTIFY_TYPE_LIKE = 3;
	public static final int NOTIFY_TYPE_FOLLOW = 4;
	public static final int NOTIFY_TYPE_USER = 5;
	public static final int NOTIFY_TYPE_VIDEO = 6;
	public static final int NOTIFY_TYPE_AUDIO = 7;
	
	public static final int AUDIO_TYPE_UPLOAD = 0;
	public static final int AUDIO_TYPE_SYNTH = 1;
	
	private int object_type_post = 0;
	private int object_type_photo = 1;
	private int object_type_album = 2;
	private int object_type_following = 3;
	private int object_type_shortpost = 4;
	private int object_type_user = 5;
	private int object_type_video = 6;
	private int object_type_audio = 7;
	
	private  int notify_type_system = 0;
	private  int notify_type_comment = 1;
	private  int notify_type_comment_reply = 2;
	private  int notify_type_like = 3;
	private  int notify_type_follow = 4;
	private  int notify_type_user = 5;
	private  int notify_type_video = 6;
	private  int notify_type_audio = 7;
	
	public static String toNotifyTypeDesc(int notify_type){
		String type = null;
		
		switch (notify_type) {
		case NOTIFY_TYPE_SYSTEM:
			type= "system";
			break;
		case NOTIFY_TYPE_COMMENT:
			type="comment";
			break;
		case NOTIFY_TYPE_COMMENT_REPLY:
			type="comment_reply";
			break;
		case NOTIFY_TYPE_LIKE:
			type="like";
			break;
		case NOTIFY_TYPE_FOLLOW:
			type="follow";
			break;
		case NOTIFY_TYPE_USER:
			type="user";
			break;
		case NOTIFY_TYPE_VIDEO:
			type="video";
			break;
		case NOTIFY_TYPE_AUDIO:
			type="audio";
			break;
		
		default:
			break;
		}
		return type;
	}
	
	public static String checkType(int object_type){
		if(object_type == OBJECT_TYPE_ALBUM){
			return "album";
		} else if(object_type == OBJECT_TYPE_PHOTO){
			return "photo";
		} else if(object_type == OBJECT_TYPE_POST) {
			return "post";
		} else if(object_type == OBJECT_TYPE_SHORTPOST){
			return "shortpost";
		} else if(object_type == OBJECT_TYPE_FOLLOWING){
			return "following";
		} else if(object_type == OBJECT_TYPE_USER) {
			return "user";
		} else if(object_type == OBJECT_TYPE_VIDEO) {
			return "video";
		} else if(object_type == NOTIFY_TYPE_AUDIO) {
			return "audio";
		} else {
			return null;
		}
	}
}
