<%@ include file="/commons/taglibs.jsp"%>
<c:url var="iconsFolder" value="/commons/icons"/>
<html>
	<head>
		<title><c:choose><c:when test="${ticket.ticketKey gt 0}">${ticket.tipoDeTicket.descricao} #${ticket.ticketKey}</c:when><c:otherwise>Incluir ${ticket.tipoDeTicket.descricao}</c:otherwise></c:choose></title>
		<link rel="stylesheet" type="text/css" media="all" href="${raiz}ticket/ticket.editar.css" />
		<script type="text/javascript" src="${raiz}ticket/ticket.editar.js"></script>
		<script>
			var ordens = eval(${ordens});
		</script>
	</head>
	<body>
		
		<c:choose>
			<c:when test="${ticket.ticketKey gt 0}">
				<h1>${ticket.tipoDeTicket.descricao} #${ticket.ticketKey} - ${ticket.titulo}</h1>
			</c:when>
			<c:otherwise>
				<h1>Incluir ${ticket.tipoDeTicket.descricao}</h1>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${ticket.ticketKey gt 0}">
		<div id="progressBar" align="right">
			<c:forEach items="${kanbanStatus}" var="status">
				<c:set var="progressClass" value="${status.kanbanStatusKey eq ticket.kanbanStatus.kanbanStatusKey ? 'ui-state-active' : 'ui-state-disabled'}"/>
				<div title="Clique para mover para '${status.descricao}'" onclick="alterarStatuDoKanbanPara(${status.kanbanStatusKey})"  class="kanbanStep ui-corner-all ${progressClass}">
					${status.descricao}
				</div>
			</c:forEach>
		</div>
		</c:if>
		
		<div id="ticketTabs">
			<ul>
				<li><a href="#detalhes">Detalhes</a></li>
				<c:if test="${!empty ticket.filhos}">
					<li><a href="#tarefas">Tarefas (${fn:length(ticket.filhosProntos)} / ${fn:length(ticket.filhos)})</a></li>
				</c:if>
				<c:if test="${ticket.ticketKey gt 0}">
					<li><a href="#comentarios">Comentários (${fn:length(ticket.comentarios)})</a></li>
					<li><a href="#anexos">Anexos (${fn:length(anexos)})</a></li>
					<c:if test="${zendeskTicket ne null}">
						<li><a href="#zendesk">Zendesk (${fn:length(zendeskTicket.comments)})</a></li>
					</c:if>
					<li><a href="#historico">Histórico (${fn:length(ticket.logs)})</a></li>
					<li><a href="#movimentos">Kanban (${fn:length(movimentos)})</a></li>
					<div align="right">
					<!-- Operacoes -->
					<c:if test="${ticket.pai ne null and ticket.ticketKey gt 0}">
						<a href="${raiz}tickets/${ticket.pai.ticketKey}"><pronto:icons name="estoria.png" title="Ir para Estória" /></a>
					</c:if>
					<c:if test="${(ticket.tipoDeTicket.tipoDeTicketKey eq 1) or (ticket.tipoDeTicket.tipoDeTicketKey eq 2)}">
						<pronto:icons name="transformar_em_bug.png" title="Transformar em Defeito" onclick="pronto.transformarEmDefeito('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 3 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
						<pronto:icons name="transformar_em_estoria.png" title="Transformar em Estória" onclick="pronto.transformarEmEstoria('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${(ticket.backlog.backlogKey eq 1 or ticket.backlog.backlogKey eq 3) and (usuarioLogado.administrador or usuarioLogado.productOwner) and !ticket.tarefa}">
							<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="pronto.moverParaProductBacklog('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.backlog.backlogKey eq 2 and !ticket.tarefa}">
							<pronto:icons name="mover_para_ideias.png" title="Mover para a Caixa de Entrada" onclick="pronto.moverParaInbox('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${(ticket.backlog.backlogKey ne 4) and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
						<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="pronto.jogarNoLixo('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.backlog.backlogKey eq 1 or (ticket.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)) or ticket.backlog.backlogKey eq 3}">
						<pronto:icons name="mover_para_impedimentos.png" title="Mover para Impedimentos" onclick="pronto.impedir('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.backlog.backlogKey eq 2 and (usuarioLogado.administrador or usuarioLogado.productOwner)}">
						<pronto:icons name="mover_para_o_sprint_atual.png" title="Mover para um Sprint" onclick="escolherSprintParaMover('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.backlog.backlogKey eq 4 or ticket.backlog.backlogKey eq 5}">
						<c:if test="${!ticket.tarefa or (ticket.tarefa && ticket.pai.backlog.backlogKey ne 4 && ticket.pai.backlog.backlogKey ne 5)}">
							<pronto:icons name="restaurar.png" title="${ticket.backlog.backlogKey eq 4 ? 'Restaurar' : 'Desimpedir'} ${ticket.tipoDeTicket.descricao}" onclick="pronto.restaurar('${ticket.ticketKey}')"></pronto:icons>
						</c:if>
					</c:if>
					<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 1 or ticket.tipoDeTicket.tipoDeTicketKey eq 2}">
						<pronto:icons name="nova_tarefa.png" title="Incluir Tarefa" onclick="pronto.incluirTarefa('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 6}">
						<pronto:icons name="nova_tarefa.png" title="Incluir Tarefa" onclick="pronto.incluirTarefa('${ticket.pai.ticketKey}')"></pronto:icons>
					</c:if>
					<c:if test="${ticket.tarefa}">
						<pronto:icons name="desacoplar_tarefa.png" title="Desacoplar Tarefa da Estória" onclick="pronto.desacoplarTarefa('${ticket.ticketKey}')"></pronto:icons>
					</c:if>
					
					<c:choose>
						<c:when test="${ticket.script eq null}">
							<pronto:icons name="adicionar_script.png" title="Adicionar Script de Banco de Dados" onclick="adicionarScript()"/>
						</c:when>
						<c:otherwise>
							<pronto:icons name="editar_script.png" title="Editar Script de Banco de Dados" onclick="editarScript()"/>
						</c:otherwise>
					</c:choose>
					<!-- Fim das Operacoes -->
					<br/><br/>
				</c:if>
				
				</div>
			</ul>
			<div id="detalhes">
				<c:if test="${ticket.ticketKey gt 0}">
					<div class="htmlbox">
						${ticket.html}
					</div>
				</c:if>
				<form action="${raiz}tickets" method="post" id="formTicket">
					<form:hidden path="ticket.tipoDeTicket.tipoDeTicketKey" />
					<form:hidden path="ticket.prioridadeDoCliente"/>
					
					<c:if test="${ticket.ticketKey gt 0}">
						<form:hidden path="ticket.ticketKey"/>
						<c:if test="${ticket.script ne null}">
							<form:hidden path="ticket.script.scriptKey" />
						</c:if>	
					</c:if>
					
					<c:if test="${ticket.pai ne null}">
						<input type="hidden" name="paiKey" value="${ticket.pai.ticketKey}"/>
						<a href="${raiz}tickets/${ticket.pai.ticketKey}">
							<b>#${ticket.pai.ticketKey} - ${ticket.pai.titulo}</b>
						</a>
						<p>Estória</p>
					</c:if>

					<div id="divReporter">
						<div align="center" class="person">
							<img alt="Gravatar" align="bottom" title="${ticket.reporter.nome}" src="http://www.gravatar.com/avatar/${ticket.reporter.emailMd5}?s=80" />
							<div class="person_name">${ticket.reporter.username}</div>
							<div>Reporter</div>
						</div>
						<form:hidden path="ticket.reporter.username"/><br/>
					</div>
					
					<c:if test="${zendeskTicketKey ne null}">
						<div id="divZendesk" style="clear: both;">
							<div align="center" class="person">
								<pronto:icons name="zendesk.png" title="Abrir Ticket no Zendesk" onclick="openWindow('${zendeskUrl}/tickets/${zendeskTicketKey}')"/>
								<div class="person_name">#${zendeskTicketKey}</div>
								<div>Zendesk</div>
							</div>
							<br/>
						</div>
					</c:if>
					
					<div class="group">
						
						<div>
							<form:input path="ticket.titulo" size="85" id="titulo" cssClass="required"/>
							<p>Título</p>
						</div>							

						<div class="bloco">

							<div id="divBacklog">
								<form:hidden path="ticket.backlog.backlogKey"/>
								<b>${ticket.backlog.descricao}</b>
								<c:if test="${ticket.backlog.backlogKey ne 3}">
									<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="openWindow('${raiz}backlogs/${ticket.backlog.slug}')"/>
								</c:if>				
								<p>Backlog</p>
							</div>
							
							<div id="divSprint">
								<c:if test="${ticket.sprint.sprintKey gt 0}">
									<c:choose>
										<c:when test="${!ticket.tarefa and !ticket.sprint.fechado}">
											<form:select path="ticket.sprint.sprintKey">
												<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
											</form:select>
											<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="openWindow('${raiz}backlogs/sprints/${ticket.sprint.sprintKey}')"/>
											<p>Sprint</p>
										</c:when>
										<c:otherwise>
											<form:hidden path="ticket.sprint.sprintKey"/>
											<b>${ticket.sprint.nome}</b>
											<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="openWindow('${raiz}backlogs/sprints/${ticket.sprint.sprintKey}')"/>
											<p>Sprint</p>
										</c:otherwise>
									</c:choose>
								</c:if>
							</div>
						</div>
						
						<div class="bloco">
						
							<div id="divCliente">
								<c:choose>
									<c:when test="${!ticket.tarefa}">
										<select name="clienteKey" id="clienteKey">
											<c:forEach var="c" items="${clientes}">
												<c:choose>
													<c:when test="${c.clienteKey eq ticket.cliente.clienteKey}">
														<option value="${c.clienteKey}" selected="selected">${c.nome}</option>	
													</c:when>
													<c:otherwise>
														<option value="${c.clienteKey}">${c.nome}</option>
													</c:otherwise>
												</c:choose>
												
											</c:forEach>							
										</select>
										<br/>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="clienteKey" value="${ticket.cliente.clienteKey}" />
										<b>${ticket.cliente eq null ? '-' : ticket.cliente.nome}</b>							
									</c:otherwise>
								</c:choose>
								<p>Cliente</p>
							</div>
							
							<div id="divSolicitador">
								<c:choose>
									<c:when test="${!ticket.tarefa}">
										<form:input path="ticket.solicitador"  cssClass="required"/><br/>
									</c:when>
									<c:otherwise>
										<form:hidden path="ticket.solicitador"/>
										<b>${ticket.solicitador eq null ? '-' : ticket.solicitador}</b>
									</c:otherwise>
								</c:choose>
								<p>Solicitador</p>
							</div>
							
							<c:if test="${!ticket.tarefa}">
								<div id="divValorDeNegocio">
									<c:choose>
										<c:when test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
											<form:input path="ticket.valorDeNegocio" cssClass="required digits" size="4" maxlength="4"/><br/>
										</c:when>
										<c:otherwise>
											<form:hidden path="ticket.valorDeNegocio"/>
											${ticket.valorDeNegocio gt 0 ? ticket.valorDeNegocio : "-"}<br/>
										</c:otherwise>
									</c:choose>
									<p>Valor de Negócio</p>
								</div>
							</c:if>
							
						</div>
						
						<div class="bloco">
							
							<c:if test="${ticket.ticketKey gt 0}">
								<div id="divTempoDeVida">
									<b>${ticket.tempoDeVidaEmDias} dias</b>
									<p>Tempo de Vida</p> 
								</div>
								
								<div id="divDataDeCriacao">
									<fmt:formatDate value="${ticket.dataDeCriacao}" type="both" var="dataDeCriacao"/>
									<b>${dataDeCriacao}</b>
									<input type="hidden" name="dataDeCriacao" value="${dataDeCriacao}">
									<p>Data de Criação</p> 
								</div>
								
							
								<div id="divDataUltimaAlteracao">
									<fmt:formatDate var="dataDaUltimaAlteracao" value="${ticket.dataDaUltimaAlteracao}" pattern="dd/MM/yyyy HH:mm:ss"/>
									<input type="hidden" name="dataDaUltimaAlteracao" value="${dataDaUltimaAlteracao}"/>
									<b>${dataDaUltimaAlteracao}</b>
									<p>Data da Última Alteração</p>
								</div>
							</c:if>
						
						</div>
						
						
						<div class="bloco">
							
							<div id="divEsforco">
								<c:choose>
									<c:when test="${(usuarioLogado.administrador or usuarioLogado.equipe) and empty ticket.filhos}">
										<c:choose>
											<c:when test="${configuracoes['tipoDeEstimativa'] eq 'PMG'}">
												<form:select path="ticket.esforco" >
													<form:option value="10">Pequeno</form:option>
													<form:option value="20">Médio</form:option>
													<form:option value="30">Grande</form:option>
												</form:select>
											</c:when>
											<c:otherwise>
												<form:input path="ticket.esforco" cssClass="required number" size="5"/>	
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<form:hidden path="ticket.esforco"/>
										<b>${ticket.esforco gt 0 ? ticket.esforco : "-"}</b>
									</c:otherwise>
								</c:choose>
								<br/><p>Esforço</p>
							</div>
							
							<div>
								<form:select path="ticket.par">
									<form:option value="true">Sim</form:option>
									<form:option value="false">Não</form:option>
								</form:select>
								<br/>
								<p>Em Par?</p>
							</div>
							
							<c:if test="${ticket.defeito}">
								<div>
										<form:select path="ticket.causaDeDefeito.causaDeDefeitoKey" cssClass="causaDoDefeito">
											<form:option value="0" cssClass="nenhuma">Selecione uma causa</form:option>
											<form:options items="${causasDeDefeito}" itemLabel="descricao" itemValue="causaDeDefeitoKey"/>
										</form:select>
										<br/>
										<p>Causa do Defeito</p>
								</div>
							</c:if>
							
							
						</div>
						
						<div class="bloco">
							<div>
								<form:select path="ticket.planejado">
									<form:option value="true">Sim</form:option>
									<form:option value="false">Não</form:option>
								</form:select>
								<br/>
								<p>Planejado?</p>
							</div>
							
							<div>
								<form:select path="ticket.categoria.categoriaKey">
									<form:option value="0" cssClass="nenhuma">Nenhuma</form:option>
									<form:options items="${categorias}" itemLabel="descricao" itemValue="categoriaKey"/>
								</form:select>
								<br/>
								<p>Categoria</p>
							</div>
							
							<div>
								<form:select path="ticket.modulo.moduloKey">
									<form:option value="0" cssClass="nenhuma">Nenhum</form:option>
									<form:options items="${modulos}" itemLabel="descricao" itemValue="moduloKey"/>
								</form:select>
								<br/>
								<p>Módulo</p>
							</div>
						</div>
						<div class="bloco">
							
							<c:if test="${ticket.defeito}">
								<div>
									<c:choose>
										<c:when test="${!empty ticket.ticketOrigem}">
											<span>Ticket de origem de defeito associado&nbsp;</span>
												<p id="descricaoOrigem">
													<b>Origem: <a style="cursor:pointer" onclick="abrirTicket(${ticket.ticketOrigem.ticketKey})">#${ticket.ticketOrigem.ticketKey}</a></b>
													<pronto:icons name="excluir.png" title="Clique aqui para desassociar este ticket de origem de defeito" onclick="excluirTicketDeOrigem(${ticket.ticketKey});"/>
												</p>
										</c:when>
										<c:otherwise>
											<span id="spanTicketOrigem">Associar origem de defeito&nbsp;
												<pronto:icons name="buscar.png" id="iconBuscarOrigem" title="Clique aqui para associar o ticket que originou este defeito" onclick="buscarTicketDeOrigem(${ticket.ticketKey});"/>
											</span>
											<p id="descricaoOrigem"> </p>
										</c:otherwise>
									</c:choose>
								</div>
							</c:if>
							
							<c:if test="${empty ticket.filhos}">
								<div>
									<form:input path="ticket.branch" size="30"/><br/>
									<p>Branch</p>
								</div>
							</c:if>
							
						</div>
						
						<div class="bloco">
							<div>
								<input type="hidden" id="kanbanStatusAnterior" name="kanbanStatusAnterior" value="${ticket.kanbanStatus.kanbanStatusKey}">
								<form:select path="ticket.kanbanStatus.kanbanStatusKey" onchange="alterarStatuDoKanban()" id="kanbanStatusKey">
									<form:options items="${kanbanStatus}" itemLabel="descricao" itemValue="kanbanStatusKey"/>
								</form:select>
								<br/>
								<p>Kanban Status</p>
							</div>
							
							<div id="motivoReprovacaoDiv">
								<select name="motivoReprovacaoKey" id="motivoReprovacaoKey">
									<option class="nenhuma" selected="selected" value="-1">Selecione um Motivo</option>
									<c:forEach items="${motivosReprovacao}" var="motivo">
										<option value="${motivo.motivoReprovacaoKey}">${motivo.descricao}</option>
									</c:forEach>
								</select>
								<br/>
								<p>Motivo de Reprovação</p>
							</div>
							
							<div>
								<fmt:formatDate var="dataDePronto" value="${ticket.dataDePronto}" pattern="dd/MM/yyyy"/>
								<input type="text" value="${dataDePronto}" name="dataDePronto" class="datePicker"/>
								<p>Data de Pronto</p>
							</div>
							
							<div>
								<c:choose>
									<c:when test="${empty zendeskTicketKey}">
										<span>Zendesk:&nbsp;
											<pronto:icons name="adicionar.png" title="Vincular esta tarefa com uma tarefa do Zendesk" onclick="adicionarVinculoComZendesk()"/>
										</span>
										<p>&nbsp;</p>
									</c:when>
									<c:otherwise>
										<span>Zendesk: <b>${zendeskTicketKey}</b> vinculado&nbsp; 
											<pronto:icons name="excluir.png" title="Desvincular esta tarefa com a tarefa do Zendesk" onclick="excluirVinculoComZendesk(${ticket.ticketKey})"/>
										</span>
										<p>&nbsp;</p>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<c:if test="${ticket.tarefa or empty ticket.filhos}">
							<div class="linha">
							<div>
								<c:forEach items="${desenvolvedores}" var="u" varStatus="s">
									<c:set var="checked" value="${false}"/>
									<c:forEach items="${ticket.desenvolvedores}" var="d">
										<c:if test="${d.username eq u.username}">
											<c:set var="checked" value="${true}"/>
										</c:if>
									</c:forEach>
									<div class="person desenvolvedor" style="display: ${checked ? 'inline' : 'none'}">
										<img alt="${u.username} - Clique para adicionar/remover" id="dev_img_${u.username}" class="${checked ? 'ativo' : 'inativo'}" align="bottom" title="${u.nome}" src="http://www.gravatar.com/avatar/${u.emailMd5}?s=45" onclick="toogleDesenvolvedor('${u.username}')" style="cursor:pointer"/>
										<input id="dev_chk_${u.username}"  type="checkbox" name="desenvolvedor" value="${u.username}" ${checked ? 'checked="checked"' : ''} style="display: none;">
										<div class="person_name">${u.username}</div>
									</div>
								</c:forEach>
								<p style="clear: both"><b>Desenvolvedores</b><pronto:icons name="editar.png" title="Alterar Desenvolvedores" onclick="alterarDesenvolvedores(this)"/></p>
							</div>
							</div>
							<div class="linha">
							<div>
								<c:forEach items="${testadores}" var="u" varStatus="s">
									<c:set var="checked" value="${false}"/>
									<c:forEach items="${ticket.testadores}" var="d">
										<c:if test="${d.username eq u.username}">
											<c:set var="checked" value="${true}"/>
										</c:if>
									</c:forEach>
									<div class="person testador" style="display: ${checked ? 'inline' : 'none'}">
										<img alt="${u.username} - Clique para adicionar/remover" id="tes_img_${u.username}" class="${checked ? 'ativo' : 'inativo'}" align="bottom" title="${u.nome}" src="http://www.gravatar.com/avatar/${u.emailMd5}?s=45" onclick="toogleTestador('${u.username}')" style="cursor:pointer"/>
										<input id="tes_chk_${u.username}"  type="checkbox" name="testador" value="${u.username}" ${checked ? 'checked="checked"' : ''} style="display: none;">
										<div class="person_name">${u.username}</div>
									</div>
								</c:forEach>
								<p style="clear: both"><b>Testadores</b><pronto:icons name="editar.png" title="Alterar Testadores" onclick="alterarTestadores(this)"/></p>
							</div>
							</div>
						</c:if>
						
						<form:hidden path="ticket.prioridade"/><br/>

						<div class="linha">						
							<h3>Descrição</h3>
							<div>
								<form:textarea path="ticket.descricao" id="descricao"/>
							</div>
						</div>
						
					</div>
					<div align="center">
						<c:choose>
							<c:when test="${ticket.sprint ne null}">
								<button type="button" onclick="pronto.doGet('${raiz}backlogs/sprints/${ticket.sprint.sprintKey}')">Cancelar</button>
							</c:when>
							<c:otherwise>
								<button type="button" onclick="pronto.doGet('${raiz}backlogs/${ticket.backlog.backlogKey}')">Cancelar</button>
							</c:otherwise>
						</c:choose>
						<button type="button" onclick="salvar()">Salvar</button>
					</div>
				</form>	
			</div>
			<c:if test="${!empty ticket.filhos}">
				<div id="tarefas">
					<ul id="listaTarefas">
						<c:forEach items="${ticket.filhosOrdenadosKanbanStatus}" var="filho">
							<c:set var="cssClass" value="${filho.dataDePronto ne null or filho.backlog.backlogKey eq 4 ? 'ui-state-disabled': 'ui-state-default'}"/>
							<li class="${cssClass}" id="${filho.ticketKey}">
								<span style="float: left;">
									<b>${filho.ticketKey}</b>
									<span class="titulo">${filho.titulo}</span>
								</span>
								<span style="float: right;">
									${filho.kanbanStatus.descricao}
									<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${filho.ticketKey});"/>
									<a href="${raiz}tickets/${filho.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
								</span>
							</li>
						</c:forEach>
					</ul>
					<br/>
				</div>
			</c:if>
			
			<c:if test="${ticket.ticketKey gt 0}">
			<div id="comentarios">
				<%@ include file="ticket.comentarios.jsp" %>
			</div>
			
			<c:if test="${zendeskTicket ne null}">
				<div id="zendesk">
					<%@ include file="ticket.zendesk.jsp" %>
				</div>
			</c:if>
			
			<div id="anexos">
				<c:if test="${ticket.ticketKey gt 0}">
				<ul style="list-style-type: none;">
					<c:forEach items="${anexos}" var="anexo">
						<li>
							<c:choose>
								<c:when test="${anexo.imagem}">
									<pronto:icons name="imagem.gif" title="Imagem ${anexo.extensao}"/>
								</c:when>
								<c:when test="${anexo.planilha}">
									<pronto:icons name="excel.png" title="Planílha ${anexo.extensao}"/>
								</c:when>
								<c:when test="${anexo.extensao eq 'pdf'}">
									<pronto:icons name="pdf.png" title="Arquivo PDF"/>
								</c:when>
								<c:otherwise>
									<pronto:icons name="anexo.png" title="${anexo.extensao}"/>
								</c:otherwise>
							</c:choose>
							
							${anexo.nomeParaExibicao}
							<pronto:icons name="download.gif" title="Baixar Anexo" onclick="goTo('${raiz}tickets/${ticket.ticketKey}/anexos?file=${anexo}')"/>
							<pronto:icons name="excluir.png" title="Excluir Anexo" onclick="excluirAnexo(${ticket.ticketKey},'${anexo}');"/>
						</li>
					</c:forEach>
				</ul>
				<h4>Incluir anexo</h4>						
				<form action="${raiz}tickets/${ticket.ticketKey}/upload" method="post" enctype="multipart/form-data">
					<input type="file" name="arquivo">
					<button type="submit">Upload</button>
				</form>
			</div>
			
			<div id="historico">
				<ul id="listaHistorico">
					<c:set var="dataGrupo" value="${null}"/>
					<c:forEach items="${ticket.logs}" var="log">
						<fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy" var="dataAtual"/>
						<c:if test="${dataGrupo eq null or dataGrupo ne dataAtual}">
							<h4><fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy"/></h4>
							<fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy" var="dataGrupo"/>
						</c:if>
						<c:choose>
							<c:when test="${log.campo eq 'descrição' or log.campo eq 'descricao'}">
								<li><fmt:formatDate value="${log.data}" pattern="HH:mm"/> - ${log.usuario} - descrição alterada <a href="${raiz}tickets/${ticket.ticketKey}/log/${log.ticketHistoryKey}">(ver)</a></li>
							</c:when>
							<c:otherwise>
								<li>${log.descricaoSemData}</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		
			<div id="movimentos">
				<ul id="listaMovimentos">
					<c:set var="dataGrupo" value="${null}"/>
					<c:forEach items="${movimentos}" var="movimento">
						<fmt:formatDate value="${movimento.data}" pattern="dd/MM/yyyy" var="dataAtual"/>
						<c:if test="${dataGrupo eq null or dataGrupo ne dataAtual}">
							<h4><fmt:formatDate value="${movimento.data}" pattern="dd/MM/yyyy"/></h4>
							<fmt:formatDate value="${movimento.data}" pattern="dd/MM/yyyy" var="dataGrupo"/>
						</c:if>
						<li>${movimento.descricao}</li>
					</c:forEach>
				</ul>
			
			</div>
		</div>
			
		</c:if>
	</c:if>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
		
		<div title="Número do ticket do Zendesk" id="dialogVincularAoZendesk" style="display: none; width: 500px;">
			<input id="zendeskTicketKeyVincular" size="10" maxlength="15"><br/><br/>
			<button onclick="confirmarVinculo(${ticket.ticketKey})">Confirmar</button>
		</div>
		
		<div title="Escolha um Sprint" id="dialogSelecionarSprint" style="display: none; width: 500px;">
			<select id="selecionarSprint">
				<c:forEach items="${sprints}" var="s">
					<option ${s.atual ? 'selected':''} value="${s.sprintKey}">${s.nome} ${s.atual ? '(Atual)' : ''}</option>
				</c:forEach>			
			</select>
			<br/><br/>
			<button onclick="$('#dialogSelecionarSprint').dialog('close');">Cancelar</button>
			<button onclick="pronto.moverParaSprint('${ticket.ticketKey}', $('#selecionarSprint').val(), false)">Mover</button>
		</div>
		
		<script>
			var iconsFolder;
			$(function(){
				$('#ticketTabs').tabs();
				$('.datePicker').datepicker();

				iconsFolder = "${iconsFolder}";
			});

			//Variaveis Globais para usar no .js
			var ticketKey = '${ticket.ticketKey}'; 
			var scriptKey = '${ticket.script.scriptKey}'; 
		</script>
	</body>
</html>