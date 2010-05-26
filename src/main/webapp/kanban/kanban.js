var pronto = pronto ? pronto : {}; 

pronto.kanban = {
		
	startup: function(){
		this.urlMover = pronto.raiz + 'kanban/mover';
		this.setDragAndDrop();
	},

	mover: function(event, ui, drop) {
		var $item = ui.draggable;
		$item.fadeOut();
		var kanbanStatusKey = drop.attr('status');
		var ticketKey = ui.draggable.attr('id');
		var data = {'kanbanStatusKey' : kanbanStatusKey, 'ticketKey' : ticketKey};
		var url = this.urlMover;
		$.ajax({
			url: url, 
			data: data,
			dataType:'json',
			success: function(resposta) {
				if (resposta.sucesso == 'true') {
					$item.appendTo(drop);			
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
	}
		
};

$(function() {
	pronto.kanban.startup();
});

