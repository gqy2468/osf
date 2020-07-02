<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>注册</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
	
	<script src="${request.contextPath}/js/jquery.js"></script>
	<script src="${request.contextPath}/js/basic.js"></script>
	<script src="${request.contextPath}/js/code.js"></script>
	<script src="${request.contextPath}/js/register.js"></script>
</head>
<body>
    <#include "../topbar.ftl">
	<div class="container">
		<div class="row">
			<div class="span8 offset2">	
					<div class="ui header">注册</div>
					<div class="ui divider"></div>			
					<div class="row">
						<div class="span4 offset2">
							<div class="registerarea">
								<div class="ui form">							
								  <div class="field">
								    <label>邮箱<span id="emailTip" class="tip"></span></label>
								    <input type="text" name="email" id="email">
								  </div>
								  <div class="field">
								    <label>用户名<span id="usernameTip" class="tip"></span></label>
								    <input type="text" name="username" id="username">
								  </div>
								  <div class="field">
								    <label>密码<span id="passwordTip" class="tip"></span></label>
								    <input type="password" name="password" id="password" placeholder="8-16位数字和字母的组合">
								  </div>
								  <div class="field">
								    <label>确认密码<span id="cfmPwdTip" class="tip"></span></label>
								    <input type="password" name="cfmPwd" id="cfmPwd">
								  </div>
	
								  <div class="field">
								  	<div class="ui blue button" id="registerBtn">注册</div>
								  </div>								
								</div>				
	
							</div>						
						</div>

					</div>					


			</div>
		</div>
	</div>
</body>
</html>