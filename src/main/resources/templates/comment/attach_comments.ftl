
	 <!-- <div class="comments-attach"> -->
	 		<div class="ui divider"></div>
			<div class="ui middle aligned list">
			  <div class="item" style="margin-bottom: 20px;margin-top: 14px">
			    <div class="content">
					<div class="ui fluid mini action input">
					  <input type="text">
					  <div class="ui blue button reply">评论</div>
					  <div class="ui button cancle" style="margin-left: 10px">取消</div>
					</div>
			    </div>
			  </div>
			  
			  
			  <#list comments as comment>
				  <div class="item">

				    <#if user?exists>
				    	<#if comment.commentAuthor != user.id>
						    <div class="right floated content actions">
						      <a class="reply" comment_object_id="${comment.commentObjectId }" 
						      reply_to_author="${comment.commentAuthor }" 
						      reply_to_authorname="${comment.commentAuthorName }" 
						      comment_object_type=${comment.commentObjectType  } 
						      comment_parent=${comment.id }>回复</a>
						    </div>
				    	</#if>
				    </#if>
				    <img class="ui avatar image" src="${img_base_url }${comment.commentAuthorAvatar }">
					<div class="content">
					  	<#if comment.commentParent == 0>
					    	<a class="author" href="/user/${comment.commentAuthor }" >${comment.commentAuthorName }</a>
					    <#else >
					    	<a class="author" href="/user/${comment.commentAuthor }">${comment.commentAuthorName }</a> 回复 <a class="author" href="/user/${comment.commentParentAuthor }">${comment.commentParentAuthorName }</a>
					    </#if>
					    ${comment.commentContent }
					 </div>
					 <!-- end content -->
				  </div>
	
				  <div class="ui divider"></div>	
			  </#list>
			</div>   	
<!-- 	   </div> -->
	   <!-- end attach -->  
