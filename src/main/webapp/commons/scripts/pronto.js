function goTo(url) {
	window.location.href = url;
}

function openWindow(url) {
	window.open(url, "_blank");
}

var pronto = pronto ? pronto : {};

pronto.doDelete = function(url) {
	var f = $('<form>');
	f.attr({
		method : 'post',
		action : url
	});
	f.append('<input type="hidden" name="_method" value="DELETE" />');
	f.submit();
};

pronto.doPost = function(url) {
	var f = $('<form>');
	f.attr({
		method : 'post',
		action : url
	});
	f.submit();
};

pronto.doPut = function(url) {
	var f = $('<form>');
	f.attr({
		method : 'post',
		action : url
	});
	f.append('<input type="hidden" name="_method" value="PUT" />');
	f.submit();
};

pronto.confirm = function(message, yesCallback, noCallback, onOpen, onClose) {
	var id = "divModal" + Math.ceil(Math.random()*1000);
	var divMensagem = $("<div id='"+id+"'>" + message + "</div>");  
	divMensagem.dialog({
		title: "Atenção",
		height: 170,
		modal: true, 
	    overlay: { 
	        opacity: 0.5, 
	        background: "black" 
	    },  
	    open: function() {
	    	if ($.isFunction(onOpen)) {
				onOpen.apply();
			} 
	    },
	    close: function() {
	    	if ($.isFunction(onClose)) {
				onClose.apply();
			}
	    },
	    buttons: { 
	        "Não": function() { 
	            if ($.isFunction(noCallback)) {
					noCallback.apply();
				}	
	            $(this).dialog("close"); 
	            $('#'+id).remove();
	        },
	        "Sim": function() { 
	            if ($.isFunction(yesCallback)) {
					yesCallback.apply();
				}		
	            $(this).dialog("close");
	            $('#'+id).remove(); 
	        }
	    } 
	});
}