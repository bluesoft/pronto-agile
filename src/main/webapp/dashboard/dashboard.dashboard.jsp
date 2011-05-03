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
			
			.backlog h4 {
				font-weight: bold;
				clear: both;
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
			
			.etapa {
				margin-left: 25px;
				display: block;
				clear: both;
			}
			
			.etapa-nome {
				margin-left: 200px;
				float: left;
			}
			
			.etapa-quantidade {
				float: right;
				display: block;
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
					<div class="backlogs">
						<c:forEach items="${item.mapaPorBacklogESprintEEtapa}" var="backlog">
							<div class="backlog">
								<h4>${backlog.key.value}</h4>
								<div>
									<div class="sprints">
										<c:forEach items="${backlog.value}" var="sprint">
											<div class="sprint">
												<c:if test="${sprint.key ne null}">
													<span class="sprint-nome ui-state-default">
													<a href="${raiz}buscar?projetoKey=${item.projetoKey}&sprintKey=${sprint.key.key}">
														${sprint.key.value}
													</a>
													</span>
												</c:if>
												<div class="etapas">  
													<c:forEach items="${sprint.value}" var="etapa">
														<div class="etapa">
															<span class="etapa-descricao">${etapa.key.value}</span>
															<span class="etapa-quantidade">
																<a href="${raiz}buscar?projetoKey=${item.projetoKey}&backlogKey=${backlog.key.key}&sprintKey=${sprint.key.key}&kanbanStatusKey=${etapa.key.key}">
																	${etapa.value}
																</a>
															</span>
														</div>
													</c:forEach>
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

	</body>
</html>