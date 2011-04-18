<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Sprints</title>
	</head>
	<body>

		<h1>Cadastro de Sprints</h1>
	
		<form action="${raiz}sprints" method="post" id="formSprint">
			<div class="group">
					<c:if test="${sprint.sprintKey gt 0}">
						<form:hidden path="sprint.sprintKey"/>
						<div>
							<b>${sprint.sprintKey}</b>
							<p>Código</p>
						</div>
						<div>
							<img src="${raiz}sprints/${sprint.sprintKey}/imagem"/>
							<p>Logo do Sprint</p>
						</div>
					</c:if>
				
				<div>
					<form:input path="sprint.nome" cssClass="required" />
					<p>Nome</p>
				</div>
				
				<div>
					<select name="projetoKey"  id="projetoKey">
						<c:forEach var="m" items="${projetos}">
							<option value="${m.projetoKey}" ${sprint.projeto.projetoKey eq m.projetoKey ? 'selected' : ''}>${m.nome}</option>
						</c:forEach>
					</select>
					<p>Projeto</p>
				</div>
				
				<div>
					<form:input path="sprint.meta" maxlength="255" size="80"/>
					<p>Meta</p>
				</div>
				
				<div>
					<fmt:formatDate var="dataInicial" value="${sprint.dataInicial}"/>
					<input type="text" name="dataInicial" class="required dateBr" value="${dataInicial}" size="12"/>
					<p>Data Inicial</p>
				</div>
				<div>
					<fmt:formatDate var="dataFinal" value="${sprint.dataFinal}"/>
					<input type="text" name="dataFinal" class="required dateBr" value="${dataFinal}" size="12"/>
					<p>Data Final</p>
				</div>
				<div>
					<b>${sprint.atual ? 'Sim' : 'Não'}</b>
					<p>Sprint Atual?</p>
				</div>
				<div>
					<form:hidden path="sprint.atual"/>
					<b>${sprint.fechado ? 'Sim' : 'Não'}</b>
				</div>
				<div>
					<form:hidden path="sprint.fechado"/>
					<p>Fechado?</p>
				</div>
				
			</div>

			<div align="center" class="buttons">
				<br/>
				<button type="button" onclick="window.location.href='${raiz}sprints'">Cancelar</button>
				<button type="button" onclick="salvar()">Salvar</button><br/>
			</div>
		</form>		

		<c:if test="${sprint.sprintKey gt 0}">
			<br /><br /><br />
			<h2>Logo do Sprint</h2>
			<h4>Definir imagem</h4>
			<form action="${raiz}sprints/${sprint.sprintKey}/upload" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<div class="buttons">
					<br />
					<button type="submit">Upload</button>
				</div>
			</form>
		</c:if>
		
		<script>
			$(function(){
				$('#formSprint').validate();
				$('.dateBr').datepicker();
			});

			function salvar() {
				if ($('#formSprint').validate().form()) {
					$('#formSprint').submit();
				}
			}

			
		</script>
	</body>
</html>