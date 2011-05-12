<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Modelos de Checklist</title>
	</head>
	<body>
		<h1>Modelos de Checklist</h1>
		<table style="width: 100%" id="checklists">
			<thead>
			<tr>
				<th>Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${checklists}" var="c">
					<tr>
						<td>
							<span class="checklist">
								${c.nome}
							</span>
						</td>
						<c:if test="${usuarioLogado.equipe or usuarioLogado.administrador}">
							<td>
								<pronto:icons name="editar.png" title="Editar Checklist" onclick="goTo('${raiz}checklists/${c.checklistKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir Checklist" onclick="pronto.doDelete('${raiz}checklists/${c.checklistKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.equipe  or usuarioLogado.administrador}">
			<div align="center" class="buttons">
				<button type="button" onclick="window.location.href='${raiz}checklists/novo'">Incluir Checklist</button>
			</div>
		</c:if>
		<script>
			$(function(){
				$('#checklists').zebrar();
			});
		</script>
	</body>
</html>