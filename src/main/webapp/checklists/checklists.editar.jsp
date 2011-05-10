<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Modelos de Checklist</title>
		<style>
			.checklist .ui-icon-close {
				cursor: pointer;
				display: inline-block;
		</style>
		<script>
			$(function() {
				$('#formChecklist').validate();
				$('#nome').focus();
				$('.addChecklistItem').live('keypress', function(event) {
					if (event.keyCode == '13') {
					     event.preventDefault();
					     $campo = $(event.target);
					     var checklistKey = $campo.attr('checklistKey');
					     incluirItemNoChecklist(checklistKey, $campo.val());
					     $campo.val('');
					   }
				});
				
				$('.excluirChecklistItem').live('click',function(event){
					var checklistItemKey = $(event.target).parents('li').attr('checklistItemKey');
					excluirChecklistItem(checklistItemKey);
				});
			});
			
			function salvar() {
				$('#formChecklist').submit();
			}
			
			function excluirChecklistItem(checklistItemKey) {
				$.post(pronto.raiz+'/checklists/removerItem', {
					checklistItemKey:checklistItemKey
				}, function(resposta) {
					$('#checklistItem-'+checklistItemKey).remove();
				});
			}
			
			function incluirItemNoChecklist(checklistKey, descricao){
				if (descricao == null || descricao.length == 0) {
					return;
				}
				$.post(pronto.raiz+'checklists/'+checklistKey+'/incluirItem', {
					descricao:descricao
				}, function(checklistItemKey) {
					$('ul.checklist').append('<li checklistItemKey="'+checklistItemKey+'" id="checklistItem-'+checklistItemKey+'">'+descricao+'<span class="excluirChecklistItem ui-icon ui-icon-close"></span></li>');
				});
			}
			
		</script>
	</head>
	<body>
		<form action="${raiz}checklists/" id="formChecklist" method="post">
			<ul class="info">
				<h1>Cadastro de Modelos de Checklists</h1>
			</ul>
			<div class="group">
				<form:hidden path="checklist.checklistKey"/>
				<div>
					<form:input path="checklist.nome" id="nome" cssClass="required" size="35" maxlength="35"/>
					<p>Nome</p>
				</div>
				<ul class="checklist">
					<c:forEach items="${checklist.itens}" var="item">
						<li id="checklistItem-${item.checklistItemKey}" checklistItemKey="${item.checklistItemKey}">
							${item.descricao}
							<span class="excluirChecklistItem ui-icon ui-icon-close" title="Excluir item do checklist"></span>
						</li>
					</c:forEach>
				</ul>
				<div>
					<input type="text" class="addChecklistItem" checklistKey="${checklist.checklistKey}" maxlength="120" size="60"/>
					<p>Incluir Item</p>
				</div>
				
				<input type="text" name="avoidSubmitOnEnter" style="display: none;"/>
			</div>
				
			<div align="center" class="buttons">
				<br />
				<button type="button" onclick="window.location.href='${raiz}checklists'">Cancelar</button>
				<button type="button" onclick="salvar()">Salvar</button><br/>
			</div>
		</form>		
	</body>
</html>