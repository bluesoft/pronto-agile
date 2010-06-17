<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Motivos de Reprovação</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<h1>Motivos de Reprovação</h1>
		<table style="width: 100%" id="motivosReprovacao">
			<thead>
			<tr>
				<th>Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
				<c:forEach items="${motivosReprovacao}" var="c">
					<tr>
						<td>
							<span class="motivoReprovacao">
								${c.descricao}
							</span>
						</td>
						<c:if test="${usuarioLogado.equipe or usuarioLogado.administrador}">
							<td>
								<pronto:icons name="editar.png" title="Editar" onclick="goTo('${raiz}motivosReprovacao/${c.motivoReprovacaoKey}')"/>
							</td>
							<td>
								<pronto:icons name="excluir.png" title="Excluir" onclick="pronto.doDelete('${raiz}motivosReprovacao/${c.motivoReprovacaoKey}')"/>
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<c:if test="${usuarioLogado.equipe or usuarioLogado.administrador}">
			<div align="center">
				<button type="button" onclick="window.location.href='${raiz}motivosReprovacao/novo'">Incluir Motivo de Reprovação</button>
			</div>
		</c:if>
		<script>
			$(function(){
				$('#motivosReprovacao').zebrar();
			});
		</script>
	</body>
</html>