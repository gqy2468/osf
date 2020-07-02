<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="type" content="photo">
    <meta name="id" content="${album.id }">
	<#if user?exists>
		<meta name="isLogin" content="true"/>
	<#else>
		<meta name="isLogin" content="false"/>
	</#if>
	<title>相册详情</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">

	<script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/semantic.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/basic.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/code.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/album.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/comment.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/follow.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/login.js"></script>

</head>
<body>
	<#include "../topbar.ftl">
	<#include "../login_modal.ftl">
	<div class="container">
		<div class="row">
			<div class="span8">
				<div id="imgcontainer">
					<img src="" alt="" id="mainimg" aid="${album.id }" class="ui centered image"> 
				</div>
				<div class="action">
					<#if u.id == user.id>
					<div class="ui circular icon basic button">						
					  <i class="share alternate icon"></i>	
					</div>							
					<div class="ui circular orange icon button">
					  <i class="weibo icon" id="weiboshare"></i>
					</div>
					<div class="ui circular blue icon button">
					  <i class="qq icon" id="qqshare"></i>
					</div>
					<div class="ui circular green icon button">
					  <i class="wechat icon" id="wechatshare"></i>
					</div>
					<div class="ui circular icon basic button post trash">
						<i class="trash outline icon"></i>
					</div>
					</#if>
				</div>
			</div>
			<div class="span4">
				<div id="rightside">
					<div class="album metas">
						<div class="meta author">
							<a href="/user/${u.id }"><img class="ui avatar image" src="${img_base_url}${u.userAvatar }"></a>
							<span>${u.userName }</span>
							<#if user?exists>
								<#if user.id != u.id>
									<#if follow?exists>
										<span class="ui tiny basic button follow" following="${u.id }">已关注</span>
									<#else>
										<span class="ui inverted tiny yellow button follow" following="${u.id }">+关注</span> 
									</#if>
								</#if>
							<#else>
								<span class="ui inverted tiny yellow button follow" following="${u.id }">+关注</span>
							</#if>
						</div>
						
						<!---- <jsp:include page="/popup_usercard/${u.id }" flush="true"></jsp:include> ---->
						
						<div class="ui tiny images meta" id="imgbox">
							<#list album.photos as photo>
								<a href="javascript:;">
									<img src="${img_base_url}${photo.key }${album_thumbnail }" source="${img_base_url}${photo.src }${album_thumbnail }" id="preview_photo_${photo.id }">
								</a>
							</#list>
						</div>
						<#if album.photos[0].src?default("")?trim?length gt 1>
					    	<div class="ui tiny button" id="switch">查看原图片</div>
						</#if>
						<div class="meta tags">
							<#list album.albumTagList as tag>
                        		<a class="ui label" href="/tag/${tag.id }">${tag.tag }</a>
                        	</#list>
						</div>
					</div>

				</div>
			</div>	
		</div>
	</div>
	<!--  -->
		
	<div class="album comments">
		<div class="container">
			<div class="row">
				<div class="span8">
					<div class="ui comments" id="comments">
		
					  <div id="replyarea">
						  <form class="ui reply form" id="replyform">
						    <div class="field">
						      <textarea id="replycontent"></textarea>
						    </div>
							<div class="ui tiny blue button" id="replybtn">
							  评论
							</div>
						  </form>
					  </div>

					  <#include "../comment/index.ftl">
					  <!-- comment list -->
					</div>
					<!-- end comment -->
				</div>
			</div>
		</div>
	</div>
	
	<#include "../trash_tip.ftl">
	
	<script>
	$(document).ready(function(){
		var first_img_id = $('#imgbox img:first').attr('id').split('_')[2];
		$('meta[name=id]').attr('content', first_img_id);
		$('#mainimg').attr('src', $('#imgbox img:first').attr('src').split('?')[0]);
	});
	</script>
</body>
</html>