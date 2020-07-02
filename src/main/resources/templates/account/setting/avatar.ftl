<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>头像设置</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/jquery.Jcrop.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
	
	<script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.Jcrop.min.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/basic.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/code.js"></script>
	
	
	<style>
	/* Apply these styles only when #preview-pane has
	   been placed within the Jcrop widget */
	.jcrop-holder #preview-pane {
	  display: block;
	  position: absolute;
	  top: 0px;
	  right: -200px;
	  
	}
	
	/* The Javascript code will set the aspect ratio of the crop
	   area based on the size of the thumbnail preview,
	   specified here */
	#preview-pane .preview-container {
	  width: 150px;
	  height: 150px;
	  overflow: hidden;
	  margin-bottom: 20px;
	}
	
	.change_avatar{
		position: relative;
		margin-top: 60px;
	}
	.change_avatar .button{
		position: relative;
		width: 90px;
		height: 30px;

	}
	#avatar_file{
		position: absolute;
		top:0;
		left: 0;
		width: 90px;
		height: 30px;
		opacity: 0;
		text-align: center;
	}
	.crop_avatar_area{
		display: none;
		margin-top: 40px;
	}
	#avatar_save, #avatar_cancle{
		width: 100%;
		margin-bottom: 10px;
	}
	</style>
	

</head>
<body>
	<#include "../../topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span4">
				<div class="ui vertical menu">
				  <a class="item" href="/account/setting/info">
				    个人信息
				  </a>
				  <a class="active teal item" href="/account/setting/avatar">
				    头像
				  </a>
				  <a class="item" href="/account/setting/security">
				    账户安全
				  </a>
				</div>
			
			</div>
			<div class="span8">
				<div class="ui header">
					头像
				</div>
				<div class="ui divider"></div>
				
				<div class="row">
					<div class="span2">
						<div class="avatar">
							<img class="ui small circular image" src="${img_base_url }${user.userAvatar }">
						</div>					
					</div>
					<div class="span2">
						<div class="change_avatar">
							<div class="ui tiny button">更改头像</div>
							<input type="file" id="avatar_file" name="avatar_file" class="ui tiny button" />
						</div>					
					</div>
				</div>
				
				<div class="crop_avatar_area">
				  <div id="target_img_cnt">	
				  	<img src="/img/avatar.jpg" id="target" alt="[Jcrop Example]" />
				  </div>
				  
				  <div id="preview-pane">
			    	<div class="preview-container">
			      		<img src="/img/avatar.jpg" class="jcrop-preview" alt="Preview" />
			    	</div>
					<div class="ui tiny blue button" id="avatar_save">
				  		保存
					</div>
					<div class="ui tiny  button" id="avatar_cancle">
				  		取消
					</div>						
			  	 </div>					
			   </div>
				
			</div>
		</div>
	
	</div>

<script type="text/javascript" src="${request.contextPath}/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/avatar.js"></script>
</body>
</html>