<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Clientes</title>
		<script>
			$(function() {
				$('#formCliente').validate();
			});
		
			function salvar() {
				$('#formCliente').submit();
			}
		</script>
	</head>
	<body>
		<form action="${raiz}clientes" method="POST" id="formCliente">
			<ul class="info">
				<h1>Cadastro de Clientes</h1>
			</ul>
			<div class="group">
				<form:hidden path="cliente.clienteKey"/>
				<c:if test="${cliente.clienteKey gt 0}">
					<div>
						<b>${cliente.clienteKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="cliente.nome" cssClass="required"/>
					<p>Nome</p>
				</div>
			</div>
				
			<div align="center" class="buttons">
				<br/>
				<button type="button" onclick="window.location.href='${raiz}clientes'">Cancelar</button>
				<button type="button" onclick="salvar()">Salvar</button><br/>
			</div>
		</form>		
	</body>
</html>

