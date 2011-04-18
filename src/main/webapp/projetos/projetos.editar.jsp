<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Cadastro de Projetos</title>
		<style type="text/css">
		.icon-adicionar {
			float: right;
		}
		
		#kanban {
			list-style-type: none;
		}
		
		#kanban li {
			height: 20px;
			padding: 2px;
			margin: 2px;
		}
		
		.ui-icon {
			float: left;
		}
		
		.ui-icon-closethick {
			float: right !important;
		}
		
		
		.etapa-descricao {
			margin-left: 5px;
		}
		
		.hover {
			cursor: pointer;
		}
		
		</style>
	</head>
	<body>
		<form action="${raiz}projetos/" id="formProjeto" method="post">

			<ul class="info">
				<h1>Cadastro de Projetos</h1>
			</ul>
			<div class="group">
				<form:hidden path="projeto.projetoKey"/>
				<c:if test="${projeto.projetoKey gt 0}">
					<div>
						<b>${projeto.projetoKey}</b>
						<p>Código</p>
					</div>
				</c:if>
				
				<div>
					<form:input path="projeto.nome" cssClass="required" size="40"/>
					<p>Nome</p>
					<input type="hidden" name="paraEvitarProblemaDoSubmitNoEnter" value="Nada"/>
				</div>
			</div>

			<div align="center" class="buttons">
				<br />
				<button type="button" onclick="window.location.href='${raiz}projetos'">Cancelar</button>
				<button type="submit">Salvar</button><br/>
			</div>
		</form>

	<c:if test="${projeto.projetoKey gt 0}">
				<br/>
				<div>
					<h3>Etapas do Kanban
						<pronto:icons name="adicionar.png" title="Incluir Etapa no Kanban" clazz="icon-adicionar" onclick="incluirEtapa()"/>
					</h3>	
					<ul id="kanban">
						<c:forEach items="${projeto.etapasDoKanban}" var="etapa" varStatus="status">
							<li class="${status.first ? 'first' : (status.last ? 'last' : 'middle')} etapa" key="${etapa.kanbanStatusKey}">
								<span class="etapa-descricao">${etapa.descricao}</span>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
		
		<script>
		
			var projetoKey = "${projeto.projetoKey}";
			$(function() {
				$('#formProjeto').validate();
				var $kanban = $('#kanban');
				$kanban.find('.first').addClass('ui-state-disabled').prepend('<span class="ui-icon"></span>');;
				$kanban.find('.last').addClass('ui-state-disabled').prepend('<span class="ui-icon"></span>');;
				$kanban.find('.middle').addClass('ui-state-default').prepend('<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>').append('<span class="ui-icon ui-icon-closethick"></span>');
				$kanban.sortable({cancel: ".ui-state-disabled"});
				
				$('.ui-icon-closethick').live('click', function() {
					var $li = $(this).parents('li'); 
					var key = $li.attr('key');
					$.post('excluirEtapa', {kanbanStatusKey:key}, function(salvou){
						if (salvou) {
							$li.remove();
						} else {
							alert('Não foi possível excluir.');
						}
					});
				});
				
			});
			
			function incluirEtapa() {
				var nome = prompt('Informe o Nome da Etapa');
				$.post("incluirEtapa.action", {projetoKey:projetoKey, nome:nome}, function(salvou){
					var $kanban = $('#kanban');
					var $novo = $('<li class="middle"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span><span class="etapa-descricao">'+nome+'</span><span class="ui-icon ui-icon-closethick"></span></li>');
					$novo.addClass('ui-state-default').prepend();
					$kanban.find('li.middle:last').after($novo);
				});
			}
			
			
		</script>		
	</body>
</html>