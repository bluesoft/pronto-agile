<%@ include file="/commons/taglibs.jsp"%>
<c:url var="listarScriptsUrl" value="/scripts"/>
<c:url var="listarBancosUrl" value="/bancosDeDados"/>
<c:url var="editarTicketUrl" value="/ticket/editar.action"/>
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
				$("#dialog").dialog({ autoOpen: false, height: 530, width: 600, modal: true });
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
						<tr>
							<th style="width: 20px;"></th>
							<th style="width: 40px;"></th>
							<th>Descrição</th>
							<th>Ticket</th>
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
								<td>${e.execucaoKey}</td>
								<td class="descricao">${e.script.descricao}</td>
								<td>
									<c:if test="${e.script.ticket ne null}">
										<c:choose>
											<c:when test="${e.script.ticket.estoria}">
												<pronto:icons name="estoria.png" title="Ir para Estória - ${e.script.ticket}" onclick="goTo('${editarTicketUrl}?ticketKey=${e.script.ticket.ticketKey}')"/>								
											</c:when>
											<c:when test="${e.script.ticket.tarefa}">
												<pronto:icons name="tarefa.png" title="Ir para Tarefa - ${e.script.ticket}" onclick="goTo('${editarTicketUrl}?ticketKey=${e.script.ticket.ticketKey}')"/>
											</c:when>
											<c:otherwise>
												<pronto:icons name="defeito.png" title="Ir para Defeito - ${e.script.ticket}" onclick="goTo('${editarTicketUrl}?ticketKey=${e.script.ticket.ticketKey}')"/>
											</c:otherwise>
										</c:choose>
										#${e.script.ticket.ticketKey}
										(${e.script.ticket.kanbanStatus.descricao})
									</c:if>
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