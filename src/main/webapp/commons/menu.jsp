<%@ include file="/commons/taglibs.jsp"%>

<c:if test="${usuarioLogado ne null}">
<span id="prontoMenu">
<table class="rootVoices" cellspacing='0' cellpadding='0' border='0'>
	<tr>
	    <td class="rootVoice {menu: 'menuCadastros'}" >Cadastros</td>
	    <td class="rootVoice {menu: 'menuBacklogs'}" >Backlogs</td>
		<c:if test="${usuarioLogado.administrador or usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
	   		<td class="rootVoice {menu: 'empty'}" onclick="goTo('${raiz}kanban');">Kanban</td>
			<td class="rootVoice {menu: 'empty'}" onclick="goTo('${raiz}sprints');">Sprints</td>
		</c:if>
	    <td class="rootVoice {menu: 'menuFerramentas'}" >Ferramentas</td>
	    <td class="rootVoice {menu: 'menuRelatorios'}">Relatórios</td>
	    <td class="rootVoice {menu: 'empty'}" onclick="goTo('${raiz}logout');">Sair</td>
	</tr>
</table>
</span>

<div id="menuCadastros" class="mbmenu">
    <c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
		<a img="categorias.png" href="${raiz}categorias">Categorias</a>
	</c:if>
	<c:if test="${usuarioLogado.administrador or usuarioLogado.equipe}">
		<a img="defeito.png" href="${raiz}causasDeDefeito">Causas de Defeito</a>
	</c:if>
	<c:if test="${usuarioLogado.administrador or usuarioLogado.scrumMaster or usuarioLogado.productOwner}">
		<a img="clientes.png" href="${raiz}clientes">Clientes</a>
	</c:if>
	<c:if test="${usuarioLogado.administrador or usuarioLogado.productOwner}">
		<li><a img="modulos.png" href="${raiz}modulos">Módulos</a></li>
	</c:if>
	<c:if test="${usuarioLogado.administrador or usuarioLogado.equipe}">
		<li><a img="reprovacao.png" href="${raiz}motivosReprovacao">Motivos De Reprovação</a></li>
	</c:if>
	<a img="usuarios.png" href="${raiz}usuarios">Usuários</a>
    <a rel="separator"></a>
</div>

<div id="menuBacklogs" class="mbmenu">
	<c:if test="${usuarioLogado.administrador or usuarioLogado.scrumMaster or usuarioLogado.productOwner or usuarioLogado.equipe}">
		<a href="${raiz}backlogs/sprints/atual" img="sprint_atual.png">Sprint Atual</a>
		<a href="${raiz}backlogs/inbox" img="inbox.png">Inbox</a>
		<a href="${raiz}backlogs/productBacklog" img="estorias.gif">Product Backlog</a>
		<a href="${raiz}backlogs/futuro" img="futuro.png">Futuro</a>
		<a href="${raiz}backlogs/impedimentos" img="impedimentos.png">Impedimentos</a>
		<a href="${raiz}backlogs/lixeira" img="lixeira.png">Lixeira</a>
		<a href="${raiz}backlogs/clientes" img="pendentes.png">Pendentes</a>
		<c:if test="${usuarioLogado.clientePapel}">
			<li><a href="${raiz}clientes/backlog" img="estorias.gif">Cliente</a></li>
		</c:if>
		<a href="${raiz}buscar" img="buscar.png">Buscar Tickets</a>
	</c:if>
</div>

<div id="menuFerramentas" class="mbmenu">
	<c:if test="${usuarioLogado.administrador or usuarioLogado.equipe}">
		<a img="branches.png" href="${raiz}tickets/branches">Branches</a>
		<a img="banco_de_dados.png" href="${raiz}bancosDeDados">Bancos de Dados</a>
		<a img="script.png" href="${raiz}scripts">Scripts</a>
		<a img="execucoes.png" href="${raiz}execucoes/pendentes">Execuções de Scripts</a>
	</c:if>
	<c:if test="${usuarioLogado.administrador}">
		<a img="configuracoes.png" href="${raiz}configuracoes">Configurações</a>
	</c:if>
</div>

<div id="menuRelatorios" class="mbmenu">
	<a img="burndown_chart.png" href="${raiz}burndown">Burndown Chart</a>
	<a img="defeito.png" href="${raiz}relatorios/defeitos">Gráfico de Defeitos</a>
	<a img="nota.gif" href="${raiz}relatorios/releaseNotes">Notas de Release</a>
</div>

</c:if>