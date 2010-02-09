<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
		<script>

			function recarregar(sprintKey) {
				goTo('${raiz}backlogs/sprints/' + sprintKey);
			}
		
			function apagarLinha(ticketKey) {
				$('#'+ticketKey).add('tr[pai='+ticketKey+']').fadeOut('slow', function(){
					$(this).remove();
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
		<c:choose>
			<c:when test="${sprint.nome ne null}">
				<h1>
					Sprint ${sprint.nome}
					
					 <c:if test="${usuarioLogado.productOwner}">
						<a href="${raiz}sprints/${sprint.sprintKey}">
							<pronto:icons name="editar.png" title="Editar Sprint" />
						</a>
					</c:if>
					
					<c:if test="${(usuarioLogado.equipe or usuarioLogado.productOwner)}">
						<a href="${raiz}/sprints/${sprint.sprintKey}/estimar"><pronto:icons name="estimar.png" title="Estimar Sprint" /></a>
					</c:if>
					
					<a href="${raiz}sprints/${sprint.sprintKey}/adicionarTarefas">
						<pronto:icons name="adicionar.png" title="Adicionar Estórias ou Defeitos do Product Backlog ao Sprint" />
					</a>
					
					<%@ include file="/commons/sprintLinks.jsp" %>
				</h1>	
			</c:when>
			<c:otherwise>
				<h1>
					${backlog.descricao} 
					<c:if test="${(usuarioLogado.equipe or usuarioLogado.productOwner) and (backlog.backlogKey le 3)}">
						<a href="${raiz}backlogs${backlog.backlogKey}/estimar"><pronto:icons name="estimar.png" title="Estimar Backlog" /></a>  
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
		
		<c:set var="cor" value="${true}"/>
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Valor de Negócio</th>
				<th>Esforço</th>
				<th>Status</th>
				<th title="Tempo de Vida em Dias">LifeTime</th>
				<th style="width: 128px" colspan="8"></th>
			</tr>
			<c:forEach items="${tickets}" var="t">
				<c:set var="cor" value="${!cor}"/>
				<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
					<td>${t.ticketKey}</td>
					<td class="titulo">${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td class="valorDeNegocio">${t.valorDeNegocio}</td>
					<td class="esforco">${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
					<td>${t.tempoDeVidaEmDias}</td>
					<td>
						<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 3) and usuarioLogado.productOwner}">
							<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="pronto.moverParaProductBacklog(${t.ticketKey},true)"></pronto:icons>
						</c:if>
					</td>
					<td>
						<c:if test="${t.backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
								<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Ideias" onclick="pronto.moverParaIdeias(${t.ticketKey},true)"></pronto:icons>
						</c:if>
					</td>
					<td>
						<c:if test="${t.backlog.backlogKey eq 1 or (t.backlog.backlogKey eq 2 and usuarioLogado.productOwner) or t.backlog.backlogKey eq 3}">
							<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${t.ticketKey},true)"></pronto:icons>
						</c:if>
					</td>
					<td>
						<c:if test="${(t.backlog.backlogKey eq 2 and usuarioLogado.productOwner)}">
							<pronto:icons name="mover_para_o_sprint_atual.png" title="Mover para o Sprint Atual" onclick="pronto.moverParaSprintAtual(${t.ticketKey},true)"></pronto:icons>
						</c:if>
					</td>
					<td>
						<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
							<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${t.ticketKey},true)"></pronto:icons>
						</c:if>
					</td>
					<td>
					<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
						<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${t.ticketKey},true)"></pronto:icons>
					</c:if>
					</td>
					<td>
						<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
					</td>
					<td>
						<a href="${raiz}tickets/${t.ticketKey}">
							<pronto:icons name="editar.png" title="Editar" />
						</a>
					</td>
				</tr>
				<c:forEach items="${t.filhos}" var="f">
					<c:if test="${f.backlog.backlogKey eq t.backlog.backlogKey}">
						<c:set var="cor" value="${!cor}"/>	 
						<tr class="${cor ? 'odd' : 'even'}" id="${f.ticketKey}" pai="${t.ticketKey}">
							<td>${f.ticketKey}</td>
							<td class="titulo">${f.titulo}</td>
							<td>${f.tipoDeTicket.descricao}</td>
							<td>${f.cliente}</td>
							<td style="color:gray;" class="valorDeNegocio"></td>
							<td style="color:gray;"class="esforco">${f.esforco}</td>
							<td>${f.kanbanStatus.descricao}</td>
							<td>-</td>
							<td></td>
							<td></td>
							<td>
								<c:if test="${f.backlog.backlogKey eq 1 or (f.backlog.backlogKey eq 2 and usuarioLogado.productOwner) or f.backlog.backlogKey eq 3}">
									<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${f.ticketKey},true)"></pronto:icons>
								</c:if>
							</td>
							<td></td>
							<td>
								<c:if test="${(f.backlog.backlogKey eq 1 or f.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
									<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${f.ticketKey},true)"></pronto:icons>
								</c:if>
							</td>
							<td>
							<c:if test="${f.backlog.backlogKey eq 4 or f.backlog.backlogKey eq 5}">
								<c:if test="${f.pai.backlog.backlogKey ne 4 and f.pai.backlog.backlogKey ne 5}">
									<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${f.ticketKey},true)"></pronto:icons>
								</c:if>
							</c:if>
							</td>
							<td>
								<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${f.ticketKey});"/>
							</td>
							<td>
								<a href="${raiz}tickets/${f.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
							</td>
						</tr>
					</c:if>
				</c:forEach>
				<tr style="height: 1px;">
					<td colspan="16" style="background-color:#b4c24b"></td>
				</tr>
			</c:forEach>
			<!-- Tarefas Soltas -->
			<c:forEach items="${tarefasSoltas}" var="s">
					<c:set var="cor" value="${!cor}"/>	 
					<tr class="${cor ? 'odd' : 'even'}" id="${s.ticketKey}" pai="${s.pai.ticketKey}">
						<td>${s.ticketKey}</td>
						<td class="titulo">${s.titulo}</td>
						<td>${s.tipoDeTicket.descricao}</td>
						<td>${s.cliente}</td>
						<td style="color:gray;" class="valorDeNegocio">${s.valorDeNegocio}</td>
						<td style="color:gray;"class="esforco">${s.esforco}</td>
						<td>${s.kanbanStatus.descricao}</td>
						<td>-</td>
						<td></td>
						<td></td>
						<td>
							<c:if test="${s.backlog.backlogKey eq 1 or (s.backlog.backlogKey eq 2 and usuarioLogado.productOwner) or s.backlog.backlogKey eq 3}">
								<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${f.ticketKey},true)" />
							</c:if>
						</td>
						<td></td>
						<td>
							<c:if test="${(s.backlog.backlogKey eq 1 or s.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
								<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${f.ticketKey},true)" />
							</c:if>
						</td>
						<td>
						<c:if test="${s.backlog.backlogKey eq 4 or s.backlog.backlogKey eq 5}">
							<c:if test="${s.pai.backlog.backlogKey ne 4 and s.pai.backlog.backlogKey ne 5}">
								<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${s.ticketKey},true)" />
							</c:if>
						</c:if>
						</td>
						<td>
							<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${s.ticketKey});"/>
						</td>
						<td>
							<a href="${raiz}tickets/${s.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
						</td>
					</tr>
				</c:forEach>
			<tr>
				<th colspan="4">Total</th>
				<th id="somaValorDeNegocio">${sprint.valorDeNegocioTotal}${backlog.valorDeNegocioTotal}</th>
				<th id="somaEsforco">${sprint.esforcoTotal}${backlog.esforcoTotal}</th>
				<th></th>
				<th id="tempoDeVidaMedio">${sprint.tempoDeVidaMedioEmDias}${backlog.tempoDeVidaMedioEmDias}</th>
				<th colspan="8"></th>
			</tr>
			<tr>
				<td colspan="15"><i>* ${descricaoTotal}</i></td>
			</tr>
		</table>	
		
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1')">Nova Ideia</button>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2 and usuarioLogado.productOwner}">
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2')">Nova Estória</button>&nbsp;&nbsp;
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3')">Novo Defeito</button>&nbsp;&nbsp;
				</c:when>
			</c:choose>
		</div>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>