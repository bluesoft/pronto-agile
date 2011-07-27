var modelos = null;

function alterarEnvolvidos(icone) {
	$('.envolvido').css('display', 'inline');
	$(icone).hide();
}

function toogleEnvolvido(username) {
	var $imagem = $('#dev_img_' + username);
	var $campo = $('#dev_chk_' + username);
	tooglePerson($imagem, $campo);
}

function toogleNotificar(username) {
	var $imagem = $('#notificar_img_' + username);
	var $campo = $('#notificar_chk_' + username);
	tooglePerson($imagem, $campo);
}

function tooglePerson($imagem, $campo) {
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
	$.post(pronto.raiz + 'tickets/' + ticketKey + '/ordenar', {
		'ticketKey' : novaOrdem
	});
};

$(function() {
	$('#formTicket').validate();
	$("#descricao").markItUp(mySettings);
	$("#comentario").markItUp(mySettings);
	$("#comentarioZendesk").markItUp({onShiftEnter:	{keepDefault:false, replaceWith:'\n\n'},markupSet: []});
	$("#titulo").focus();
	$("#dialog").dialog( {
		autoOpen : false,
		height : $(document).height() - 50,
		width : $(document).width() - 50,
		modal : true
	});
	$("#dialogResponsavel").dialog( {
		autoOpen : false,
		height : 300,
		width : 600,
		modal : true
	});
	$("#listaTarefas").sortable( {
		placeholder : 'ui-state-highlight',
		stop : alterarPrioridadeDaTarefa
	});
	$("#listaTarefas").disableSelection();
});

function escolherResponsavel() {
	$("#dialogResponsavel").dialog("open");
}

function adicionarScript() {
	goTo(pronto.raiz + 'scripts/novo?ticketKey=' + ticketKey);
}

function editarScript() {
	goTo(pronto.raiz + 'scripts/' + scriptKey);
}

function excluirAnexo(ticketKey, anexo) {
	if (confirm('Tem certeza que deseja excluir este anexo?')) {
		pronto.doDelete(pronto.raiz + 'tickets/' + ticketKey + '/anexos/', [ {
			name : 'file',
			value : anexo
		} ]);
	}
}


