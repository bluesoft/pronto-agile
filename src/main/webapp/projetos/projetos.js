$(function() {
	$('#formProjeto').validate();
	var $kanban = $('#kanban');
	$kanban.find('.first').addClass('ui-state-disabled').prepend('<span class="ui-icon"></span>');
	$kanban.find('.last').addClass('ui-state-disabled').prepend('<span class="ui-icon"></span>');
	$kanban.find('.middle').addClass('ui-state-default').prepend('<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>').append('<span class="ui-icon ui-icon-closethick"></span>').append('<span class="ui-icon ui-icon-pencil"></span>');
	$kanban.sortable({
		cancel : ".ui-state-disabled",
		stop : atualizarOrdensDasEtapas
	});

	$('.ui-icon-closethick').live('click', function() {
		var $li = $(this).parents('li');
		var key = $li.attr('key');
		$.post('excluirEtapa', {
			kanbanStatusKey : key
		}, function(salvou) {
			if (salvou == "true") {
				$li.remove();
			} else {
				alert('Não foi possível excluir.');
			}
		});
	});

	$('.ui-icon-pencil').live('click', function() {
		var $li = $(this).parents('li');
		var key = $li.attr('key');
		var $descricao = $li.find('.etapa-descricao');
		var novoNome = prompt("Informe a nova descrição da etapa", $descricao.text());
		

		if(novoNome != null || novoNome != '') {
			alert("lalalala");
			alert(novoNome);
			$.post('editarEtapa', {
				kanbanStatusKey : key,
				nome : novoNome
			}, function(salvou) {
				if (salvou) {
					$descricao.text(novoNome);
				} else {
					alert('Não foi possível editar a etapa.');
				}
			});
	}
});

});

function incluirEtapa() {
	var nome = prompt('Informe o Nome da Etapa');
	if (nome != null && nome.length > 0){
		$.post("incluirEtapa.action", {
			projetoKey : projetoKey,
			nome : nome
		}, function(key) {
			var $kanban = $('#kanban');
			var $novo = $('<li class="middle" key="' + key + '"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span><span class="etapa-descricao">' + nome + '</span><span class="ui-icon ui-icon-closethick"></span><span class="ui-icon ui-icon-pencil"></span></li>');
			$novo.addClass('ui-state-default').prepend();
			$kanban.find('li.middle:last').after($novo);
		});
	}
}


function atualizarOrdensDasEtapas() {
	var itens = new Array();
	$('#kanban').find('.middle').each(function(i, el) {
		itens.push($(el).attr('key'));
	});
	$.post("atualizarOrdensDasEtapas.action", {
		kanbanStatusKey : itens
	}, function(salvou) {
	});
}