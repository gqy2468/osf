$(document).ready(function(){
	var photos = [];

	$('#uploader_input').live('change', function(event) {
		$.ajaxFileUpload({
			url: basePath+'/album/upload/photo', 
			secureuri:false,
			fileElementId:'uploader_input',
			success: function (data, status){
				data = jQuery(data).find('pre:first').text();
				data = jQuery.parseJSON(data);
				if (typeof(data.error) != 'undefined' || typeof(data.warn) != 'undefined') {
					alert(data.error ? data.error : (data.warn ? data.warn : '未知错误'));
					location.href = basePath+'/album/upload';
				} else {
					var $imgCard = $('<div class="card" id="card'+data.id+'">'+
										'<a class="image" href="javascript:;">'+
											'<img src="'+img_base_url+data.key+album_thumbnail+'">'+
										'</a>'+
										'<div class="content">'+
											'<textarea rows="" cols="" placeholder="添加描述..."></textarea>'+
										'</div>'+
										'<div class="extra meta">'+
											'<a href="javascript:;"><i class="delete icon"></i>删除</a>'+
										'</div>'+
									'</div>');
					$('#uploadedphotos').append($imgCard);
				}
			},
			error: function (data, status, e){
	    		alert(e);
			}
		});
	});

	$('#saveAlbumBtn').click(function(event) {
      	if ($('#uploadedphotos .card:first').length <= 0) {
      		alert("请上传图片");
      		return false;
      	}
		$('#uploadedphotos .card').each(function(index, el) {
			var photo_id = $(this).attr('id').substring(4);
			var photo_desc = $(this).find('textarea:first()').val();
			photos.push({"id":photo_id, "desc":photo_desc});
		});
		var desc = $('#desc').val();
		var tags = $('#tags').val();
		tags = tags.length > 0 ? tags.split(',') : new Array();

      	var that = this;
		$(this).addClass('loading');
		$.ajax({
			url: basePath+'/album/create',
			type: 'POST',
			dataType: 'json',
			contentType:'application/json;charset=UTF-8',
			data: JSON.stringify({album_desc: desc, photos: photos, tags: tags})
		})
		.done(function(data) {
			var status = data.status;
			var author = data.album.user_id;
			if(SUCCESS_ALBUM_CREATE == status || SUCCESS_ALBUM_UPDATE == status) {
				self.location = basePath;
			}
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
    		$(that).removeClass('loading');
		});
		
	});
	
	$('.delete.icon').live('click', function(){
		var card = $(this).parents('.card');
		var photo_id = $(card).attr('id').substring(4);
		var that = this;
		
		$.ajax({
			url: basePath+'/album/delete/photo/'+photo_id,
			type: 'POST',
			dataType: 'json'
		})
		.success(function(data){
			if(data.status == SUCCESS_PHOTO_DELETE){
				$(card).remove();
			}
		});
		return false;
	});
	
	$('.trash').live('click', function(){
		$('.trash-tip')
		  .modal({
		    closable  : true,
		    onDeny    : function(){
		      return true;
		    },
		    onApprove : function() {
				var id = $('#mainimg').attr('aid');
				$.ajax({
					url: basePath + '/album/delete/' + id,
					type: 'POST',
					dataType: 'json'
				})
				.success(function(data){
					if (data.status == SUCCESS_ALBUM_DELETE) {
						self.location = basePath;
					}
				});
		    }
		  })
		  .modal('show');
	});
		
	$('#imgbox img').click(function() {
		var src = $(this).attr('src').split('?')[0];
		var img_id = $(this).attr('id').split('_')[2];
		$('#mainimg').attr('src', src);
		$('meta[name=id]').attr('content', img_id);
		
		$.ajax({
			url: basePath + '/comment/photo/'+img_id,
			type: 'GET',
		})
		.success(function(data){
			$('#commentList').remove();
			$('#comments').append($('<code></code>').append(data).find('#commentList'));
		});
	});
	
	$("#switch").click(function() {
		$("#imgbox img").each(function(idx, obj) {
			var src = $(obj).attr("src");
			var source = $(obj).attr("source");
			$(obj).attr("src", source);
			$(obj).attr("source", src);
		});
		$("#imgbox img:eq(0)").click();
		if ($(this).text() == "查看原图片") {
			$(this).text("查看3D图片");
		} else {
			$(this).text("查看原图片");
		}
	});
})