function excluirComentario(ticketKey, ticketComentarioKey) {
	if (confirm('Tem certeza que deseja excluir este comentário?')) {
		var url = pronto.raiz + 'tickets/' + ticketKey + '/comentarios/' + ticketComentarioKey;
		$.post(url, {'_method':'delete'}, function(result){
			if (result == "true") {
				$('#comentario-'+ticketComentarioKey).fadeOut();
			} 
		});
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

function salvar() {
	$('#formTicket').submit();
}

function alterarStatuDoKanbanPara(statusKey) {
	$("#kanbanStatusKey").val(statusKey);
	alterarStatuDoKanban();
	$('#formTicket').submit();
}

function alterarStatuDoKanban() {
	var kanbanStatusAnterior = $("#kanbanStatusAnterior").val();
	var kanbanStatusKey = $("#kanbanStatusKey").val();
	var $motivoReprovacaoCombo = $("#motivoReprovacaoKey");
	var $motivoReprovacaoDiv = $("#motivoReprovacaoDiv");
	
	if (ordens) {
		if (parseInt(ordens[kanbanStatusAnterior]) > parseInt(ordens[kanbanStatusKey])) {
			$motivoReprovacaoCombo.addClass('requiredCombo');
			$motivoReprovacaoDiv.show();
		} else {
			$motivoReprovacaoCombo.removeClass('requiredCombo');
			$motivoReprovacaoDiv.hide();
		}
	}
}

function buscarTicketDeOrigem(ticketKey) {
	openWindow(pronto.raiz+"tickets/"+ticketKey+"/selecionarOrigem", 'selecaoDeOrigem');
}

function definirOrigem(ticketKey, ticketOrigemKey) {
	$("#iconBuscarOrigem").hide();
	$("#descricaoOrigem").text("");
	$("#spanTicketOrigem").text("Ticket de origem de defeito associado");
	$("<b>Origem: <a style='cursor:pointer' onclick='abrirTicket("+ticketOrigemKey+")'>#"+ticketOrigemKey+"</a></b>").appendTo("#descricaoOrigem");
	$("<img src='"+iconsFolder+"/excluir.png' title='Clique aqui para desassociar este ticket de origem de defeito' onclick='excluirTicketDeOrigem("+ticketKey+");'/>").appendTo("#descricaoOrigem");
}

function abrirTicket(ticketKey) {
	openWindow(pronto.raiz+"tickets/"+ticketKey, 'ticketKeyOrigem');
}

function excluirTicketDeOrigem(ticketKey) {
	pronto.doPost(pronto.raiz+"tickets/"+ticketKey+"/excluirTicketDeOrigem");
}

function escolherSprintParaMover(ticketKey) {
	if ($('#selecionarSprint').find('option').length == 1){
		pronto.moverParaSprintAtual(ticketKey,false);
	} else {
		var $div = $("#dialogSelecionarSprint");
		$div.find('button').button();
		var $dialog = $div.dialog();
		$dialog.dialog('open');
	}	
}

function adicionarVinculoComZendesk(){
	$("#dialogVincularAoZendesk").dialog( {
		height : 200,
		width : 300,
		modal : true
	});
}

function confirmarVinculo(ticketKey){
	var zendeskTicketKeyVincular = $('#zendeskTicketKeyVincular').val();
	if(zendeskTicketKeyVincular == "" || zendeskTicketKeyVincular == null){
		alert("Informe o número do ticket do Zendesk");
	}else{
		$.ajax( {
			url : pronto.raiz + 'tickets/' + ticketKey + '/vincularTicketAoZendesk?zendeskTicketKey=' +zendeskTicketKeyVincular,
			cache : false,
			success : function(retorno) {
				var data = eval('(' + retorno + ')');
				if (data.isSucces == "true") {
					$("#dialogVincularAoZendesk").dialog('close');
					salvar();
				} else {
					pronto.erro(data.mensagem);
				}
			}
		});
	}
}

function excluirVinculoComZendesk(ticketKey){
	$.ajax( {
		url : pronto.raiz + 'tickets/' + ticketKey + '/excluirVinculoComZendesk',
		cache : false,
		success : function(data) {
			if (data == "true") {
				salvar();
			} else {
				pronto.erro("Ocorreu um erro ao excluir o vinculo com o ticket que fazia referencia com o Zendesk.");
			}
		}
	});
}

function alterarTamanhoPara(tamanho) {	
	$("[id^=camiseta_]").removeClass('ativo').addClass('inativo');
	$("[id^=camiseta_"+tamanho+"]").toggleClass("inativo ativo");
	
	$("#esforco").val(tamanho);
}

function filtrarEtapas(){
	var projetoKey = $('#projetoKey').val();
	var $first = null;
	var $anterior = null;
	var kanbanStatusAnterior = $('#kanbanStatusAnterior').val();
	$('#kanbanStatusKey').find('option').each(function(i,el){
		var $el = $(el);
		if ($el.attr('projetoKey') == projetoKey) {
			$el.show();
			$el.removeAttr('disabled');
			if ($first == null) { 
				$first = $el; 
			}
			if ($el.val() == kanbanStatusAnterior) {
				$anterior = $el; 		
			}
		} else {
			$el.hide();
			$el.attr('disabled','disabled');			
		}
		
		if ($anterior) {
			$anterior.attr('selected','selected');
		} else if ($first) {
			$first.attr('selected','selected');
		}
	});
}

function incluirChecklist(){
	var nome = prompt('Informe o nome do checklist.');
	if (nome == null || nome.length == 0) {
		return;
	}
	$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists', {
		nome:nome
	}, function(checklistKey) {
		incluirChecklistHtml(checklistKey, nome);
	});
}

