<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<meta http-equiv=refresh content="120" />
		<title>Kanban</title>
		<link rel="stylesheet" type="text/css" media="all" href="${raiz}kanban/kanban.css" />
		<script type="text/javascript" src="${raiz}kanban/kanban.js"></script>
		<script>
			var ordens = eval(${ordens});
		</script>
	</head>
	<body>
		<div align="left">
			<h1>
				Kanban do Sprint ${sprint.nome}
				<%@ include file="/commons/sprintLinks.jsp" %>
			</h1>
		</div>
		
		<c:if test="${fn:length(sprints) gt 1}">
			<div align="right">
				Sprint: 
				<form:select path="sprint.sprintKey" onchange="pronto.kanban.recarregar(this.value)">
					<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
				</form:select>
			</div>
		</c:if>
		
		<table align="center" style="width: 100%;" id="kanbanTable">
			<tr>
                <c:forEach items="${status}" var="s">
                    <c:choose>
                        <c:when test="${s.kanbanStatusKey eq 1 || s.kanbanStatusKey eq 2 || s.kanbanStatusKey eq 100}">
                            <c:set var="width" value="20"/>
						</c:when>
						<c:otherwise>
							<c:set var="width" value="10"/>
						</c:otherwise>
					</c:choose>
					<td style="width: ${width}%; height: 100%;">
                              <div class="ui-widget ui-helper-clearfix kanban-area" align="center">
                                  <h4 class="ui-widget-header">${s.descricao} (${mapaDeQuantidades[s.kanbanStatusKey] != null ? mapaDeQuantidades[s.kanbanStatusKey] : 0}) </h4>
                                  <ul class="kanbanColumn ui-helper-reset ui-helper-clearfix drop" status="${s.kanbanStatusKey}">
                                      <c:forEach items="${mapaDeTickets[s.kanbanStatusKey]}" var="t">
                                              <li id="${t.ticketKey}" class="ticket ui-corner-tr ${t.tipoDeTicket.tipoDeTicketKey eq 3 ? 'bug' : (t.tipoDeTicket.tipoDeTicketKey eq 6 ? 'task' : 'story')}" ondblclick="pronto.kanban.openTicket(${t.ticketKey});" title="${t.titulo}">
                                                  <p><span class="ticketKey">#${t.ticketKey}</span><br>${t.tituloResumido}</p>
                                              </li>
                                      </c:forEach>
                                  </ul>
                              </div>
                          </td>
                </c:forEach>
			</tr>
		</table>
		<div align="center">* Clique duas vezes sobre o cartão para abrí-lo.</div>
		<div id="motivo">
			<select id="motivoReprovacaoKey" onchange="pronto.kanban.alterarMotivo(this)">
				<option value="-1">=== Selecione um Motivo ===</option>
				<c:forEach items="${motivos}" var="motivo">
					<option value="${motivo.motivoReprovacaoKey}">${motivo.descricao}</option>				
				</c:forEach>
			</select>
		</div>
	</body>
</html>