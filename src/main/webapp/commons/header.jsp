<%@ include file="/commons/taglibs.jsp"%>
	<c:url var="buscarUrl" value="/buscar.action"/>
	<script>
		function buscar(){
			goTo('${buscarUrl}?query=' + $('#busca').val());
		}

		$(function() {
			$('#busca').keypress(function(e) {
				 if (e.keyCode == 13) {
					buscar();
				 }
			});
		});
	</script>
	<div id="branding">
		<h1><a href="<c:url value="/kanban/kanban.action"/>"><pronto:images name="pronto_logo.png" title="Pronto!" /></a></h1>
	</div> <!-- end branding -->
	
	<c:if test="${usuarioLogado ne null and (usuarioLogado.productOwner or usuarioLogado.scrumMaster or usuarioLogado.equipe)}">
		<div align="right"">
			Busca <input type="text" name="busca" id="busca" accesskey="b"/>
			<pronto:icons name="buscar.png" title="Buscar" onclick="buscar();"/>
		</div>
	</c:if>
	
	 <hr />
