 <%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Incluir Ticket do Zendesk</title>
	</head>
	<body>
		<form action="${raiz}zendesk/tickets" method="post" id="formZendesk">
			<ul class="info">
				<h1>Incluir Ticket #${zendeskTicket.nice_id} do Zendesk</h1>
			</ul>
			
			<h2>Título: ${zendeskTicket.subject}</h2>
			<div class="group">
				<input type="hidden" name="zendeskTicketKey" value="${zendeskTicket.nice_id}">
				<div>
					<p>
						<b>Cliente:</b> ${zendeskCliente.name}<br/><br/>
						<b>Solicitador:</b> ${zendeskSolicitador.name}<br/><br/>
						<b>Descrição:</b>
					</p>
					
				</div>
				${zendeskTicket.html}
			</div>
			
			<br/>
			
			<h2>Dados para inclusão no Pronto</h2>
				<div class="group">
					<div style="display: block">
						<input type="hidden" id="tipoDeTicketKey" name="tipoDeTicketKey" value="3"/>
						<div style="clear: both;">Tipo de Ticket:<br/><br /></div>
						<div align="center" class="person">
							<pronto:icons name="defeito_grande.png" title="Incluir como um novo Defeito" clazz="ativo" onclick="definirComoDefeito();" id="defeito"/>
							<div class="person_name">Defeito</div>
						</div>
						<div align="center" class="person">
							<pronto:icons name="estoria_grande.png" title="Incluir como uma nova Estória" clazz="inativo" onclick="definirComoEstoria();" id="estoria"/>
							<div class="person_name">Estória</div>
						</div>
				</div>
			
				<div style="clear: both;">
					<br/>	
					Cliente:<br/><br/>
					<select name="clienteKey" id="clienteKey">
						<c:forEach var="c" items="${clientes}">
							<option ${zendeskCliente.name eq c.nome ? 'selected="selected"' : ''}   value="${c.clienteKey}">${c.nome}</option>
						</c:forEach>							
					</select>
				</div>
				
				<div style="clear: both;">
					<br/>	
					Projeto:<br/><br/>
					<select name="projetoKey" id="projetoKey">
						<c:forEach var="p" items="${projetos}">
							<option value="${p.projetoKey}">${p.nome}</option>
						</c:forEach>							
					</select>
				</div>
			</div>

			<div align="center" class="buttons">
				<br/>
				<button type="submit" onclick="incluir()">Incluir ticket</button>
			</div>			
			
		</form>		
		<script>
			
			function definirComoEstoria(){
				$('#tipoDeTicketKey').val('2');
				$('#estoria').removeClass('inativo').addClass('ativo');;
				$('#defeito').removeClass('ativo').addClass('inativo');;
			}

			function definirComoDefeito() {
				$('#tipoDeTicketKey').val('3');
				$('#defeito').removeClass('inativo').addClass('ativo');;
				$('#estoria').removeClass('ativo').addClass('inativo');;
			}
		</script>
	</body>
</html>