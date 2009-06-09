<%@ include file="/commons/taglibs.jsp"%>
<c:url var="estimarPorSprintUrl" value="/ticket/estimarSprint.action"/>
<c:url var="estimarPorBacklogUrl" value="/ticket/estimarBacklog.action"/>
<c:url var="adicionarTarefasUrl" value="/ticket/listarTarefasParaAdicionarAoSprint.action"/>
<c:url var="verDescricao" value="/ticket/verDescricao.action"/>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
		<script>
			function toTrash(ticketKey){
				var url = 'jogarNoLixo.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						$('#'+ticketKey).remove();
						recalcular();		
					}
				});
			}

			function toProductBacklog(ticketKey){
				var url = 'moverParaProductBacklog.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						$('#'+ticketKey).remove();
						recalcular();		
					}
				});
			}

			function toIdeias(ticketKey){
				var url = 'moverParaIdeias.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						$('#'+ticketKey).remove();
						recalcular();		
					}
				});
			}

			function toImpedimentos(ticketKey){
				var url = 'moverParaImpedimentos.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						$('#'+ticketKey).remove();
						recalcular();		
					}
				});
			}
			
			function restaurar(ticketKey){
				var url = 'restaurar.action?ticketKey=' + ticketKey; 
				$.post(url, {
					success: function() {
						$('#'+ticketKey).remove();
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
					<c:if test="${usuarioLogado.desenvolvedor or usuarioLogado.productOwner}">
						<pronto:icons name="estimar.png" title="Estimar Sprint" onclick="goTo('${estimarPorSprintUrl}?sprintKey=${sprint.sprintKey}')"/>
					</c:if>
					<pronto:icons name="adicionar.png" title="Adicionar Estórias ou Defeitos do Product Backlog ao Sprint" onclick="goTo('${adicionarTarefasUrl}?sprintKey=${sprint.sprintKey}')"/>
				</h1>	
			</c:when>
			<c:otherwise>
				<h1>${backlog.descricao} <pronto:icons name="estimar.png" title="Estimar Backlog" onclick="goTo('${estimarPorBacklogUrl}?backlogKey=${backlog.backlogKey}')"/>  </h1>
			</c:otherwise>
		</c:choose>
		
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Valor de Negócio</th>
				<th>Esforço</th>
				<th>Status</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			<c:forEach items="${tickets}" var="t">
				<tr id="${t.ticketKey}">
					<td>${t.ticketKey}</td>
					<td class="titulo">${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td class="valorDeNegocio">${t.valorDeNegocio}</td>
					<td class="esforco">${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
					<td>
						<pronto:icons name="editar.png" title="Editar" onclick="goTo('editar.action?ticketKey=${t.ticketKey}')"></pronto:icons>
					</td>
					<c:if test="${(t.backlog.backlogKey eq 1 and usuarioLogado.productOwner) or t.backlog.backlogKey eq 3}">
						<td>
							<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="toProductBacklog(${t.ticketKey})"></pronto:icons>
						</td>
					</c:if>
					<c:if test="${t.backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
						<td>
							<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Idéias" onclick="toIdeias(${t.ticketKey})"></pronto:icons>
						</td>
					</c:if>
					
					<c:if test="${backlog.backlogKey eq 1 or (backlog.backlogKey eq 2 and usuarioLogado.productOwner) or backlog.backlogKey eq 3}">
						<td>
							<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="toImpedimentos(${t.ticketKey})"></pronto:icons>
						</td>
					</c:if>
					
					<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
						<td>
							<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="toTrash(${t.ticketKey})"></pronto:icons>
						</td>
					</c:if>
					<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
						<td>
							<pronto:icons name="restaurar.png" title="Restaurar" onclick="restaurar(${t.ticketKey})"></pronto:icons>
						</td>
					</c:if>
					<td>
						<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="4"></th>
				<th id="somaValorDeNegocio">${sprint.valorDeNegocioTotal}${backlog.valorDeNegocioTotal}</th>
				<th id="somaEsforco">${sprint.esforcoTotal}${backlog.esforcoTotal}</th>
				<th colspan="8"></th>
			</tr>
		</table>	
		
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1">Nova Idéia</a>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2">Nova Estória</a>&nbsp;&nbsp;
					|&nbsp;&nbsp;<a href="editar.action?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3">Novo Defeito</a>&nbsp;&nbsp;
				</c:when>
			</c:choose>
		</div>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>