<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>关注</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">

  <script src="${request.contextPath}/js/jquery.js"></script>
  <script src="${request.contextPath}/js/semantic.js"></script>
  <script src="${request.contextPath}/js/basic.js"></script>
  <script src="${request.contextPath}/js/code.js"></script>
  <script src="${request.contextPath}/js/follow.js"></script>
</head>
<body>
  <#include "topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span4">
				<div class="ui vertical menu">
				  <a class="active teal item" href="/followings/${uid}">
				    关注
				  </a>
				  <a class="item" href="/followers/${uid}">
				    粉丝
				  </a>
				</div>
			
			</div>
			<div class="span8">
				<div class="ui header">
					我的关注
				</div>
				<div class="ui divider">
				</div>

				<div class="ui cards">
					<#list followings as following>
			            <div class="ui card" style="width:33%">
			              <div class="ui small centered circular  image">
			                <a href="/user/${following.id }" target="_blank"><img src="${img_base_url }${following.userAvatar }"></a>
			              </div>
			              <div class="content">
			                <a class="header centered" href="/user/${following.id}">
			                	${following.userName }
			                </a>
			                <#if uid == me.id>
							<div class="meta centered" style="margin-top: 10px">
								<div class="ui basic button follow" following="${following.id }">取消关注</div>
							</div>
			                </#if>
			              </div>
			            </div>
					</#list>
				</div>
				<!-- end cards -->

			</div>
		</div>
	
	</div>
</body>
</html>