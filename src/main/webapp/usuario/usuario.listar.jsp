<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Pronto Agile</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<table style="width: 100%">
			<thead>
			<tr>
				<th>username</th>
				<th>Nome</th>
				<th>e-mail</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${usuarios}" var="u">
				<tr>
					<td>${u.username}</td>
					<td>${u.nome}</td>
					<td>${u.email}</td>
					<td>
						<pronto:icons name="editar_usuario.png" title="Editar Usuário" onclick="goTo('editar.action?username=${u.username}')"/>
					</td>
					<td>
						<pronto:icons name="excluir_usuario.png" title="Excluir Usuário" onclick="goTo('excluir.action?username=${u.username}')"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		<div align="center">
			<a href="editar.action">Incluir Usuário</a>
		</div>
	</body>
</html>