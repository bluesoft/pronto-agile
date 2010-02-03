<%@ include file="/commons/taglibs.jsp"%>
<c:url var="retrospectivaUrl" value="/retrospectiva/ver.action"/>
<c:url var="burndownUrl" value="/burndown/burndown.action"/>
<c:url var="kanbanUrl" value="/kanban/kanban.action"/>
<html>
	<head>
		<title>Sprints</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
		<c:url var="urlSprint" value="/sprints"/>
		<script>
			function fechar(sprintKey) {
				var msg = "Tem certeza que desejar fechar o Sprint? As estórias ou defeitos em aberto serão movidas para o Sprint Atual.";
				if (confirm(msg)) {
					goTo('${urlSprint}/'+sprintKey+'/fechar');
				}
			}

			function reabrir(sprintKey) {
				var msg = "Tem certeza que desejar reabrir o Sprint?";
				if (confirm(msg)) {
					goTo('${urlSprint}/'+sprintKey+'/reabrir');
				}
			}		
		</script>
	</head>
	<body>
		<h1>Sprints</h1>
		<table style="width: 100%">
			<thead>
			<tr>
				<th style="width: 18px"></th>
				<th>Nome</th>
				<th>Período</th>
				<th>Esforço</th>
				<th>Valor de Negócio</th>
				<th style="width: 88px" colspan="7"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${false}"/>
			<c:forEach items="${sprints}" var="s">
				<c:set var="cor" value="${!cor}"/>
				<tr style="${s.atual ? 'background-color:ebf5fc;' : ''}" class="${cor ? 'even' : 'odd'}">
					<td>
						<c:choose>
							<c:when test="${s.atual}">
								<pronto:icons name="sprint_atual.png" title="Sprint Atual" />
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${s.fechado}">
										<pronto:icons name="fechado.png" title="Sprint Fechado" />
									</c:when>
									<c:otherwise>
										<pronto:icons name="definir_sprint_atual.png" title="Definir Sprint como Atual" onclick="pronto.doPost('${urlSprint}/${s.sprintKey}/mudarParaAtual')"/>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
					<td>${s.nome}</td>
					<td><fmt:formatDate value="${s.dataInicial}"/> à <fmt:formatDate value="${s.dataFinal}"/></td>
					<td>${s.esforcoTotal}</td>
					<td>${s.valorDeNegocioTotal}</td>
					<td><pronto:icons name="editar_sprint.png" title="Editar Sprint" onclick="goTo('${urlSprint}/${s.sprintKey}')"/></td>
					<td>
						<c:if test="${!s.atual}">
							<c:choose>
								<c:when test="${s.fechado}">
									<pronto:icons name="reabrir.gif" title="Reabrir Sprint" onclick="reabrir(${s.sprintKey})"/>
								</c:when>
								<c:otherwise>
									<pronto:icons name="fechar.png" title="Fechar Sprint" onclick="fechar(${s.sprintKey})"/>
								</c:otherwise>
							</c:choose>
						</c:if>
					</td>
					<td><pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="goTo('${urlSprint}../ticket/listarPorSprint.action?sprintKey=${s.sprintKey}')"/></td>
					<td><pronto:icons name="kanban.png" title="Kanban do Sprint" onclick="goTo('${kanbanUrl}?sprintKey=${s.sprintKey}')"/></td>
					<td><pronto:icons name="burndown_chart.png" title="Burndown Chart do Sprint" onclick="goTo('${burndownUrl}?sprintKey=${s.sprintKey}')"/></td>
					<td><pronto:icons name="retrospectiva.png" title="Retrospectiva" onclick="goTo('${retrospectivaUrl}?sprintKey=${s.sprintKey}')"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		<div align="center">
			<button type="button" onclick="window.location.href='${urlSprint}/novo'">Incluir Sprint</button>
		</div>
	</body>
</html>