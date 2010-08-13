<%@ include file="/commons/taglibs.jsp"%>
<div class="htmlbox comentario" style="position: relative;">

	<div class="comentario-data">${zenDeskTicket.created_at}</div>

	<div class="person-comentario">		
		<div class="person">
			<pronto:icons name="zendesk.png" title="Zendesk" onclick="openWindow('${zenDeskUrl}/tickets/${zenDeskTicketKey}')"/>
			<div class="person_name">Zendesk</div>
		</div>
	</div>
	
	<div class="comentario-html">
		<h3>Ticket <a href="${zenDeskUrl}/tickets/${zenDeskTicketKey}" target="_blank">#${zenDeskTicket.nice_id}</a> do Zendesk</h3>
		<b>Status:</b> ${zenDeskTicket.status}<br/>
		<b>Tipo:</b> ${zenDeskTicket.tipo}<br/>
		
		
	</div>

</div>

<c:if test="${!empty zenDeskTicket.comments}">
	<c:forEach items="${zenDeskTicket.comments}" var="comentario">
		<div class="htmlbox comentario" style="position: relative;">
			
			<div class="comentario-data">${comentario.created_at}</div>
			
			<div class="person-comentario">
				<div class="person">
					<img alt="Gravatar" align="left" src="http://www.gravatar.com/avatar/${comentario.author.emailMD5}?s=50"/>
					<div class="person_name">${comentario.author.name}</div>
				</div>
			</div>
			
			
			
			<div class="comentario-html">
				${comentario.value}
				<br/>
				<ul style="list-style-type: none;">
					<c:forEach items="${comentario.attachments}" var="anexo">
						<li>
							<pronto:icons name="anexo.png" title="${anexo.filename}"/>
							${anexo.filename}
							<pronto:icons name="download.gif" title="Baixar Anexo" onclick="openWindow('${anexo.url}')"/>
						</li>
					</c:forEach>
				</ul>
				
				
			</div>
			
		</div>
	</c:forEach>
	<br/>
</c:if>

<h3>Incluir comentário privado no Zendesk</h3>
<form action="${raiz}/zendesk/tickets/${zenDeskTicket.nice_id	}/comentarios" method="post">
	<div>
		<textarea id="comentarioZenDesk" name="comentarioZenDesk"></textarea>
	</div>
	<div align="center">
		<button type="submit">Incluir</button>
	</div>
</form>