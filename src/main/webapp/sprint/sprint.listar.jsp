<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Sprints</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<table style="width: 100%">
			<thead>
			<tr>
				<th>nome</th>
				<th>período</th>
				<th></th>
				<th></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${sprints}" var="s">
				<tr>
					<td>${s.nome}</td>
					<td>${s.dataInicial} à ${s.dataFinal}</td>
					<td><a href="editar.action?sprintKey=${s.sprintKey}">Editar</a></td>
					<td><a href="../ticket/listarPorSprint.action?sprintKey=${s.sprintKey}">Ver Estórias</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		<div align="center">
			<a href="editar.action">Novo</a>
		</div>
	</body>
</html>