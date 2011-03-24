<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Configurações</title>
	</head>
	<body>
		<h1>Configurações</h1>
		
		<form action="${raiz}configuracoes/salvar" id="formConfiguracoes" method="POST">
			
			<h4>Geral</h4>
			<div class="group">
			
				<div>
					<div>
						<input type="text" name="pronto.url" class="required url" size="40" value="${mapa['pronto.url']}"/>
						<p>URL do Pronto</p>
					</div>
				
					<select name="tipoDeEstimativa">
						<c:forEach items="${tiposDeEstimativa}" var="tipo">
							<c:choose>
								<c:when test="${tipo.string eq mapa['tipoDeEstimativa']}">
									<option selected="selected" value="${tipo}">${tipo.descricao}</option>
								</c:when>
								<c:otherwise>
									<option value="${tipo}">${tipo.descricao}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<p>Tipo de Estimativa</p>
				</div>
				
				
			</div>

			<h4>Zendesk</h4>			
			<div class="group" id="zendesk">
				<div>
					<select name="zendesk.ativo" id="zendeskAtivo" onchange="toogleZendesk()">
						<option ${mapa['zendesk.ativo'] eq 'true' ? 'selected' : ''}  value="true">Sim</option>
						<option ${mapa['zendesk.ativo'] ne 'true' ? 'selected' : ''} value="false">Não</option>
					</select>
					<p>Integrar com <a href="http://www.zendesk.com">Zendesk</a>?</p>
				</div>
			
				<div id="zendeskOptions">
					<div>
						<input type="text" name="zendesk.url" class="url" size="40" value="${mapa['zendesk.url']}"/>
						<p>URL do Zendesk</p>
					</div>
					<div>
						<input type="text" name="zendesk.username" value="${mapa['zendesk.username']}"/>
						<p>Usuário</p>
					</div>
					<div>
						<input type="password" name="zendesk.password" value="${mapa['zendesk.password']}"/>
						<p>Senha</p>
					</div>
				</div>
			</div>
			
			<h4>Jabber</h4>			
			<div class="group" id="jabber">
				<div>
					<select name="jabber.ativo" id="jabberAtivo" onchange="toogleJabber()">
						<option ${mapa['jabber.ativo'] eq 'true' ? 'selected' : ''}  value="true">Sim</option>
						<option ${mapa['jabber.ativo'] ne 'true' ? 'selected' : ''} value="false">Não</option>
					</select>
					<p>Integrar com <a href="http://pt.wikipedia.org/wiki/Extensible_Messaging_and_Presence_Protocol">Jabber/XMPP</a>?</p>
				</div>
			
				<div id="jabberOptions">
					<div>
						<input type="text" name="jabber.url" size="40" value="${mapa['jabber.url']}"/>
						<p>URL do Jabber</p>
					</div>
					<div>
						<input type="text" name="jabber.username" value="${mapa['jabber.username']}"/>
						<p>Usuário</p>
					</div>
					<div>
						<input type="password" name="jabber.password" value="${mapa['jabber.password']}"/>
						<p>Senha</p>
					</div>
				</div>
			</div>
			
			<div align="center" class="buttons">
				<br />
				<button type="button" onclick="salvar()">Salvar</button>
			</div>
		</form>

	<script>
		$(function(){
			toogleZendesk();
			toogleJabber();
			$('#formConfiguracoes').validate();
		})
	
		function salvar() {
			$("#formConfiguracoes").submit();
		}

		function toogleZendesk() {
			if ($('#zendeskAtivo').val() == 'true') {
				$('#zendeskOptions input').removeAttr('disabled');
				$('#zendeskOptions input').addClass('required');
			} else {
				$('#zendeskOptions input').attr('disabled','disabled').val('');
				$('#zendeskOptions input').removeClass('required');
			}
		}

		function toogleJabber() {
			if ($('#jabberAtivo').val() == 'true') {
				$('#jabberOptions input').removeAttr('disabled');
				$('#jabberOptions input').addClass('required');
			} else {
				$('#jabberOptions input').attr('disabled','disabled').val('');
				$('#jabberOptions input').removeClass('required');
			}
		}
		</script>	
	</body>
</html>
	