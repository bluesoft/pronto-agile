<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Dashboard</title>
		<link rel="stylesheet" type="text/css" media="all" href="dashboard.css" />
 	     <script type='text/javascript'>
	      google.load('visualization', '1', {packages:['gauge']});
	      google.setOnLoadCallback(drawChart);
	      function drawChart() {
	        
	        var options = {
	          width: 800, height: 140, minorTicks: 5
	        };
	
	        var dataDefeitos = google.visualization.arrayToDataTable(${defeitos});
	        var defeitos = new google.visualization.Gauge(document.getElementById('defeitos')).draw(dataDefeitos, options);
	        
	        var dataDefeitosEntregues = google.visualization.arrayToDataTable(${defeitosEntregues});
	        var defeitos = new google.visualization.Gauge(document.getElementById('defeitosEntregues')).draw(dataDefeitosEntregues, options);
	        
	        var dataEstorias = google.visualization.arrayToDataTable(${estorias});
	        var estorias = new google.visualization.Gauge(document.getElementById('estorias')).draw(dataEstorias, options);
	        
	        var dataEstoriasEntregues = google.visualization.arrayToDataTable(${estoriasEntregues});
	        var estoriasEntregues = new google.visualization.Gauge(document.getElementById('estoriasEntregues')).draw(dataEstoriasEntregues, options);
	      }
	    </script>
	</head>
	<body>
			<h1>
				Dashboard
				<%@ include file="/commons/sprintLinks.jsp" %>
			</h1>

			<h3 class="ui-state-default">Defeitos Reportados</h3>
    		<div id='defeitos' align="center"></div>
    		<br/>
    		
    		<h3 class="ui-state-default">Defeitos Resolvidos</h3>
    		<div id='defeitosEntregues' align="center"></div>
    		<br/>

			<h3 class="ui-state-default">Estórias Criadas</h3>
    		<div id='estorias' align="center"></div>
    		<br/>

			<h3 class="ui-state-default">Estórias Entregues</h3>
    		<div id='estoriasEntregues' align="center"></div>
    		<br/>
			
			<h3 class="ui-state-default">Projetos</h3>
			<c:forEach items="${itens}" var="item">
				<div class="projeto ui-widget">
					<h3 class="ui-state-default">${item.projeto}</h3>
					
					<div class="tipos">
						<c:forEach items="${item.quantidadesPorTipoDeTicket}" var="tipo">
							<div class="tipo">
								<span class="tipo-descricao">${tipo.key.value}s</span>
								<span class="tipo-quantidade">
									<a href="${raiz}buscar?projetoKey=${item.projetoKey}&tipoDeTicketKey=${tipo.key.key}&kanbanStatusKey=-1&ignorarLixeira=true">
										${tipo.value}
									</a>
								</span>
							</div>
						</c:forEach>
					</div>
					<hr/>
					
					<div class="tipos">
						<c:forEach items="${item.percentualPorMilestone}" var="milestone">
							<div class="milestone">
								<span class="milestone-percentual">
									<a href="${raiz}buscar?projetoKey=${item.projetoKey}&milestoneKey=${milestone.key.key}&kanbanStatusKey=0&ignorarLixeira=true">
										<div class="progress" percent="${milestone.value}">
											<span class="milestone-descricao">${milestone.key.value}</span>
										</div>
									</a>
								</span>
							</div>
						</c:forEach>
					</div>
					<hr/>
					
					
					<div class="backlogs">
						<c:forEach items="${item.mapaPorBacklogESprintEEtapa}" var="backlog">
							<div class="backlog">
								<div class="backlog-name">
									<c:if test="${backlog.key.key ne 3}">
										<a href="${raiz}backlogs/${backlog.key.key}">
											<h4>${backlog.key.value}</h4>
										</a>
									</c:if>
								</div>
								<div>
									<div class="sprints">
										<c:forEach items="${backlog.value}" var="sprint">
											<c:set var="total" value="${0}" />
											<div class="sprint">
												<c:if test="${sprint.key ne null}">
													<span class="sprint-nome ui-state-default">
													<a href="${raiz}buscar?projetoKey=${item.projetoKey}&sprintKey=${sprint.key.key}&ignorarLixeira=true">
														${sprint.key.value}
													</a>
													</span>
												</c:if>
												<div class="etapas">  
													<c:forEach items="${sprint.value}" var="etapa">
														<div class="etapa">
															<span class="etapa-descricao">${etapa.key.value}</span>
															<span class="etapa-quantidade">
																<a href="${raiz}buscar?projetoKey=${item.projetoKey}&backlogKey=${backlog.key.key}&sprintKey=${sprint.key.key}&kanbanStatusKey=${etapa.key.key}&ignorarLixeira=true">
																	${etapa.value}
																	<c:set var="total" value="${total + etapa.value}" />
																</a>
															</span>
														</div>
													</c:forEach>
													<c:if test="${sprint.key ne null}">
														<div class="etapa-total">
															<span class="etapa-descricao">Total</span>
															<span class="etapa-quantidade">
																<a href="${raiz}backlogs/sprints/${sprint.key.key}">
																	${total}
																</a>
															</span>
														</div>
													</c:if>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
					<hr/>
					<div>
						<h5 style="height: 20px; text-align: center;" class="ui-state-default">Tickets</h5>
						<table class="tabela">
							<tr>
								<th>Cliente</th>
								<th>Pendentes</th>
								<th>Entregues</th>
								<th>Total</th>
							</tr>
							<c:forEach items="${item.pendencias}" var="pendencia">
								<tr>
									<td>${pendencia.nome}</td>
									<td>${pendencia.pendente}</td>
									<td>${pendencia.pronto}</td>
									<td>${pendencia.total}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:forEach>
 		<script type="text/javascript">
			$(function(){
				
				$(".progress").each(function(i, el){
					$el = $(el);
					var percent = parseInt($el.attr('percent'));
					$el.progressbar({
						value: percent
					});
					$el.attr('title','Milestone ' + percent + '% completo');
				});
			});
		</script>
	</body>
</html>