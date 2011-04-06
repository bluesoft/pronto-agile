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
			}
			.iconeBusca {
				margin-left: 10px;
			}
		</style>
	</head>
	<body>
		<h1>Resultado da Busca</h1>
		
		<div align="center" class="divFormBusca">
		<form id="formBuscaAvancada">
			
			<div class="linha">
				<div class="opcao">
					Título:<br/>
					<input id="query" type="text" name="query" value="${filtro.query}"/>
				</div>
				<div class="opcao">
					Backlog:<br/>
					<select name="backlogKey" onchange="recarregar()" id="backlogKey">
						<option value="0" ${filtro.backlogKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${backlogs}">
							<option value="${m.backlogKey}" ${filtro.backlogKey eq m.backlogKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Sprint:<br/>
					<input id="sprintNome" type="text" name="sprintNome" value="${filtro.sprintNome}"/>
				</div>
				<div class="opcao">
					Status:<br/>
					<select name="kanbanStatusKey" onchange="recarregar()" id="kanbanStatusKey">
						<option value="0" ${filtro.kanbanStatusKey eq 0 ? 'selected' : ''}>Todos</option>
						<option value="-1" ${filtro.kanbanStatusKey eq -1 ? 'selected' : ''}>Pendentes</option>
						<c:forEach var="k" items="${kanbanStatus}">
							<option value="${k.kanbanStatusKey}" ${filtro.kanbanStatusKey eq k.kanbanStatusKey ? 'selected' : ''}>${k.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Cliente: <br/>
					<select name="clienteKey" onchange="recarregar()" id="clienteKey">
						<option value="-1">Todos</option>
						<c:forEach var="c" items="${clientes}">
							<option value="${c.clienteKey}" ${clienteKey eq c.clienteKey ? 'selected' : ''}>${c.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="iconeBusca opcao">
					<pronto:icons name="buscar_grande.png" title="Refinar Busca" onclick="recarregar();"/>
				</div>
			</div>
			<div class="linha">
				<div class="opcao">
					Módulo:<br/>
					<select name="moduloKey" onchange="recarregar()" id="moduloKey">
						<option value="0" ${filtro.moduloKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${modulos}">
							<option value="${m.moduloKey}" ${filtro.moduloKey eq m.moduloKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Categoria:<br/>
					<select name="categoriaKey" onchange="recarregar()" id="categoriaKey">
						<option value="0" ${filtro.categoriaKey eq 0 ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${categorias}">
							<option value="${m.categoriaKey}" ${filtro.categoriaKey eq m.categoriaKey ? 'selected' : ''}>${m.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Reporter:<br/>
					<select name="reporter" onchange="recarregar()" id="reporter">
						<option value="" ${filtro.reporter eq null ? 'selected' : ''}>Todos</option>
						<c:forEach var="m" items="${usuarios}">
							<option value="${m.username}" ${filtro.reporter eq m.username ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Exibir tickets da lixeira: <br/>
					<select name="ignorarLixeira" onchange="recarregar()" id="ignorarLixeira">
						<option value="false" ${filtro.ignorarLixeira eq false ? 'selected' : ''}>Sim</option>
						<option value="true" ${filtro.ignorarLixeira eq true ? 'selected' : ''}>Não</option>
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
					<select name="ordem" onchange="recarregar()" id="ordem">
						<c:forEach var="o" items="${ordens}">
							<option value="${o}" ${o eq filtro.ordem ? 'selected' : ''}>${o.descricao}</option>
						</c:forEach>
					</select>
				</div>
				<div class="opcao">
					Classificação: <br/>
					<select name="classificacao" onchange="recarregar()" id="classificacao">
						<c:forEach var="c" items="${classificacoes}">
							<option value="${c}" ${c eq filtro.classificacao ? 'selected' : ''}>${c.descricao}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</form>
		</div>
		
		
		<table style="width: 100%">
			<tr>
				<th>#</th>
				<th>Título</th>
				<th>Tipo</th>
				<th>Cliente</th>
				<th>Backlog/Sprint</th>
				<th title="Valor de Negócio">VN</th>
				<th>Esforço</th>
				<th>Status</th>
				<th colspan="2">&nbsp;</th>
			</tr>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:set var="cor" value="${!cor}"/>
				<tr id="${t.ticketKey}" class="${cor ? 'odd' : 'even'}">
					<td><a href="${raiz}tickets/${t.ticketKey}">${t.ticketKey}</a></td>
					<td class="titulo">${t.titulo}</td>
					<td>${t.tipoDeTicket.descricao}</td>
					<td>${t.cliente}</td>
					<td>${t.backlog.descricao} <c:if test="${t.sprint ne null}">(${t.sprint.nome})</c:if></td>
					<td>${t.valorDeNegocio}</td>
					<td>${t.esforco}</td>
					<td>${t.kanbanStatus.descricao}</td>
					<td>
						<pronto:icons name="ver_descricao.png" title="Ver Descrição" onclick="verDescricao(${t.ticketKey});"/>
					</td>
					<td>
						<a href="${raiz}tickets/${t.ticketKey}"><pronto:icons name="editar.png" title="Editar" /></a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="10"><i>* ${fn:length(tickets)} resultado(s) encontrado(s)</i></th>
			</tr>
		</table>	
		<div title="Descrição" id="dialog" style="display: none; width: 500px;">
			<div align="left" id="dialogDescricao">Aguarde...</div>
		</div>
	</body>
</html>