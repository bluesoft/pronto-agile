<%@ include file="/commons/taglibs.jsp"%>
<c:url var="adicionarTarefasUrl" value="/ticket/adicionarAoSprint.action"/>
<c:url var="cancelarUrl" value="/ticket/listarPorSprint.action"/>
<html>
	<head>
		<title>${sprint.nome}</title>
		<script>
			function recalcular() {
	
				var valorDeNegocio = 0;
				var esforco = 0;
				$(':checked').each(function(i, el){
					valorDeNegocio += parseFloat($(el).parents('tr').children('.valorDeNegocio').text());
					esforco += parseFloat($(el).parents('tr').children('.esforco').text());
				});
				$('#somaEsforco').text(esforco);
				$('#somaValorDeNegocio').text(valorDeNegocio);
			}

			function enviar() {
				if($(":checkbox:checked").length == 0) {
					alert("Selecione ao menos uma estória ou defeito");
				} else {
					document.form1.submit();
				}
			}
		</script>	
	</head>
	<body>
		<h1>Sprint ${sprint.nome}</h1>
		<h2>Adicionar Estórias ou Defeitos do Product Backlog ao Sprint</h2>	
		<form action="${adicionarTarefasUrl}" method="post" name="form1">
			<input type="hidden" name="sprintKey" value="${sprint.sprintKey}"/>
			<table style="width: 100%">
				<tr>
					<th></th>
					<th>#</th>
					<th>Título</th>
					<th>Tipo</th>
					<th>Cliente</th>
					<th>Valor de Negócio</th>
					<th>Esforço</th>
				</tr>
				<c:set var="cor" value="${true}"/>
				<c:forEach items="${tickets}" var="t">
					<c:set var="cor" value="${!cor}"/>
					<tr id="${t.ticketKey}" class="${cor ? 'even' : 'odd'}">
						<td><input type="checkbox" value="${t.ticketKey}" name="ticketKey" onchange="recalcular()"/></td>
						<td>${t.ticketKey}</td>
						<td>${t.titulo}</td>
						<td>${t.tipoDeTicket.descricao}</td>
						<td>${t.cliente}</td>
						<td class="valorDeNegocio">${t.valorDeNegocio}</td>
						<td class="esforco">${t.esforco}</td>
					</tr>
				</c:forEach>
				<tr>
					<th colspan="5">Total</th>
					<th id="somaValorDeNegocio"></th>
					<th id="somaEsforco"></th>
				</tr>
			</table>	
			<div align="center">
				<button type="button" onclick="goTo('${cancelarUrl}?sprintKey=${sprint.sprintKey}')">Cancelar</button>
				<button type="button" onclick="enviar()">Adicionar</button>
			</div>
		</form>
	</body>
</html>