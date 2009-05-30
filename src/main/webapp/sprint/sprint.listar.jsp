<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Sprints</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
		<c:url var="urlSprint" value="/sprint/"/>
	</head>
	<body>
		<table style="width: 100%">
			<thead>
			<tr>
				<th style="width: 18px"></th>
				<th>nome</th>
				<th>período</th>
				<th style="width: 18px"></th>
				<th style="width: 18px"></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${sprints}" var="s">
				<tr style="${s.atual ? 'background-color:FFFFCC;' : ''}">
					<td>
						<c:choose>
							<c:when test="${s.atual}">
								<pronto:icons name="sprint_atual.png" title="Sprint Atual" />
							</c:when>
							<c:otherwise>
								<pronto:icons name="definir_sprint_atual.png" title="Definir Sprint como Atual" onclick="goTo('${urlSprint}atual.action?sprintKey=${s.sprintKey}')"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td>${s.nome}</td>
					<td>${s.dataInicial} à ${s.dataFinal}</td>
					<td><pronto:icons name="editar_sprint.png" title="Editar Sprint" onclick="goTo('${urlSprint}editar.action?sprintKey=${s.sprintKey}')"/></td>
					<td><pronto:icons name="ver_estorias.png" title="Ver Estórias" onclick="goTo('${urlSprint}../ticket/listarPorSprint.action?sprintKey=${s.sprintKey}')"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		<div align="center">
			<a href="editar.action">Novo</a>
		</div>
	</body>
</html>