$(document).ready(function () {
//	var currentModel = $("div.ttopMenu").attr("name");
	// 鼠标进入元素事件
//	$('#Topmenu .Toolbar1 .CentreBox .ttopMenu .List1 li').mouseenter(function () {
////        var index = $(this).parent().children().index(this);
//		$('#Topmenu .Toolbar1 .CentreBox .ttopMenu .List1 li').parent().children().each(function () {
//			alert(1);
//			if($(this).hasClass('Select')){
				
//			}
//            if (=="") {				if(){
//                $(this).removeClass('Select');
//            }
//        });
//        $(this).addClass('Select');
//    });
		
		$('#Topmenu .Toolbar1 .CentreBox .ttopMenu .List1 li').mouseenter(function(){
				if($(this).hasClass('Select')){
					$(this).removeClass('Select');
				}
				$(this).addClass('Select');
		});
		
		$('#Topmenu .Toolbar1 .CentreBox .ttopMenu .List1 li').mouseleave(function(){
					$(this).removeClass('Select');
		});
		
    
});