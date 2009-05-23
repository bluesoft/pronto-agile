<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="salvar.action">
			
			<c:choose>
				<c:when test="${usuario.usuarioKey gt 0}">
					<form:hidden path="usuario.usuarioKey"/>
					<form:hidden path="usuario.username"/>
					username: ${usuario.username}<br/>
				</c:when>
				<c:otherwise>
					username: <form:input path="usuario.username"/><br/>
				</c:otherwise>
			</c:choose>
			Nome: <form:input path="usuario.nome"/><br/>
			e-mail: <form:input path="usuario.email"/><br/>
			<button type="submit">Salvar</button><br/>
		</form>		
	</body>
</html>