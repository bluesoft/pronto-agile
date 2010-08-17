<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Causas de Defeito</title>
	</head>
	<body>
		<h1>Causas de Defeito</h1>
		<table style="width: 100%" id="causasDeDefeito">
			<thead>
			<tr>
				<th>Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${causasDeDefeito}" var="c">
					<tr>
						<td>
							<span class="causaDeDefeito">
								${c.descricao}
							</span>
						</td>
						<c:if test="${usuarioLogado.equipe or usuarioLogado.administrador}">
							<td>
								<pronto:icons name="editar.png" title="Editar" onclick="goTo('${raiz}causasDeDefeito/${c.causaDeDefeitoKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir" onclick="pronto.doDelete('${raiz}causasDeDefeito/${c.causaDeDefeitoKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.equipe  or usuarioLogado.administrador}">
			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}causasDeDefeito/novo'">Incluir Causa de Defeito</button>
			</div>
		</c:if>
		<script>
			$(function(){
				$('#causasDeDefeito').zebrar();
			});
		</script>
	</body>
</html>