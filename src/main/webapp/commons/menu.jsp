<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${usuarioLogado ne null}">
	<c:url var="raiz" value="/"/>
	<ul id="jsddm">
		<li><a href="#">Cadastros</a>
		<ul>
			<li><a href="${raiz}usuarios">Usuários</a></li>
			<c:if test="${usuarioLogado.scrumMaster or usuarioLogado.productOwner}">
				<li><a href="${raiz}clientes">Clientes</a></li>
			</c:if>
			
		</ul>
		</li>
		
		<c:if test="${usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
			<li><a href="#">Backlogs</a>
				<ul>
					<li><a href="${raiz}ticket/sprintAtual.action">Sprint Atual</a></li>
					<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=2">Product Backlog</a></li>
					<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=1">Ideias</a></li>
					<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=5">Impedimentos</a></li>
					<li><a href="${raiz}ticket/listarPorBacklog.action?backlogKey=4">Lixeira</a></li>
					<li><a href="${raiz}ticket/listarPendentesPorCliente.action?kanbanStatusKey=-1">Pendentes</a></li>
				</ul>
			</li>
		</c:if>
		
		<c:if test="${usuarioLogado.clientePapel}">
			<li><a href="${raiz}clientes/backlog">Backlog</a></li>
		</c:if>
		
		<c:if test="${usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
			<li><a href="${raiz}sprints">Sprints</a></li>
			<li><a href="${raiz}kanban/kanban.action">Kanban</a></li>
			<li><a href="${raiz}burndown/burndown.action">Burndown</a></li>
		</c:if>
		
		<c:if test="${usuarioLogado.equipe}">
			<li><a href="#">Ferramentas</a>
				<ul>
					<li><a href="${raiz}ticket/branches.action">Branches</a></li>
					<li><a href="${raiz}bancoDeDados/listar.action">Bancos de Dados</a></li>
					<li><a href="${raiz}script/listar.action">Scripts</a></li>
					<li><a href="${raiz}execucao/listar.action">Execuções de Scripts</a></li>
				</ul>
			</li>
		</c:if>
		
		<li><a href="${raiz}logout.action">Sair</a></li>
	</ul>
	<div class="clear"></div>
</c:if>