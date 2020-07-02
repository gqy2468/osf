<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>登录</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
	
	<script src="${request.contextPath}/js/jquery.js"></script>
	<script src="${request.contextPath}/js/basic.js"></script>
	<script src="${request.contextPath}/js/code.js"></script>
	<script src="${request.contextPath}/js/login.js"></script>
</head>
<body>
	<#include "../topbar.ftl"/>
	<div class="container">
		<div class="row">
			<div class="span8 offset2">	
					<div class="ui header">登录</div>
					<div class="ui divider"></div>			
					<div class="row">
						<div class="loginarea span4 offset2">
							<div class="ui form">
							  <div class="field">
							    <label>邮箱<span id="emailTip" class="tip"></span></label>
							    <input type="text" name="email" id="email">
							  </div>
							  <div class="field">
							    <label>密码<span id="paswordTip" class="tip"></span></label>
							    <input type="password" name="password" id="password">
							  </div>
							  <div class="field">
							  	<div class="ui green button" id="loginbtn">登录</div>
							  </div>								
							</div>				
							<div class="ui section divider"></div>
							<div class="social">
								<a class="ui orange basic button span4" href="/account/register">
									邮箱注册
								</a>		
							</div>	
						</div>
					</div>					


			</div>
		</div>
	</div>
</body>
</html>