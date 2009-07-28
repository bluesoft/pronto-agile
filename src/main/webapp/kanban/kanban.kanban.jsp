<%@ include file="/commons/taglibs.jsp"%>
<c:url var="moverUrl" value="/kanban/mover.action"/>
<c:url var="editarTicket" value="/ticket/editar.action"/>
<c:url var="kanbanUrl" value="/kanban/kanban.action" />
<html>
	<head>
		<meta http-equiv=refresh content="60" />
		<title>Kanban</title>
		<style>
			#kanbanTable tr, #kanbanTable td  {
				vertical-align: top;
				width: 100%;
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
				cursor: crosshair;
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
					var kanbanStatusKey = drop.attr('status');
					var ticketKey = ui.draggable.attr('id');
					$.post(urlMover, {
						'kanbanStatusKey': kanbanStatusKey,
						'ticketKey': ticketKey
					});
	
					$item.appendTo(drop).fadeIn();
				}
	
			});
	
			
			function openTicket(ticketKey) {
				openWindow('${editarTicket}?ticketKey=' + ticketKey);
			}
	
			function recarregar(sprintKey) {
				goTo('${kanbanUrl}?sprintKey=' + sprintKey);
			}
		
		</script>	

	</head>
	<body>
		<h1>Kanban do Sprint
			<c:choose>
				<c:when test="${fn:length(sprints) eq 1}">
					${sprints[0].nome}
				</c:when>
				<c:otherwise>
					<form:select path="sprint.sprintKey" onchange="recarregar(this.value)">
						<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
					</form:select>
				</c:otherwise>
			</c:choose>
		</h1>
		<table align="center" style="width: 100%;" id="kanbanTable">
			<tr>
                <c:forEach items="${status}" var="s">
                    <c:choose>
                        <c:when test="${s.kanbanStatusKey eq 1 || s.kanbanStatusKey eq 2 || s.kanbanStatusKey eq 100}">
                            <td style="width: 20%; height: 100%">
						</c:when>
						<c:otherwise>
							<td style="width: 10%; height: 100%">
						</c:otherwise>
					</c:choose>
                                <div class="ui-widget ui-helper-clearfix">
                                    <h4 class="ui-widget-header">${s.descricao}</h4>
                                    <ul class="kanbanColumn ui-helper-reset ui-helper-clearfix drop" status="${s.kanbanStatusKey}">
                                        <c:forEach items="${tickets}" var="t">
                                            <c:if test="${t.kanbanStatus.kanbanStatusKey eq s.kanbanStatusKey}">
                                                <li id="${t.ticketKey}" class="ticket ui-corner-tr ${t.tipoDeTicket.tipoDeTicketKey eq 3 ? 'bug' : (t.tipoDeTicket.tipoDeTicketKey eq 6 ? 'task' : 'story')}" ondblclick="openTicket(${t.ticketKey});" title="${t.titulo}">
                                                    <p><b>#${t.ticketKey}</b><br>${t.tituloResumido}</p>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </td>
                </c:forEach>
			</tr>
		</table>
		<div align="center">* Clique duas vezes sobre o cartão para abrí-lo.</div>
	</body>
</html>