$(document).ready(function(){
	var options = {minMargin: 5, maxMargin: 10, itemSelector: ".box", firstItemClass: "first-item"};
	$(".gallery").rowGrid(options);	
	
	var index = 0;
	$(".topbar .header>div").click(function(){
		index = $(this).index();
		var explore = $('.gallery:first');
		var tags = $('.tags:first');
		var users = $('.users:first');
		var active_tip = $('.topbar .active');
		if(index == 0){	
			$(explore).fadeIn(300);
			$(tags).fadeOut(200);
			$(users).fadeOut(200);
		} else if(index == 1 ){	
			$(tags).fadeIn(300);
			$(explore).fadeOut(200);
			$(users).fadeOut(200);
		} else{
			$(explore).fadeOut(300);
			$(tags).fadeOut(200);
			$(users).fadeIn(200);
		}
		var left = $(this).offset().left + ($(this).width() - $(active_tip).width()) / 2;
		$(active_tip).css('left', left);
	});
	$(window).resize(function() {
		var active_tip = $('.topbar .active');
		var tab = $(".topbar .header>div:eq(" + index + ")");
		var left = $(tab).offset().left + ($(tab).width() - $(active_tip).width()) / 2;
		$(active_tip).css('left', left);
	});
	$(".topbar .header>div:first").click();
		
	var isLogin = $('meta[name=isLogin]').attr('content');
	$('.explore .tags .tagbox a').live('click', function(){
		if(isLogin == 'false'){
			$('.ui.small.modal').modal('show');
			return false;
		}

		var tag_id = $(this).attr('id');
		var that = $(this);
		var url = basePath+'/tag/'+tag_id;
		var action = $(this).attr('action');

		$.ajax({
			url: basePath + '/tag/'+tag_id+'/'+action,
			type: 'GET',
			dataType: 'json'
		})
		.success(function(data){
			if(data.status == SUCCESS_INTEREST){
				$(that).text('已关注');
				$(that).attr('action', 'undointerest');
				$(that).parent('.hidden').removeClass();
				$(that).parent().addClass('interested');
				//css('opacity', '0.7');
			} else if(data.status=SUCCESS_INTEREST_UNDO){
				$(that).text('加关注');
				$(that).attr('action', 'interest');
				$(that).parent('.interested').removeClass()
				$(that).parent().addClass('hidden');
			}
		})

	});
})