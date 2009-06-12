<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title><c:choose><c:when test="${ticket.ticketKey gt 0}">${ticket.tipoDeTicket.descricao} #${ticket.ticketKey}</c:when><c:otherwise>Incluir ${ticket.tipoDeTicket.descricao}</c:otherwise></c:choose></title>
		<script>
		$(function() {
			  $('#formTicket').validate();
		      $("#descricao").markItUp(mySettings);
		      $("#comentario").markItUp(mySettings);
		      $("#titulo").focus();
		 });
		</script>
	</head>
	<body>
		<form action="salvar.action" method="post" id="formTicket">
			<c:choose>
				<c:when test="${ticket.ticketKey gt 0}">
					<form:hidden path="ticket.ticketKey"/>
					<h1>${ticket.tipoDeTicket.descricao} #${ticket.ticketKey} - ${ticket.titulo}</h1>
				</c:when>
				<c:otherwise>
					<h1>Incluir ${ticket.tipoDeTicket.descricao}</h1>
				</c:otherwise>
			</c:choose>
			<form:hidden path="ticket.tipoDeTicket.tipoDeTicketKey" />

			<c:if test="${ticket.ticketKey gt 0}">
				<!-- Operacoes -->
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 2}">
					<pronto:icons name="transformar_em_bug.png" title="Transformar em Defeito" onclick="goTo('transformarEmDefeito.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 3}">
					<pronto:icons name="transformar_em_estoria.png" title="Transformar em Estória" onclick="goTo('transformarEmEstoria.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${(ticket.backlog.backlogKey eq 1 or ticket.backlog.backlogKey eq 3) and usuarioLogado.productOwner and !ticket.tarefa}">
						<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="goTo('moverParaProductBacklog.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 2 and !ticket.tarefa}">
						<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Idéias" onclick="goTo('moverParaIdeias.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${(ticket.backlog.backlogKey eq 1 or ticket.backlog.backlogKey eq 2) and usuarioLogado.productOwner}">
					<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="goTo('jogarNoLixo.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 1 or (ticket.backlog.backlogKey eq 2 and usuarioLogado.productOwner) or ticket.backlog.backlogKey eq 3}">
					<pronto:icons name="impedimento.png" title="Mover para Impedimentos" onclick="goTo('moverParaImpedimentos.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 4 or ticket.backlog.backlogKey eq 5}">
					<c:if test="${!ticket.tarefa or (ticket.tarefa && ticket.pai.backlog.backlogKey ne 4 && ticket.pai.backlog.backlogKey ne 5)}">
						<pronto:icons name="restaurar.png" title="Restaurar" onclick="goTo('restaurar.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
					</c:if>
				</c:if>
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 2}">
					<pronto:icons name="nova_tarefa.png" title="Incluir Tarefa" onclick="goTo('incluirTarefa.action?paiKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<!-- Fim das Operacoes -->
				<br/><br/>
				
				<h3>Descrição</h3>
				<div class="htmlbox">
					${ticket.html}
				</div>
			</c:if>
		
			
			<c:if test="${!empty ticket.comentarios}">
				<h3>Comentários</h3>
				<c:forEach items="${ticket.comentarios}" var="comentario">
					<div class="htmlbox">
						${comentario.html}
						<br/>
						<div align="right"><i>Por ${comentario.usuario} em <fmt:formatDate value="${comentario.data}" type="both"/></i></div> 
					</div>
				</c:forEach>
				<br/>
			</c:if>
			
			<div class="group">
				
				<div>
					<form:hidden path="ticket.backlog.backlogKey"/>
					<b>${ticket.backlog.descricao}</b>					
					<p>Backlog</p>
				</div>
				
				<c:if test="${ticket.backlog.backlogKey eq 3}">
					<c:choose>
						<c:when test="${!ticket.tarefa}">
							<form:select path="ticket.sprint.sprintKey">
								<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
							</form:select>
							<p>Sprint</p>
						</c:when>
						<c:otherwise>
							<form:hidden path="ticket.sprint.sprintKey"/>
							<b>${ticket.sprint.nome}</b>
							<p>Sprint</p>
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<c:if test="${ticket.pai ne null}">
					<input type="hidden" name="paiKey" value="${ticket.pai.ticketKey}"/>
					<c:if test="${ticket.ticketKey gt 0}">
						<pronto:icons name="estoria.png" title="Ir para Estória" onclick="goTo('editar.action?ticketKey=${ticket.pai.ticketKey}')"/>
					</c:if>
					<b>#${ticket.pai.ticketKey} - ${ticket.pai.titulo}</b>
					<p>Estória</p>
				</c:if>
				
				<c:if test="${!empty ticket.filhos}">
					<c:forEach items="${ticket.filhos}" var="filho">
						<pronto:icons name="tarefa.png" title="Ir para Tarefa" onclick="goTo('editar.action?ticketKey=${filho.ticketKey}')"/>
						<b>#${filho.ticketKey} - ${filho.titulo}</b>
						<br/>
					</c:forEach>
					<p>Tarefas</p>					
				</c:if>
				
				<div>
					<form:input path="ticket.titulo" size="100" id="titulo" cssClass="required"/>
					<p>Título</p>
				</div>
				<div>
					<c:choose>
						<c:when test="${!ticket.tarefa}">
							<form:input path="ticket.cliente"  cssClass="required"/><br/>
						</c:when>
						<c:otherwise>
							<form:hidden path="ticket.cliente" />
							<b>${ticket.cliente eq null ? '-' : ticket.cliente}</b>							
						</c:otherwise>
					</c:choose>
					<p>Cliente</p>
				</div>
				<div>
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
				<div>
					<b>${ticket.reporter.nome}</b>
					<form:hidden path="ticket.reporter.username"/><br/>
					<p>Reporter</p> 
				</div>
				<c:if test="${ticket.ticketKey gt 0}">
					<div>
						<b><fmt:formatDate value="${ticket.dataDeCriacao}" type="both"/></b>
						<p>Data de Criação</p> 
					</div>
				</c:if>
				<c:if test="${!ticket.tarefa}">
					<div>
						<c:choose>
							<c:when test="${usuarioLogado.productOwner}">
								<form:input path="ticket.valorDeNegocio" cssClass="required digits"/><br/>
							</c:when>
							<c:otherwise>
								<form:hidden path="ticket.valorDeNegocio"/>
								${ticket.valorDeNegocio gt 0 ? ticket.valorDeNegocio : "-"}<br/>
							</c:otherwise>
						</c:choose>
						<p>Valor de Negócio</p>
					</div>
				</c:if>
				
				<div>
					<c:choose>
						<c:when test="${usuarioLogado.desenvolvedor and empty ticket.filhos}">
							<form:input path="ticket.esforco" cssClass="required number" /><br/>
						</c:when>
						<c:otherwise>
							<form:hidden path="ticket.esforco"/>
							<b>${ticket.esforco gt 0 ? ticket.esforco : "-"}</b><br/>
						</c:otherwise>
					</c:choose>
					<p>Esforço</p>
				</div>
					<div>
					<c:forEach items="${desenvolvedores}" var="u" varStatus="s">
						<c:set var="checked" value="${false}"/>
						<c:forEach items="${ticket.desenvolvedores}" var="d">
							<c:if test="${d.username eq u.username}">
								<c:set var="checked" value="${true}"/>
							</c:if>
						</c:forEach>
						<i><input type="checkbox" name="desenvolvedor" value="${u.username}" ${checked ? 'checked="checked"' : ''}>${u.nome}</i>
						<c:if test="${s.count % 3 == 0}"><br/></c:if>
					</c:forEach>					
					<p><b>Desenvolvedores</b></p>
				</div>
				</div>
					<div>
					<c:forEach items="${testadores}" var="u" varStatus="s">
						<c:set var="checked" value="${false}"/>
						<c:forEach items="${ticket.testadores}" var="d">
							<c:if test="${d.username eq u.username}">
								<c:set var="checked" value="${true}"/>
							</c:if>
						</c:forEach>
						<i><input type="checkbox" name="testador" value="${u.username}" ${checked ? 'checked="checked"' : ''}>${u.nome}</i>
						<c:if test="${s.count % 3 == 0}"><br/></c:if>
					</c:forEach>					
					<p><b>Testadores</b></p>
				</div>
				<div>
					<form:select path="ticket.kanbanStatus.kanbanStatusKey">
						<form:options items="${kanbanStatus}" itemLabel="descricao" itemValue="kanbanStatusKey"/>
					</form:select>
					<br/>
					<p>Kanban Status</p>
				</div>
				
				
				<div>
					<form:input path="ticket.dataDePronto"/><br/>
					<p>Data de Pronto</p>
				</div>
				
				
				<div>
					<form:input path="ticket.branch"/><br/>
					<p>Branch</p>
				</div>
				<div>
					<form:select path="ticket.par">
						<form:option value="true">Sim</form:option>
						<form:option value="false">Não</form:option>
					</form:select>
					<br/>
					<p>Em Par?</p>
				</div>
				<div>
					<form:select path="ticket.planejado">
						<form:option value="true">Sim</form:option>
						<form:option value="false">Não</form:option>
					</form:select>
					<br/>
					<p>Planejado?</p>
				</div>
				<h3>Descrição</h3>
				<div>
					<form:textarea path="ticket.descricao" id="descricao"/>
				</div>
				<h3>Comentário</h3>
				<div>
					<textarea id="comentario" name="comentario"></textarea>
				</div>
			</div>
			<div align="center">
				<c:choose>
					<c:when test="${ticket.sprint ne null}">
						<button type="button" onclick="window.location.href='listarPorSprint.action?sprintKey=${ticket.sprint.sprintKey}'">Cancelar</button>
					</c:when>
					<c:otherwise>
						<button type="button" onclick="window.location.href='listarPorBacklog.action?backlogKey=${ticket.backlog.backlogKey}'">Cancelar</button>
					</c:otherwise>
				</c:choose>
				<button type="submit">Salvar</button>
			</div>
		</form>		
	
		<c:if test="${ticket.ticketKey gt 0}">
			<h2>Anexos</h2>
			<ul>
				<c:forEach items="${anexos}" var="anexo">
					<li>${anexo} - <a href="download.action?ticketKey=${ticket.ticketKey}&file=${anexo}">download</a> - <a href="excluirAnexo.action?ticketKey=${ticket.ticketKey}&file=${anexo}">excluir</a></li>
				</c:forEach>
			</ul>
			
			<h4>Incluir anexo</h4>						
			<form action="upload.action?ticketKey=${ticket.ticketKey}" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<button type="submit">Upload</button>
			</form>
		
			<h2>Log</h2>
			<ul>
				<c:set var="dataGrupo" value="${null}"/>
				<c:forEach items="${ticket.logs}" var="log">
					<fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy" var="dataAtual"/>
					<c:if test="${dataGrupo eq null or dataGrupo ne dataAtual}">
						<h4><fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy"/></h4>
						<fmt:formatDate value="${log.data}" pattern="dd/MM/yyyy" var="dataGrupo"/>
					</c:if>
					<c:choose>
						<c:when test="${log.campo eq 'descrição'}">
							<li><fmt:formatDate value="${log.data}" pattern="HH:mm"/> - ${log.usuario} - Descrição Alterada <a href="logDescricao.action?ticketHistoryKey=${log.ticketHistoryKey}">(ver)</a></li>
						</c:when>
						<c:otherwise>
							<li><fmt:formatDate value="${log.data}" pattern="HH:mm"/> - ${log.descricaoSemData}</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</c:if>
	</body>
</html>