<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
	</head>
	<body>
		<c:choose>
			<c:when test="${sprint.nome ne null}">
				<h1>Sprint ${sprint.nome}</h1>	
			</c:when>
			<c:otherwise>
				<h1>${backlog.descricao}</h1>
			</c:otherwise>
		</c:choose>
		
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Valor de Negócio</th>
				<th>Esforço</th>
				<th>Status</th>
			</tr>
			<c:forEach items="${tickets}" var="t">
				<tr id="${t.ticketKey}">
					<td>
						${t.ticketKey}
						<input type="hidden" name="ticketKey" value="${t.ticketKey}"/>
					</td>
					<td>${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td><input type="text" size="5" name="valorDeNegocio" value="${t.valorDeNegocio}"/></td>
					<td><input type="text" size="5" name="esforco" value="${t.esforco}"/></td>
					<td>${t.kanbanStatus.descricao}</td>
				</tr>
			</c:forEach>
		</table>	
	
		<div align="center">
			<button type="button">Cancelar</button>
			<button type="button">Salvar</button>	
		</div>
	</body>
</html>