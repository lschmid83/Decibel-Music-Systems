if("undefined"==typeof Help)var Help={};!function(e){Help={toggleHelp:function(t){var i=e("#help_layer_"+t.id),n=e(t);if(i.is(":visible"))i.hide(),n.removeClass("active"),t.src="/assets/provider/icons/questionPassive.png",t.style.zIndex=10;else{n.addClass("active");var o=function(e){var t=e.offset().top-(e.offset().top-e.position().top)+e.height()-1,i=e.offset().left-(e.offset().left-e.position().left);return{top:t,left:i}}(n,i);i.css({zIndex:99999}),i.show(),i.css({top:o.top+"px",left:o.left+"px"}),t.src="/assets/provider/icons/questionClose.png",t.style.zIndex=1e5}}}}(jQuery);