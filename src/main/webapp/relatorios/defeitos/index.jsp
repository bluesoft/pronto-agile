<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Relatório de Defeitos</title>
	</head>
	<body>
		<h1>
			Relatório de Defeitos
		</h1>
		
		<div id="topo">
			Tipo de Gráfico:
			<select id="tipo">
				<option value="semana" chart="FCF_Line">Por Semana</option>
				<option value="mes" chart="FCF_Line">Por Mês</option>
				<option value="ano" chart="FCF_Line">Por Ano</option>
				<option value="categoria" chart="FCF_Bar2D">Por Categoria</option>
				<option value="cliente" chart="FCF_Bar2D">Por Cliente</option>
				<option value="modulo" chart="FCF_Bar2D">Por Módulo</option>
				<option value="sprint" chart="FCF_Bar2D">Por Sprint</option>
			</select>
			
			<fmt:formatDate var="strDataInicial" value="${dataInicial}"/>
			Data Inicial: <input type="text" id="dataInicial" class="required dateBr" value="${strDataInicial}" size="12"/>
			<fmt:formatDate var="strDataFinal" value="${dataFinal}"/>
			Data Final: <input type="text" id="dataFinal" class="required dateBr" value="${strDataFinal}" size="12"/>
			
			&nbsp; &nbsp;
			
			<button type="button" onclick="gerar()">Gerar</button>
		</div>
		
		<div id="chartdiv" align="center" style="height: 510px;">
		</div>
		<script type="text/javascript">
			function gerar() {
				var di = $('#dataInicial').val();
				var df = $('#dataFinal').val();
				var tipo = $("#tipo").val();
				var parametros = "?dataInicial=" + di + "&dataFinal=" + df +"&tipo=" + tipo;
				var url = encodeURIComponent("${raiz}relatorios/defeitos/gerar.xml" + parametros);
				var chartType = $('#tipo').find('option:selected').attr('chart');
				var chart = new FusionCharts("${raiz}/commons/charts/"+chartType+".swf", "chart", "920", "500");
				chart.setDataURL(url);
				chart.render("chartdiv");	
			}

			$(function(){
				$('#formSprint').validate();
				$('.dateBr').datepicker();
			});
		</script> 
	</body>
</html>