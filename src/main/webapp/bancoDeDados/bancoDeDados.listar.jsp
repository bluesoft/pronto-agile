<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Bancos de Dados</title>
		<script type="text/javascript">

			function incluir(){
				goTo('${raiz}bancosDeDados/novo');
			}

			function excluir(bancoDeDadosKey){
				var callback = function(){
					pronto.doDelete('${raiz}bancosDeDados/' + bancoDeDadosKey);
				};
				pronto.confirm('Tem certeza que deseja excluir este banco de dados? Todos as execuções de scripts associadas a ele serão excluídas.', callback);
			}
		</script>
	</head>
	<body>
		<h1>
			Bancos de Dados
			<pronto:icons name="script.png" title="Ver Scripts" onclick="goTo('${raiz}scripts')"/>
			<pronto:icons name="execucao.png" title="Ver Execuções de Todos os Bancos de Dados" onclick="goTo('${raiz}execucoes')"/>
		</h1>
		<c:set var="cor" value="${true}"/>
		<table style="width: 100%">
			<tr>
				<th style="width: 20px;">#</th>
				<th>Nome</th>
				<th style="width: 16px;"></th>
				<th style="width: 16px;"></th> 
				<th style="width: 16px;"></th>
			</tr>
			<c:forEach items="${bancos}" var="b">
				<c:set var="cor" value="${!cor}"/>
				
				<tr id="${b.bancoDeDadosKey}" class="${cor ? 'odd' : 'even'}">
					<td>${b.bancoDeDadosKey}</td>
					<td class="descricao">${b.nome}</td>
					<td>
						<pronto:icons name="execucao.png" title="Ver Execuções do Banco de Dados ${b.nome}" onclick="goTo('${raiz}execucoes/${b.bancoDeDadosKey}/pendentes')"/>
					</td>
					<td>
						<a href="${raiz}bancosDeDados/${b.bancoDeDadosKey}"><pronto:icons name="editar_banco_de_dados.png" title="Editar" /></a>
					</td>
					<td>
						<a href="#"><pronto:icons name="excluir_banco_de_dados.png" title="Excluir" onclick="excluir(${b.bancoDeDadosKey})"/></a>
					</td>
				</tr>
			</c:forEach>
		</table>	
		
		<div align="center">
			<button onclick="incluir()">Incluir</button>
		</div>
		
	</body>
</html>