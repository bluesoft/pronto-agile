<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Clientes</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
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
		<form action="salvar.action" method="post" id="formCliente">
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
				
				<div align="center">
					<button type="button" onclick="window.location.href='listar.action'">Cancelar</button>
					<button type="button" onclick="salvar()">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>

