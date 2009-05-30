<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<script>
		$(function() {
		      $("#descricao").markItUp(mySettings);
		      $("#comentario").markItUp(mySettings);
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
			
			<c:if test="${ticket.ticketKey gt 0}">
				<h3>Descrição</h3>
				<div class="htmlbox">
					${ticket.html}
				</div>
			</c:if>
			
			<c:if test="${!empty ticket.comentarios}">
				<h3>Comentários</h3>
				<c:forEach items="${ticket.comentarios}" var="comentario">
					<div class="htmlbox">
						${comentario.html}
						<br/>
						<div align="right"><i>Por ${comentario.usuario} em <fmt:formatDate value="${comentario.data}" type="both"/></i></div> 
					</div>
				</c:forEach>
				<br/>
			</c:if>
			
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
					<form:input path="ticket.branch"/><br/>
					<p>Branch</p>
				</div>
				<div>
					<form:select path="ticket.par">
						<form:option value="true">Sim</form:option>
						<form:option value="false">Não</form:option>
					</form:select>
					<br/>
					<p>Em Par?</p>
				</div>
				<div>
					<form:select path="ticket.planejado">
						<form:option value="true">Sim</form:option>
						<form:option value="false">Não</form:option>
					</form:select>
					<br/>
					<p>Planejado?</p>
				</div>
				<h3>Descrição</h3>
				<div>
					<form:textarea path="ticket.descricao" id="descricao"/>
				</div>
				<h3>Comentário</h3>
				<div>
					<textarea id="comentario" name="comentario"></textarea>
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
	
		<c:if test="${ticket.ticketKey gt 0}">
			<h2>Anexos</h2>
			<form action="upload.action?ticketKey=${ticket.ticketKey}" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<input type="hidden" name="ticketKey" value="${ticket.ticketKey}">
				<button type="submit">Upload</button>
			</form>
		
			<h2>Logs</h2>
			<ul>
				<c:forEach items="${ticket.logs}" var="log">
					<li>${log.descricao}</li>
				</c:forEach>
			</ul>
		</c:if>
	</body>
	
	
</html>