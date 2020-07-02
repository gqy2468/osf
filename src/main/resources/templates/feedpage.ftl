<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
</head>
<body>
<div class="container">
<#if feeds?exists && (feeds?size > 0)>
	<div class="row ${refer }">
	<table class="ui table">
	<tr>
	<#list feeds as feed>
		<td style="text-align:center">
			<a href="javascript:;">
				<#if feed.objectType == dic.object_type_video>
					<video src="${img_base_url}${feed.title }" width="150" height="150"/>     		
				</#if>
				<#if feed.objectType == dic.object_type_audio>
					<audio src="${img_base_url}${feed.title }" controls="controls" width="300"/>
					<div class="extra">${feed.summary }</div>   		
				</#if>
				<#if feed.objectType != dic.object_type_video && feed.objectType != dic.object_type_audio>
					<img src="${img_base_url}${feed.title }" width="150" height="150"/> 		
				</#if>
			</a>
		</td>
		<#if feed_index % 4 == 3>
		</tr>
		<tr>
		</#if>
	</#list>
	</tr>
	</table>
	</div>
</#if>
</div>
</body>
</html>