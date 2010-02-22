<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Configurações</title>
	</head>
	<body>
		
		<h1>Configurações</h1>
		
		<form action="${raiz}configuracoes/salvar" id="formConfiguracoes" method="POST">
			
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
			<button type="button" onclick="salvar()">Salvar</button>
			
		</form>
		

	<script>
		function salvar() {
			$("#formConfiguracoes").submit();
		}
	</script>	
	</body>
</html>
	