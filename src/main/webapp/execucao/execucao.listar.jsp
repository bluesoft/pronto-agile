<%@ include file="/commons/taglibs.jsp"%>
<c:url var="listarScriptsUrl" value="/scripts"/>
<c:url var="listarBancosUrl" value="/bancosDeDados"/>
<html>
	<head>
		<title>Execuções de Scripts</title>
		<script type="text/javascript">

			function reload() {
				var bancoDeDadosKey = $('#bancoDeDadosKey').val();
				var pendentes = $('#pendentes').val();

				var url = pronto.raiz + 'execucoes';
				if (parseInt(bancoDeDadosKey) > 0){
					url += '/' + bancoDeDadosKey;
				}

				if (pendentes == 'true') {
					url += "/pendentes";
				}

				pronto.doGet(url);
				
			}

			 function verScript(scriptKey) {
					$.ajax({
						url: '${raiz}scripts/' + scriptKey + '/json',
						cache: false,
						dataType: 'json',
						success: function (script) {
							$("#dialog").dialog('option', 'title', script.descricao);
							$("#dialogDescricao").html(new String(script.script).replace(/\n/g,'<br/>'));
							$("#dialog").dialog('open');
						}
					});
			}

			$(function(){
				$("#dialog").dialog({ 
					autoOpen: false, 
					height: $(document).height() - 50, 
					width: $(document).width() - 50, 
					modal: true });
			});
				
		</script>
	</head>
	<body>
		<h1>
			Execuções de Scripts
			<pronto:icons name="banco_de_dados.png" title="Ver Bancos de Dados" onclick="goTo('${listarBancosUrl}')"/>
			<pronto:icons name="script.png" title="Ver Scripts" onclick="goTo('${listarScriptsUrl}')"/>
		</h1>
		
		<div align="right">
			<form action="${raiz}execucoes" id="formListar" method="GET">
				Banco de Dados:
				<select name="bancoDeDadosKey" id="bancoDeDadosKey" onchange="reload()">
					<option value="0">Todos</option>
					<c:forEach items="${bancos}" var="b">
						<c:set var="selected" value="${bancoDeDadosKey eq b.bancoDeDadosKey}"/>
						<option value="${b.bancoDeDadosKey}" ${selected ? 'selected="selected"' : ''}>${b.nome}</option>
					</c:forEach>
				</select>
				Exibir:
				<select name="pendentes" id="pendentes" onchange="reload()">
					<option value="true"  ${pendentes ? 'selected="selected"' : ''}>Pendentes</option>
					<option value="false" ${!pendentes ? 'selected="selected"' : ''}>Todos</option>
				</select>
			</form>
		</div>
		
		<c:forEach items="${bancosComExecucoes}" var="b">
			<c:set var="execucoes" value="${pendentes ? b.execucoesPendentes : b.execucoes}"/>
			<c:if test="${!empty execucoes}">
				<form action="${raiz}execucoes/gerar" method="post">
					<input type="hidden" name="bancoDeDadosKey" value="${b.bancoDeDadosKey}"/>
					<h2>${b.nome}</h2>		
					<table style="width: 100%">
						<tr class="topHeader">
							<th colspan="3">Script</th>
							<th colspan="3">Ticket</th>
							<th colspan="3">Execução</th>
						</tr>
						<tr>
							<th colspan="2" style="width: 60px;"></th>
							<th>Descrição</th>
							<th>#</th>
							<th>Branch</th>
							<th>Kanban</th>
							<th>Status</th>
							<th style="width: 16px;"></th>
							<th style="width: 16px;"></th>
						</tr>
						<c:set var="cor" value="${true}"/>
						<c:forEach items="${execucoes}" var="e">
							<c:set var="cor" value="${!cor}"/>
							
							<tr id="${e.execucaoKey}" class="${cor ? 'odd' : 'even'}">
								<td>
									<c:if test="${!e.executado}">
										<input name="execucaoKey" type="checkbox" value="${e.execucaoKey}" />
									</c:if>
								</td>
								<td>${e.script.scriptKey}</td>
								<td class="descricao">${e.script.descricao}</td>
								<td>
									<c:if test="${e.script.ticket ne null}">
										<a href="${raiz}tickets/${e.script.ticket.ticketKey}">${e.script.ticket.ticketKey}</a>	
									</c:if>
								</td>
								<td>
									${e.script.ticket.branch}								
								</td>
								<td>
									${e.script.ticket.kanbanStatus.descricao}
								</td>
								<td class="descricao">${e.status}</td>
								<td>
									<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verScript(${e.script.scriptKey});"/>
								</td>
								<td>
									<a href="${raiz}scripts/${e.script.scriptKey}"><pronto:icons name="editar_script.png" title="Editar Script" /></a>
								</td>
							</tr>
						</c:forEach>
					</table>
					
					<c:if test="${!empty b.execucoesPendentes}">
						<div align="center">
							<button type="submit" style="width: 200px;">Gerar Script do Banco ${b.nome}</button>
						</div>
					</c:if>
					
				</form>
			</c:if>
		</c:forEach>	
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
		
	</body>
</html>