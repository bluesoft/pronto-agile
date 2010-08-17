<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Causas de Defeito</title>
		<script>
			$(function() {
				$('#formCausaDeDefeito').validate();
				$('#descricao').focus();
			});
		</script>
	</head>
	<body>
		<form action="${raiz}causasDeDefeito/" id="formCausaDeDefeito" method="post">

			<ul class="info">
				<h1>Cadastro de Causas de Defeitos</h1>
			</ul>
			<div class="group">
				<form:hidden path="causaDeDefeito.causaDeDefeitoKey"/>
				<c:if test="${causaDeDefeito.causaDeDefeitoKey gt 0}">
					<div>
						<b>${causaDeDefeito.causaDeDefeitoKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="causaDeDefeito.descricao" id="descricao" cssClass="required" size="35" maxlength="35"/>
					<p>Nome</p>
				</div>
				
				<div align="center">
					<button type="button" onclick="window.location.href='${raiz}causasDeDefeito'">Cancelar</button>
					<button type="submit">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>