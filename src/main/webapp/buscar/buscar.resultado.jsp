<%@ include file="/commons/taglibs.jsp"%>
<c:url var="editarTicketUrl" value="/ticket/editar.action"/>

<html>
	<head>
		<title>Busca</title>
	</head>
	<body>
		<h1>Resultado da Busca</h1>
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
					<td>${t.ticketKey}</td>
					<td>${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td>${t.valorDeNegocio}</td>
					<td>${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
					<td>
						<pronto:icons name="editar.png" title="Editar" onclick="goTo('${editarTicketUrl}?ticketKey=${t.ticketKey}')"></pronto:icons>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="4"></th>
				<th>${sprint.valorDeNegocioTotal}${backlog.valorDeNegocioTotal}</th>
				<th>${sprint.esforcoTotal}${backlog.esforcoTotal}</th>
				<th></th>
			</tr>
		</table>	
	</body>
</html>