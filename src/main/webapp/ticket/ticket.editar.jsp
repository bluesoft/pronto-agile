<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="ticket.salvar.action">
			<c:if test="${ticket.ticketKey gt 0}">
				#${ticket.ticketKey}<br/>
				<form:hidden path="ticket.ticketKey"/>
			</c:if>
			Título: <form:input path="ticket.titulo"/><br/>
			Descrição: <form:textarea path="ticket.descricao"/><br/>
			<button type="submit">Salvar</button><br/>
		</form>		
	</body>
</html>