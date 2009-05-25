<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Login</title>
	</head>
	<body>
		
		<form action="login.login.action">
			<ul class="info"><h1>Ticket #${ticket.ticketKey}</h1></ul>
			<div class="group">
				<div>
					<input type="text" name="username" id="username" value="${username}">
					<p>Username</p>
				</div>
				<div>
					<input type="password" name="password" id="password">
					<p>Password</p>
				</div>
			</div>
		</form>	
		
		<div align="center">
			<a href="editar.action">Novo</a>
		</div>
	</body>
</html>