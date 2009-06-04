<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
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
		<form action="trocarSenha.action" method="post" id="formUsuario">
			<ul class="info">
				<h1>Troca de Senha</h1>
			</ul>
			<div class="group">
				<form:hidden path="usuario.username"/>
				<div>
					<b>${usuario.username}</b>
					<p>username</p>
				</div>
				<div>
					<b>${usuario.nome}</b>
					<p>Nome</p>
				</div>
				<div>
					<input type="password" name="password" class="required">
					<p>password</p>
				</div>
				<div align="center">
					<button type="button" onclick="window.location.href='listar.action'">Cancelar</button>
					<button type="button" onclick="salvar()">Confirmar</button><br/>
				</div>
			</div>
		</form>		
	</body>
</html>