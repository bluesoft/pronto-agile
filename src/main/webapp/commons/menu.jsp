<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${usuario ne null}">
	<c:url var="raiz" value="/"/>
	<ul id="jsddm">
		<li><a href="#">Cadastros</a>
		<ul>
			<li><a href="${raiz}usuario/listar.action">Usuários</a></li>
		</ul>
		</li>
		<li><a href="${raiz}kanban/kanban.action">Kanban</a></li>
		<li><a href="${raiz}ticket/listar.action">Product Backlog</a></li>
		<li><a href="#">Backlog de Idéias</a></li>
		<li><a href="#">Sprints</a>
		<ul>
			<li><a href="#">Portugal</a></li>
			<li><a href="#">Inglaterra</a></li>
		</ul>
		</li>
		<li><a href="${raiz}logout">Sair</a></li>
	</ul>
	<div class="clear"></div>
</c:if>