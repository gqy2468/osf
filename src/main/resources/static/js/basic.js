basePath = 'http://127.0.0.1:8787/';
//img_base_url= 'http://pp62pwq9r.bkt.clouddn.com/';
img_base_url= 'http://127.0.0.1:8787/files/';
post_cover_thumbnail='?imageView2/2/w/500';
album_thumbnail='?imageView2/1/w/200/h/200';

var current_version = 'develop';
var osf = {
		develop:{
			basePath:'',
			post_cover_style:null,
			album_thumbnail:null
		},
		release:{
			basePath:'',
			post_cover_style:'@!postcover',
			album_thumbnail:'@!albumthumbnail'
		}
		
}

$(document).ready(function(){
	var request_uri = window.location.pathname;
	$("ul.navbar-nav li").removeClass('active');
	$("ul.navbar-nav a[href='" + request_uri + "']").parents('li').addClass('active');
	
	$("a.right").click(function(){
  		var that = this;
		if ($(this).hasClass('loading')) return false;
		var pnum = $(this).parent().attr("pnum");
		pnum = typeof pnum != 'undefined' ? parseInt(pnum) : 1;
	  	var url = $(this).parent().attr("url");
	  	var refer = $(this).parent().attr("refer");
	  	$(this).addClass('loading');
		$.ajax({
			url: url + (pnum + 1),
			type: 'GET',
			dataType: 'html'
		})
		.success(function(data) {
			$(that).removeClass('loading');
			var content = $.trim($(data).find('.' + refer).html());
			if (content) {
				$(document).find('.content .' + refer).html(content);
				$(that).parent().attr("pnum", pnum + 1);
			} else {
				alert('没有下一页了');
			}
		})
		.fail(function() {
			alert("请求失败");
			$(that).removeClass('loading');
		});
	});
	
	$("a.left").click(function(){
  		var that = this;
		if ($(this).hasClass('loading')) return false;
		var pnum = $(this).parent().attr("pnum");
		pnum = typeof pnum != 'undefined' ? parseInt(pnum) : 1;
		if (pnum <= 1) {
			alert('没有上一页了');
			return false;
		}
	  	var url = $(this).parent().attr("url");
	  	var refer = $(this).parent().attr("refer");
	  	$(this).addClass('loading');
		$.ajax({
			url: url + (pnum - 1),
			type: 'GET',
			dataType: 'html'
		})
		.success(function(data) {
			$(that).removeClass('loading');
            var content = $.trim($(data).find('.' + refer).html());
			if (content) {
				$(document).find('.content .' + refer).html(content);
				$(that).parent().attr("pnum", pnum - 1);
			} else {
				alert('没有上一页了');
			}
		})
		.fail(function() {
			alert("请求失败");
			$(that).removeClass('loading');
		});
	});
});