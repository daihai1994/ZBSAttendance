$(function(){
	
	//给背景颜色赋高度
	var a;
	a=$(".g-wrap").offset().top;
	$(".g-bg-color").css({"height":a+420+"px"})
	
	//动态改变高度
	$(window).resize(function(){
		a=$(".g-wrap").offset().top;
		$(".g-bg-color").css({"height":a+420+"px"})	
	})
	
//placeholder兼容ie
var JPlaceHolder = {
    //检测
    _check : function(){
        return 'placeholder' in document.createElement('input');
    },
    //初始化
    init : function(){
        if(!this._check()){
            this.fix();
        }
    },
    //修复
    fix : function(){
        jQuery(':input[placeholder]').each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
            var holder = $('<span></span>').text(txt).css({position:'absolute', left:pos.left+12, top:pos.top+8, height:h, lienHeight:h, paddingLeft:paddingleft, color:'#cccccc',fontSize:14}).appendTo(self.parent());
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};

JPlaceHolder.init();  
	
})
