<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Projetos</title>
		<script>
			$(function() {
				$('#formProjeto').validate();
			});
		</script>
	</head>
	<body>
		<form action="${raiz}projetos/" id="formProjeto" method="post">

			<ul class="info">
				<h1>Cadastro de Projetos</h1>
			</ul>
			<div class="group">
				<form:hidden path="projeto.projetoKey"/>
				<c:if test="${projeto.projetoKey gt 0}">
					<div>
						<b>${projeto.projetoKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="projeto.nome" cssClass="required" size="40"/>
					<p>Nome</p>
					<input type="hidden" name="paraEvitarProblemaDoSubmitNoEnter" value="Nada"/>
				</div>
			</div>
				
			<div align="center" class="buttons">
				<br />
				<button type="button" onclick="window.location.href='${raiz}projetos'">Cancelar</button>
				<button type="submit">Salvar</button><br/>
			</div>
		</form>		
	</body>
</html>