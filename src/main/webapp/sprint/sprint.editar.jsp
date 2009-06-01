<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/commons/scripts/scripts.jsp" %>
		<c:url var="urlSprint" value="/sprint/"/>
		
		<script>
			$(function(){
				$('#formSprint').validate();
			});

			function salvar() {
				if ($('#formSprint').validate().form()) {
					$('#formSprint').submit();
				}
			}
		</script>
		
	</head>
	<body>

		<h1>Cadastro de Sprints</h1>
	
		<form action="salvar.action" method="post" id="formSprint">
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
						<img src="${urlSprint}imagem.action?sprintKey=${sprint.sprintKey}"/>
						<p>Logo do Sprint</p>
					</div>
				</c:if>
				<div>
					<form:input path="sprint.nome" cssClass="required"/>
					<p>Nome</p>
				</div>
				<div>
					<form:input path="sprint.dataInicial" cssClass="required dateBr"/>
					<p>Data Inicial</p>
				</div>
				<div>
					<form:input path="sprint.dataFinal" cssClass="required dateBr"/>
					<p>Data Final</p>
				</div>
				<div>
					<b>${sprint.atual ? 'Sim' : 'Não'}</b>
					<p>Sprint Atual?</p>
				</div>
				<div>
					<c:choose>
						<c:when test="${sprint.atual}">
							<b>${sprint.fechado ? 'Sim' : 'Não'}</b>						
						</c:when>
						<c:otherwise>
							<form:select path="sprint.fechado"  cssClass="required">
								<form:option value="true">Não</form:option>
								<form:option value="true">Sim</form:option>
							</form:select>
						</c:otherwise>
					</c:choose>
					<p>Fechado?</p>
				</div>
				
				<div align="center">
					<button type="button" onclick="window.location.href='${urlSprint}listar.action'">Cancelar</button>
					<button type="button" onclick="salvar()">Salvar</button><br/>
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
			<form action="${urlSprint}upload.action?sprintKey=${sprint.sprintKey}" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<button type="submit">Upload</button>
			</form>
		</c:if>
	</body>
</html>