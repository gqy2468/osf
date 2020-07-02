<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>首页</title>
  <link rel="shortcut icon" href="${request.contextPath}/img/favicon.ico" />
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/style.css">

  <script src="${request.contextPath}/js/jquery.js"></script>
  <script src="${request.contextPath}/js/jquery.infinitescroll.js"></script>
  <script src="${request.contextPath}/js/semantic.js"></script>
  <script src="${request.contextPath}/js/basic.js"></script>
  <script src="${request.contextPath}/js/code.js"></script>
  <script src="${request.contextPath}/js/like.js"></script>
  <script src="${request.contextPath}/js/spost.js"></script>
</head>
<body>
  <#include "topbar.ftl">
  <div class="container">
    <div class="row">  
          <div class="span8">  
	          	<div class="header_box">
	          		<div class="ui avatar image">
	                	<img src="${img_base_url}${user.userAvatar}">
	                </div>                
					<div id="action_bar">
						<div class="ui labeled icon menu actions" >
						  <a class="item sport_link" href="#">
						    <i class="blue big font icon"></i>
						    发状态
						  </a>
						  <a class="item post_link" href="/post/create">
						    <i class="big write icon"></i>
						    写日志
						  </a>
						  <a class="item album_link" href="/album/upload">
						    <i class="pink big photo icon"></i>
						    传图片
						  </a>
						  <a class="item link">
						    <i class="green big linkify icon"></i>
						    链接
						  </a>
						</div>

					  <div class="short_post">
					  	<textarea placeholder="说点什么..." id="spost_content"></textarea>
					  	<div class="bar">
					  		<div class="ui tiny blue button" id="spost_send">
					  			发表
					  		</div>
					  		<div class="ui tiny basic button" id="sport_cancel">
					  			取消
					  		</div>
					  	</div>
					  </div>			  
					</div>
	               </div>	
	               <!-- end header_box -->         
                  <div class="ui feed" id="feeds">
                   <div class="event empty row">
                      <div class="label span2">
                        <img src="">
                      </div>
                      <div class="content span6">
                        <div class="summary">
                          <a href=""></a> 说
                          <div class="date">
                          	刚刚
                          </div>
                        </div>
                        <div class="extra">
                        </div>
                        <div class="meta">							                     	
                          <div class="actions">
							  <a class="comment">
	                            <i class="comment outline icon"></i><span>0</span>
	                          </a>                           
	                          <a class="like">
	                          	<i class="heart icon" object_type="4" object_id=""></i><span>0</span>
	                          </a>                         
                          </div>

                        </div>                                               
                      </div>
                    </div> 
                    <!-- empty event , wait for full and show -->

                    <#include "nextpage.ftl">
                  </div>  <!--end feed -->
                  <a id="next" href="/page/2"></a>
            </div>
          <div class="span4">
          	<div id="rightside">
          		<#if (user)??>
	            <div class="ui card">
	              <div class="ui small centered circular  image">
	                <a href="/user/${user.id }"><img src="${img_base_url }${user.userAvatar }"></a>
	              </div>
	              <div class="content">
	                <a class="header centered" href="/user/${user.id}">
	                	${user.userName }
	                </a>
	                <div class="meta centered">
	                  <span class="date">不想成为画家的黑客不是好摄影师</span>
	                </div>	                
					<div class="ui mini statistics">
					  <div class="statistic">
					    <div class="value">
					      <a href="/followers/${user.id}">${counter.follower }</a>
					    </div>
					    <div class="label">粉丝
					    </div>
					  </div>
					  <div class="statistic">
					    <div class="value">
					      <a href="/followings/${user.id}">${counter.following }</a>
					    </div>
					    <div class="label">关注
					    </div>
					  </div>
					  <div class="statistic">
					    <div class="value">
					      <a href="#">${counter.spost }</a>
					    </div>
					    <div class="label">状态
					    </div>
					  </div>
					</div>	<!-- end statistics --> 
	              </div>
	            </div> 
	            </#if>
				<#include "sidebar.ftl">
            </div>           
          </div>
        </div>
      </div>

    </div>

  </div>
	
  <#include "trash_tip.ftl">
  
  <script src="${request.contextPath}/js/feed.js"></script>
</body>
</html>