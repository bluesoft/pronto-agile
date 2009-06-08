<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Pronto Agile</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<h1>Usuários</h1>
		<table style="width: 100%">
			<thead>
			<tr>
				<th>username</th>
				<th>Nome</th>
				<th>e-mail</th>
				<th colspan="3"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${usuarios}" var="u">
				<c:set var="cor" value="${!cor}"/>
				<tr style="height: 18px" class="${cor ? 'even' : 'odd'}">
					<td>${u.username}</td>
					<td>${u.nome}</td>
					<td>${u.email}</td>
					<td>
						<c:if test="${usuarioLogado.username eq u.username or usuarioLogado.scrumMaster}">
							<pronto:icons name="trocar_senha.png" title="Trocar Senha" onclick="goTo('digitarSenha.action?username=${u.username}')"/>
						</c:if>
					</td>
					<td>
						<c:if test="${usuarioLogado.scrumMaster}">
							<pronto:icons name="editar_usuario.png" title="Editar Usuário" onclick="goTo('editar.action?username=${u.username}')"/>
						</c:if>
					</td>
					<td>
						<c:if test="${usuarioLogado.scrumMaster}">
							<pronto:icons name="excluir_usuario.png" title="Excluir Usuário" onclick="goTo('excluir.action?username=${u.username}')"/>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.scrumMaster}">
			<div align="center">
				<a href="editar.action">Incluir Usuário</a>
			</div>
		</c:if>
	</body>
</html>