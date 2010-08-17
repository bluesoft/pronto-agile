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
	var query = $('#busca').val();
	pronto.doPost(pronto.raiz + 'buscar/', [{name: 'kanbanStatusKey', value:-1}, {name:'query', value:query}]);
};

pronto.transformarEmEstoria = function(ticketKey, ajax){
	var url = pronto.raiz + 'tickets/' + ticketKey + '/transformarEmEstoria';
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.transformarEmDefeito = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/transformarEmDefeito');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.moverParaProductBacklog = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/moverParaProductBacklog');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.moverParaIdeias = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/moverParaIdeias');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.jogarNoLixo = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/jogarNoLixo');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.impedir = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/moverParaImpedimentos');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.restaurar = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/restaurar');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.incluirTarefa = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/incluirTarefa');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.moverParaSprintAtual = function(ticketKey, ajax){
	var url = (pronto.raiz + 'tickets/' + ticketKey + '/moverParaSprintAtual');
	pronto.moverTicket(ticketKey, url, ajax);
};

pronto.moverTicket = function(ticketKey, url, ajax){
	if (ajax) {
		$.get(url);
		$('#'+ticketKey).fadeOut('slow', function(){
			$(this).remove();
		});
	} else {
		pronto.doGet(url);	
	}
};

jQuery.fn.zebrar = function(){
    return this.each(function(){
    	$(this).find('tbody tr:odd').addClass('odd');
    	$(this).find('tbody tr:even').addClass('even');
    });
};

pronto.erro = function(mensagem){
	$.gritter.add({
		title: 'Erro',
		text: mensagem,
		image: pronto.raiz + 'commons/icons/erro_big.png'
	});
};

pronto.alerta = function(mensagem){
	$.gritter.add({
		title: 'Atenção',
		text: mensagem,
		image: pronto.raiz + 'commons/icons/alerta_big.png'
	});
};
