<%@ include file="/commons/taglibs.jsp"%>
<c:url var="buscarUrl" value="/buscar/"/>
<html>
	<head>
		<title>Busca</title>
		<script>

		$(function(){ 
			$("#dialog").dialog({ 
				autoOpen: false, 
				height: $(document).height() - 50, 
				width: $(document).width() - 50, 
				modal: true });
		});

		function verDescricao(ticketKey) {
			$.ajax({
				url: '${raiz}tickets/'+ ticketKey + '/descricao',
				cache: false,
				success: function (data) {
					$("#dialog").dialog('option', 'title', '#' + ticketKey + ' - ' + $('#' + ticketKey + ' .titulo').text());
					$("#dialogDescricao").html(data);
					$("#dialog").dialog('open');
				}
			});
		}

		function recarregar() {
			var parametros = $('#formBuscaAvancada').serializeArray();
			pronto.doPost('${buscarUrl}', parametros);
		}

		$(function(){
			$('#query').keypress(function(e) {
				 if (e.keyCode == 13) {
					 recarregar();
					 return false;
				 }
			});
			$('.dateBr').datepicker();
		});

		</script>
		<style type="text/css">
			.linha {
				clear: both;
			}
			.opcao {
				float: left;
				text-align: left;
				padding: 5px;
			}
			.divFormBusca {
				width: 80%;
				margin: auto;
				padding: 5px;
				margin-bottom: 20px;
				background-color: #f0f0f0;
			}
			.iconeBusca {
				margin-left: 10px;
				float: right;
			}
		</style>
	</head>
	<body>
		<h1>Resultado da Busca</h1>
		
		<div align="center" class="divFormBusca">
		<form id="formBuscaAvancada">
			
			<div class="linha">
				<div class="opcao">
					Backlog:<br/>
					<select name="backlogKey"  id="backlogKey">
						<option value="0" ${filtro.backlogKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${backlogs}">
							<option value="${m.backlogKey}" ${filtro.backlogKey eq m.backlogKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Projeto:<br/>
					<select name="projetoKey"  id="projetoKey">
						<option value="0" ${filtro.projetoKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${projetos}">
							<option value="${m.projetoKey}" ${filtro.projetoKey eq m.projetoKey ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Sprint:<br/>
					<input id="sprintNome" type="text" name="sprintNome" value="${filtro.sprintNome}"/>
				</div>
				<div class="opcao">
					Tipo de Ticket:<br/>
					<select name="tipoDeTicketKey"  id="tipoDeTicketKey">
						<option value="0" ${filtro.tipoDeTicketKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${tiposDeTicket}">
							<option value="${m.tipoDeTicketKey}" ${filtro.tipoDeTicketKey eq m.tipoDeTicketKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Status:<br/>
					
					<select name="kanbanStatusKey"  id="kanbanStatusKey">
						<option value="0" ${filtro.kanbanStatusKey eq 0 ? 'selected' : ''}>Todos</option>
						<option value="-1" ${filtro.kanbanStatusKey eq -1 ? 'selected' : ''}>Pendentes</option>
						<c:forEach items="${projetos}" var="projeto">
							<optgroup label="${projeto.nome}"></optgroup>
							<c:forEach items="${projeto.etapasDoKanban}" var="item">
								<option value="${item.kanbanStatusKey}" ${filtro.kanbanStatusKey eq item.kanbanStatusKey ? 'selected' : ''}>${item.descricao}</option>
							</c:forEach>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Cliente: <br/>
					<select name="clienteKey"  id="clienteKey">
						<option value="-1">Todos</option>
						<c:forEach var="c" items="${clientes}">
							<option value="${c.clienteKey}" ${filtro.clienteKey eq c.clienteKey ? 'selected' : ''}>${c.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="iconeBusca">
					<pronto:icons name="buscar_grande.png" title="Refinar Busca" onclick="recarregar();"/>
				</div>
			</div>
			<div class="linha">
				<div class="opcao">
					Título:<br/>
					<input id="query" type="text" name="query" value="${filtro.query}"/>
				</div>
				<div class="opcao">
					Módulo:<br/>
					<select name="moduloKey"  id="moduloKey">
						<option value="0" ${filtro.moduloKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${modulos}">
							<option value="${m.moduloKey}" ${filtro.moduloKey eq m.moduloKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Milestone:<br/>
					<select name="milestoneKey"  id="milestoneKey">
						<option value="0" ${filtro.milestoneKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${milestones}">
							<option value="${m.milestoneKey}" ${filtro.milestoneKey eq m.milestoneKey ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Categoria:<br/>
					<select name="categoriaKey"  id="categoriaKey">
						<option value="0" ${filtro.categoriaKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${categorias}">
							<option value="${m.categoriaKey}" ${filtro.categoriaKey eq m.categoriaKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Reporter:<br/>
					<select name="reporter"  id="reporter">
						<option value="" ${filtro.reporter eq null ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${usuarios}">
							<option value="${m.username}" ${filtro.reporter eq m.username ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Responsável:<br/>
					<select name="responsavel"  id="responsavel">
						<option value="" ${filtro.responsavel eq null ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${usuarios}">
							<option value="${m.username}" ${filtro.responsavel eq m.username ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					<span title="Excluir Tickets que estão na Lixeira do Resultado da Busca?">Lixeira:</span> <br/>
					<select name="ignorarLixeira"  id="ignorarLixeira">
						<option value="true" ${filtro.ignorarLixeira eq true ? 'selected' : ''}>Ignorar</option>
						<option value="false" ${filtro.ignorarLixeira eq false ? 'selected' : ''}>Considerar</option>
					</select>
				</div>
			</div>
			<div class="linha">
				<div class="opcao">
					Período de Inclusão:<br/>
					<fmt:formatDate var="dataInicialCriacao" value="${filtro.dataInicialCriacao}"/>
					<input type="text" name="dataInicialCriacao" class="dateBr" value="${dataInicialCriacao}" size="12"/>
					<fmt:formatDate var="dataFinalCriacao" value="${filtro.dataFinalCriacao}"/>
					<input type="text" name="dataFinalCriacao" class="dateBr" value="${dataFinalCriacao}" size="12"/>
				</div>
				<div class="opcao">					
					Período de Pronto:<br/>
					<fmt:formatDate var="dataInicialPronto" value="${filtro.dataInicialPronto}"/>
					<input type="text" name="dataInicialPronto" class="dateBr" value="${dataInicialPronto}" size="12"/>
					<fmt:formatDate var="dataFinalPronto" value="${filtro.dataFinalPronto}"/>
					<input type="text" name="dataFinalPronto" class="dateBr" value="${dataFinalPronto}" size="12"/>
				</div>	
				<div class="opcao">
					Ordem: <br/>
					<select name="ordem" id="ordem">
						<c:forEach var="o" items="${ordens}">
							<option value="${o}" ${o eq filtro.ordem ? 'selected' : ''}>${o.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Classificação: <br/>
					<select name="classificacao"  id="classificacao">
						<c:forEach var="c" items="${classificacoes}">
							<option value="${c}" ${c eq filtro.classificacao ? 'selected' : ''}>${c.descricao}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<br/>
			<br/>
			<br/>
		</form>
		</div>
		
		
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Categoria</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Criação</th>
				<th>Backlog</th>
				<th>Milestone</th>
				<th>Reporter</th>
				<th title="Valor de Negócio">VN</th>
				<th>Esforço</th>
				<th>Status</th>
				<th title="Data de Pronto">Pronto</th>
				<th>Dias</th>
			</tr>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:set var="cor" value="${!cor}"/>
				<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
					<td><a href="${raiz}tickets/${t.ticketKey}">${t.ticketKey}</a></td>
					<td>
						<span class="categoria categoria-${t.categoria.cor}">
							${t.categoria.descricao}
						</span>
					</td>
					<td class="titulo" title="${t.titulo}">
						<a onclick="verDescricao(${t.ticketKey});" class="link">
							${t.tituloResumido}
						</a>
					</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td><fmt:formatDate value="${t.dataDeCriacao}" dateStyle="short"/></td>
					<td>
						<c:choose>
							<c:when test="${t.sprint ne null}">
								Sprint ${t.sprint.nome}
							</c:when>
							<c:otherwise>
								${t.backlog.descricao}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${t.milestone.nome}</td>
					<td title="${t.reporter.nome}">${t.reporter.username}</td>
					<td>${t.valorDeNegocio}</td>
					<td>${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>	
					<td><fmt:formatDate value="${t.dataDePronto}" dateStyle="short"/></td>
					<td>${t.tempoDeVidaEmDias}</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="14"><i>* ${fn:length(tickets)} resultado(s) encontrado(s)</i></th>
			</tr>
		</table>	
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>