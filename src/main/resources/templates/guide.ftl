<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<#if user?exists>
	<meta name="isLogin" content="true"/>
<#else>
	<meta name="isLogin" content="false"/>
</#if>
<title>OSF</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
  <script src="${request.contextPath}/js/jquery.js"></script>
  <script src="${request.contextPath}/js/semantic.js"></script>
  <script src="${request.contextPath}/js/basic.js"></script>
  <script src="${request.contextPath}/js/code.js"></script>
  <script src="${request.contextPath}/js/explore.js"></script>
  <script src="${request.contextPath}/js/follow.js"></script>
  <script src="${request.contextPath}/js/login.js"></script>
  <script src="${request.contextPath}/js/guide.js"></script>
  
</head>
<body>
 	<#include "topbar.ftl">
	<#include "login_modal.ftl">
	<div class="explore">
		<div class="topbar">
			<div class="container">
				<div class="header" style="text-align: center">
					<div>关注你喜欢的</div>
				</div>
			</div>
		</div>

		<div class="main" style="text-align: center;">
			<div class="tags">
				<div class="container">
					<div class="row">
						<div>
							<#list tags as tag>
								<#if tag_index <= 9>
								<div class="tagbox">
									<div>
										<img class="visible" src="${img_base_url }${tag.cover }?imageView2/1/w/200/h/200" alt="" />
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
			<!-- end tags -->
			
			<div class="ui green button" id="ok" style="margin: 0 auto; margin-bottom: 50px">好了</div>
			
		</div>
	</div>
</body>
</html>