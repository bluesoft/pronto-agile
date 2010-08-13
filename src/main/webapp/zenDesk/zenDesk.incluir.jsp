 <%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Incluir Ticket do Zen Desk</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="${raiz}zendesk/tickets" method="post" id="formZenDesk">
			<ul class="info">
				<h1>Incluir Ticket do Zen Desk</h1>
			</ul>
			
			<h2>Ticket #${zenDeskTicket.nice_id} do Zen Desk</h2>
			<div class="group">
				<input type="hidden" name="zenDeskTicketKey" value="${zenDeskTicket.nice_id}">
				
				<div>
					<b>${zenDeskTicket.subject}</b>
					<p>Título</p>
				</div>
				
				<div>
					<b>${zenDeskTicket.description}</b>
					<p>Descrição</p>
				</div>
				
				<div>
					<b>${zenDeskCliente.name}</b>
					<p>Cliente</p>
				</div>
				
				<div>
					<b>${zenDeskSolicitador.name}</b>
					<p>Solicitador</p>
				</div>
				
				<div>
					<b>${zenDeskTicket.current_tags}</b>
					<p>Tags</p>
				</div>
				
			</div>
			
			<h2>Dados para Inclusão no Pronto</h2>
				<div class="group">
					<div style="display: block">
						<input type="hidden" id="tipoDeTicketKey" name="tipoDeTicketKey" value="3"/>
						<div align="center" class="person">
							<pronto:icons name="defeito_grande.png" title="Incluir como um novo Defeito" clazz="ativo" onclick="definirComoDefeito();" id="defeito"/>
							<div class="person_name">Defeito</div>
						</div>
						<div align="center" class="person">
							<pronto:icons name="ideia_grande.png" title="Incluir como uma nova Ideia" clazz="inativo" onclick="definirComoIdeia();" id="ideia"/>
							<div class="person_name">Idéia</div>
						</div>
						<div style="clear: both;">Tipo de Ticket</div>
				</div>
			
				<div style="clear: both;">
					<br/>	
					<b>
						<select name="clienteKey" id="clienteKey">
							<c:forEach var="c" items="${clientes}">
								<option ${zenDeskCliente.name eq c.nome ? 'selected="selected"' : ''}   value="${c.clienteKey}">${c.nome}</option>
							</c:forEach>							
						</select>
					</b>
					<p>Cliente</p>
				</div>

			</div>

			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}'">Cancelar</button>
				<button type="submit" onclick="incluir()">Incluir</button><br/>
			</div>			
			
		</form>		
		<script>
			
			function definirComoIdeia(){
				$('#tipoDeTicketKey').val('3');
				$('#ideia').removeClass('inativo').addClass('ativo');;
				$('#defeito').removeClass('ativo').addClass('inativo');;
			}

			function definirComoDefeito() {
				$('#tipoDeTicketKey').val('1');
				$('#defeito').removeClass('inativo').addClass('ativo');;
				$('#ideia').removeClass('ativo').addClass('inativo');;
			}
		</script>
	</body>
</html>