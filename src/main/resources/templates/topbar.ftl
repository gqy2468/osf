	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/menu.css">
	<nav class="navbar navbar-default navbar-fixed-top">
	  <div class="nav-container container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/">首页</a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse">
	      <ul class="nav navbar-nav">
	        <li><a href="/explore">探索</a></li>
	      </ul>
	      <ul class="nav navbar-nav navbar-right">
	      	<#if (user)??>
	      		<li>
	      			<!---- <a href=""/user/${user.id }">${user.userName }</a> ---->
	      			<div class="ui simple dropdown item">
				      ${user.userName }
				      <i class="dropdown icon"></i>
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
						  <a class="item" href="/notifications/system">
						    系统消息
						    <div class="ui red label">${notifications.system }</div>
						  </a>
						  <a href="/account/setting/info" class="item">设置</a>
						  <a href="/account/logout" class="item">退出</a>
					  </div>
				    </div>
	      		</li>
	      	<#else>
		        <li><a href="/account/register">注册</a></li>
		        <li></li>
		        <li><a href="/account/login">登录</a></li>
	        </#if>
	      </ul>
	            
	      
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
    