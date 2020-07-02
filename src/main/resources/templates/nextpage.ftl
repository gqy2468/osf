<#if feeds?exists && (feeds?size > 0)>
<#list feeds as feed>
	<#if feed.objectType == dic.object_type_shortpost>
		<div class="event row" type="spost" object_type="${feed.objectType }" object_id="${feed.objectId }">
		   <div class="label span2">
		     <a href="/user/${feed.userId }"><img src="${img_base_url}${feed.userAvatar }"></a>
		   </div>
		   <div class="content span6">
		     <div class="summary">
		       <a href="/user/${feed.userId }">${feed.userName }</a> 说
		       <div class="date">
		         ${feed.ts?datetime }
		       </div>
		       <div class="actions">
					<#if feed.userId == user.id>
					<a class="delete">
						<i class="delete icon" event_id=${feed.id }></i>
		        	</a>   
					</#if>
		       </div>
		     </div>
		     <!-- end summary -->
		     
		     <div class="extra">                         
		       ${feed.summary }
		     </div>
		     <div class="meta">							                     	
				<div class="actions">
					<a class="comment">
				    	<i class="comment outline icon"></i> ${feed.commentCount }
					</a>                           
			        <a class="like">
			       		<i class="<#if feed.like>red </#if>heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i> 
			       		<span>${feed.likeCount }</span> 
			    	</a>                         
		   		</div>
		 	</div>
		 	<!-- end meta -->   
		    <div class="comments-attach"></div>
		    <!-- end comments-attach -->   
		   </div>
		   <!-- end content -->
		 </div>   
		 <!-- end event -->                 		
	</#if>
	
