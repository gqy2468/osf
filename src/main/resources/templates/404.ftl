<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>404</title>
  <link rel="shortcut icon" href="${request.contextPath}/img/favicon.ico" />
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
  <style>
  body{
  	background-color: white;
  }
  
  </style>
</head>
<body>
  <#include "topbar.ftl">
	
	<div class="container">
		<div class="row">
			<div class="span9 offset1">
				<div class="error page img">
					<img src="/img/404.jpg" alt="" />
				</div>
				<div class="error page code">
					404
				</div>
			</div>
		</div>
	</div>

</body>
</html>