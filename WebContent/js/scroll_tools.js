/*
Author      : yenange
Description : "返回顶部/快速回复/返回列表/返回底部" 边栏功能实现
Date        : 2013-11-06
*/
(function ($) {
    $.fn.scrolltools = function ( op ) {
        var defaultOp = {
            showToTop               : true,
            showToReply             : false,
            showToList              : false,
            showToBottom            : true,
            alwaysShowToTop         : true,
            alwaysShowToBottom      : true,
            hiddenTopIconHeight     : 100,
            hiddenBottomIconHeight  : 100,
            replayFn                : function(){ alert('进入回复'); },
            listFn                  : function(){ alert('进入列表'); }
        };

        op = $.extend(defaultOp, op);

        var $outDiv   = $(this);                    //外围div
        var $toTop    = $outDiv.find(".toTop");     //返回顶部
        var $toBottom = $outDiv.find(".toBottom");  //返回底部

        //将设置为不显示的元素给隐藏
        $toTop.toggle(op.showToTop);
        $toBottom.toggle(op.showToBottom);

        //将外层位置设置在页面中部
        $outDiv.css( {"top" : ($(window).height())/2  });
        $(window).resize(function(){
            $outDiv.css( {"top" : ($(window).height())/2  });
        });

        //如果 [向上图标] 需要显示，但不需要一直显示 ( 离顶部距离很小时消失 )
        if(op.showToTop===true && op.alwaysShowToTop===false){
            $(window).scroll(function () {
                $toTop.toggle( $(window).scrollTop() > op.hiddenTopIconHeight );
            });
        }
        //如果 [向下图标] 需要显示，但不需要一直显示 ( 离底部距离很小时消失 )
        if(op.showToBottom===true && op.alwaysShowToBottom===false){
            $(window).scroll(function () {
                $toBottom.toggle( op.hiddenBottomIconHeight + $(window).scrollTop() < $(document).height() - $(window).height() );
            });
        }
        // [向上图标] 需要显示, 为其定义事件
        if(op.showToTop===true){
            $toTop.click(function(){
                window.scroll(0,0);
            });
        }
        // [向下图标] 需要显示, 为其定义事件
        if(op.showToBottom===true){
            $toBottom.click(function(){
                window.scroll(0,$(document).height() - $(window).height());
            });
        }
    }
})(jQuery);