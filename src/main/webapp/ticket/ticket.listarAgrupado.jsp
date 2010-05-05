<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Estórias e Defeitos por Cliente</title>
		<script>

			function recarregar() {
				var parametros = $('#formListar').serializeArray();
				var clienteKey = $('#clienteKey').val();
				pronto.doPost('${raiz}backlogs/clientes/' + clienteKey, parametros);
			}
		
			function apagarLinha(ticketKey) {
				$('#'+ticketKey).add('tr[pai='+ticketKey+']').fadeOut('slow', function(){ $(this).remove(); });
			}
		
			function toTrash(ticketKey){
				var url = '${raiz}tickets/'+ticketKey+'/jogarNoLixo/'; 
				$.post(url, {
					success: function() {
						apagarLinha(ticketKey);
						recalcular();		
					}
				});
			}

			function recalcular() {

				var valorDeNegocio = 0;
				$('.valorDeNegocio').each(function(i, el){
					valorDeNegocio += parseFloat($(el).text());
				});
				$('#somaValorDeNegocio').text(valorDeNegocio);
				
				var esforco = 0;
				$('.esforco').each(function(i, el){
					esforco += parseFloat($(el).text());
				});
				$('#somaEsforco').text(esforco);
			}

			$(function() {
				$("#dialog").dialog({ 
					autoOpen: false, 
					height: $(document).height() - 50, 
					width: $(document).width() - 50, 
					modal: true });
			});

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
		</script>
	</head>
	<body>
		<h1>Estórias e Defeitos por Cliente</h1>
		
		<div align="right">
		<form action="${raiz}backlogs/clientes" id="formListar">
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
		</form>
		</div>
		
		<c:set var="quantidadeTotal" value="${0}"/>
				<table style="width: 100%">
		<c:forEach items="${grupos}" var="g">
			<c:if test="${!empty ticketsAgrupados[g]}">
			<tr>
				<td align="center" colspan="15"><br/><h2>${g}</h2></td>
			</tr>
			<tr>
				<th>#</th>
				<th>Tipo</th>
				<th>Título</th>
				<th>Data</th>
				<th>Valor de Negócio</th>
				<th>Prioridade do Cliente</th>
				<th>Esforço</th>
				<th>Status</th>
				<th style="width: 112px" colspan="7"></th>
			</tr>
			<c:set var="cor" value="${true}"/>
					<c:set var="quantidade" value="${0}"/>
					<c:forEach items="${ticketsAgrupados[g]}" var="t">
							<c:set var="quantidade" value="${quantidade+1}"/>
							<c:set var="quantidadeTotal" value="${quantidadeTotal+1}"/>
							<c:set var="cor" value="${!cor}"/>
							<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
								<td>${t.ticketKey}</td>
								<td>${t.tipoDeTicket.descricao}</td>
								<td class="titulo">${t.titulo}</td>
								<td><fmt:formatDate value="${t.dataDeCriacao}"/></td>
								<td class="valorDeNegocio">${t.valorDeNegocio}</td>
								<td class="prioridadeCliente">${t.prioridadeDoCliente}</td>
								<td class="esforco">${t.esforco}</td>
								<td>${t.kanbanStatus.descricao}</td>
								<td>
									<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 3) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
										<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="pronto.moverParaProductBacklog(${t.ticketKey}, true)"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${t.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
											<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Ideias" onclick="pronto.moverParaIdeias(${t.ticketKey}, true)"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${t.backlog.backlogKey eq 1 or (t.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)) or t.backlog.backlogKey eq 3}">
										<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${t.ticketKey}, true)"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
										<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${t.ticketKey}, true)"></pronto:icons>
									</c:if>
								</td>
								<td>
								<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
									<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${t.ticketKey}, true)"></pronto:icons>
								</c:if>
								</td>
								<td>
									<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
								</td>
								<td>
									<a href="${raiz}tickets/${t.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
								</td>
							</tr>
							<tr style="height: 1px;">
								<td colspan="15" style="background-color:#b4c24b"></td>
							</tr>
						</c:forEach>
						<tr>
							<th colspan="15">Subtotal: ${quantidade}</th>
						</tr>
				</c:if>
		</c:forEach>
			<tr>
				<th colspan="15" style="font-weight: bold;">Total: ${quantidadeTotal}</th>
			</tr>
		</table>
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					&nbsp;&nbsp;<button type="button" onclick="goTo('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1')">Nova Ideia</button>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
					&nbsp;&nbsp;<button type="button" onclick="goTo('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2')">Nova Estória</button>&nbsp;&nbsp;
					&nbsp;&nbsp;<button type="button" onclick="goTo('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3')">Novo Defeito</button>&nbsp;&nbsp;
				</c:when>
			</c:choose>
		</div>
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>
