<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>

		<h1>Cadastro de Sprints</h1>
	
		<form action="salvar.action" method="post">
			<div class="group">
					<c:if test="${sprint.sprintKey gt 0}">
						<form:hidden path="sprint.sprintKey"/>
						<div>
							<b>${sprint.sprintKey}</b>
							<p>Código</p>
						</div>
					</c:if>
				<c:if test="${sprint.imagem ne null}">
					<div>
						<img src="imagem.action?sprintKey=${sprint.sprintKey}"/>
						<p>Logo do Sprint</p>
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
		
		<h2>Logo do Sprint</h2>
		<c:if test="${sprint.sprintKey gt 0}">
			<c:choose>
				<c:when test="${sprint.imagem eq null}">
					<h4>Incluir imagem</h4>
				</c:when>
				<c:otherwise>
					<h4>Substituir imagem</h4>
				</c:otherwise>
			</c:choose>
			<form action="upload.action?sprintKey=${sprint.sprintKey}" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<button type="submit">Upload</button>
			</form>
		</c:if>
	</body>
</html>