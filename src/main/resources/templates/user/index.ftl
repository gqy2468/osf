<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
	<title>${user.userName }</title>
	<#if user?exists>
		<meta name="isLogin" content="true"/>
	<#else>
		<meta name="isLogin" content="false"/>
	</#if>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">
    
  	<script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/semantic.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/basic.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/code.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/comment.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/post.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/media.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/follow.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/login.js"></script>
</head>
<body>
  <#include "../topbar.ftl">
  <#include "../login_modal.ftl">
  <div class="container">
    <div class="row">  
          <div class="span8">     
          		<div class="ui feed">
          			<!--<h4 class="ui header">我的日志</h4>
                	<#if posts?if_exists>
	          			<div class="ui divider"></div>
                	</#if>
                	<#if posts?exists>
          			<#list posts as post>
	          			<div class="ui divider"></div>
	                    <div class="event">
	                      <div class="content">
	                        <div class="text">
	                        	<div class="row">
	                        	<#if post.postCover?exists>
	                        		<div class="span6">
	                        	<#else>
	                        		<div class="span8">
	                        	</#if>
	                        			<h3 class="ui header">
	                        				<a href="${request.contextPath}/post/${post.id}">${post.postTitle }</a>
	                        			</h3>
	                        			<div>
	                        				${post.postExcerpt }
	                        			</div>
	                        			<div class="postmeta">
	
	                        			</div>
	                        		</div>
	                        		<#if post.postCover?exists>
		                        		<div class="span2">
		                        			<img class="ui small image" src="${img_base_url }${post.postCover }${album_thumbnail }" alt=""  />
		                        		</div>
	                        		</#if>
	                        		
	                        	</div>
	                        </div>
	                        <div class="meta">
	                          <span style="float: left"> 
	                        	<i class="tag icon"></i>
	                        	<#list post.postTags as tag>
	                        		<a href="/tag/${tag.id }">${tag.tag }</a>
	                        	</#list>
	                          </span>
	                          <span style="float: right">
		                          <%-- <a class="like">
		                            <i class="like icon"></i> ${post.likeCount }
		                          </a>
		                          <a class="share">
		                            <i class="share alternate icon"></i> ${post.shareCount }
		                          </a>   
		                          <a class="comment">
		                            <i class="comment outline icon"></i> ${post.commentCount }
		                          </a> --%>
							  </span>
	                        </div>
	                      </div>
	                    </div>     <!-- end event -->  
                    </#list>
                	</#if>-->
          		
          			<h4 class="ui header albumheader" url="/user/${(u.id)!("") }/album/" refer="albums">我的相册
	          			<a class="right" style="float:right;cursor:pointer;" title="下一页"><i class="right chevron icon"></i></a>
	          			<a class="left" style="float:right;cursor:pointer;" title="上一页"><i class="left chevron icon"></i></a>
          			</h4>
          			<div class="ui divider"></div>
                	<#if albums?exists>
                    <div class="event">
                      <div class="content">
						<div class="ui four cards albums">
						  <#list albums as album>
							  <div class="card">
							    <a class="image" href="/album/${album.id}/photos">
							      <img src="${img_base_url}${album.cover }${album_thumbnail }">
							    </a>
							    <div class="extra">
							      ${album.albumDesc }
							    </div>
					    		<!--<div class="ui tiny button transform" aid="${album.id}">转换成3D图片</div>
					    		<div class="ui tiny button restore" aid="${album.id}" style="margin-top:5px;">还原成原图片</div>-->
							  </div>
						  </#list>
						</div>
                      </div>
                    </div>
                	</#if>
          		
          			<h4 class="ui header videoheader" url="/user/${(u.id)!("") }/video/" refer="videos">我的视频
	          			<a class="right" style="float:right;cursor:pointer;" title="下一页"><i class="right chevron icon"></i></a>
	          			<a class="left" style="float:right;cursor:pointer;" title="上一页"><i class="left chevron icon"></i></a>
          			</h4>
          			<div class="ui divider"></div>
                	<#if videos?exists>
                    <div class="event">
                      <div class="content">
						<div class="ui four cards videos">
						  <#list videos as video>
							  <div class="card">
							    <a class="image" href="/video/${video.id}">
							      <video src="${img_base_url}${video.key }" width="200" height="200"></video>
							    </a>
							  </div>
						  </#list>
						</div>
                      </div>
                    </div>
                	</#if>
          		
          			<h4 class="ui header audioheader" url="/user/${(u.id)!("") }/audio/" refer="audios">我的音频
	          			<a class="right" style="float:right;cursor:pointer;" title="下一页"><i class="right chevron icon"></i></a>
	          			<a class="left" style="float:right;cursor:pointer;" title="上一页"><i class="left chevron icon"></i></a>
          			</h4>
          			<div class="ui divider"></div>
                	<#if audios?exists>
                    <div class="event">
                      <div class="content">
						<div class="ui four cards audios">
						  <#list audios as audio>
							  <div class="card" style="width:300px;margin-right:0.5em;">
							    <audio src="${img_base_url}${audio.key }" width="300" controls="controls"></audio>
							    <div class="extra">
							      <a class="image" href="/audio/${audio.id}">
							      ${audio.desc }
							      </a>
							    </div>
							  </div>
						  </#list>
						</div>
                      </div>
                    </div>
                	</#if>
          		
          			<h4 class="ui header mediaheader" url="/user/${(u.id)!("") }/media/" refer="medias">我的萌作
	          			<a class="right" style="float:right;cursor:pointer;" title="下一页"><i class="right chevron icon"></i></a>
	          			<a class="left" style="float:right;cursor:pointer;" title="上一页"><i class="left chevron icon"></i></a>
          			</h4>
          			<div class="ui divider"></div>
                	<#if medias?exists>
                    <div class="event">
                      <div class="content">
						<div class="ui four cards medias">
						  <#list medias as media>
							  <div class="card">
							    <a class="image" href="/media/${media.id}">
							    <#if (media.media)?default("")?trim?length gt 1>
							    	<#if media.mediaType != 'mp4'>
							    	<img src="${img_base_url}${media.media }${album_thumbnail }">
							    	<#else>
							    	<video src="${img_base_url}${media.media }${album_thumbnail }" width="139">
							    	</#if>
							    <#else>
							    	<img src="${img_base_url}${media.role }${album_thumbnail }">
							    </#if>
							    </a>
					    		<a class="ui tiny button edit" href="/media/edit/${media.id}">修改</a>
					    		<div class="ui tiny button generate" mid="${media.id}">生成3D图片</div>
							  </div>
						  </#list>
						</div>
                      </div>
                    </div>
                	</#if>
          	</div> <!-- end feed --> 
          </div> <!-- end span8 -->
          
          <div class="span4">
          	<div id="rightside">
				<#include "../usercard.ftl">
            </div>           
          </div>
          <!-- end span4 -->
     </div>
     <!-- end row -->
   </div>
   <!-- end container -->
</body>
</html>