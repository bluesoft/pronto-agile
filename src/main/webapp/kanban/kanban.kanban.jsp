<%@ include file="/commons/taglibs.jsp"%>
<c:url var="moverUrl" value="/kanban/mover.action"/>
<c:url var="editarTicket" value="/ticket/editar.action"/>
<html>
	<head>
		<title>Kanban</title>
		<style>

			#kanbanTable tr, #kanbanTable td  {
				vertical-align: top;
			}

			.kanbanColumn.custom-state-active { background: #eee; }
			.kanbanColumn li { float: left; width: 96px; padding: 0.4em; margin: 0 0.4em 0.4em 0; text-align: center; }
			.kanbanColumn li h5 { margin: 0 0 0.4em; cursor: move; }
		
			.drop {
				height: 100%;
				height: ${sprint.quantidadeDeTickets * 65 + 100}px;
			}
		
			.ticket {
				width: 100px;
				height: 70px; 
				font-family: Tahoma;
				font-size: 10px;
			}
		
			.bug {
				background-color: #FFCCCC;
			}
			
			.story {
				background-color: #FFFF99;
			}
			
			.task {
				background-color: #dddddd;
			}
			
			
		</style>
		<script>
		$(function() {
			var urlMover = '${moverUrl}';

			var $kanbanColumns = ('.kanbanColumn');
			var $drop = $('.drop');
			
			// let the gallery items be draggable
			$('li',$kanbanColumns).draggable({
				revert: 'invalid', // when not dropped, the item will revert back to its initial position
				helper: 'clone',
				cursor: 'move'
			});

			// let the trash be droppable, accepting the gallery items
			$drop.droppable({
				accept: 'li',
				activeClass: 'ui-state-highlight',
				drop: function(event, ui) {
					mover(event, ui, $(this));
				}
			});
			
			

			function mover(event, ui, drop) {

				var $item = ui.draggable;
				$item.fadeOut();
				

				var kanbanStatusKey = $(this).attr('status');
				var ticketKey = ui.draggable.attr('id');
				$.post(urlMover, {
					'kanbanStatusKey': kanbanStatusKey,
					'ticketKey': ticketKey
				});

				$item.appendTo(drop).fadeIn();
			}

		});

		
		function openTicket(ticketKey) {
			goTo('${editarTicket}?ticketKey=' + ticketKey);
		}
		</script>	

	</head>
	<body>
		<h1>Kanban (Sprint ${sprint.nome})</h1>
		<table align="center" style="width: 100%;" id="kanbanTable">
			<tr>
				<td style="width: 25%" class="drop"> 		
					<div class="ui-widget ui-helper-clearfix" status="1">
						<h4 class="ui-widget-header">TO DO</h4>

						<ul id="todo" class="kanbanColumn ui-helper-reset ui-helper-clearfix">
							<c:forEach items="${tickets}" var="t">
								<c:if test="${t.kanbanStatus.kanbanStatusKey eq 1}">
									<li class="ticket ui-corner-tr ${t.tipoDeTicket.tipoDeTicketKey eq 3 ? 'bug' : (t.tipoDeTicket.tipoDeTicketKey eq 6 ? 'task' : 'story')}" ondblclick="openTicket(${t.ticketKey});">
										<p>#${t.ticketKey} - ${t.tituloResumido}</p>
									</li> 
								</c:if>
							</c:forEach>
						</ul>
						
						
					</div>
				</td>
				<td style="width: 25%; height: 100%">
					<div class="ui-widget ui-helper-clearfix" status="2">
						<h4 class="ui-widget-header">DOING</h4>
						<ul id="todo" class="kanbanColumn ui-helper-reset ui-helper-clearfix drop">
						</ul>
					</div>
				</td>
				<td style="width: 25%">
					<div class="ui-widget ui-helper-clearfix" status="21">
						<h4 class="ui-widget-header">TESTING</h4>
						<ul id="todo" class="kanbanColumn ui-helper-reset ui-helper-clearfix drop">
						</ul>
					</div>
				</td>
				<td  style="width: 25%">
					<div class="ui-widget ui-helper-clearfix" status="200">
						<h4 class="ui-widget-header">DONE</h4>
						<ul id="todo" class="kanbanColumn ui-helper-reset ui-helper-clearfix drop">
						</ul>
					</div>
				</td>
			</tr>
		</table>
		<div align="center">* Clique duas vezes sobre o cartão para abrí-lo.</div>
	</body>
</html>