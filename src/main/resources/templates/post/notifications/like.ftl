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
				  <a class="item" href='<c:url value="/notifications/comment"/>' >
				  	评论
				    <div class="ui red label">${notifications.comment }</div>
				  </a>
				  <a class="active teal item" href='<c:url value="/notifications/like"/>'>
				   	喜欢
				   	<div class="ui red label">${notifications.like }</div>
				  </a>
				  <a class="item" href='<c:url value="/notifications/follow"/>'>
				    关注
				    <div class="ui red label">${notifications.follow }</div>
				  </a>
				  <a class="item" href='<c:url value="/notifications/system"/>'>
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
							<c:forEach items="${notis }" var="notification" begin="0" end="9">
							  <c:if test="${notification.notify_type eq dic.notify_type_like }">
								  <div class="item">
								    <img class="ui avatar image" src="<c:url value="${img_base_url }${notification.notifier_avatar }" />">
								    <div class="content">
								      <a class="header" href="<c:url value="/user/${notification.notifier }" /> ">${notification.notifier_name }</a>
								      <c:if test="${notification.object_type eq dic.object_type_post }">
								      	<div class="description">喜欢了你的文章:<a href="<c:url value="/post/${notification.object_id }" />" ><b>${notification.object_title }</b></a> ${notification.ts }</div>
								      </c:if>
								      <c:if test="${notification.object_type eq dic.object_type_album }">
								      	<div class="description">喜欢了你的
								      		<a href="<c:url value="/album/${notification.object_id }/photos" />" >
								      			<b>
								      				<c:if test="${empty notification.object_title}">
								      					相册
								      				</c:if>
								      				<c:if test="${not empty notification.object_title}">
								      					${notification.object_title }
								      				</c:if>								      				
								      			</b>
								      		</a> ${notification.ts }
								      	</div>
								      </c:if>
								    </div>
								  </div>	
							  </c:if>							

							</c:forEach>
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