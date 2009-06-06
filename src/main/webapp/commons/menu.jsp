<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${usuarioLogado ne null}">
	<c:url var="raiz" value="/"/>
	<ul id="jsddm">
		<li><a href="#">Cadastros</a>
		<ul>
			<li><a href="${raiz}usuario/listar.action">Usuários</a></li>
		</ul>
		</li>
		<li><a href="#">Backlogs</a>
			<ul>
				<li><a href="${raiz}ticket/sprintAtual.action">Sprint Atual</a></li>
				<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=2">Product Backlog</a></li>
				<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=1">Idéias</a></li>
				<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=5">Impedimentos</a></li>
				<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=4">Lixeira</a></li>
			</ul>
		</li>
		<li><a href="${raiz}sprint/listar.action">Sprints</a></li>
		<li><a href="${raiz}kanban/kanban.action">Kanban</a></li>
		<li><a href="${raiz}burndown/burndown.action">Burndown</a></li>
		<li><a href="${raiz}logout.action">Sair</a></li>
	</ul>
	<div class="clear"></div>
</c:if>