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
					<ul class="info"><h1>${ticket.tipoDeTicket.descricao} #${ticket.ticketKey}</h1></ul>
				</c:when>
				<c:otherwise>
					<ul class="info"><h1>Incluir ${ticket.tipoDeTicket.descricao}</h1></ul>
				</c:otherwise>
			</c:choose>

			<c:if test="${ticket.ticketKey gt 0}">
				<!-- Operacoes -->
				<c:if test="${ticket.backlog.backlogKey eq 1}">
						<pronto:icons name="mover_para_pb.png" title="Mover para o Product Backlog" onclick="goTo('moverParaProductBacklog.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 2}">
						<pronto:icons name="mover_para_ideias.png" title="Mover para o Backlog de Idéias" onclick="goTo('moverParaIdeias.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 1 or ticket.backlog.backlogKey eq 2}">
					<pronto:icons name="lixeira.png" title="Mover para a Lixeira" onclick="goTo('jogarNoLixo.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 1 or ticket.backlog.backlogKey eq 2 or ticket.backlog.backlogKey eq 3}">
					<pronto:icons name="impedimento.png" title="Mover para a Impedimentos" onclick="goTo('moverParaImpedimentos.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.backlog.backlogKey eq 4 or ticket.backlog.backlogKey eq 5}">
					<pronto:icons name="restaurar.png" title="Restaurar" onclick="goTo('restaurar.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 2}">
					<pronto:icons name="nova_tarefa.png" title="Incluir Tarefa" onclick="goTo('incluirTarefa.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 2}">
					<pronto:icons name="transformar_em_bug.png" title="Transformar em Defeito" onclick="goTo('transformarEmDefeito.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
				</c:if>
				<c:if test="${ticket.tipoDeTicket.tipoDeTicketKey eq 3}">
					<pronto:icons name="transformar_em_estoria.png" title="Transformar em Estória" onclick="goTo('transformarEmEstoria.action?ticketKey=${ticket.ticketKey}')"></pronto:icons>
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
					<form:hidden path="ticket.tipoDeTicket.tipoDeTicketKey"/>
					<b>${ticket.tipoDeTicket.descricao}</b>					
					<p>Tipo</p>
				</div>
				<div>
					<form:hidden path="ticket.backlog.backlogKey"/>
					<b>${ticket.backlog.descricao}</b>					
					<p>Backlog</p>
				</div>
				<c:if test="${ticket.backlog.backlogKey eq 3}">
					<form:select path="ticket.sprint.sprintKey">
						<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
					</form:select>
					<p>Sprint</p>
				</c:if>
				<div>
					<form:input path="ticket.titulo" size="100" id="titulo" cssClass="required"/>
					<p>Título</p>
				</div>
				<div>
					<form:input path="ticket.cliente"  cssClass="required"/><br/>
					<p>Cliente</p>
				</div>
				<div>
					<form:input path="ticket.solicitador"  cssClass="required"/><br/>
					<p>Solicitador</p>
				</div>
				<div>
					<b>${ticket.reporter.nome}</b>
					<form:hidden path="ticket.reporter.username"/><br/>
					<p>Reporter</p> 
				</div>
				<div>
					<c:choose>
						<c:when test="${usuarioLogado.productOwner}">
							<form:input path="ticket.valorDeNegocio" cssClass="required digits"/><br/>
						</c:when>
						<c:otherwise>
							<form:hidden path="ticket.valorDeNegocio"/>
							${ticket.valorDeNegocio}<br/>
						</c:otherwise>
					</c:choose>
					<p>Valor de Negócio</p>
				</div>
				<div>
					<c:choose>
						<c:when test="${usuarioLogado.desenvolvedor}">
							<form:input path="ticket.esforco" cssClass="required number" /><br/>
						</c:when>
						<c:otherwise>
							<form:hidden path="ticket.esforco"/>
							${ticket.esforco}<br/>
						</c:otherwise>
					</c:choose>
					
					<p>Esforço</p>
				</div>
					<div>
					<c:forEach items="${usuarios}" var="u" varStatus="s">
						<c:set var="checked" value="${false}"/>
						<c:forEach items="${ticket.desenvolvedores}" var="d">
							<c:if test="${d.username eq u.username}">
								<c:set var="checked" value="${true}"/>
							</c:if>
						</c:forEach>
						<i><input type="checkbox" name="desenvolvedor" value="${u.username}" ${checked ? 'checked="checked"' : ''}>${u.nome}</i>
						
						<c:if test="${s.count % 3 == 0}">
							<br/>
						</c:if>
					</c:forEach>					
					<p>Desenvolvedores</p>
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
							<li>${log.descricaoSemData}</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</c:if>
	</body>
</html>