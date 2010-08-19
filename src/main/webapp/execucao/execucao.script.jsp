<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Scripts</title>
	</head>
	<body>
		<h1>Execuções</h1>
		<textarea id="script" class="mono"></textarea>

		<br/>
		<form id="formExecucoes" action="${raiz}execucoes" method="POST">
			
			<input type="hidden" name="bancoDeDadosKey" value="${bancoDeDadosKey}"/>

			<c:forEach items="${execucaoKey}" var="key">
				<input type="hidden" name="execucaoKey" value="${key}"/>
			</c:forEach>	
			
			<div align="center">
				<button type="button" onclick="voltar()">Voltar</button>
				<button type="submit">Confirmar</button>
			</div>
			
		</form>
		<script type="text/javascript">

			function voltar() {
				goTo('${raiz}execucoes/pendentes');
			}

			$(function(){
				$.blockUI({ message: '<h1>Gerando Script...</h1>' });
				var execucoes = new Array(); 
				$('[name=execucaoKey]').each(function(i,el){ 
					execucoes.push($(el).val());
				}); 

				console.log(execucoes.toString());
				$('#script').load("${raiz}execucoes/gerarScript", $('#formExecucoes').serializeArray(),
					function() {
						$.unblockUI();
				});
			});	
		</script>
		
	</body>
</html>