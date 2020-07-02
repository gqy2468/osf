<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账户激活</title>
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
		<div class="span8 offset2">
			<div class="ui piled segment" style="text-align: center;">
				<div style="margin: 40px;">
					<i class="massive mail outline icon"></i>
				</div>
				<#if status?if_exists>
					<div class="content">
						激活链接已发送到您的邮箱<a href="#">${email }</a>, 请激活 
					</div>
					<div class="ui divider"></div>
					<div class="content">
						没有收到？<a href="#" class="ui small primary button" id="resend" email="${email}">重新发送</a> 
					
					</div>
				</#if>
				<#if ERROR_ACCOUNT_ACTIVATION_NOTEXIST == status>
					<div class="content">
						账户不存在
					</div>				
				</#if>
				<#if ERROR_ACCOUNT_ACTIVATION_EXPIRED == status>
					<div class="content">
						链接已失效 <a href="#" class="ui small primary button" id="resend" email="${email}">重新发送</a>
					</div>				
				</#if>
				<#if ERROR_ACCOUNT_ACTIVATION == status>
					<div class="content">
						未知错误
					</div>				
				</#if>
			</div>	
		
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$('#resend').live('click', function(){
		if($(this).hasClass('loading')){
			return false;
		}
		var that = $(this);
		var email=$(this).attr('email');
		$.ajax({
			url: basePath + '/account/activation/mail/resend?email='+email,
			type: 'GET',
			dataType: 'json',
			beforeSend: function(){
				$(that).addClass('loading');
			}

		})
		.success(function(data){
			$(that).removeClass('loading');
			if(data.status == SUCCESS_ACCOUNT_ACTIVATION_EMAIL_RESEND){
				$(that).text('已发送');
			}
		})

	})
})




</script>
</body>
</html>