function alterarDesenvolvedores(icone) {
	$('.desenvolvedor').css('display', 'inline');
	$(icone).hide();
}

function alterarTestadores(icone) {
	$('.testador').css('display', 'inline');
	$(icone).hide();
}

function toogleDesenvolvedor(username) {
	var $imagem = $('#dev_img_' + username);
	var $campo = $('#dev_chk_' + username);

	if ($imagem.hasClass('ativo')) {
		$imagem.removeClass('ativo');
		$imagem.addClass('inativo');
		$campo.removeAttr('checked');
	} else {
		$imagem.removeClass('inativo');
		$imagem.addClass('ativo');
		$campo.attr('checked', 'checked');
	}
}

function toogleTestador(username) {
	var $imagem = $('#tes_img_' + username);
	var $campo = $('#tes_chk_' + username);

	if ($imagem.hasClass('ativo')) {
		$imagem.removeClass('ativo');
		$imagem.addClass('inativo');
		$campo.removeAttr('checked');
	} else {
		$imagem.removeClass('inativo');
		$imagem.addClass('ativo');
		$campo.attr('checked', 'checked');
	}
}

var alterarPrioridadeDaTarefa = function(ui, event) {
	var $tarefas = $('#listaTarefas li');
	var novaOrdem = new Array($tarefas.length);
	var indice = 0;
	$tarefas.each(function(i, el) {
		novaOrdem[indice++] = el.id;
	});
	$.post(pronto.raiz + 'tickets/${ticket.ticketKey}/ordenar', {
		'ticketKey' : novaOrdem
	});
};

$(function() {
	$('#formTicket').validate();
	$("#descricao").markItUp(mySettings);
	$("#comentario").markItUp(mySettings);
	$("#titulo").focus();
	$("#dialog").dialog( {
		autoOpen : false,
		height : $(document).height() - 50,
		width : $(document).width() - 50,
		modal : true
	});
	$("#listaTarefas").sortable( {
		placeholder : 'ui-state-highlight',
		stop : alterarPrioridadeDaTarefa
	});
	$("#listaTarefas").disableSelection();
});

function adicionarScript() {
	goTo(pronto.raiz + 'scripts/novo?ticketKey=${ticket.ticketKey}');
}

function editarScript() {
	goTo(pronto.raiz + 'scripts/${ticket.script.scriptKey}');
}

function excluirAnexo(ticketKey, anexo) {
	if (confirm('Tem certeza que deseja excluir este anexo?')) {
		pronto.doDelete(pronto.raiz + 'tickets/' + ticketKey + '/anexos/', [ {
			name : 'file',
			value : anexo
		} ]);
	}
}

function verDescricao(ticketKey) {
	$.ajax( {
		url : pronto.raiz + 'tickets/' + ticketKey + '/descricao',
		cache : false,
		success : function(data) {
			$("#dialog").dialog(
					'option',
					'title',
					'#' + ticketKey + ' - '
							+ $('#' + ticketKey + ' .titulo').text());
			$("#dialogDescricao").html(data);
			$("#dialog").dialog('open');
		}
	});
}