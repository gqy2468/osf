<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">

<#if user?exists>
	<meta name="isLogin" content="true"/>
<#else>
	<meta name="isLogin" content="false"/>
</#if>
<title>探索</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
  <script src="${request.contextPath}/js/jquery.js"></script>
  <script src="${request.contextPath}/js/jquery.row-grid.js"></script>
  <script src="${request.contextPath}/js/semantic.js"></script>
  <script src="${request.contextPath}/js/basic.js"></script>
  <script src="${request.contextPath}/js/code.js"></script>
  <script src="${request.contextPath}/js/explore.js"></script>
  <script src="${request.contextPath}/js/follow.js"></script>
  <script src="${request.contextPath}/js/login.js"></script>
  
</head>
<body>
 	<#include "topbar.ftl">
	<#include "login_modal.ftl">
	<div class="explore">
		<div class="topbar">
			<div class="container">
				<div class="header">
					<div>探索</div>
					<div>标签</div>
					<div>用户</div>
				</div>
			</div>
			<div class="active"></div>
		</div>

		<div class="main">
			<div class="gallery" >
				<div class="box first-item"></div>
				<#list events as event>
					<div class="box">
						<#if event.objectType == dic.object_type_post>
							<a href="/post/${event.objectId }">
								<img src="${(img_base_url)!""}${event.content }?imageView2/2/h/200" alt="" />
							</a>
						</#if>
						<#if event.objectType == dic.object_type_album>
							<a href="/album/${event.objectId }/photos">
								<img src="${(img_base_url)!""}${event.title }?imageView2/2/h/200" alt="" />
							</a>
						</#if>
						<div class="meta">
							<a href="/user/${event.userId }">
								<img class="ui avatar image" src="${(img_base_url)!""}${event.userAvatar}?imageView2/1/w/48/h/48">
								<span>${event.userName}</span>
							</a>
						</div>
					</div>
				</#list>
			</div>	
			<div class="tags" style="display: none">
				<div class="container">
					<div class="row">
						<div>
							<#list tags as tag>
								<#if tag_index <= 9>
								<div class="tagbox">
									<div>
										<img class="visible" src="${(img_base_url)!""}${tag.cover }?imageView2/1/w/200/h/200" alt="" />
										<span class="desc">#${tag.tag }</span>
									</div>
									<#if isInterests[tag.id]?if_exists>
										<div class="hidden">
											<a href="#" id="${tag.id }" action="interest">加关注</a>
										</div>									
									<#else>
										<div class="interested">
											<a href="#" id="${tag.id }" action="undointerest">已关注</a>
										</div>											
									</#if>

								</div>
								</#if>
							</#list>

						</div>
					</div>
				</div>
			</div>
			<div class="users" style="display: none">
				<div class="container">
					<div class="row">
						<#if feeds?exists && (feeds?size > 0)>
						<#list feeds as key,value>
							<div class="userbox">
								<div class="header">
									<a href="/user/${key.id }"><img class="avatar" src="${(img_base_url)!""}${key.userAvatar }" /></a>
									<div class="desc">${key.userName }</div>
									<#if isFollowings[key.id+""]?exists>
										<div class="ui tiny basic button follow" following="${key.id }">已关注</div>
									<#else>
										<div class="ui inverted yellow tiny  button follow" following="${key.id }">+关注</div>
									</#if>
								</div>
								<div class="content">	
									<#list value as f>
										<#if f.objectType == dic.object_type_post>
										   <a class="box" href="/post/${f.objectId }" href="/post/${f.objectId }">
												<img src="${(img_base_url)!""}${f.content }${album_thumbnail}" alt="" />
												<div class="cover">
													${f.title }
												</div>
											</a>
								
										</#if>
										<#if f.objectType == dic.object_type_album>
											<a class="box" href="/album/${f.objectId }/photos" href="/album/${f.objectId }/photos">
												<img src="${(img_base_url)!""}${f.title }${album_thumbnail}" alt="" />
												<div class="cover">
													${f.summary }
												</div>		
											</a>							
										</#if>
										<#if f.objectType == dic.object_type_shortpost>
											<div class="box" >
												<i class="disabled large quote left icon"></i>
												${f.summary }
												<i class="disabled large quote right icon"></i>
											</div>											
										</#if>
									</#list>
								</div>
							</div>
						</#list>
						</#if>
					</div>
					<!-- end a row -->

				</div>
				
			</div>
		</div>
	</div>
</body>
</html>