<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Scripts</title>
		<script type="text/javascript">

			function voltar() {
				goTo('${raiz}execucoes');
			}

		</script>
	</head>
	<body>
		<h1>Execuções</h1>
		
		<textarea id="script" class="mono">${script}</textarea>
		<br/>
		
		
		<form action="${raiz}execucoes" method="POST">
			<input type="hidden" name="bancoDeDadosKey" value="${bancoDeDadosKey}"/>

			<c:forEach items="${execucaoKey}" var="key">
				<input type="hidden" name="execucaoKey" value="${key}"/>
			</c:forEach>		

			<div align="center">
				<button type="button" onclick="voltar()">Voltar</button>
				<button type="submit">Confirmar</button>
			</div>
		</form>
		
	</body>
</html>