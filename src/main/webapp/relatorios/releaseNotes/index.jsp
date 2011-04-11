<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Release Notes</title>
		<style type="text/css">
			.titulo {
				font-weight: bold;
			}
			.nota {
			
			}
			
			#notas {
				padding: 5px;
				margin: 25px; 
				clear: both;
			}
			
			#observacao {
				font-size:9px;
				font-style: italic;  
			}
		</style>
	</head>
	<body>
		<h1>
			Release Notes
		</h1>
		
		<div id="topo">
			<fmt:formatDate var="strDataInicial" value="${dataInicial}"/>
			Data Inicial: <input type="text" id="dataInicial" class="required dateBr" value="${strDataInicial}" size="12"/>
			<fmt:formatDate var="strDataFinal" value="${dataFinal}"/>
			Data Final: <input type="text" id="dataFinal" class="required dateBr" value="${strDataFinal}" size="12"/>
			&nbsp; &nbsp;
			<button type="button" onclick="listar()">Listar</button>
		</div>
		
		<div id="notas">
		
		</div>
		
		<span id="observacao">
		* Este relatório exibe as notas para release de todas as tarefas, que contém notas e que a data de pronto estiver dentro do período filtrado.
		</span>
				
		<script>
			$(function(){
				$('.dateBr').datepicker();
			});

			function listar() {
				var di = $('#dataInicial').val();
				var df = $('#dataFinal').val();
				$.getJSON('listar', { dataInicial:di, dataFinal:df }, function(notas) {
					var $notas = $("#notas"); 
					$notas.children().remove();
					$.each(notas, function(i, nota){
						$notas.append('<span class="titulo"><a href="${raiz}tickets/'+nota.ticketKey+'">#' + nota.ticketKey + '</a> - ' + nota.titulo + '</span><p class="nota">' + nota.notas + '</p>');
					});
				});
			}
		</script>
	</body>
</html>