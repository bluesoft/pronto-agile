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
			<c:if test="${usuarioLogado.administrador}">
				<li><a href="${raiz}configuracoes">Configurações</a></li>
			</c:if>
		</ul>
		</li>
		
		<c:if test="${usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
			<li><a href="#">Backlogs</a>
				<ul>
					<li><a href="${raiz}backlogs/sprints/atual">Sprint Atual</a></li>
					<li><a href="${raiz}backlogs/2">Product Backlog</a></li>
					<li><a href="${raiz}backlogs/1">Ideias</a></li>
					<li><a href="${raiz}backlogs/5">Impedimentos</a></li>
					<li><a href="${raiz}backlogs/4">Lixeira</a></li>
					<li><a href="${raiz}backlogs/clientes">Pendentes</a></li>
				</ul>
			</li>
		</c:if>
		
		<c:if test="${usuarioLogado.clientePapel}">
			<li><a href="${raiz}clientes/backlog">Backlog</a></li>
		</c:if>
		
		<c:if test="${usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
			<li><a href="${raiz}sprints">Sprints</a></li>
			<li><a href="${raiz}kanban">Kanban</a></li>
			<li><a href="${raiz}burndown">Burndown</a></li>
		</c:if>
		
		<c:if test="${usuarioLogado.equipe}">
			<li><a href="#">Ferramentas</a>
				<ul>
					<li><a href="${raiz}tickets/branches">Branches</a></li>
					<li><a href="${raiz}bancosDeDados">Bancos de Dados</a></li>
					<li><a href="${raiz}scripts">Scripts</a></li>
					<li><a href="${raiz}execucoes/pendentes">Execuções de Scripts</a></li>
				</ul>
			</li>
		</c:if>
		
		<li><a href="${raiz}logout">Sair</a></li>
	</ul>
	<div class="clear"></div>
</c:if>