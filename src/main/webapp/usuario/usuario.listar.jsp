<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Usuários</title>
	</head>
	<body>
		<h1>Usuários</h1>
		<table style="width: 100%">
			<thead>
			<tr>
				<th colspan="2">Username</th>
				<th>Nome</th>
				<th>E-mail</th>
				<th style="width: 54px" colspan="3"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${usuarios}" var="u">
				
				<c:if test="${usuarioLogado.username eq u.username or usuarioLogado.administrador}">
			
				<c:set var="cor" value="${!cor}"/>
				<tr style="height: 18px" class="${cor ? 'even' : 'odd'}">
					<td style="width: 30px;">
						<c:if test="${u.emailMd5 ne null && u.emailMd5 ne ''}">
							<img alt="Gravatar" align="top" title="Gravatar - Globally Recognized Avatars" src="http://www.gravatar.com/avatar/${u.emailMd5}?s=25" />
						</c:if>
					</td>
					<td>${u.username}</td>
					<td>${u.nome}</td>
					<td>${u.email}</td>
					<td>
						<c:if test="${usuarioLogado.username eq u.username or usuarioLogado.administrador}">
							<pronto:icons name="trocar_senha.png" title="Trocar Senha" onclick="goTo('${raiz}usuarios/${u.username}/trocarSenha')"/>
						</c:if>
					</td>
					<td>
						<c:if test="${usuarioLogado.administrador}">
							<pronto:icons name="editar_usuario.png" title="Editar Usuário" onclick="goTo('${raiz}usuarios/${u.username}')"/>
						</c:if>
					</td>
					<td>
						<c:if test="${usuarioLogado.administrador}">
							<pronto:icons name="excluir_usuario.png" title="Excluir Usuário" onclick="excluir('${u.username}')"/>
						</c:if>
					</td>
				</tr>
				
				</c:if>
			</c:forEach>
			</tbody>
		</table>	
		<c:if test="${usuarioLogado.administrador}">
			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}usuarios/novo'">Incluir Usuário</button>
			</div>
		</c:if>
		<script>
			function excluir(username) {
				pronto.confirm('Confirma a exclusão do usuário ' + username + '?', function() { pronto.doDelete('${raiz}usuarios/' + username); });
			}
		</script>
	</body>
</html>