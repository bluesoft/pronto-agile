<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="salvar.action" method="post">
			<ul class="info">
				<h1>Cadastro de Usuários</h1>
			</ul>
			<div class="group">
				<c:choose>
					<c:when test="${usuario.usuarioKey gt 0}">
						<form:hidden path="usuario.usuarioKey"/>
						<form:hidden path="usuario.username"/>
						<div>
							<b>${usuario.username}</b>
							<p>username</p>
						</div>
					</c:when>
					<c:otherwise>
						<div>
							<form:input path="usuario.username"/>
							<p>username</p>
						</div>
					</c:otherwise>
				</c:choose>
				<div>
					<form:input path="usuario.nome"/>
					<p>Nome</p>
				</div>
				<div>
					<form:input path="usuario.email"/>
					<p>e-mail</p>
				</div>
				<div>
					<select size="5" multiple="multiple" name="papel">
						<c:forEach items="${papeis}" var="papel">
							<option value="${papel.papelKey}">${papel.descricao}</option>
						</c:forEach>
					</select>
					<p>Papéis</p>
				</div>
				<div align="center">
					<button type="button" onclick="window.location.href='listar.action'">Cancelar</button>
					<button type="submit">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>