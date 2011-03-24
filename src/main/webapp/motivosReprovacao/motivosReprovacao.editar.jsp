<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Motivos de Reprovação</title>
		<script>
			$(function() {
				$('#formMotivoReprovacao').validate();
				$('#descricao').focus();
			});
		</script>
	</head>
	<body>
		<form action="${raiz}motivosReprovacao/" id="formMotivoReprovacao" method="post">

			<ul class="info">
				<h1>Cadastro de Motivos de Reprovações</h1>
			</ul>
			<div class="group">
				<form:hidden path="motivoReprovacao.motivoReprovacaoKey"/>
				<c:if test="${motivoReprovacao.motivoReprovacaoKey gt 0}">
					<div>
						<b>${motivoReprovacao.motivoReprovacaoKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="motivoReprovacao.descricao" id="descricao" cssClass="required" size="35" maxlength="35"/>
					<p>Nome</p>
				</div>
			</div>
				
			<div align="center" class="buttons">
				<br/>
				<button type="button" onclick="window.location.href='${raiz}motivosReprovacao'">Cancelar</button>
				<button type="submit">Salvar</button><br/>
			</div>
		</form>		
	</body>
</html>