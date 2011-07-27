<%@ include file="/commons/taglibs.jsp"%>

<html>
	<head>
		<title>Priorizar ${backlog.descricao}${sprint.nome}</title>
		<script type="text/javascript" src="${raiz}ticket/ticket.priorizar.js"  charset="UTF-8"></script>
		<style type="text/css">
			.ticketsTable tr {
				height: 22px;
				font-size: 12px;
				background-color: white;
			}
			
			
			.ticketsTable th, .ticketsTable td {
				padding-left: 10px;
				padding-right: 10px;
			}
			.ticketsTable td {
				border-style: solid;
				border-color: #dddddd;
				border-bottom-width: 1px;
				border-top-width: 0px;
				border-left-width: 0px;
				border-right-width: 0px;
			}
			
			h4 {
				text-align: right;
			}
		</style>
	</head>
	<body>
	
		<h1>
			Priorizar
			<c:choose>
				<c:when test="${backlog ne null}">
					<a href="${raiz}backlogs/${backlog.backlogKey}">${backlog.descricao}</a>
					<a href="${raiz}backlogs/${backlog.backlogKey}/estimar"><pronto:icons name="estimar.png" title="Estimar Backlog" /></a>  
				</c:when>
				<c:otherwise>
					<a href="${raiz}backlogs/sprints/${sprint.sprintKey}">Sprint ${sprint.nome}</a>
				</c:otherwise>
			</c:choose>
			<pronto:icons name="adicionar.png" title="Adicionar Grupo de Valor e Negócio [Shift+A]" onclick="exibirDialogDeCriarGrupo();"/>
		</h1>

		<div id="errorBox"></div><br/>
		<form name="form" id="form">
			
			<c:forEach items="${mapa}" var="entry">

					<h4 class="valor valor-${entry.key}" valor="${entry.key}">Valor de negócio: ${entry.key}</h4>			
					<table style="width: 100%" class="ticketsTable" valor="${entry.key}">
							<thead>
								<tr class="header">
									<th style="width: 30px;"></th>
									<th style="width: 40px">#</th>
									<th style="width: 100%">Título</th>
									<th style="width: 100px">Tipo</th>
									<th style="width: 100px">Cliente</th>
									<th style="width: 50px">Esforço</th>
									<th style="width: 120px">Status</th>
									<th title="Tempo de Vida em Dias">Dias</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${entry.value}" var="t">
									<tr id="${t.ticketKey}" class="tr-ticket" ticketKey="${t.ticketKey}">
										<td>
											<c:if test="${t.categoria ne null}">
												<span class="categoria categoria-${t.categoria.descricaoDaCor}">${t.categoria.descricao}</span>
											</c:if>
										</td>
										<td>
											<a href="${raiz}tickets/${t.ticketKey}">${t.ticketKey}</a>
											<input type="hidden" name="ticketKey" value="${t.ticketKey}"/>
										</td>
										<td>
											<span class="titulo">${t.titulo}</span>
											<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
										</td>
										<td>
											${t.tipoDeTicket.descricao}
										</td>
										<td>${t.cliente}</td>
										<td class="esforco">
											<span>${t.esforco}</span>
										</td>
										<td nowrap="nowrap">${t.kanbanStatus.descricao}</td>
										<td>${t.tempoDeVidaEmDias}</td>
									</tr>
								</c:forEach>
							</tbody>
					</table>
					<br/>
			</c:forEach>	
			
		
			<div align="center" class="buttons" id="buttons">
				<c:choose>
					<c:when test="${backlog ne null}">
						<button type="button" onclick="goTo('${raiz}backlogs/${backlog.backlogKey}')">Voltar</button>  
					</c:when>
					<c:otherwise>
						<button type="button" onclick="goTo('${raiz}backlogs/sprints/${sprint.sprintKey}')">Voltar</button>
					</c:otherwise>
				</c:choose>
			</div>
		</form>
		
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
		
		<div id="dialogValor" title="Novo Grupo de Valor de Negócio" style="display: none; width: 500px;">
				<p>Informe o valor de Negócio:</p>
				<input type="text" value="100" />
		</div>
		
		<div style="display: none;" id="modelo">
			<h4 class="valor-${entry.key}">${entry.key}</h4>			
			<table style="width: 100%" class="ticketsTable" valor="">
					<thead>
						<tr class="header">
							<th style="width: 30px;"></th>
							<th style="width: 40px">#</th>
							<th style="width: 100%">Título</th>
							<th style="width: 100px">Tipo</th>
							<th style="width: 100px">Cliente</th>
							<th style="width: 50px">Esforço</th>
							<th style="width: 120px">Status</th>
							<th title="Tempo de Vida em Dias">Dias</th>
						</tr>
					</thead>
					<tbody>
						<tr class="vazio"><td colspan="8">&nbsp;</td></tr>
					</tbody>
			</table>
			<br/>
		</div>
		
		<script>
			var backlogKey = '${backlog.backlogKey}';
			var sprintKey = '${sprint.sprintKey}';
		</script>
		
	</body>
</html>