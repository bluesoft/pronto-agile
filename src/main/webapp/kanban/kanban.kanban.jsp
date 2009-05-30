<%@ include file="/commons/taglibs.jsp"%>
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
			$(".draggable").draggable();
			$(".droppable").droppable({
				drop: function(event, ui) {
					$(this).find('p').html('Dropped!');
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
							<div id=${t.ticketKey}" class="ui-widget-content draggable">
								<p>${t.titulo}</p>
							</div>
						</c:forEach>
					</div>
				</td>
				<td style="width: 25%">
					<div id="doing" class="ui-widget-header droppable" status="2">
						<p>DOING</p>
					</div>
				</td>
				<td style="width: 25%">
					<div id="testing" class="ui-widget-header droppable" status="21">
						<p>TESTING</p>
					</div>
				</td>
				<td  style="width: 25%">
					<div id="done" class="ui-state-highlight droppable" status="100">
						<p>DONE</p>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>