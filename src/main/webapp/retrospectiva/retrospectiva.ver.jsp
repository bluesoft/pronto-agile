<%@ include file="/commons/taglibs.jsp"%>
<html>	
	<head>
		<title>Retrospectiva do Sprint ${retrospectiva.sprint.nome}</title>
		<link rel="stylesheet" type="text/css" media="all" href="${raiz}retrospectiva/retrospectiva.css" />
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
				var url = '${raiz}retrospectivas/itens/' + key;
				var data = {'_method': 'delete'};
				$.post(url, data, function(){
					$('#'+key).fadeOut('slow',function(){ 
						$(this).remove(); 
					});
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
					var $li = $('<li class="ui-state-default" style="display:none" id="'+key+'">'+ descricao +'</li>');
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

					$li.fadeIn('slow');
				}; 	
				
				$.ajax({
					type:'post',
					url: '${raiz}retrospectivas/${retrospectiva.retrospectivaKey}',
					'data': data,
					success: callback,
					contentType: 'application/x-www-form-urlencoded; charset=ISO-8859-1',
					async: false
				});
			}

			function trocarTipo(){
				$('#formAlterar').submit();				
			}

			function excluirAnexo(retrospectivaKey, anexo) {
				if (confirm('Tem certeza que deseja excluir este anexo?')) {
					pronto.doDelete(pronto.raiz + 'retrospectivas/' + retrospectivaKey + '/anexos/', [ {
						name : 'file',
						value : anexo
					} ]);
				}
			}
		</script>
	</head>
	<body>
		<h1>
			Retrospectiva do Sprint ${retrospectiva.sprint.nome} (${retrospectiva.tipoRetrospectiva.descricao})
			<%@ include file="/commons/sprintLinks.jsp" %>
		</h1>
		
		<form action="${raiz}retrospectivas/${retrospectiva.retrospectivaKey}/alterarTipoDeRetrospectiva" id="formAlterar" method="post">
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
		
		<div id="anexos" style="clear: both;">
			<br/><br/>
			<h2>Anexos</h2>
			<ul style="list-style-type: none;">
				<c:forEach items="${anexos}" var="anexo">
					<li>
						<c:choose>
							<c:when test="${anexo.imagem}">
								<pronto:icons name="imagem.gif" title="Imagem ${anexo.extensao}"/>
							</c:when>
							<c:when test="${anexo.planilha}">
								<pronto:icons name="excel.png" title="Planílha ${anexo.extensao}"/>
							</c:when>
							<c:when test="${anexo.extensao eq 'pdf'}">
								<pronto:icons name="pdf.png" title="Arquivo PDF"/>
							</c:when>
							<c:otherwise>
								<pronto:icons name="anexo.png" title="${anexo.extensao}"/>
							</c:otherwise>
						</c:choose>
						
						${anexo.nomeParaExibicao}
						<pronto:icons name="download.gif" title="Baixar Anexo" onclick="goTo('${raiz}retrospectivas/${retrospectiva.retrospectivaKey}/anexos?file=${anexo}')"/>
						<pronto:icons name="excluir.png" title="Excluir Anexo" onclick="excluirAnexo(${retrospectiva.retrospectivaKey},'${anexo}');"/>
					</li>
				</c:forEach>
			</ul>
		
			<h4>Incluir anexo</h4>						
			<form action="${raiz}retrospectivas/${retrospectiva.retrospectivaKey}/upload" method="post" enctype="multipart/form-data">
				<input type="file" name="arquivo">
				<button type="submit">Upload</button>
			</form>
		</div>
		
	</body>
</html>