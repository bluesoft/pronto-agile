<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Estórias e Defeitos por Cliente</title>
		<script src="${raiz}ticket/ticket.listar.js" type="text/javascript"  charset="UTF-8"></script>
		<script>
			function recarregarPagina() {
				var parametros = $('#formListar').serializeArray();
				var clienteKey = $('#clienteKey').val();
				pronto.doPost('${raiz}backlogs/clientes/' + clienteKey, parametros);
			}
		</script>
		<style type="text/css">
			#ticketsTable tr {
				height: 22px;
			}
		</style>
	</head>
	<body>
		<h1>Estórias e Defeitos por Cliente</h1>
		
		<div align="right">
		<form action="${raiz}backlogs/clientes" id="formListar">
			Cliente: 
			<select name="clienteKey" onchange="recarregarPagina()" id="clienteKey">
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
		
		<table id="ticketsTable" style="width: 100%">
		<c:forEach items="${grupos}" var="g">
			<c:if test="${!empty ticketsAgrupados[g]}">
			<thead>
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
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:set var="quantidade" value="${0}"/>
			<c:forEach items="${ticketsAgrupados[g]}" var="t">
				<c:set var="quantidade" value="${quantidade+1}"/>
				<c:set var="quantidadeTotal" value="${quantidadeTotal+1}"/>
				<c:set var="cor" value="${!cor}"/>
				<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
					<td>${t.ticketKey}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>
						<span class="titulo">${t.titulo}</span>
						<span class="opcao" style="display: none;">
							<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
							<a href="${raiz}tickets/${t.ticketKey}">
								<pronto:icons name="editar.png" title="Editar" />
							</a>
							
							<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
								<c:if test="${t.backlog.backlogKey eq 2 or t.backlog.backlogKey eq 6}">
										<pronto:icons name="mover_para_inbox.png" title="Mover para o Inbox" onclick="pronto.moverParaInbox(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2 or t.backlog.backlogKey eq 6}">
									<pronto:icons name="mover_para_o_sprint_atual.png" title="Mover para um Sprint" onclick="escolherSprintParaMover(${t.ticketKey})"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 3 or t.backlog.backlogKey eq 6}">
									<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="pronto.moverParaProductBacklog(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2}">
									<pronto:icons name="mover_para_futuro.png" title="Mover para Futuro" onclick="pronto.moverParaFuturo(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey ne 4 and t.backlog.backlogKey ne 5}">
									<pronto:icons name="mover_para_lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${t.ticketKey},true)"></pronto:icons>
								</c:if>
							</c:if>
							<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
								<pronto:icons name="restaurar.png" title="Restaurar para o Inbox" onclick="pronto.restaurar(${t.ticketKey},true)"></pronto:icons>
							</c:if>
						</span>
					</td>
					<td><fmt:formatDate value="${t.dataDeCriacao}"/></td>
					<td class="valorDeNegocio">${t.valorDeNegocio}</td>
					<td class="prioridadeCliente">${t.prioridadeDoCliente}</td>
					<td class="esforco">${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
				</tr>
				<tr style="height: 1px;">
					<td colspan="8" style="background-color:#b4c24b"></td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="8">Subtotal: ${quantidade}</th>
			</tr>
			</c:if>
		</c:forEach>
		<tr>
			<th colspan="8" style="font-weight: bold;">Total: ${quantidadeTotal}</th>
		</tr>
			</tbody>
		</table>
	<div title="Descrição" id="dialog" style="display: none; width: 500px;">
		<div align="left" id="dialogDescricao">Aguarde...</div>
	</div>
</body>
</html>