<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${!empty ticket.comentarios}">
	<c:forEach items="${ticket.comentarios}" var="comentario">
		<div class="htmlbox comentario" style="position: relative;">
			<div class="comentario-data"><fmt:formatDate value="${comentario.data}" type="both"/></div>
			
			<div class="person-comentario">
				<div class="person">
					<img alt="Gravatar" align="left" src="http://www.gravatar.com/avatar/${comentario.usuario.emailMd5}?s=50"/>
					<div class="person_name">${comentario.usuario.username}</div>
				</div>
			</div>
			
			<div class="comentario-html">${comentario.html}</div>
		</div>
	</c:forEach>
	<br/>
</c:if>
<h3>Incluir Comentário</h3>
<form action="${raiz}tickets/${ticket.ticketKey}/comentarios" method="post">
	<div>
		<textarea id="comentario" name="comentario"></textarea>
	</div>
	<div align="center">
		<button type="submit">Incluir</button>
	</div>
</form>