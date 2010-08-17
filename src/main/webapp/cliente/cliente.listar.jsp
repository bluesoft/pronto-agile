<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Clientes</title>
	</head>
	<body>
		<h1>Clientes</h1>
		<table style="width: 100%">
			<thead>
			<tr>
				<th style="width: 100%">Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${clientes}" var="c">
				<c:set var="cor" value="${!cor}"/>
				<tr style="height: 18px" class="${cor ? 'even' : 'odd'}">
					<td>${c.nome}</td>
					<td>
						<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
							<pronto:icons name="editar_usuario.png" title="Editar Usuário" onclick="goTo('${raiz}clientes/${c.clienteKey}')"/>
						</c:if>
					</td>
					<td>
						<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
							<pronto:icons name="excluir_usuario.png" title="Excluir Usuário" onclick="pronto.doDelete('${raiz}clientes/${c.clienteKey}')"/>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}clientes/novo'">Incluir Cliente</button>
			</div>
		</c:if>
	</body>
</html>