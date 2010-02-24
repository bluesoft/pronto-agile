function criarDialogDeVerTicket(){
	$("#dialog").dialog( {
		autoOpen : false,
		height : $(window).height() - 50,
		width : $(window).width() - 50,
		modal : true
	});
}

function verDescricao(ticketKey) {
	$.ajax({
		url: pronto.raiz + 'tickets/' + ticketKey + '/descricao',
		cache: false,
		success: function (data) {
			$("#dialog").dialog('option', 'title', '#' + ticketKey + ' - ' + $('#' + ticketKey + ' .titulo').text());
			$("#dialogDescricao").html(data);
			$("#dialog").dialog('open');
		}
	});
}

function criarPriorizacaoComDragAndDrop(){
	$(".ticketsTable tbody").sortable({
		items: "tr",
		cancel: ".header",
		accept: '.tr-ticket',
		connectWith: '.ticketsTable tbody',
		stop: onStopSorting	
	}).disableSelection();
}

function onStopSorting(event, ui) {
	
		var $table = ui.item.parents('table');
		var tickets = new Array();
		var valor = $table.attr('valor');
		$table.find('tr.tr-ticket').each(function(i, el){
			tickets.push(el.id);
		});
		
		$table.find('.vazio').remove();
		
		var url = pronto.raiz + 'backlogs/' + backlogKey + "/priorizar";
		$.post(url, {ticketKey: tickets, valor: valor});
		
		var from = $(this);
		if (from.find('tr').length == 0) {
			from.append('<tr class="vazio"><td colspan="5">&nbsp;</td></tr>');
		
		}
}

function criarDialogDeNovoGrupoDeValor() {
	$("#dialogValor").dialog({
			autoOpen:false,
			modal : true
	});
	$("#dialogValor").find('input').keypress(function(e) {
		 if (e.keyCode == 13) {
			 criarGrupo($(this).val());
			 $("#dialogValor").dialog("close");
			 $(this).val('');
			 return false;
		 }
	});
}

function exibirDialogDeCriarGrupo(){
	var dialog = $("#dialogValor");
	dialog.dialog("open");
	dialog.find('input').val('').focus();
}

function criarGrupo(valor){
	if ($('h4.valor-'+valor).length > 0){
		alert('Este Grupo Já Existe!');
	} else {
		var $novoGrupo = $('#modelo').clone();
		$novoGrupo.find('table').attr('valor', valor);
		$novoGrupo.find('h4').addClass('valor-'+valor).addClass('valor').text(valor).attr('valor',valor);

		//Insere grupo na ordem correta
		var valores = new Array();
		$('h4.valor').each(function(i,el){
			valores.push(parseInt($(el).attr('valor')));
		});
		if (valor > valores[0]) {
			$('#form').prepend($novoGrupo.children());
		} else if (valor < valores[valores.length-1]) {
			$('#buttons').before($novoGrupo.children());	
		} else {
			for (var i = 0; i < valores.length; i++) {
				if (valores[i] < valor) {
					$('h4.valor-'+valores[i]).before($novoGrupo.children());
					break;
				}
			}
		}
		
		criarPriorizacaoComDragAndDrop();
	}
};

$(function(){ 
	criarDialogDeVerTicket();
	criarPriorizacaoComDragAndDrop();
	criarDialogDeNovoGrupoDeValor();
	
	$(document).bind('keyup', 'Shift+A', exibirDialogDeCriarGrupo);

});
