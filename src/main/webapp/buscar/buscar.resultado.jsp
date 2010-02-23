<%@ include file="/commons/taglibs.jsp"%>
<c:url var="buscarUrl" value="/buscar"/>
<html>
	<head>
		<title>Busca</title>
		<script>

		$(function(){ 
			$("#dialog").dialog({ 
				autoOpen: false, 
				height: $(document).height() - 50, 
				width: $(document).width() - 50, 
				modal: true });
		});

		function verDescricao(ticketKey) {
			$.ajax({
				url: '${raiz}tickets/'+ ticketKey + '/descricao',
				cache: false,
				success: function (data) {
					$("#dialog").dialog('option', 'title', '#' + ticketKey + ' - ' + $('#' + ticketKey + ' .titulo').text());
					$("#dialogDescricao").html(data);
					$("#dialog").dialog('open');
				}
			});
		}

		function recarregar() {
			var parametros = $('#formBuscaAvancada').serializeArray();
			var query = $('#query').val();
			pronto.doPost('${buscarUrl}/' + query, parametros);
		}

		$(function(){
			$('#query').keypress(function(e) {
				 if (e.keyCode == 13) {
					 recarregar();
					 return false;
				 }
			});
		});

		</script>
	</head>
	<body>
		<h1>Resultado da Busca</h1>
		
		<form id="formBuscaAvancada">
			<div align="right">
				Busca:
					<input type="text" name="query" value="${query}" id="query"/>
				Cliente: 
				<select name="clienteKey" onchange="recarregar()" id="clienteKey">
					<option value="-1">Todos</option>
					<c:forEach var="c" items="${clientes}">
						<option value="${c.clienteKey}" ${clienteKey eq c.clienteKey ? 'selected' : ''}>${c.nome}</option>
					</c:forEach>
				</select>
				Status: 
				<select name="kanbanStatusKey" onchange="recarregar()" id="kanbanStatusKey">
					<option value="-1">Pendentes</option>
					<option value="0">Todos</option>
					<c:forEach var="k" items="${kanbanStatus}">
						<option value="${k.kanbanStatusKey}" ${kanbanStatusKey eq k.kanbanStatusKey ? 'selected' : ''}>${k.descricao}</option>
					</c:forEach>
				</select>
				Ordem: 
				<select name="ordem" onchange="recarregar()" id="ordem">
					<c:forEach var="o" items="${ordens}">
						<option value="${o}" ${o eq ordem ? 'selected' : ''}>${o.descricao}</option>
					</c:forEach>
				</select>
				Classificação: 
				<select name="classificacao" onchange="recarregar()" id="classificacao">
					<c:forEach var="c" items="${classificacoes}">
						<option value="${c}" ${c eq classificacao ? 'selected' : ''}>${c.descricao}</option>
					</c:forEach>
				</select>
				
				<pronto:icons name="buscar.png" title="Refinar Busca" onclick="recarregar();"/>
			</div>
		</form>
		
		
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
						<a href="${raiz}tickets/${t.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="10"><i>* ${fn:length(tickets)} resultado(s) encontrado(s)</i></th>
			</tr>
		</table>	
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>