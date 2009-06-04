<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${usuario ne null}">
	<c:url value="/ticket/editar.action" var="editarTicketUrl"/>
	<c:url value="/ticket/sprintAtual.action" var="sprintAtualUrl"/>
	<div style="width: 90%" align="center">
		<div align="right">
			<pronto:icons onclick="goTo('${editarTicketUrl}?backlogKey=2&tipoDeTicketKey=3');" name="novo_defeito.png" title="Incluir Defeito"/>
			<pronto:icons onclick="goTo('${editarTicketUrl}?backlogKey=1&tipoDeTicketKey=1');" name="nova_ideia.png" title="Incluir Idéia"/>
			<pronto:icons onclick="goTo('${editarTicketUrl}?backlogKey=2&tipoDeTicketKey=2');" name="nova_estoria.png" title="Incluir Estória"/>
			<pronto:icons onclick="goTo('${sprintAtualUrl}');" name="sprint_atual.png" title="Sprint Atual"/>
		</div>
	</div>
</c:if>




















