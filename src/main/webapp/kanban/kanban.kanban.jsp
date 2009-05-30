<%@ include file="/commons/taglibs.jsp"%>
<c:url var="moverUrl" value="/kanban/mover.action"/>
<html>
	<head>
		<title>Kanban</title>
		<style>
			.draggable {
				width: 90px;
				height: 60px; 
			}
			.droppable {
				height: 500px;
			}
		</style>
		<script>
		$(function() {
			var urlMover = '${moverUrl}';
			$(".draggable").draggable();
			$(".droppable").droppable({
				drop: function(event, ui) {
					var kanbanStatusKey = $(this).attr('status');
					var ticketKey = ui.draggable.attr('id');
					$.post(urlMover, {
						'kanbanStatusKey': kanbanStatusKey,
						'ticketKey': ticketKey
					});
				}
			});

		});
		</script>	

	</head>
	<body>
		<h1>Kanban</h1>
		
		<table align="center" style="width: 100%; height: 500px">
			<tr>
				<td style="width: 25%">		
					<div id="todo" class="ui-widget-header droppable" status="1">
						<p>TO DO</p>
						<c:forEach items="${tickets}" var="t">
							<c:if test="${t.kanbanStatus.kanbanStatusKey eq 1}">
								<div id="${t.ticketKey}" class="ui-widget-content draggable">
									<p>#${t.ticketKey} - ${t.titulo}</p>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</td>
				<td style="width: 25%">
					<div id="doing" class="ui-widget-header droppable" status="2">
						<p>DOING</p>
						<c:forEach items="${tickets}" var="t">
							<c:if test="${t.kanbanStatus.kanbanStatusKey eq 2}">
								<div id="${t.ticketKey}" class="ui-widget-content draggable">
									<p>#${t.ticketKey} - ${t.titulo}</p>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</td>
				<td style="width: 25%">
					<div id="testing" class="ui-widget-header droppable" status="21">
						<p>TESTING</p>
						<c:forEach items="${tickets}" var="t">
							<c:if test="${t.kanbanStatus.kanbanStatusKey eq 21}">
								<div id="${t.ticketKey}" class="ui-widget-content draggable">
									<p>#${t.ticketKey} - ${t.titulo}</p>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</td>
				<td  style="width: 25%">
					<div id="done" class="ui-state-highlight droppable" status="100">
						<p>DONE</p>
						<c:forEach items="${tickets}" var="t">
							<c:if test="${t.kanbanStatus.kanbanStatusKey eq 100}">
								<div id="${t.ticketKey}" class="ui-widget-content draggable">
									<p>#${t.ticketKey} - ${t.titulo}</p>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>