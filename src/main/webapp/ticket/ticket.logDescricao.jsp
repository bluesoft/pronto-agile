<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>#${log.ticket.ticketKey} - Log</title>
	</head>
	<body>
			<h1>#${log.ticket.ticketKey} - Log de Alterações</h1>
			<div class="group">
				<div>
					<b>${log.usuario}</b>					
					<p>Usuário</p>
				</div>
				<div>
					<b><fmt:formatDate value="${log.data}" type="both"/></b>					
					<p>Data e Hora</p>
				</div>
			</div>

			<h3>Descrição Antiga</h3>
			<div class="htmlbox">
				${log.valorAntigoHtml}
			</div>
			
			<h3>Descrição Nova</h3>
			<div class="htmlbox">
				${log.valorNovoHtml}
			</div>

			<div align="center">
				<button type="button" onclick="goTo('editar.action?ticketKey=${log.ticket.ticketKey}');">Voltar</button>
			</div>
	</body>
</html>