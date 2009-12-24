<%@ include file="/commons/taglibs.jsp"%>
<c:url var="urlSprint" value="/sprint/"/>
<c:url var="salvarUrl" value="/retrospectiva/salvarItem.action"/>
<c:url var="alterarTipoDeRetrospectivaUrl" value="/retrospectiva/alterarTipoDeRetrospectiva.action"/>
<c:url var="excluirUrl" value="/retrospectiva/excluirItem.action"/>
<html>	
	<head>
		<title>Retrospectiva do Sprint ${retrospectiva.sprint.nome}</title>
		<link rel="stylesheet" type="text/css" media="all" href="retrospectiva.css" />
		<script>

			var $listaAtual = null; 
			var $descricao = null;
		
			$(function(){
				$('.descricao').keypress(function(e) {
					 if (e.keyCode == 13) {
						salvar($(this).parents('ul'));
					 }
				});
			});

			function excluir(key){
				$.post('${excluirUrl}', {retrospectivaItemKey:key}, function(){
					$('#'+key).remove();
				});
			}
			
			function salvar($lista){

				var retrospectivaKey = $('#retrospectivaKey').val();
				var tipo = $lista.attr('tipo');

				var $descricao = $lista.find('input:text');
				var descricao = $descricao.val();
				$descricao.val('');
				
				var data = {
						'retrospectivaKey': retrospectivaKey,
						'tipoRetrospectivaItemKey': tipo,
						'descricao': descricao
				};

				var callback = function(key){
					var $li = $('<li class="ui-state-default" id="'+key+'">'+ descricao +'</li>');
					var $icon = $('#excluirModelo').clone();
					$li.append($icon);
					$icon.click(function(){
						excluir($(this).parents('li').attr('id'));
					});

					if ($lista.find('li:last').length > 0) {
						$lista.find('li:last').before($li);
					} else {
						$lista.append($li);
					}
				}; 	
				
				$.ajax({
					type:'post',
					url: '${salvarUrl}',
					'data': data,
					success: callback,
					contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
					async: false
				});
			}

			function trocarTipo(){
				$('#formAlterar').submit();				
			}
		</script>
	</head>
	<body>
		<h1>Retrospectiva do Sprint ${retrospectiva.sprint.nome} (${retrospectiva.tipoRetrospectiva.descricao})</h1>
		
		<form action="${alterarTipoDeRetrospectivaUrl}" id="formAlterar">
			Alterar para o modelo: 
			<input type="hidden" id="retrospectivaKey" name="retrospectivaKey" value="${retrospectiva.retrospectivaKey}">
			<select onchange="trocarTipo()" id="tipoRetrospectivaKey" name="tipoRetrospectivaKey">
				<c:forEach var="tipo" items="${tiposDeRetrospectiva}">
					<c:choose>
						<c:when test="${tipo.tipoRetrospectivaKey eq retrospectiva.tipoRetrospectiva.tipoRetrospectivaKey}">
							<option value="${tipo.tipoRetrospectivaKey}" selected="selected">${tipo.descricao}</option>
						</c:when>
						<c:otherwise>
							<option value="${tipo.tipoRetrospectivaKey}">${tipo.descricao}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</form>
		
		<div style="display: none">
			<pronto:icons name="excluir.png" title="excluir item" id="excluirModelo" clazz="icon"/>
		</div>
		
		<c:forEach var="tipo" items="${tiposDeItem}">
			<ul class="lista" tipo="${tipo.tipoRetrospectivaItemKey}">
				<span class="title">
					<h4 class="ui-widget-header">${tipo.descricao}</h4>
				</span>
				<c:forEach items="${retrospectiva.itens}" var="item">
					<c:if test="${item.tipoRetrospectivaItem.tipoRetrospectivaItemKey eq tipo.tipoRetrospectivaItemKey}">
						<li class="ui-state-default" id="${item.retrospectivaItemKey}">
							<pronto:icons name="excluir.png" title="excluir item" clazz="icon"  onclick="excluir('${item.retrospectivaItemKey}')"/>
							${item.descricao}
						</li>	
					</c:if>
				</c:forEach>
				<li class="ui-state-hover incluir">
					<input type="text" class="descricao" />
				</li>
			</ul>	
		</c:forEach>
	</body>
</html>