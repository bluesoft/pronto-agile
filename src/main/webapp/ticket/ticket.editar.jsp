<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<script>
		$(function() {
		      $("#descricao").markItUp(mySettings);
		 });
		</script>
	</head>
	<body>
		<form action="salvar.action" method="post">
			<c:choose>
				<c:when test="${ticket.ticketKey gt 0}">
					<form:hidden path="ticket.ticketKey"/>
					<ul class="info"><h1>${ticket.tipoDeTicket.descricao} #${ticket.ticketKey}</h1></ul>
				</c:when>
				<c:otherwise>
					<ul class="info"><h1>${ticket.tipoDeTicket.descricao}</h1></ul>
				</c:otherwise>
			</c:choose>
			<div class="group">
				<div>
					<form:hidden path="ticket.tipoDeTicket.tipoDeTicketKey"/>
					<b>${ticket.tipoDeTicket.descricao}</b>					
					<p>Tipo</p>
				</div>
				<div>
					<form:hidden path="ticket.backlog.backlogKey"/>
					<b>${ticket.backlog.descricao}</b>					
					<p>Backlog</p>
				</div>
				<div>
					<form:input path="ticket.titulo" size="120"/>
					<p>Título</p>
				</div>
				<div>
					<form:input path="ticket.cliente"/><br/>
					<p>Cliente</p>
				</div>
				<div>
					<form:input path="ticket.solicitador"/><br/>
					<p>Solicitador</p>
				</div>
				<div>
					<b>${ticket.reporter.nome}</b>
					<form:hidden path="ticket.reporter.usuarioKey"/><br/>
					<p>Reporter</p> 
				</div>
				<div>
					<form:input path="ticket.valorDeNegocio"/><br/>
					<p>Valor de Negócio</p>
				</div>
				<div>
					<form:input path="ticket.esforco"/><br/>
					<p>Esforço</p>
				</div>
				<div>
					<form:textarea path="ticket.descricao" id="descricao"/><br/>
				</div>
			</div>
			<div align="center">
				<c:choose>
					<c:when test="${ticket.sprint ne null}">
						<button type="button" onclick="window.location.href='listarPorSprint.action?sprintKey=${ticket.sprint.sprintKey}'">Cancelar</button>
					</c:when>
					<c:otherwise>
						<button type="button" onclick="window.location.href='listarPorBacklog.action?backlogKey=${ticket.backlog.backlogKey}'">Cancelar</button>
					</c:otherwise>
				</c:choose>
				
				<button type="submit">Salvar</button>
			</div>
			
		</form>		
	
	
	</body>
	
	
</html>