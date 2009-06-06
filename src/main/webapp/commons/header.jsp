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
		<h1><a href="<c:url value="/default.do"/>">Pronto!</a></h1>
        <p>Sistema de Gestão de Processos Ágeis</p>
	</div> <!-- end branding -->
		<c:if test="${usuario ne null}">
			<div align="right"">
				Busca <input type="text" name="busca" id="busca"/>
				<pronto:icons name="buscar.png" title="Buscar" onclick="buscar();"/>
			</div>
		</c:if>
	 <hr />
