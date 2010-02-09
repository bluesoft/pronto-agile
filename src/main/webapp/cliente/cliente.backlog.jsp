<%@ include file="/commons/taglibs.jsp"%>
<c:url var="urlAlterarOrdem" value="/clientes/priorizar"/>
<html>
	<head>
		<title>Backlog</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
		<script>
			function verDescricao(ticketKey) {
				$.ajax({
					url: '${raiz}tickets/' + ticketKey + '/descricao',
					cache: false,
					success: function (data) {
						$("#dialog").dialog('option', 'title', '#' + ticketKey + ' - ' + $('#' + ticketKey + ' .titulo').text());
						$("#dialogDescricao").html(data);
						$("#dialog").dialog('open');
					}
				});
			}
	
			$(function() {
				$("#dialog").dialog({ autoOpen: false, height: 530, width: 600, modal: true });

				$("#pendentes tbody").sortable({
    				placeholder: 'ui-state-highlight',
    				stop: alterarPrioridadeDaTarefa,
    				items: 'tr'
    		  	});
				
			});

			function alterarPrioridadeDaTarefa() {
				var $pendentes = $('#pendentes tr');
				var novaOrdem = new Array($pendentes.length);
				var indice = 0;
				$pendentes.each(function(i, el) {
					novaOrdem[indice++] = el.id;
				});

				$.post('${urlAlterarOrdem}', { 'ticketKey': novaOrdem, 'clienteKey':${cliente.clienteKey} });
			}
		</script>
	</head>
	<body>
	
		<h1>Backlog</h1>
		<h2>Solicitações Pendentes</h2>
		
		<span>Arraste e solte as linhas da tabela para priorizar as solicitações.</span>
		
		<table style="width: 100%" id="pendentes">
			<thead>
			<tr>
				<th colspan="2">#</th>
				<th>Título</th>
				<th>Solicitador</th>
				<th>Data</th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:if test="${!t.done}">
					<c:set var="cor" value="${!cor}"/>
					<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
						<td><span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>
						<td>${t.ticketKey}</td>	
						<td class="titulo">${t.titulo}</td>
						<td>${t.solicitador}</td>
						<td><fmt:formatDate value="${t.dataDeCriacao}"/></td>
						<td>
							<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
						</td>
					</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>	
		
		<h2>Solicitações Prontas</h2>
		<table style="width: 100%">
			<thead>
			<tr>
				<th>#</th>
				<th class="titulo">Título</th>
				<th>Solicitador</th>
				<th>Data da Solicitação</th>
				<th>Data da Entrega</th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:if test="${t.done}">
					<c:set var="cor" value="${!cor}"/>
					<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
						<td>${t.ticketKey}</td>	
						<td class="titulo">${t.titulo}</td>
						<td>${t.solicitador}</td>
						<td><fmt:formatDate value="${t.dataDeCriacao}"/></td>
						<td><fmt:formatDate value="${t.dataDePronto}"/></td>
						<td>
							<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
						</td>
					</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>	
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
		
	</body>
</html>