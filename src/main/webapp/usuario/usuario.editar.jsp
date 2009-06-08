<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Usuários</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
		<script>

			$(function() {
				$('#formUsuario').validate();
			});
		
			function salvar() {
				$('#formUsuario').submit();
			}		
		</script>
	</head>
	<body>
		<form action="salvar.action" method="post" id="formUsuario">
			<ul class="info">
				<h1>Cadastro de Usuários</h1>
			</ul>
			<div class="group">
				<c:choose>
					<c:when test="${usuario.username ne null}">
						<form:hidden path="usuario.username"/>
						<div>
							<b>${usuario.username}</b>
							<p>username</p>
						</div>
					</c:when>
					<c:otherwise>
						<div>
							<form:input path="usuario.username" cssClass="required"/>
							<p>username</p>
						</div>
					</c:otherwise>
				</c:choose>
				<div>
					<form:input path="usuario.nome" cssClass="required"/>
					<p>Nome</p>
				</div>
				<div>
					<form:input path="usuario.email" cssClass="email required"/>
					<p>e-mail</p>
				</div>
				<c:if test="${usuario.username eq null}">
					<div>
						<input type="password" name="password">
						<p>password</p>
					</div>
				</c:if>
				<div>
					<select size="5" multiple="multiple" name="papel" class="required">
						<c:forEach items="${papeis}" var="papel">
							<option value="${papel.papelKey}" ${usuario.mapaPapeis[papel.papelKey] ? 'selected="selected"' : ''}>${papel.descricao}</option>
						</c:forEach>
					</select>
					<p>Papéis</p>
				</div>
				<div align="center">
					<button type="button" onclick="window.location.href='listar.action'">Cancelar</button>
					<button type="button" onclick="salvar()">Salvar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>