<!-- new post -->
 <#if feed.objectType == dic.object_type_post>
 <div class="event row" type="post" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="/user/${feed.userId }"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a href="/user/${feed.userId }">${feed.userName }</a> 发表了日志
       <div class="date">
         ${feed.ts?datetime }
       </div>
       <div class="actions">
			<#if feed.userId == user.id>
			<a class="delete">
				<i class="delete icon" event_id=${feed.id }></i>
        	</a>   
			</#if>
       </div>
     </div>
     <div class="extra">
		<div class="postheader">
			<a href="/post/${feed.objectId }">${feed.title }</a>
		</div>
		<#if feed.content?exists>
			<img src="${img_base_url}${feed.content }${post_cover_thumbnail}" alt="" />
		</#if>
   	</div>
     <div class="extra">
     	${feed.summary }
     </div>
     <div class="meta">
       <#if feed.tagList?exists>
	       <div class="tags">
		     	<i class="tag icon"></i>
		     	<#list feed.tagList as tag>
		     		<a href="/tag/${tag.id }">${tag.tag }</a>
		     	</#list>
		   </div>
	   </#if>
       <div class="actions">
	 		<a class="comment">
	            <i class="comment outline icon"></i> ${feed.commentCount }
	        </a>                           
	        <a class="like">
        		<i class="<#if feed.like>red </#if>heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i> 
        		<span>${feed.likeCount }</span>
	        </a>                       
       </div>

     </div>  
     <!-- end meta --> 
     <div class="comments-attach"></div>
     <!-- end comments-attach -->                                            
   </div>
   <!-- end content -->

 </div>                    	
 </#if>
 
 <!-- new album -->
 <#if feed.objectType == dic.object_type_album>
 <div class="event row" type="album" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="/user/${feed.userId }"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a>${feed.userName }</a> 上传了相册 <a href="/album/${feed.objectId }"></a>
       <div class="date">${feed.ts?datetime }</div>
       <div class="actions">
			<#if feed.userId == user.id>
			<a class="delete">
				<i class="delete icon" event_id=${feed.id }></i>
        	</a>   
			</#if>                                   
       </div>
     </div>
     <div class="extra images">
       <#list feed.content?split(":") as img>
       	<#if img_index <= 2 && img?default("")?trim?length gt 1>
       	<a href="/album/${feed.objectId }/photos"><img alt="" src="${img_base_url }${img }${album_thumbnail}"></a>
       	</#if>
	   </#list>
     </div>
     <div class="extra">${feed.summary }</div>
     <div class="meta">
        <#if feed.tagList?exists>
        <div class="tags">
	      	<i class="tag icon"></i>
	      	<#list feed.tagList as tag>
	      		<a href="/tag/${tag.id }">${tag.tag }</a>
	      	</#list>
		</div> 
		</#if>                       
        <div class="actions">
			<a class="comment">
				<i class="comment outline icon"></i> ${feed.commentCount }
        	</a>                           
	        <a class="like">
        		<i class="<#if feed.like>red </#if>heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i> 
        		<span>${feed.likeCount }</span>
	        </a>                          
       </div>
       <!-- end actions -->
     </div>
     <!-- end meta -->
     <div class="comments-attach"></div>
     <!-- end comments-attach -->   
   </div>
 </div>
 </#if>
 <!-- end album  -->    
 
 <!-- new video -->
 <#if feed.objectType == dic.object_type_video>
 <div class="event row" type="video" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="/user/${feed.userId }"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a>${feed.userName }</a> 上传了视频 <a href="/video/${feed.objectId }"></a>
       <div class="date">${feed.ts?datetime }</div>
       <div class="actions">
			<#if feed.userId == user.id>
			<a class="delete">
				<i class="delete icon" event_id=${feed.id }></i>
        	</a>
			</#if>
       </div>
     </div>
     <div class="extra images">
       <#list feed.content?split(":") as video>
       <#if video_index <= 1 && video?default("")?trim?length gt 1>
       	<a href="/video/${feed.objectId }"><video src="${img_base_url }${video }" width="200" height="200"></video></a>
	   </#if>
       </#list>
     </div>
     <div class="extra">${feed.summary }</div>
     <div class="meta">
        <#if feed.tagList?exists>
        <div class="tags">
	      	<i class="tag icon"></i>
	      	<#list feed.tagList as tag>
	      		<a href="/tag/${tag.id }">${tag.tag }</a>
	      	</#list>
		</div> 
		</#if>                       
        <div class="actions">
			<a class="comment">
				<i class="comment outline icon"></i> ${feed.commentCount }
        	</a>                           
	        <a class="like">
        		<i class="<#if feed.like>red </#if>heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i> 
        		<span>${feed.likeCount }</span>
	        </a>                          
       </div>
       <!-- end actions -->
     </div>
     <!-- end meta -->
     <div class="comments-attach"></div>
     <!-- end comments-attach -->   
   </div>
 </div>
 </#if>
 <!-- end video  -->  
 
 <!-- new audio -->
 <#if feed.objectType == dic.object_type_audio>
 <div class="event row" type="video" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="/user/${feed.userId }"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a>${feed.userName }</a> 上传了音频 <a href="/video/${feed.objectId }"></a>
       <div class="date">${feed.ts?datetime }</div>
       <div class="actions">
			<#if feed.userId == user.id>
			<a class="delete">
				<i class="delete icon" event_id=${feed.id }></i>
        	</a>   
			</#if>                                   
       </div>
     </div>
     <div class="extra images">
       <#list feed.content?split(":") as audio>
       <#if audio_index <= 1 && audio?default("")?trim?length gt 1>
       	<audio src="${img_base_url }${audio }" width="300" controls="controls"></audio>
	   </#if>
	   </#list>
     </div>
     <div class="extra"><a href="/audio/${feed.objectId }">${feed.summary }</a></div>
     <div class="meta">
        <#if feed.tagList?exists>
        <div class="tags">
	      	<i class="tag icon"></i>
	      	<#list feed.tagList as tag>
	      		<a href="/tag/${tag.id }">${tag.tag }</a>
	      	</#list>
		</div> 
		</#if>                       
        <div class="actions">
			<a class="comment">
				<i class="comment outline icon"></i> ${feed.commentCount }
        	</a>                           
	        <a class="like">
        		<i class="<#if feed.like>red </#if>heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i> 
        		<span>${feed.likeCount }</span>
	        </a>                          
       </div>
       <!-- end actions -->
     </div>
     <!-- end meta -->
     <div class="comments-attach"></div>
     <!-- end comments-attach -->   
   </div>
 </div>
 </#if>
 <!-- end audio  -->
             
</#list>
</#if>