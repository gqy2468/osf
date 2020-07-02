<div class="ui header">
    热门用户
</div>
<div class="ui vertical menu popusers">
  <div class="item">
  	<#if popusers?exists && (popusers?size > 0)>
  	<#list popusers as popuser>
		<a href="<c:url value="/user/${popuser.id }" />" class="popuser">
			<img class="ui inline image" src="${img_base_url }${popuser.userAvatar}?imageView2/1/w/100/h/100">
		</a>
		<%-- <#include "/popup_usercard/${popuser.id }">	 --%>
	</#list>
	</#if>
	<!-- end popup -->

	
  </div>
</div>
	
<!-- end menu -->	

<div class="ui header">
    热门标签
</div>
<div class="ui vertical menu poptags">
  <#if poptags?exists && (poptags?size > 0)>
  <#list poptags as poptag>
  <#if poptag_index <= 4>
	  <a href="/tag/${poptag.id }" target="_blank" />
		  <div class="tagitem" style="background: url(${img_base_url}${poptag.cover }?imageView2/1/w/255/h/80)">
		  	<div class="mask"></div>
		  	<div class="tag">
		  		# ${poptag.tag }
		  	</div>
		  </div>
	  </a>
  </#if>
  </#list>
  </#if>
</div>					
  