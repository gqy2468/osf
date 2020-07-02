<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>粉丝</title>
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
  <#include "topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span4">
				<div class="ui vertical menu">
				  <a class="item" href="/followings/${uid}">
				    关注
				  </a>
				  <a class="active teal item" href="/followers/${uid}">
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
					<#list followers as follower>
			            <div class="ui card" style="width:33%">
			              <div class="ui small centered circular  image">
			                <a href="/user/${follower.id }"><img src="${img_base_url }${follower.userAvatar }"></a>
			              </div>
			              <div class="content">
			                <a class="header centered" href="/user/${following.id}">
			                	${follower.userName }
			                </a>
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