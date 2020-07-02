<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<#if user?exists>
		<meta name="isLogin" content="true"/>
	<#else>
		<meta name="isLogin" content="false"/>
	</#if>
  	<title>${tag }</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
  	<script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/jquery.infinitescroll.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/semantic.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/basic.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/code.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/interest.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/login.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/feed.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/like.js"></script>
</head>
<body>
	<#include "../topbar.ftl">
	<#include "../login_modal.ftl">
	<div class="container">
    <div class="row">  
          <div class="span8">             
                  <div class="ui feed" id="feeds">
 					<#include "../nextpage.ftl">
                  </div>  <!--end feed -->
                  <a id="next" href=/${id}/page/2"></a>
            </div> <!-- end span8  -->
          <div class="span4">
          	<div id="rightside">
				<div class="tagheader">
				  	<div class="content">
				  		${tag }
				  	</div>
				  	<#if isInterest?exists && isInterest == true>
				  		<div class="ui mini basic button interest" tag_id="${id }">
				  			 已关注
				  		</div>
				  	<#else>
				  		<div class="ui mini inverted yellow button interest" tag_id="${id }">
				  			+关注
				  		</div>
				  	</#if>
				</div>          		
			
				<#include "../sidebar.ftl">
            </div>           
          </div>
        </div>
      </div>


    </div>

  </div>

</body>
</html>