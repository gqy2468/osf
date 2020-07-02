var tags = new Array();
$(document).ready(function(){
	$(document).on('click', '.delete.icon', function(event) {
		var rmtag = $(this).parent('.ui.label').text();
		for(var i=0; i<tags.length; i++) {
			if(tags[i] == rmtag) {
				tags.splice(i, 1);
				$(this).parent('.ui.label').remove();
			}
		}
	});

	$('.tagfield .button').click(function(event) {
		var tag_input = $('#tags').val();
		var tag_name = $(this).val();
		if (!tag_input) {
			tag_input = tag_name;
		} else if (tag_input.indexOf(tag_name) < 0) {
			tag_input += ',' + tag_name;
		}
		$('#tags').val(tag_input);
	});
})