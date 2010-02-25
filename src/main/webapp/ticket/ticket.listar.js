function recarregar(sprintKey) {
	goTo(pronto.raiz + 'backlogs/sprints/' + sprintKey);
}

function apagarLinha(ticketKey) {
	$('#' + ticketKey).add('tr[pai=' + ticketKey + ']').fadeOut('slow',
			function() {
				$(this).remove();
			});
}

function salvarCategoria(select) {

	var $select = $(select);
	var categoriaKey = $select.val();
	var ticketKey = $select.attr('ticketKey');

	var url = pronto.raiz + 'tickets/' + ticketKey + '/salvarCategoria';
	$.post(url, {
		'ticketKey' : ticketKey,
		'categoriaKey' : categoriaKey
	});

	if (categoriaKey > 0) {
		var $td = $select.parents('td');
		var $selectedOption = $select.find('option:selected');
		var clazz = $selectedOption.attr('categoriaClass');
		var $label = $('<span class="categoria ' + clazz + '"/>');
		$label.text($selectedOption.text());
		$td.append($label);
	}

	$select.hide();
	$select.removeAttr('ticketKey');
}

function trocarCategoria(td) {
	var ticketKey = $(td).parents('tr').attr('id');
	var $select = $('#trocaCategoria');
	if (ticketKey != $select.attr('ticketKey')) {
		$select.attr('ticketKey', ticketKey);
		$(td).append($select);
		$select[0].selectedIndex = -1;
		$select.show();
		var $td = $select.parents('td');
		$td.find('.categoria').remove();
	}
}

function recalcular() {

	var valorDeNegocio = 0;
	$('.valorDeNegocio').each(function(i, el) {
		valorDeNegocio += parseFloat($(el).text());
	});
	$('#somaValorDeNegocio').text(valorDeNegocio);

	var esforco = 0;
	$('.esforco').each(function(i, el) {
		esforco += parseFloat($(el).text());
	});
	$('#somaEsforco').text(esforco);
}

function verDescricao(ticketKey) {
	var titulo = $('#' + ticketKey + ' .titulo').text();
	$.ajax( {
		url : pronto.raiz + 'tickets/' + ticketKey + '/descricao',
		cache : false,
		success : function(data) {
			$("#dialog").dialog('option', 'title',
					'#' + ticketKey + ' - ' + titulo);
			$("#dialogDescricao").html(data);
			$("#dialog").dialog('open');
		}
	});
}

function exibirOpcoes(tr) {
	$(tr).find('.opcao').show();
}

function esconderOpcoes(tr) {
	$(tr).find('.opcao').hide();
}

function criarDialog(){
	$("#dialog").dialog( {
		autoOpen : false,
		height : $(document).height() - 50,
		width : $(document).width() - 50,
		modal : true
	});
}

function criarEventoDeTrocarCategoria(){
	var $table = $('#ticketsTable'); 
	$table.find('tbody tr').mouseenter(function(){
		exibirOpcoes(this);
	}).mouseleave(function(){
		esconderOpcoes(this);
	});
}

$(function() {
	criarDialog();
	criarEventoDeTrocarCategoria();
});
