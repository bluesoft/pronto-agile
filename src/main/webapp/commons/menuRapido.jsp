<%@ include file="/commons/taglibs.jsp"%>
<c:url value="/tickets/novo" var="editarTicketUrl"/>
<c:url value="/backlogs/sprints/atual" var="sprintAtualUrl"/>
<c:url value="/backlogs/productBacklog" var="productBacklogUrl"/>
<c:url value="/backlogs/inbox" var="inboxUrl"/>
<c:url value="/kanban" var="kanbanUrl"/>
<c:url value="/stream" var="streamUrl"/>
<c:url var="ticketsEnvolvidosUrl" value="/buscar/?kanbanStatusKey=-1&ignorarLixeira=true&envolvido=${usuarioLogado.username}"/>

<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner or usuarioLogado.equipe or usuarioLogado.scrumMaster}">
	<span id="menuRapido" align="right">
		<a href="${ticketsEnvolvidosUrl}"><pronto:icons name="minhas_pendencias.png" title="Ver ticket pendêntes em que você está envolvido."/></a>
		<a href="${inboxUrl}"><pronto:icons name="inbox.png" title="Ir para Inbox"/></a>
		<a href="${productBacklogUrl}"><pronto:icons name="product_backlog.png" title="Ir para Product Backlog"/></a>
		<a href="${sprintAtualUrl}"><pronto:icons name="sprint_atual.png" title="Ir para Sprint Atual"/></a>
		<a href="${kanbanUrl}"><pronto:icons name="kanban.png" title="Kanban do Sprint Atual"/></a>
		<a href="${editarTicketUrl}?tipoDeTicketKey=2"><pronto:icons name="nova_estoria.png" title="Incluir Estória"/></a>
		<a href="${editarTicketUrl}?tipoDeTicketKey=3"><pronto:icons name="novo_defeito.png" title="Incluir Defeito"/></a>
		<a href="${streamUrl}"><pronto:icons name="stream.png" title="Ir para Stream"/></a>
	</span>
</c:if>
