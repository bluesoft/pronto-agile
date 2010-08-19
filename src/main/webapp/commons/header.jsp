<%@ include file="/commons/taglibs.jsp"%>
<script>
	$(function(){
		$('#busca').keypress(function(e) {
			 if (e.keyCode == 13) {
				pronto.buscar();
			 }
		});
	});
</script>
<span id="branding">
	<h1><a href="<c:url value="/kanban"/>"><pronto:images name="pronto_logo.png" title="Pronto!" /></a></h1>
</span>

<c:if test="${usuarioLogado ne null and (usuarioLogado.administrador or usuarioLogado.productOwner or usuarioLogado.scrumMaster or usuarioLogado.equipe)}">
	<span id="search">
		Busca <input type="text" name="busca" id="busca" accesskey="b"/>
		<pronto:icons name="buscar.png" title="Buscar" onclick="pronto.buscar();"/>
	</span>
	<jsp:include page="/commons/menu.jsp"/>
	<div id="menuSeparator">
		<hr/>
	</div>
	<jsp:include page="/commons/menuRapido.jsp"/>
</c:if>