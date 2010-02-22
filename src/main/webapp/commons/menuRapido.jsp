<%@ include file="/commons/taglibs.jsp"%>
<c:url value="/tickets/novo" var="editarTicketUrl"/>
<c:url value="/backlogs/sprints/atual" var="sprintAtualUrl"/>
<c:url value="/kanban" var="kanbanUrl"/>
<c:if test="${usuarioLogado ne null and (usuarioLogado.productOwner or usuarioLogado.equipe or usuarioLogado.scrumMaster)}">
	<div style="width: 90%" align="center">
		<div align="right">
			<a href="${editarTicketUrl}?backlogKey=2&tipoDeTicketKey=3"><pronto:icons name="novo_defeito.png" title="Incluir Defeito"/></a>
			<a href="${editarTicketUrl}?backlogKey=1&tipoDeTicketKey=1"><pronto:icons name="nova_ideia.png" title="Incluir Ideia"/></a>
			<c:if test="${usuarioLogado.productOwner}">
				<a href="${editarTicketUrl}?backlogKey=2&tipoDeTicketKey=2"><pronto:icons name="nova_estoria.png" title="Incluir Estória"/></a>
			</c:if>
			<a href="${sprintAtualUrl}"><pronto:icons name="sprint_atual.png" title="Lista de Estória e Defeitos do Sprint Atual"/></a>
			<a href="${kanbanUrl}"><pronto:icons name="kanban.png" title="Kanban do Sprint Atual"/></a>
		</div>
	</div>
</c:if>
