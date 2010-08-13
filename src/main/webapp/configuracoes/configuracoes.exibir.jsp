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

			<h4>ZenDesk</h4>			
			<div class="group" id="zenDesk">
				<div>
					<select name="zenDesk.ativo" id="zenDeskAtivo" onchange="toogleZenDesk()">
						<option ${mapa['zenDesk.ativo'] eq 'true' ? 'selected' : ''}  value="true">Sim</option>
						<option ${mapa['zenDesk.ativo'] ne 'true' ? 'selected' : ''} value="false">Não</option>
					</select>
					<p>Integrar com <a href="http://www.zendesk.com">ZenDesk</a>?</p>
				</div>
			
				<div id="zenDeskOptions">
					<div>
						<input type="text" name="zenDesk.url" class="url" size="40" value="${mapa['zenDesk.url']}"/>
						<p>URL do ZenDesk</p>
					</div>
					
					<div>
						<input type="text" name="zenDesk.username" value="${mapa['zenDesk.username']}"/>
						<p>User Name</p>
					</div>
					
					<div>
						<input type="password" name="zenDesk.password" value="${mapa['zenDesk.password']}"/>
						<p>Senha</p>
					</div>
					
				</div>
				
			</div>
			
			<button type="button" onclick="salvar()">Salvar</button>
			
		</form>
		

	<script>
		$(function(){
			toogleZenDesk();
		})
	
		function salvar() {
			$("#formConfiguracoes").submit();
		}
		function toogleZenDesk() {
			if ($('#zenDeskAtivo').val() == 'true') {
				$('#zenDeskOptions input').removeAttr('disabled');
				$('#zenDeskOptions input').removeClass('required');
			} else {
				$('#zenDeskOptions input').attr('disabled','disabled').val('');
				$('#zenDeskOptions input').addClass('required');
			}
		}
		</script>	
	</body>
</html>
	