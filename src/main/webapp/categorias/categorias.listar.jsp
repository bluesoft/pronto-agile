<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Categorias</title>
	</head>
	<body>
		<h1>Categorias</h1>
		<table style="width: 100%" id="categorias">
			<thead>
			<tr>
				<th>Nome</th>
				<th>Cor</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${categorias}" var="c">
					<tr>
						<td>
							<span class="categoria categoria-${c.descricaoDaCor}">
								${c.descricao}
							</span>
						</td>
						<td>${c.descricaoDaCor}</td>
						<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
							<td>
								<pronto:icons name="editar.png" title="Editar Categoria" onclick="goTo('${raiz}categorias/${c.categoriaKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir Categoria" onclick="pronto.doDelete('${raiz}categorias/${c.categoriaKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
			<div align="center" class="buttons">
				<button type="button" onclick="window.location.href='${raiz}categorias/novo'">Incluir Categoria</button>
			</div>
		</c:if>
		
		<script>
			$(function(){
				$('#categorias tbody tr:odd').addClass('odd');
				$('#categorias tbody tr:even').addClass('even');
			});
		</script>
		
	</body>
</html>