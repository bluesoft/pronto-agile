<%@ include file="/commons/taglibs.jsp"%>
<c:url var="listarExecucoesUrl" value="/execucao/listar.action"/>
<c:url var="listarBancosUrl" value="/bancoDeDados/listar.action"/>
<html>
	<head>
		<title>Scripts</title>
		<script type="text/javascript">

			function incluir(){
				goTo('incluir.action');
			}

			function excluir(scriptKey) {
				if (confirm('Tem certeza que deseja excluir este script? Todos as execuções associadas a ele serão excluídas.')) {
					goTo('excluir.action?scriptKey=' + scriptKey);
				}
			}
		
		</script>
	</head>
	<body>
		<h1>
			Scripts
			<pronto:icons name="banco_de_dados.png" title="Ver Bancos de Dados" onclick="goTo('${listarBancosUrl}')"/>
			<pronto:icons name="execucao.png" title="Ver Execuções" onclick="goTo('${listarExecucoesUrl}')"/>
		</h1>
		<c:set var="cor" value="${true}"/>
		<table style="width: 100%">
			<tr>
				<th style="width: 40px;">#</th>
				<th>Descrição</th>
				<th style="width: 16px;"></th>
				<th style="width: 16px;"></th>
			</tr>
			<c:forEach items="${scripts}" var="s">
				<c:set var="cor" value="${!cor}"/>
				
				<tr id="${s.scriptKey}" class="${cor ? 'odd' : 'even'}">
					<td>${s.scriptKey}</td>
					<td class="descricao">${s.descricao}</td>
					<td>
						<a href="editar.action?scriptKey=${s.scriptKey}"><pronto:icons name="editar_script.png" title="Editar" /></a>
					</td>
					<td>
						<a href="#"><pronto:icons name="excluir_script.png" title="Excluir" onclick="excluir(${s.scriptKey});"/></a>
					</td>
				</tr>
			</c:forEach>
		</table>	
		
		<div align="center">
			<button onclick="incluir()">Incluir Script</button>
		</div>
		
	</body>
</html>