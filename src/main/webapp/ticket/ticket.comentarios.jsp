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
	
	<h4>Notificar</h4>
	<div>
		<c:forEach items="${desenvolvedores}" var="u" varStatus="s">
			<c:set var="checked" value="${false}"/>
			<c:forEach items="${ticket.envolvidos}" var="d">
				<c:if test="${d.username eq u.username}">
					<c:set var="checked" value="${true}"/>
				</c:if>
			</c:forEach>
			<div class="person notificar" style="display: inline;">
				<img alt="${u.username} - Clique para adicionar/remover" id="notificar_img_${u.username}" class="${checked ? 'ativo' : 'inativo'}" align="bottom" title="${u.nome}" src="http://www.gravatar.com/avatar/${u.emailMd5}?s=45" onclick="toogleNotificar('${u.username}')" style="cursor:pointer"/>
				<input id="notificar_chk_${u.username}"  type="checkbox" name="notificar" value="${u.username}" ${checked ? 'checked="checked"' : ''} style="display: none;">
				<div class="person_name">${u.username}</div>
			</div>
		</c:forEach>
	</div>
	
	<div align="center" class="buttons">
		<button type="submit">Incluir</button>
	</div>
</form>
