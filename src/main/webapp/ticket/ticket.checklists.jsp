<%@ include file="/commons/taglibs.jsp"%>
<pronto:icons name="adicionar.png" title="Incluir um novo Checklist" onclick="incluirChecklist();" id="iconIncluirChecklist"/>

<div id="checklistsArea">
	<c:forEach items="${ticket.checklists}" var="checklist">
		<div class="checklist" id="checklist-${checklist.checklistKey}">
			<span class="title">
				${checklist.nome}
				<pronto:icons name="excluir.png" title="Excluir checklist" onclick="excluirChecklist(${checklist.checklistKey});" clazz="iconExcluirChecklist"/>
			</span>
			<ul>
				<c:forEach items="${checklist.itens}" var="item">
					<li id="checklistItem-${item.checklistItemKey}">
						<input ${item.marcado ? 'checked="checked"' : ''} class="checklistItem" type="checkbox" checklistItemKey="${item.checklistItemKey}"/> 
						${item.descricao}
						<span class="excluirChecklistItem ui-icon ui-icon-close"></span>
					</li>
				</c:forEach>
			</ul>
			<input type="text" class="addChecklistItem" checklistKey="${checklist.checklistKey}"/>
			<hr/>
		</div>
	</c:forEach>
</div>