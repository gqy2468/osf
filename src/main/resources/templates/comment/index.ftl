<div class="header">
	评论
</div>
<div class="ui divider"></div>
<div id="commentList">
<#if comments?exists>
<#list comments as comment>
	<div class="comment" id="comment${comment.id }" author="${comment.commentAuthor }">
	  <a class="avatar" href="/user/${comment.commentAuthor }">
	  	<img src="${img_base_url }${comment.commentAuthorAvatar }?imageView2/1/w/48/h/48" alt="" />
	  </a>
	  <div class="content">
	  	<#if comment.commentParent == 0>
	    	<a class="author" href="/user/${comment.commentAuthor }" >${comment.commentAuthorName }</a>
	    <#else>
	    	<a class="author" href="/user/${comment.commentAuthor }">${comment.commentAuthorName }</a> 回复 <a class="author" href="/user/${comment.commentParentAuthor }">${comment.commentParentAuthorName }</a>
	    </#if>
	    <div class="metadata">
	      <span class="date">${comment.commentTs?datetime }</span>
	    </div>
	    <div class="text commentContent">
	      <p>${comment.commentContent }</p>
	    </div>
	    <#if user?exists>
	    	<#if comment.commentAuthor != user.id>
			    <div class="actions" >
			      <a class="reply" ref="${comment.id }">回复</a>
			    </div>	    		
	    	</#if>
	    <#else>
		    <div class="actions" >
		      <a class="reply" ref="${comment.id }">Reply</a>
		    </div>
	    </#if>
	  </div>
	</div>
</#list>
</#if>
</div>