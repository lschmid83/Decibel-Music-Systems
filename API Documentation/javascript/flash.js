!function(t){t.flash=function(e){t.flash.notice(e)},t.flash.timeout=5e3;var e=[],i=function(i,n,r){for(var o;o=e.shift();)clearTimeout(o);t.flash.current=i,t(".ajaxNoticeValid:first").html("<p>"+i+"</p>").removeClass("error").removeClass("info").addClass(n),t("#flashWrapper").fadeIn().click(function(){t.flash.hide()}),r&&r.hide!==!1&&e.push(setTimeout(t.flash.hide,t.flash.timeout))};t.flash.notice=function(t,e){i(t,"info",e)},t.flash.error=function(t,e){i(t,"error",e)},t.flash.hide=function(){t.flash.current=null,t("#flashWrapper").fadeOut()}}(jQuery);