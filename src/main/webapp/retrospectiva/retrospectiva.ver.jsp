<%@ include file="/commons/taglibs.jsp"%>
<c:url var="urlSprint" value="/sprint/"/>
<c:url var="salvarUrl" value="/retrospectiva/salvarItem.action"/>
<c:url var="excluirUrl" value="/retrospectiva/excluirItem.action"/>
<html>	
	<head>
		<title>Retrospectiva</title>
		<style type="text/css">
			
			#wwwDiv {
				float: left;
				width: 400px;
				vertical-align: top;
			}
			
			#wcbiDiv {
				margin-left: 80px;
				float: left;
				width: 400px;
				vertical-align: top;
			}
			
			#www { list-style-type: none; margin: 0; padding: 0; width: 60%; }
			#www li {  padding: 2px; height: 1.5em; font-size: 11px}
			html>body #www li { height: 1.5em; line-height: 1.2em; }
			
			#wcbi { list-style-type: none; margin: 0; padding: 0; width: 60%; }
			#wcbi li {margin: 1px;  padding: 2px; height: 1.5em; font-size: 11px}
			html>body #wcbi li { height: 1.5em; line-height: 1.2em; }
			
			.ui-state-highlight { height: 1.5em; line-height: 1.2em; }
			.ui-state-default { background-image: none; background-color: #f8f8f8; color: #0066cc; }
			
			.icon { float: right;}
					
		</style>
		<script>
	
			$(function(){
				$("#www").add('#wcbi').sortable({
    				placeholder: 'ui-state-highlight',
    		  	});
				$("#wcbi").add('#wcbi').disableSelection();

				$("#dialog").dialog({ autoOpen: false, height: 100, width: 600, modal: true });
				
			});


			function incluirWCBI(){
				$('#tipoRetrospectivaItemKey').val('2');
				$("#descricao").val('');
				$("#dialog").dialog('open');
			};

			function incluirWWW(){
				$('#tipoRetrospectivaItemKey').val('1');
				$("#descricao").val('');
				$("#dialog").dialog('open');
			};

			function excluir(key){
				$.post('${excluirUrl}', {retrospectivaItemKey:key}, function(){
					$('#'+key).remove();
				});
			}
			
			function salvar(){
				var data = $('#form').serialize();

				var descricao = $('#descricao').val();
				var tipo = $('#tipoRetrospectivaItemKey').val();

				var data = {
						retrospectivaKey: $('#retrospectivaKey').val(),
						tipoRetrospectivaItemKey: $('#tipoRetrospectivaItemKey').val(),
						descricao: $('#descricao').val()
				};
				
				$.post('${salvarUrl}', data, function(key){
					var $lista = null;
					if (tipo == 1) {
						$lista = $('#www');
					} else {
						$lista = $('#wcbi');
					}
					var $li = $('<li class="ui-state-default" id="'+key+'">'+ descricao +'</li>');
					var $icon = $('#excluirModelo').clone();
					$li.append($icon);
					$icon.click(function(){
						excluir($(this).parents('li').attr('id'));
					});
					$lista.append($li);
					
					$("#dialog").dialog('close');
				});
				
			}
		
		</script>
	</head>
	<body>
		<h1>Retrospectiva</h1>
		
		<div style="display: none">
			<pronto:icons name="excluir.png" title="excluir item" id="excluirModelo" clazz="icon"/>
		</div>
		
		
			<div id="wwwDiv">
				<ul style="width: 100%; font-size: 12px;" id="www">
					<span>
						<pronto:icons name="adicionar.png" title="adicionar item" clazz="icon"  onclick="incluirWWW()"/>
						<h4 class="ui-widget-header">O que foi bom?</h4>
					</span>
					<c:forEach items="${retrospectiva.www}" var="item">
						<li class="ui-state-default" id="${item.retrospectivaItemKey}">
							<pronto:icons name="excluir.png" title="excluir item" clazz="icon"  onclick="excluir('${item.retrospectivaItemKey}')"/>
							${item.descricao}
						</li>	
					</c:forEach>
				</ul>
			</div>
			
			<div id="wcbiDiv">
				<ul style="width: 100%; font-size: 12px;" id="wcbi">
					<span>
						<pronto:icons name="adicionar.png" title="adicionar item" clazz="icon"  onclick="incluirWCBI()"/>
						<h4 class="ui-widget-header">O que pode ser melhorado?</h4>
					</span>
					
					<c:forEach items="${retrospectiva.wcbi}" var="item">
						<li class="ui-state-default" id="${item.retrospectivaItemKey}">
							<pronto:icons name="excluir.png" title="excluir item" clazz="icon" onclick="excluir('${item.retrospectivaItemKey}')"/>
							${item.descricao}
						</li>	
					</c:forEach>
					
				</ul>
			</div>
		
		<div title="Retrospectiva" id="dialog" style="display: none; width: 500px;">
				<div class="group" align="left" id="form">
					
					<input type="hidden" name="retrospectivaKey" id="retrospectivaKey" value="${retrospectiva.retrospectivaKey}"/>
					<input type="hidden" name="tipoRetrospectivaItemKey" id="tipoRetrospectivaItemKey"/>
					
					<div>
						<input type="text" name="descricao" id="descricao" size="50"/>					
						<p>Descrição</p>
					</div>
					
					<button onclick="salvar()" type="button">Confirmar</button>
					
				</div>
		</div>
		
		
	</body>
</html>
