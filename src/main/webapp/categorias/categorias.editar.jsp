<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Categorias</title>
		<script>
			$(function() {
				$('#formCategoria').validate();
			});
		</script>
	</head>
	<body>
		<form action="${raiz}categorias/" id="formCategoria" method="post">

			<ul class="info">
				<h1>Cadastro de Categorias</h1>
			</ul>
			<div class="group">
				<form:hidden path="categoria.categoriaKey"/>
				<c:if test="${categoria.categoriaKey gt 0}">
					<div>
						<b>${categoria.categoriaKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="categoria.descricao" cssClass="required"/>
					<p>Nome</p>
				</div>
				
				<div>
					<form:select path="categoria.cor">
						<form:options items="${cores}" itemLabel="descricao"/>
					</form:select>
					<p>Cor</p>
				</div>
				
				<div align="center">
					<button type="button" onclick="window.location.href='${raiz}categorias'">Cancelar</button>
					<button type="submit">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>