<%@ include file="/commons/taglibs.jsp"%>
<c:url var="editarTicketUrl" value="/ticket/editar.action"/>
<c:url var="verDescricaoUrl" value="/ticket/verDescricao.action"/>
<html>
	<head>
		<title>Busca</title>
		<script>
		$(function(){ 
			$("#dialog").dialog({ autoOpen: false, height: 530, width: 600, modal: true });
		});
		function verDescricao(ticketKey) {
			$.ajax({
				url: '${verDescricaoUrl}?ticketKey=' + ticketKey,
				cache: false,
				success: function (data) {
					$("#dialog").dialog('option', 'title', '#' + ticketKey + ' - ' + $('#' + ticketKey + ' .titulo').text());
					$("#dialogDescricao").html(data);
					$("#dialog").dialog('open');
				}
			});
		}
		</script>
	</head>
	<body>
		<h1>Resultado da Busca</h1>
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Backlog</th>
				<th>Valor de Negócio</th>
				<th>Esforço</th>
				<th>Status</th>
				<th colspan="2">&nbsp;</th>
			</tr>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:set var="cor" value="${!cor}"/>
				<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
					<td>${t.ticketKey}</td>
					<td class="titulo">${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td>${t.backlog.descricao}</td>
					<td>${t.valorDeNegocio}</td>
					<td>${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
					<td>
						<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
					</td>
					<td>
						<a href="${editarTicketUrl}?ticketKey=${t.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
					</td>
				</tr>
			</c:forEach>
		</table>	
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>