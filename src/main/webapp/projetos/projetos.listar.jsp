<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Projetos</title>
	</head>
	<body>
		<h1>Projetos</h1>
		<table style="width: 100%" id="projetos">
			<thead>
			<tr>
				<th>Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${projetos}" var="c">
					<tr>
						<td>
							<a onclick="goTo('${raiz}projetos/${c.projetoKey}')" class="link">${c.nome}</a>
						</td>
						<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
							<td>
								<pronto:icons name="editar.png" title="Editar Projeto" onclick="goTo('${raiz}projetos/${c.projetoKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir Projeto" onclick="pronto.doDelete('${raiz}projetos/${c.projetoKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
			<div align="center" class="buttons">
				<button type="button" onclick="window.location.href='${raiz}projetos/novo'">Incluir Projeto</button>
			</div>
		</c:if>
		
		<script>
			$(function(){
				$('#projetos tbody tr:odd').addClass('odd');
				$('#projetos tbody tr:even').addClass('even');
			});
		</script>
		
	</body>
</html>