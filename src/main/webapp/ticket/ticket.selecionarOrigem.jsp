<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Selecionar ticket de origem</title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/themes/simplicity/theme.css'/>" />
		<%@ include file="/commons/scripts/scripts.jsp"%>
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

		function buscar() {
			var ticketKey = $('#ticketKey').val();
			var parametros = $('#formSelecionarOrigem').serializeArray();
			var url = "${raiz}tickets/"+ ticketKey +"/buscarTicketDeOrigem";
			pronto.doPost(url, parametros);
		}

		function salvarComoOrigem(ticketOrigemKey) {
			var ticketKey = $('#ticketKey').val();
			$.ajax( {
				url : "${raiz}tickets/"+ticketKey+"/salvarOrigem?ticketOrigemKey="+ticketOrigemKey,
				cache : false,
				success : function(data) {
					if (data == "true") {
						window.opener.definirOrigem(ticketKey, ticketOrigemKey);
						window.close();
					} else {
						pronto.erro("Ocorreu um erro ao salvar o ticket de origem.");
					}
				}
			});
		}
		
		$(function(){
			$('#query').keypress(function(e) {
				 if (e.keyCode == 13) {
					 buscar();
					 return false;
				 }
			});
		});

		</script>
	</head>
	<body>
		<h1>Busca de tickets</h1>
		
		<form id="formSelecionarOrigem">
			<input type="hidden" id="ticketKey" name="ticketKey" value="${ticketKey}">
			<div align="right">
				Buscar origem para o ticket ${ticketKey} :  
					<input id="query" type="text" name="query" value="${query}"/>
				
				<pronto:icons name="buscar.png" title="Refinar Busca" onclick="buscar();"/>
			</div>
		</form>
		
		<c:choose>
			<c:when test="${!empty mensagem}">
				${mensagem}
			</c:when>
			<c:otherwise>
				<table style="width: 100%">
					<tr>
						<th></th>
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
							<td><input type="radio" name="ticketOrigemKey" onclick="salvarComoOrigem(${t.ticketKey})"></td>
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
						<th colspan="11"><i>* ${fn:length(tickets)} resultado(s) encontrado(s)</i></th>
					</tr>
				</table>	
			</c:otherwise>
		</c:choose>
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>