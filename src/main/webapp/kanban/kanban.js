var pronto = pronto ? pronto : {}; 

pronto.kanban = {

	ticketData: null,
		
	startup: function(){
		this.urlMover = pronto.raiz + 'kanban/mover';
		this.urlAtualizar = pronto.raiz + 'kanban/atualizar/';
		this.setDragAndDrop();
		this.criarDialogDeMotivoDeReprovacao();
		var umMinuto = 1000 * 60;
		setInterval(this.atualizar, umMinuto);
	},
	
	mover: function(event, ui, drop) {
		var $item = ui.draggable;
		var kanbanStatusAntigo = $item.parents('.kanbanColumn').attr('status');
		var kanbanStatusKey = drop.attr('status');
		var ticketKey = ui.draggable.attr('id');
		var data = {'kanbanStatusKey' : kanbanStatusKey, 'ticketKey' : ticketKey};
		
		var motivo = null;
		if (parseInt(ordens[kanbanStatusAntigo]) > parseInt(ordens[kanbanStatusKey])) {
			this.selecionarMotivo($item, drop, data);
		} else {
			this.salvarMovimentacao($item, drop, data);
		}
	},
	
	calcularQuantidadeDeTicketsPorColuna: function() {
		$('.kanban-area').each(function(){
			var $this = $(this);
			var quantidade = $this.find('.ticket').length;
			$this.find('h4 span.quantidade').text(quantidade);
		});
	},
	
	atualizar: function() {
		var sprintKey = $('#sprintKey').val();
		$.getJSON(pronto.kanban.urlAtualizar+sprintKey, function(data){
			for (item in data) {
				var $item = $('#'+item);
				if ($item.length > 0 && parseInt($item.attr('kanbanStatus')) != parseInt(data[item])) {
					$item.fadeOut();
					var novoStatus = data[item];
					var $novaColuna = $('#kanban-'+novoStatus); 
					$item.appendTo($novaColuna);
					$item.fadeIn();
					pronto.kanban.calcularQuantidadeDeTicketsPorColuna();
				}
			}
		});
	},
	
	salvarMovimentacao: function($item, drop, data) {
		$item.fadeOut();
		var url = this.urlMover;
		$.ajax({
			url: url, 
			data: data,
			dataType:'json',
			success: function(resposta) {
				if (resposta.sucesso == 'true') {
					$item.appendTo(drop);
					$item.attr('kanbanStatus', drop.attr('status'));
					pronto.kanban.calcularQuantidadeDeTicketsPorColuna();
				} else {
					pronto.erro(resposta.mensagem);
				}
				$item.fadeIn();
			}
		});	
	},
	
	setDragAndDrop: function() {
		
		var $kanbanColumns = ('.kanbanColumn');
		var $drop = $('.drop');

		$('li', $kanbanColumns).draggable( {
			revert : 'invalid', // when not dropped, the item will return
			helper : 'clone',
			cursor : 'move'
		});
		
		$drop.droppable( {
			accept : 'li',
			drop : function(event, ui) {
				pronto.kanban.mover(event, ui, $(this));
			}
		});	
	},
	
	openTicket: function (ticketKey) {
		openWindow(pronto.raiz + 'tickets/' + ticketKey);
	},

	recarregar: function (sprintKey) {
		goTo(pronto.raiz + 'kanban/' + sprintKey);
	},
	
	selecionarMotivo: function($item, drop, data) {
		var combo = $('#motivoReprovacaoKey');
		combo.val("-1");
		combo.change();
		var divMotivo = $("#motivo");
		pronto.kanban.ticketData = {'item':$item, 'drop':drop, 'data':data };
		divMotivo.dialog('open');
	},
	
	criarDialogDeMotivoDeReprovacao: function(){
		var divMotivo = $("#motivo");
		divMotivo.dialog({
			title: "Selecione o Motivo da Reprovação",
			height: 100,
			modal: true,
			autoOpen: false, 
		    overlay: { opacity: 0.5,background: "black" },  
		    buttons: { 
		        "Confirmar": function() {
		    		var ticketData = pronto.kanban.ticketData; 	
		    		ticketData.data.motivoReprovacaoKey = $("#motivoReprovacaoKey").val();
		    		pronto.kanban.salvarMovimentacao(ticketData.item,ticketData.drop,ticketData.data);		    		
		            $(this).dialog("close"); 
		        }
		    } 
		});
	},
	
	alterarMotivo: function(combo) {
		var $combo = $(combo);
		if ($combo.val() != "-1") {
			$('#motivo').parents('.ui-dialog').find('button').show();
		} else {
			$('#motivo').parents('.ui-dialog').find('button').hide();
		}
	}
		
};

$(function() {
	pronto.kanban.startup();
});