function incluirChecklistHtml(checklistKey, nome) {
	var span = '<span class="title">'+nome+'</span>';
	var input = '<ul></ul><input type="text" class="addChecklistItem" checklistKey="'+checklistKey+'"/>';
	var html = '<div class="checklist" id="checklist-'+checklistKey+'">' + span + input + '<hr/></div>';
	$('#checklistsArea').append(html);
}

function exibirModelosDeChecklist() {
	loadModelos();
	var $div = $("#dialogSelecionarModelo");
	$div.find('button').button();
	var $dialog = $div.dialog();
	$dialog.dialog('open');
}

function incluirChecklistComBaseEmModelo() {
	var modeloKey = $('#modelosDeChecklist').val();
	$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists/modelo/'+modeloKey, function(checklist) {
		incluirChecklistHtml(checklist.checklistKey, checklist.nome);
		$.each(checklist.itens, function(index, item){
			incluirItemNoChecklistHtml(checklist.checklistKey, item.checklistItemKey, item.descricao);
		});
	});
	$("#dialogSelecionarModelo").dialog('close');
}

function loadModelos() {
	var modelos = $('#modelosDeChecklist');
	if (modelos.find('option').length == 0) {
		$.ajax({
				url: pronto.raiz+'checklists/modelos',
				async:false,
				success: function(m) {
					$.each(m, function(i, modelo){
						modelos.append('<option value="'+modelo.checklistKey+'">'+modelo.nome+'</option>');
					});
				}
		});
	}
}

function incluirItemNoChecklist(checklistKey, descricao){
	if (descricao == null || descricao.length == 0) {
		return;
	}
	$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists/'+checklistKey, {
		descricao:descricao
	}, function(checklistItemKey) {
		incluirItemNoChecklistHtml(checklistKey, checklistItemKey, descricao);
	});
}

function incluirItemNoChecklistHtml(checklistKey, checklistItemKey, descricao) {
	$('#checklist-'+checklistKey).find('ul').append('<li id="checklistItem-'+checklistItemKey+'"><input class="checklistItem" checklistItemKey="'+checklistItemKey+'" type="checkbox">'+descricao+'<span class="excluirChecklistItem ui-icon ui-icon-close" item="Excluir item do checklist"></span></li>');
}

function eventoDeIncluirItemNoChecklist() {
	$('.addChecklistItem').live('keypress', function(event) {
		if (event.keyCode == '13') {
			     event.preventDefault();
			     $campo = $(event.target);
			     var checklistKey = $campo.attr('checklistKey');
			     incluirItemNoChecklist(checklistKey, $campo.val());
			     $campo.val('');
		   }
	});
	
	$('.checklistItem').live('click', function(event){
		event.preventDefault();
		$campo = $(event.target);
		var checklistItemKey = $campo.attr('checklistItemKey');
	    toogleChecklistItem(checklistItemKey);
	});
	
	$('.excluirChecklistItem').live('click',function(event){
		var checklistItemKey = $(event.target).parents('li').find('input').attr('checklistItemKey');
		excluirChecklistItem(checklistItemKey);
	});
}

function excluirChecklist(checklistKey) {
	if (confirm('Tem certeza que deseja excluir o checklist?')) {
		$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists/'+checklistKey, {
			_method: 'DELETE'
		}, function(resposta) {
			$('#checklist-'+checklistKey).remove();
		});
	}
}

function toogleChecklistItem(checklistItemKey) {
	$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists/0/'+checklistItemKey, {
	}, function(resposta) {
		var $item = $('input[checklistItemKey='+checklistItemKey+']');
		if (resposta) {
			$item.attr('checked', 'checked');
		} else {
			$item.removeAttr('checked');
		}
	});
}

function excluirChecklistItem(checklistItemKey) {
	$.post(pronto.raiz+'tickets/'+ticketKey+'/checklists/0/'+checklistItemKey, {
		_method: 'DELETE'
	}, function(resposta) {
		$('#checklistItem-'+checklistItemKey).remove();
	});
}

$(function(){
	$("#motivoReprovacaoDiv").hide();
	filtrarEtapas();
	eventoDeIncluirItemNoChecklist();
});

