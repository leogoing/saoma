// JavaScript Document

 $('.left_menu li').click(function(){//去掉所有
 	$('.left_menu li').removeClass('action');
    //当前添加
    $(this).addClass('action');
});

$('.left_menu dt').toggle(function() {
    $(this).nextAll('dd').show(500);
},function(){
    $(this).nextAll('dd').hide(500);
});