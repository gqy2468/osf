<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>通知</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">

  <script src="${request.contextPath}/js/jquery.js"></script>
  <script src="${request.contextPath}/js/semantic.js"></script>
  <script src="${request.contextPath}/js/basic.js"></script>
  <script src="${request.contextPath}/js/code.js"></script>

</head>
<body>
	<#include "../topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span4">
				<div class="ui vertical menu">
				  <a class="item" href="/notifications/comment">
				  	评论
				    <div class="ui red label">${notifications.comment }</div>
				  </a>
				  <a class="item" href="/notifications/like">
				   	喜欢
				   	<div class="ui red label">${notifications.like }</div>
				  </a>
				  <a class="item" href="/notifications/follow">
				    关注
				    <div class="ui red label">${notifications.follow }</div>
				  </a>
				  <a class="active teal item" href="/notifications/system">
				    系统消息
				    <div class="ui red label">${notifications.system }</div>
				  </a> 
				</div>
			
			</div>
			<!-- end span4  -->
			
			<div class="span6">
				<div class="ui secondary menu">
				    <a class="item active" data-tab="notread">未读</a>
				    <a class="item" data-tab="read">已读</a>
				  </div>
				  <div class="ui tab  active" data-tab="notread">
						<div class="ui relaxed list">
						<#list notis as notification>
							<#if notification_index <= 9>
							  <#if notification.notifyType == dic.notify_type_system>
								  <div class="item">
								    <img class="ui avatar image" src="${img_base_url }${notification.notifierAvatar }">
								    <div class="content">
								      <a class="header" href="/user/${notification.notifier }">${notification.notifierName }</a>
								    </div>
								  </div>
							  </#if>
							</#if>
						</#list>
						</div>
				  </div>
				  <div class="ui tab " data-tab="read">
				    5
				  </div>
						
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$('.menu .item').tab()
	});
	</script>
</body>
</html>