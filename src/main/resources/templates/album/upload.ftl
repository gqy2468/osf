<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>相册</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
 	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">

	<script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/basic.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/code.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/tag.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/album.js"></script>
	
	<style>
	.uploader-button input {
		width: 70px;
		height:20px;
		opacity: 0;
		
	}
	.uploader-button.ui.button{
		background: url("/img/uploader_button.png") no-repeat;
	}
	#uploadedphotos textarea {
		border: 0;
		resize: none;
		width: 100%;
	}
	</style>
</head>
<body>
	<#include "../topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span8">
				<div class="ui three cards" id="uploadedphotos">
				  <#if photos?exists && (photos?size > 0)>
				  <#list photos as photo>
					  <div class="card" id="card${photo.id }">
					    <a class="image" href="#">
					      <img src="<c:url value="${img_base_url }${photo.key }?imageView2/1/w/200/h/200" />">
					    </a>
					    <div class="content">
					    	<textarea rows="" cols="" placeholder="添加描述..."></textarea>
					    </div>
					    <div class="extra meta">
							<a href="#"><i class="delete icon"></i>删除</a>
					    </div>
					  </div>
				  </#list>
				  </#if>
				</div>	
				<div id="uploadarea">
					<span class="uploader-button ui button">
						<input type="file" id="uploader_input" name="uploader_input"/>
					</span>
					<span>
						选择图片上传,支持jpg,jpeg,png,最大可上传5M
					</span>										
				</div> <!-- end uploadarea -->
			</div>	<!-- end span8 -->
			
			<div class="span4">
				<div class="ui form">
					<div class="field">
					  	<label>标签:</label>
					  	<div class="tags">
					  		<input type="text" class="tag-input" id="tags" placeholder="添加标签，多个以英文逗号隔开">
					  		<div class="tagfield">
					  			<input type="button" class="ui button small" value="角色" />
					  			<input type="button" class="ui button small" value="场景" />
					  			<input type="button" class="ui button small" value="表情" />
					  			<input type="button" class="ui button small" value="动作" />
						    </div>
					  	</div>
					</div>
				
					<div class="field">
						<label>描述:</label>
						<textarea rows="" cols="" id="desc"></textarea>
					</div>
				</div>
				<div class="ui tiny blue button" id="saveAlbumBtn">
					保存
				</div>
			</div> <!-- end span4 -->
		</div>
	</div>

</body>
</html>