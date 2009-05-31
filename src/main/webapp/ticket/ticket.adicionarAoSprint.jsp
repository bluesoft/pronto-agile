<%@ include file="/commons/taglibs.jsp"%>
<c:url var="adicionarTarefasUrl" value="/ticket/adicionarAoSprint.action"/>
<c:url var="cancelarUrl" value="/ticket/listarPorSprint.action"/>
<html>
	<head>
		<title>${sprint.nome}</title>
		</head>
	<body>
		<h1>Sprint ${sprint.nome}</h1>
		<h2>Adicionar Estórias ou Defeitos</h2>	
		<form action="${adicionarTarefasUrl}" method="post" >
			<input type="hidden" name="sprintKey" value="${sprint.sprintKey}"/>
			<table style="width: 100%">
				<tr>
					<th></th>
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
						<td><input type="checkbox" value="${t.ticketKey}" name="ticketKey"/></td>
						<td>${t.ticketKey}</td>
						<td>${t.titulo}</td>
						<td>${t.tipoDeTicket.descricao}</td>
						<td>${t.cliente}</td>
						<td>${t.valorDeNegocio}</td>
						<td>${t.esforco}</td>
						<td>${t.kanbanStatus.descricao}</td>
					</tr>
				</c:forEach>
			</table>	
			<div align="center">
				<button type="button" onclick="goTo('${cancelarUrl}?sprintKey=${sprint.sprintKey}')">Cancelar</button>
				<button type="submit">Adicionar</button>
			</div>
		</form>
	</body>
</html>