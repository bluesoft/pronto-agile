<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Dashboard</title>
		<style type="text/css">
			.projeto {
				width: 30%;
				margin-left: 20px;
				margin-right: 20px;
				padding: 5px;
				float: left;
				border: 1px solid #e0e0e0;
				background-color: #f5f5f5;
			}
			
			.backlog {
				clear: both;
			}
			
			.backlog h4 {
				font-weight: bold;
			}
			
			h3 {
				text-align: center;
				padding: 5px;
			}
			
			.sprint-nome {
				font-weight: bold;
				display: block;
				padding: 3px;
				margin-bottom: 4px;
				margin-top: 4px;
			}

			.sprint {
				margin-top: 10px;
			}

			.tipo {
				padding: 3px;
			}

			.tipos {
				margin-bottom: 10px;
			}
						
			.etapa, .etapa-total {
				margin-left: 25px;
				display: block;
				
			}
			
			.etapa-total {
				font-weight: bold;
			}
			
			.etapa-nome, .tipo-nome {
				margin-left: 200px;
				float: left;
			}
			
			.etapa-quantidade, .tipo-quantidade {
				float: right;
				display: block;
			}
			
			.backlog-name {
				float: left;
			}
			
			.milestone-descricao {
				font-size: 12px;
				font-weight: bold;
				float: right;
				margin: 5px;
			}
		</style>
	</head>
	<body>
			<h1>
				Dashboard
				<%@ include file="/commons/sprintLinks.jsp" %>
			</h1>

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