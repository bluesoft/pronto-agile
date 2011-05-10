<%@ include file="/commons/taglibs.jsp"%>
<c:url value="/tickets/novo" var="editarTicketUrl"/>
<c:url value="/backlogs/sprints/atual" var="sprintAtualUrl"/>
<c:url value="/kanban" var="kanbanUrl"/>
<c:url value="/stream" var="streamUrl"/>

<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner or usuarioLogado.equipe or usuarioLogado.scrumMaster}">
	<span id="menuRapido" align="right">
		<a href="${editarTicketUrl}?tipoDeTicketKey=3"><pronto:icons name="novo_defeito.png" title="Incluir Defeito"/></a>
		<a href="${editarTicketUrl}?tipoDeTicketKey=2"><pronto:icons name="nova_estoria.png" title="Incluir Estória"/></a>
		<a href="${sprintAtualUrl}"><pronto:icons name="sprint_atual.png" title="Lista de Estória e Defeitos do Sprint Atual"/></a>
		<a href="${kanbanUrl}"><pronto:icons name="kanban.png" title="Kanban do Sprint Atual"/></a>
		<a href="${streamUrl}"><pronto:icons name="stream.png" title="Stream"/></a>
	</span>
</c:if>
