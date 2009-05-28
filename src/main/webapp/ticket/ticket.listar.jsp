<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
	</head>
	<body>
		<h1>${backlog.descricao}${sprint.nome}</h1>
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Valor de Negócio</th>
				<th>Esforço</th>
			</tr>
			<c:forEach items="${tickets}" var="t">
				<tr>
					<td>${t.ticketKey}</td>
					<td>${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td>${t.valorDeNegocio}</td>
					<td>${t.esforco}</td>
					<td><a href="editar.action?ticketKey=${t.ticketKey}">Editar</a></td>
				</tr>
			</c:forEach>
		</table>	
		
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1">Nova Idéia</a>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2}">
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2">Nova Estória</a>&nbsp;&nbsp;
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3">Novo Defeito</a>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 5}">
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=5">Novo Impedimento</a>&nbsp;&nbsp;
				</c:when>
			</c:choose>
			|
		</div>
	</body>
</html>