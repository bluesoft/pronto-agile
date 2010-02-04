var pronto = pronto ? pronto : {}; 

pronto.kanban = {
		
	startup: function(){
		this.urlMover = pronto.raiz + 'kanban/mover.action';
		this.setDragAndDrop();
	},

	mover: function(event, ui, drop) {
		var $item = ui.draggable;
		$item.fadeOut();
		var kanbanStatusKey = drop.attr('status');
		var ticketKey = ui.draggable.attr('id');
		var data = {'kanbanStatusKey' : kanbanStatusKey, 'ticketKey' : ticketKey};
		$.post(this.urlMover, data);
		$item.appendTo(drop).fadeIn();
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
			activeClass : 'ui-state-highlight',
			drop : function(event, ui) {
				pronto.kanban.mover(event, ui, $(this));
			}
		});	
	},
	
	openTicket: function (ticketKey) {
		openWindow(pronto.raiz + 'ticket/editar.action?ticketKey=' + ticketKey);
	},

	recarregar: function (sprintKey) {
		goTo(pronto.raiz + '$kanban/kanban.action?sprintKey=' + sprintKey);
	}
		
};

$(function() {
	pronto.kanban.startup();
});

