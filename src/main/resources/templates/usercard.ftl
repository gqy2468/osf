
	            <#if u?exists>
	            <div class="ui card">
	              <div class="ui small centered circular  image">
	                <a href="/user/${u.id }"><img src="${img_base_url }${u.userAvatar }"></a>
	              </div>
	              <div class="content">
	                <a class="header centered" href="/user/${u.id}">
	                	${u.userName }
	                </a>
	                <div class="meta centered">
	                  <span class="date">不想成为画家的黑客不是好摄影师</span>
	                </div>	                
					<div class="ui mini statistics">
					  <div class="statistic">
					    <div class="value">
					      <a href="/followers/${u.id}">${counter.follower }</a>
					    </div>
					    <div class="label">粉丝
					    </div>
					  </div>
					  <div class="statistic">
					    <div class="value">
					      <a href="/followings/${u.id}">${counter.following }</a>
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
	              
	              <div class="extra content">
	              	<#if user?exists>
	              		<#if user.id != u.id>
			              	<#if follow?exists>
			              		<div class="mini ui basic button follow" following="${u.id}">已关注</div>
			              	<#else>
								<div class="mini ui yellow button follow" following="${u.id}">+关注</div>
							</#if>
						</#if>
					<#else>
						<div class="mini ui yellow button follow" following="${u.id}">+关注</div>
					</#if>
	              </div>
	            </div>
	            </#if>