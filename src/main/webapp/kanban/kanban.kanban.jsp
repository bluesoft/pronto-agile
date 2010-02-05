<%@ include file="/commons/taglibs.jsp"%>
<c:url var="moverUrl" value="/kanban/mover.action"/>
<c:url var="editarTicket" value="/ticket/editar.action"/>
<c:url var="urlSprint" value="/sprint/"/>
<c:url var="burndownUrl" value="/burndown/burndown.action"/>
<c:url var="retrospectivaUrl" value="/retrospectiva/ver.action"/>
<html>
	<head>
		<meta http-equiv=refresh content="60" />
		<title>Kanban</title>
		<link rel="stylesheet" type="text/css" media="all" href="${raiz}kanban/kanban.css" />
		<script type="text/javascript" src="${raiz}kanban/kanban.js"></script>
	</head>
	<body>
		<div align="left">
			<h1>
				Kanban do Sprint ${sprint.nome}
				<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="goTo('${raiz}backlogs/sprints/${sprint.sprintKey}')"/>
				<pronto:icons name="burndown_chart.png" title="Burndown Chart do Sprint" onclick="goTo('${burndownUrl}?sprintKey=${sprint.sprintKey}')"/>
				<pronto:icons name="retrospectiva.png" title="Retrospectiva" onclick="goTo('${retrospectivaUrl}?sprintKey=${sprint.sprintKey}')"/>
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
                              <div class="ui-widget ui-helper-clearfix" align="center">
                                  <h4 class="ui-widget-header">${s.descricao} (${mapaDeQuantidades[s.kanbanStatusKey] != null ? mapaDeQuantidades[s.kanbanStatusKey] : 0}) </h4>
                                  <ul class="kanbanColumn ui-helper-reset ui-helper-clearfix drop" status="${s.kanbanStatusKey}">
                                      <c:forEach items="${mapaDeTickets[s.kanbanStatusKey]}" var="t">
                                              <li id="${t.ticketKey}" class="ticket ui-corner-tr ${t.tipoDeTicket.tipoDeTicketKey eq 3 ? 'bug' : (t.tipoDeTicket.tipoDeTicketKey eq 6 ? 'task' : 'story')}" ondblclick="pronto.kanban.openTicket(${t.ticketKey});" title="${t.titulo}">
                                                  <p><b>#${t.ticketKey}</b><br>${t.tituloResumido}</p>
                                              </li>
                                      </c:forEach>
                                  </ul>
                              </div>
                          </td>
                </c:forEach>
			</tr>
		</table>
		<div align="center">* Clique duas vezes sobre o cartão para abrí-lo.</div>
	</body>
</html>