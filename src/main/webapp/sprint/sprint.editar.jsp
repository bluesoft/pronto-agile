<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<form action="salvar.action" method="post">
			<ul class="info">
				<h1>Cadastro de Sprints</h1>
			</ul>
			<div class="group">
					<c:if test="${sprint.sprintKey gt 0}">
						<form:hidden path="sprint.sprintKey"/>
						<div>
							<b>${sprint.sprintKey}</b>
							<p>Código</p>
						</div>
					</c:if>
				<div>
					<form:input path="sprint.nome"/>
					<p>Nome</p>
				</div>
				<div>
					<form:input path="sprint.dataInicial"/>
					<p>Data Inicial</p>
				</div>
				<div>
					<form:input path="sprint.dataFinal"/>
					<p>Data Final</p>
				</div>
				<div align="center">
					<button type="button" onclick="window.location.href='listar.action'">Cancelar</button>
					<button type="submit">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>