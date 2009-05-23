<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Pronto Agile</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<table>
			<tr>
				<th>#</th>
				<th>Título</th>
			</tr>
			<c:forEach items="${tickets}" var="t">
				<tr>
					<td>${t.ticketKey}</td>
					<td>${t.titulo}</td>
					<td><a href="ticket.editar.action?ticketKey=${t.ticketKey}">Editar</a></td>
				</tr>
			</c:forEach>
		</table>	
		
		<div align="center">
			<a href="ticket.editar.action">Novo</a>
		</div>
	</body>
</html>