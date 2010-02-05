function goTo(url) {
	window.location.href = url;
}

function openWindow(url) {
	window.open(url, "_blank");
}

var pronto = pronto ? pronto : {};

pronto.doGet = function(url) {
	goTo(url);
};

pronto.doDelete = function(url, data) {
	var f = $('<form></form>');
	f.attr({
		method: 'POST',
		action: url
	});
	f.append('<input type="hidden" name="_method" value="DELETE" />');
	$(document.body).append(f);
	pronto.includeDataInForm(f, data);
	f.submit();
};

pronto.doPost = function(url, data) {
	var f = $('<form>');
	f.attr({
		method : 'post',
		action : url
	});
	$(document.body).append(f);
	pronto.includeDataInForm(f, data);
	f.submit();
};


pronto.doPut = function(url, data) {
	var f = $('<form>');
	f.attr({
		method : 'post',
		action : url
	});
	f.append('<input type="hidden" name="_method" value="PUT" />');
	$(document.body).append(f);
	pronto.includeDataInForm(f, data);
	f.submit();
};

pronto.includeDataInForm = function ($form, data){
	if (data && data != null && data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			var entry = data[i];
			$form.append('<input type="hidden" name="'+entry.name+'" value="'+entry.value+'"/>');
		}
	}	
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
};

pronto.buscar =  function (){
	pronto.doPost(pronto.raiz + 'buscar/' + $('#busca').val(), [{name: 'kanbanStatusKey', value:1}]);
};

pronto.transformarEmEstoria = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/transformarEmEstoria');
};

pronto.transformarEmDefeito = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/transformarEmDefeito');
};

pronto.moverParaProductBacklog = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/moverParaProductBacklog');
};

pronto.moverParaIdeias = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/moverParaIdeias');
};

pronto.jogarNoLixo = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/jogarNoLixo');
};

pronto.impedir = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/moverParaImpedimentos');
};

pronto.restaurar = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/restaurar');
};

pronto.incluirTarefa = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/incluirTarefa');
};

pronto.moverParaSprintAtual = function(ticketKey){
	pronto.doGet(pronto.raiz + 'tickets/' + ticketKey + '/moverParaSprintAtual');
};
