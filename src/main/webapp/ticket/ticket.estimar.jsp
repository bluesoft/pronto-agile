<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Estimar ${backlog.descricao}${sprint.nome}</title>
		<script>

			$(function(){ 
				$('#formEstimativa').validate({
					errorLabelContainer: "#errorBox",
					wrapper: "li"
				});
			});
		
			function recalcular() {

				var valorDeNegocio = 0;
				$('.valorDeNegocio span').each(function(i, el){
					valorDeNegocio += parseFloat($(el).text());
				});
				$('.valorDeNegocio input').each(function(i, el){
					valorDeNegocio += parseFloat($(el).val());
				});

				var esforco = 0;
				$('.esforco span').each(function(i, el){
					esforco += parseFloat($(el).text());
				});
				$('.esforco input').each(function(i, el){
					esforco += parseFloat($(el).val());
				});

				$('#somaEsforco').text(esforco);
				$('#somaValorDeNegocio').text(valorDeNegocio);

			}
		</script>
	</head>
	<body>
	
		<c:choose>
			<c:when test="${sprint.nome ne null}">
				<h1>Sprint ${sprint.nome}</h1>	
			</c:when>
			<c:otherwise>
				<h1>${backlog.descricao}</h1>
			</c:otherwise>
		</c:choose>

		<div id="errorBox"></div><br/>
		
		<c:url var="urlSalvarEstimativa" value="/ticket/salvarEstimativa.action"/>
		<form action="${urlSalvarEstimativa}" name="formEstimativa" id="formEstimativa">
			<table style="width: 100%">
				<tr>
					<th>#</th>
					<th>Título</th>
					<th>Tipo</th>
					<th>Cliente</th>
					<th>Valor de Negócio</th>
					<th>Esforço</th>
					<th>Status</th>
					<td></td>
				</tr>
				<c:forEach items="${tickets}" var="t">
					<tr id="${t.ticketKey}">
						<td>
							${t.ticketKey}
							<input type="hidden" name="ticketKey" value="${t.ticketKey}"/>
						</td>
						<td>${t.titulo}</td>
						<td>${t.tipoDeTicket.descricao}</td>
						<td>${t.cliente}</td>
						
						<td class="valorDeNegocio">
							<c:choose>
								<c:when test="${usuarioLogado.productOwner}">
									<input type="text" size="5" name="valorDeNegocio" value="${t.valorDeNegocio}" onchange="recalcular()" class="required digits"/>
								</c:when>
								<c:otherwise>
									<span>${t.valorDeNegocio}</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="esforco">
							<c:choose>
								<c:when test="${usuarioLogado.desenvolvedor}">
									<input type="text" size="5" name="esforco" value="${t.esforco}" onchange="recalcular()" class="required number"/>
								</c:when>
								<c:otherwise>
									<span>${t.esforco}</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>${t.kanbanStatus.descricao}</td>
						<td>
							<pronto:icons name="editar.png" title="Editar" onclick="goTo('editar.action?ticketKey=${t.ticketKey}')"></pronto:icons>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<th colspan="4"></th>
					<th id="somaValorDeNegocio">
						${sprint.valorDeNegocioTotal} ${backlog.valorDeNegocioTotal}
					</th>
					<th id="somaEsforco">
						${sprint.esforcoTotal} ${backlog.esforcoTotal}
					</th>
					<td></td>
				</tr>
			</table>	
		
			<div align="center">
				
				<c:choose>
					<c:when test="${sprint ne null}">
						<c:url var="urlCancelar" value="/ticket/listarPorSprint.action?sprintKey=${sprint.sprintKey}"/>
					</c:when>
					<c:otherwise>
						<c:url var="urlCancelar" value="/ticket/listarPorBacklog.action?backlogKey=${backlog.backlogKey}"/>
					</c:otherwise>
				</c:choose>	
				
				<button type="button" onclick="goTo('${urlCancelar}')">Cancelar</button>
				<button type="submit">Salvar</button>	
			</div>
		</form>
	</body>
</html>