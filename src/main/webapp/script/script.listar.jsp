<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Scripts</title>
		<script type="text/javascript">

			function incluir(){
				goTo('${raiz}scripts/novo');
			}

			function excluir(scriptKey) {
				if (confirm('Tem certeza que deseja excluir este script? Todos as execuções associadas a ele serão excluídas.')) {
					pronto.doDelete('${raiz}scripts/' + scriptKey);
				}
			}

			function reload() {
				$('#formListar').submit();
			}
		
		</script>
	</head>
	<body>
		<h1>
			Scripts
			<pronto:icons name="banco_de_dados.png" title="Ver Bancos de Dados" onclick="goTo('${raiz}bancosDedados')"/>
			<pronto:icons name="execucao.png" title="Ver Execuções" onclick="goTo('${raiz}execucoes')"/>
		</h1>
		
		<div align="right">
			<form action="${raiz}scripts" id="formListar">
				Exibir:
				<select name="situacao" onchange="reload()">
					<option value="1"  ${situacao eq 1 or situacao eq null ? 'selected="selected"' : ''}>Pendentes</option>
					<option value="2" ${situacao eq 2 ? 'selected="selected"' : ''}>Executados</option>
					<option value="0" ${situacao eq 0 ? 'selected="selected"' : ''}>Todos</option>
				</select>
				Status:
				<select name="kanbanStatusKey" onchange="reload()">
					<option value="0">Todos</option>
					<c:forEach items="${kanbanStatus}" var="item">
						<option value="${item.kanbanStatusKey}"  ${item.kanbanStatusKey eq kanbanStatusKey ? 'selected="selected"' : ''}>${item.descricao}</option>
					</c:forEach>
				</select>
			</form>
		</div>
		
		<c:set var="cor" value="${true}"/>
		<table style="width: 100%">
			<tr class="topHeader">
				<th colspan="3"></th>
				<th class="center" colspan="3">Ticket</th>
				<th colspan="2"></th>
			</tr>
			<tr>
				<th style="width: 40px;"></th>
				<th>Descrição</th>
				<th>Situação</th>
				<th>#</th>
				<th>Branch</th>
				<th>Status</th>
				<th style="width: 16px;"></th>
				<th style="width: 16px;"></th>
			</tr>
			<c:forEach items="${scripts}" var="s">
				<c:set var="cor" value="${!cor}"/>
				
				<tr id="${s.scriptKey}" class="${cor ? 'odd' : 'even'}">
					<td><a href="${raiz}scripts/${s.scriptKey}">${s.scriptKey}</a></td>
					<td class="descricao">${s.descricao}</td>
					<td>${s.situacao}</td>
					<td><a href="${raiz}tickets/${s.ticket.ticketKey}">${s.ticket.ticketKey}</a></td>
					<td>${s.ticket.branch}</td>
					<td>${s.ticket.kanbanStatus.descricao}</td>
					<td>
						<a href="${raiz}scripts/${s.scriptKey}"><pronto:icons name="editar_script.png" title="Editar" /></a>
					</td>
					<td>
						<a href="#"><pronto:icons name="excluir_script.png" title="Excluir" onclick="excluir(${s.scriptKey});"/></a>
					</td>
				</tr>
			</c:forEach>
		</table>	
		
		<div align="center">
			<button onclick="incluir()">Incluir Script</button>
		</div>
		
	</body>
</html>