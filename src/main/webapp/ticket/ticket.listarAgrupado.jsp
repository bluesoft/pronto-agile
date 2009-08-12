<%@ include file="/commons/taglibs.jsp"%>
<c:url var="estimarPorSprintUrl" value="/ticket/estimarSprint.action"/>
<c:url var="estimarPorBacklogUrl" value="/ticket/estimarBacklog.action"/>
<c:url var="editarSprintUrl" value="/sprint/editar.action"/>
<c:url var="adicionarTarefasUrl" value="/ticket/listarTarefasParaAdicionarAoSprint.action"/>
<c:url var="verDescricao" value="/ticket/verDescricao.action"/>
<c:url var="listarPorSprintUrl" value="/ticket/listarPorSprint.action"/>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
		<script>

			function recarregar(sprintKey) {
				goTo('${listarPorSprintUrl}?sprintKey=' + sprintKey);
			}
		
			function apagarLinha(ticketKey) {
				$('#'+ticketKey).add('tr[pai='+ticketKey+']').remove();
			}
		
			function toTrash(ticketKey){
				var url = 'jogarNoLixo.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						apagarLinha(ticketKey);
						recalcular();		
					}
				});
			}

			function toProductBacklog(ticketKey){
				var url = 'moverParaProductBacklog.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						apagarLinha(ticketKey);
						recalcular();		
					}
				});
			}

			function toIdeias(ticketKey){
				var url = 'moverParaIdeias.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						apagarLinha(ticketKey);
						recalcular();		
					}
				});
			}

			function toImpedimentos(ticketKey){
				var url = 'moverParaImpedimentos.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						apagarLinha(ticketKey);	
						recalcular();		
					}
				});
			}
			
			function restaurar(ticketKey){
				var url = 'restaurar.action?ticketKey=' + ticketKey; 
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
				$("#dialog").dialog({ autoOpen: false, height: 530, width: 600, modal: true });
			});

			function verDescricao(ticketKey) {
				$.ajax({
					url: 'verDescricao.action?ticketKey=' + ticketKey,
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
		<c:choose>
			<c:when test="${sprint.nome ne null}">
				<h1>
					Sprint ${sprint.nome} 
					<a href="${editarSprintUrl}?sprintKey=${sprint.sprintKey}"><pronto:icons name="editar.png" title="Editar Sprint" /></a>
					<c:if test="${(usuarioLogado.desenvolvedor or usuarioLogado.productOwner)}">
						<a href="${estimarPorSprintUrl}?sprintKey=${sprint.sprintKey}"><pronto:icons name="estimar.png" title="Estimar Sprint" /></a>
					</c:if>
					<a href="${adicionarTarefasUrl}?sprintKey=${sprint.sprintKey}"><pronto:icons name="adicionar.png" title="Adicionar Estórias ou Defeitos do Product Backlog ao Sprint" /></a>
				</h1>	
			</c:when>
			<c:otherwise>
				<h1>
					${backlog.descricao} 
					<c:if test="${(usuarioLogado.desenvolvedor or usuarioLogado.productOwner) and (backlog.backlogKey le 3)}">
						<a href="${estimarPorBacklogUrl}?backlogKey=${backlog.backlogKey}"><pronto:icons name="estimar.png" title="Estimar Backlog" /></a>  
					</c:if>
				</h1>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${fn:length(sprints) gt 1}">
			<div align="right">
				Sprint: 
				<form:select path="sprint.sprintKey" onchange="recarregar(this.value)">
					<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
				</form:select>
			</div>
		</c:if>
		
		<c:forEach items="${grupos}" var="g">
			<c:if test="${!empty ticketsAgrupados[g]}">
			<h4>${g}</h4>
			<c:set var="cor" value="${true}"/>
				<table style="width: 100%">
					<tr>
						<th>#</th>
						<th>Título</th>
						<th>Tipo</th>
						<th>Data</th>
						<th>Valor de Negócio</th>
						<th>Esforço</th>
						<th>Status</th>
						<th style="width: 112px" colspan="7"></th>
					</tr>
					<c:set var="quantidade" value="${0}"/>
					<c:forEach items="${ticketsAgrupados[g]}" var="t">
							<c:set var="quantidade" value="${quantidade+1}"/>
							<c:set var="cor" value="${!cor}"/>
							<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
								<td>${t.ticketKey}</td>
								<td class="titulo">${t.titulo}</td>
								<td>${t.tipoDeTicket.descricao}</td>
								<td><fmt:formatDate value="${t.dataDeCriacao}"/></td>
								<td class="valorDeNegocio">${t.valorDeNegocio}</td>
								<td class="esforco">${t.esforco}</td>
								<td>${t.kanbanStatus.descricao}</td>
								<td>
									<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 3) and usuarioLogado.productOwner}">
										<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="toProductBacklog(${t.ticketKey})"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${t.backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
											<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Idéias" onclick="toIdeias(${t.ticketKey})"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${t.backlog.backlogKey eq 1 or (t.backlog.backlogKey eq 2 and usuarioLogado.productOwner) or t.backlog.backlogKey eq 3}">
										<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="toImpedimentos(${t.ticketKey})"></pronto:icons>
									</c:if>
								</td>
								<td>
									<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
										<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="toTrash(${t.ticketKey})"></pronto:icons>
									</c:if>
								</td>
								<td>
								<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
									<pronto:icons name="restaurar.png" title="Restaurar" onclick="restaurar(${t.ticketKey})"></pronto:icons>
								</c:if>
								</td>
								<td>
									<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
								</td>
								<td>
									<a href="editar.action?ticketKey=${t.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
								</td>
							</tr>
							<tr style="height: 1px;">
								<td colspan="15" style="background-color:#b4c24b"></td>
							</tr>
						</c:forEach>
						<tr>
							<th colspan="4">Total: ${quantidade}</th>
						</tr>
					</table>
				</c:if>
		</c:forEach>
		
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					&nbsp;&nbsp;<button type="button" onclick="window.location.href='editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1'">Nova Idéia</button>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
					&nbsp;&nbsp;<button type="button" onclick="window.location.href='editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2'">Nova Estória</button>&nbsp;&nbsp;
					&nbsp;&nbsp;<button type="button" onclick="window.location.href='editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3'">Novo Defeito</button>&nbsp;&nbsp;
				</c:when>
			</c:choose>
		</div>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>