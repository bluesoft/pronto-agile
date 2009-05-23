<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="ticket.salvar.action">
			# <form:input path="ticketKey"/><br/>
			Título: <form:input path="titulo"/><br/>
			Descrição: <form:textarea path="descricao"/><br/>
			<button type="submit">Salvar</button><br/>
		</form>		
	</body>
</html>