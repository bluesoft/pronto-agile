<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Login</title>
		<script>
			$(function() {
				$('#username').focus();
			});		
		</script>
		<style>
			input {
				font-size: 14px;
			}
		</style>
	</head>
	<body>
	
		<c:url var="loginUrl" value="/login.action"/>
		<form action="${loginUrl}" method="post">
			<h1>Login</h1>
			
			<h3>${mensagem}</h3>
			<div class="group">
				<div>
					<input type="text" name="username" id="username" value="${username}">
					<p>Username</p>
				</div>
				<div>
					<input type="password" name="password" id="password">
					<p>Senha</p>
				</div>
			</div>
			<div align="center">
				<button type="submit">Login</button>
			</div>
		</form>	
		
		
	</body>
</html>