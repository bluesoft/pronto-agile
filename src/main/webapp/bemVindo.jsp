<%@ include file="/commons/taglibs.jsp"%>
<c:url var="urlSprint" value="/sprint/" />
<html>
	<head>
		<title>Bem-vindo!</title>
	</head>
	<body>
		<h1>Bem-vindo ao Pronto!</h1>
		<br/>
		<h3>Comece <a href="${urlSprint}editar.action">criando um sprint</a> ou definindo um dos sprints como o <a href="${urlSprint}listar.action">sprint atual</a>.</h3>
	</body>
</html>