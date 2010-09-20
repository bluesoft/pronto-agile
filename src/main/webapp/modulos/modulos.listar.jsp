<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Módulos</title>
	</head>
	<body>
		<h1>Módulos</h1>
		<table style="width: 100%" id="modulos">
			<thead>
			<tr>
				<th>Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${modulos}" var="c">
					<tr>
						<td>
							${c.descricao}
						</td>
						<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
							<td>
								<pronto:icons name="editar.png" title="Editar Módulo" onclick="goTo('${raiz}modulos/${c.moduloKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir Módulo" onclick="pronto.doDelete('${raiz}modulos/${c.moduloKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}modulos/novo'">Incluir Módulo</button>
			</div>
		</c:if>
		
		<script>
			$(function(){
				$('#modulos tbody tr:odd').addClass('odd');
				$('#modulos tbody tr:even').addClass('even');
			});
		</script>
		
	</body>
</html>