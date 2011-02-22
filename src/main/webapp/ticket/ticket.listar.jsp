<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>${backlog.descricao}${sprint.nome}</title>
		<script src="${raiz}ticket/ticket.listar.js" type="text/javascript"></script>
		<style type="text/css">
			#ticketsTable tr {
				height: 22px;
			}
		</style>
	</head>
	<body>
		<c:choose>
			<c:when test="${sprint.nome ne null}">
				<h1>
					Sprint ${sprint.nome}
					
					<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
						<a href="${raiz}sprints/${sprint.sprintKey}">
							<pronto:icons name="editar.png" title="Editar Sprint" />
						</a>
					</c:if>
					
					<c:if test="${(usuarioLogado.administrador or usuarioLogado.equipe or usuarioLogado.productOwner)}">
						<a href="${raiz}sprints/${sprint.sprintKey}/estimar"><pronto:icons name="estimar.png" title="Estimar Sprint" /></a>
					</c:if>

					<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
						<a href="${raiz}sprints/${sprint.sprintKey}/adicionarTarefas">
							<pronto:icons name="adicionar.png" title="Adicionar Estórias ou Defeitos do Product Backlog ao Sprint" />
						</a>
					</c:if>
					
					<%@ include file="/commons/sprintLinks.jsp" %>
					
				</h1>	
			</c:when>
			<c:otherwise>
				<h1>
					${backlog.descricao} 
					<c:if test="${(usuarioLogado.administrador or usuarioLogado.equipe or usuarioLogado.productOwner) and (backlog.backlogKey le 3)}">
						<a href="${raiz}backlogs/${backlog.backlogKey}/estimar"><pronto:icons name="estimar.png" title="Estimar Backlog" /></a>  
					</c:if>
					
					<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
						<a href="${raiz}backlogs/${backlog.backlogKey}/priorizar">
							<pronto:icons name="priorizar.png" title="Priorizar" />
						</a>
					</c:if>
				</h1>
			</c:otherwise>
		</c:choose>
		
		
		<div align="right">
			<c:if test="${fn:length(sprints) gt 1}">
				Sprint: 
				<form:select path="sprint.sprintKey" onchange="recarregar(this.value)">
					<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
				</form:select>
			</c:if>
			
			Categorias: 
			<select name="categoriaKey" id="categoriaKey" onchange="recarregarFiltros()">
				<option value="0" selected="selected">Todas</option>
				<optgroup label="---">
					<c:forEach items="${categorias}" var="categoria">
						<option value="${categoria.categoriaKey}">${categoria.descricao}</option>
					</c:forEach>
				</optgroup>
				<optgroup label="---"></optgroup>
				<option value="-1">Sem categoria</option>
			</select>
			
			Status: 
			<select name="kanbanStatusKey" id="kanbanStatusKey" onchange="recarregarFiltros()">
				<option value="0" selected="selected">Todos</option>
				<optgroup label="---">
					<c:forEach items="${kanbanStatus}" var="item">
						<option value="${item.kanbanStatusKey}">${item.descricao}</option>
					</c:forEach>
				</optgroup>
				<optgroup label="---"></optgroup>
				<option value="-1">Pendentes</option>
			</select>
		</div>
		
		<c:set var="cor" value="${true}"/>
		<table id="ticketsTable" style="width: 100%">
			<thead>
			<tr>
				<th style="width: 30px;"></th>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Valor</th>
				<th>Esforço</th>
				<th>Status</th>
				<th title="Tempo de Vida em Dias">Dias</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${tickets}" var="t">
					<c:set var="cor" value="${!cor}"/>
					<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
						
						<td ondblclick="trocarCategoria(this)" title="Dê um duplo clique para alterar a categoria" categoriaKey="${t.categoria ne null ? t.categoria.categoriaKey : 0}">
							<c:if test="${t.categoria ne null}">
								<span class="categoria categoria-${t.categoria.descricaoDaCor}">
									${t.categoria.descricao}
								</span>
							</c:if>
						</td>
						<td>
							<a href="${raiz}tickets/${t.ticketKey}" title="Editar">
								${t.ticketKey}
							</a>
						</td>
						<td>
							<span class="titulo">${t.titulo}</span>
							<span class="opcao" style="display: none;">
								<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
								<a href="${raiz}tickets/${t.ticketKey}">
									<pronto:icons name="editar.png" title="Editar" />
								</a>
								<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 3) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
								<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="pronto.moverParaProductBacklog(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
										<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Ideias" onclick="pronto.moverParaIdeias(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 1 or (t.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)) or t.backlog.backlogKey eq 3}">
									<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${(t.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner))}">
									<pronto:icons name="mover_para_o_sprint_atual.png" title="Mover para um Sprint" onclick="escolherSprintParaMover(${t.ticketKey})"></pronto:icons>
								</c:if>
								<c:if test="${(t.backlog.backlogKey eq 1 or t.backlog.backlogKey eq 2) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
									<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${t.ticketKey},true)"></pronto:icons>
								</c:if>
								<c:if test="${t.backlog.backlogKey eq 4 or t.backlog.backlogKey eq 5}">
									<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${t.ticketKey},true)"></pronto:icons>
								</c:if>
							</span>
						</td>
						<td>${t.tipoDeTicket.descricao}</td>
						<td>${t.cliente}</td>
						<td class="valorDeNegocio">${t.valorDeNegocio}</td>
						<td class="esforco">${t.esforco}</td>
						<td>${t.kanbanStatus.descricao}</td>
						<td>${t.tempoDeVidaEmDias}</td>
					</tr>
					<c:forEach items="${t.filhos}" var="f">
						<c:if test="${f.backlog.backlogKey eq t.backlog.backlogKey}">
							<c:set var="cor" value="${!cor}"/>	 
							<tr class="${cor ? 'odd' : 'even'}" id="${f.ticketKey}" pai="${t.ticketKey}">
								<td ondblclick="trocarCategoria(this)" title="Dê um duplo clique para alterar a categoria" categoriaKey="${f.categoria ne null ? f.categoria.categoriaKey : 0}">
									<c:if test="${f.categoria ne null}">
										<span class="categoria categoria-${f.categoria.descricaoDaCor}">
											${f.categoria.descricao}
										</span>
									</c:if>
								</td>
								<td>
									<a href="${raiz}tickets/${f.ticketKey}" title="Editar">
										${f.ticketKey}
									</a>
								</td>
								<td>
									<span class="titulo">${f.titulo}</span>
									<span class="opcao" style="display: none">
										<c:if test="${f.backlog.backlogKey eq 1 or (f.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)) or f.backlog.backlogKey eq 3}">
											<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${f.ticketKey},true)"></pronto:icons>
										</c:if>
										<c:if test="${(f.backlog.backlogKey eq 1 or f.backlog.backlogKey eq 2) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
											<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${f.ticketKey},true)"></pronto:icons>
										</c:if>
										<c:if test="${f.backlog.backlogKey eq 4 or f.backlog.backlogKey eq 5}">
											<c:if test="${f.pai.backlog.backlogKey ne 4 and f.pai.backlog.backlogKey ne 5}">
												<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${f.ticketKey},true)"></pronto:icons>
											</c:if>
										</c:if>
										<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${f.ticketKey});"/>
										<a href="${raiz}tickets/${f.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
									</span>
								</td>
								<td>${f.tipoDeTicket.descricao}</td>
								<td>${f.cliente}</td>
								<td style="color:gray;" class="valorDeNegocio"></td>
								<td style="color:gray;"class="esforco">${f.esforco}</td>
								<td>${f.kanbanStatus.descricao}</td>
								<td></td>
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
					<tr class="${cor ? 'odd' : 'even'}" id="${s.ticketKey}" pai="${s.pai.ticketKey}" categoriaKey="${s.categoria ne null ? s.categoria.categoriaKey : 0}">
						<td ondblclick="trocarCategoria(this)" title="Dê um duplo clique para alterar a categoria">
							<c:if test="${s.categoria ne null}">
								<span class="categoria categoria-${s.categoria.descricaoDaCor}">
									${s.categoria.descricao}
								</span>
							</c:if>
						</td>
						<td>
							<a href="${raiz}tickets/${s.ticketKey}" title="Editar">
								${s.ticketKey}
							</a>
						</td>
						<td>
							<span class="titulo">${s.titulo}</span>
							<span class="opcao" style="display: none;">
								<c:if test="${s.backlog.backlogKey eq 1 or (s.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)) or s.backlog.backlogKey eq 3}">
									<pronto:icons name="mover_para_impedimentos.png" title="Mover para o Backlog de Impedimentos" onclick="pronto.impedir(${f.ticketKey},true)" />
								</c:if>
								<c:if test="${(s.backlog.backlogKey eq 1 or s.backlog.backlogKey eq 2) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
									<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo(${f.ticketKey},true)" />
								</c:if>
								<c:if test="${s.backlog.backlogKey eq 4 or s.backlog.backlogKey eq 5}">
									<c:if test="${s.pai.backlog.backlogKey ne 4 and s.pai.backlog.backlogKey ne 5}">
										<pronto:icons name="restaurar.png" title="Restaurar" onclick="pronto.restaurar(${s.ticketKey},true)" />
									</c:if>
								</c:if>
								<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${s.ticketKey});"/>
								<a href="${raiz}tickets/${s.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
							</span>
						</td>
						<td>${s.tipoDeTicket.descricao}</td>
						<td>${s.cliente}</td>
						<td style="color:gray;" class="valorDeNegocio">${s.valorDeNegocio}</td>
						<td style="color:gray;"class="esforco">${s.esforco}</td>
						<td>${s.kanbanStatus.descricao}</td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
			<c:if test="${param.categoriaKey eq null or param.categoriaKey eq 0}">
				<tfoot>
					<tr>
						<th colspan="5">Total</th>
						<th id="somaValorDeNegocio">${sprint.valorDeNegocioTotal}${backlog.valorDeNegocioTotal}</th>
						<th id="somaEsforco">${sprint.esforcoTotal}${backlog.esforcoTotal}</th>
						<th></th>
						<th id="tempoDeVidaMedio">${sprint.tempoDeVidaMedioEmDias}${backlog.tempoDeVidaMedioEmDias}</th>
					</tr>
					<tr>
						<td colspan="9"><i>* ${descricaoTotal}</i></td>
					</tr>
				</tfoot>
			</c:if>
		</table>	
		
		<div align="center">
			<c:choose>
				<c:when test="${backlog.backlogKey eq 1}">
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=1')">Nova Ideia</button>&nbsp;&nbsp;
				</c:when>
				<c:when test="${backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=2')">Nova Estória</button>&nbsp;&nbsp;
					&nbsp;&nbsp;<button type="button" onclick="pronto.doGet('${raiz}tickets/novo?backlogKey=${backlog.backlogKey}&tipoDeTicketKey=3')">Novo Defeito</button>&nbsp;&nbsp;
				</c:when>
			</c:choose>
		</div>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>

		<c:if test="${fn:length(sprintsEmAberto) gt 1}">
		<div title="Escolha um Sprint" id="dialogSelecionarSprint" style="display: none; width: 500px;">
			<select id="selecionarSprint">
				<c:forEach items="${sprintsEmAberto}" var="s">
					<option ${s.atual ? 'selected':''} value="${s.sprintKey}">${s.nome} ${s.atual ? '(Atual)' : ''}</option>
				</c:forEach>			
			</select>
			<input type="hidden" id="ticketKey" value="" />
			<br/><br/>
			<button onclick="$('#dialogSelecionarSprint').dialog('close');">Cancelar</button>
			<button onclick="pronto.moverParaSprint($('#ticketKey').val(), $('#selecionarSprint').val(), true)">Mover</button>
		</div>
		</c:if>
		
		<div style="display: none; width: 500px;">
			<select id="trocaCategoria" onchange="salvarCategoria(this);" >
				<option value="0" class="nenhuma">Nenhuma</option>
				<c:forEach items="${categorias}" var="c">
					<option value="${c.categoriaKey}" categoriaClass="categoria-${c.descricaoDaCor}">${c.descricao}</option>
				</c:forEach>			
			</select>
		</div>
		
		<script type="text/javascript">
			$(function(){
				var categoria = "${param.categoriaKey}";
				var kanbanStatusKey = "${param.kanbanStatusKey}";
				if (categoria.length > 0) {
					$('#categoriaKey').val(categoria);	
				}
				if (kanbanStatusKey.length > 0) {
					$('#kanbanStatusKey').val(kanbanStatusKey);	
				}
			});
		</script>
	</body>
</html>