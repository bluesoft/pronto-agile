<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Relatório de Quantidades</title>
	</head>
	<body>
		<h1>
			Relatório de Quantidades
		</h1>
		
		<div id="topo">
			Tipo de Gráfico:
			<select id="tipoDeRelatorio" name="tipoDeRelatorio">
				<option value="semana" chart="FCF_Line">Por Semana</option>
				<option value="mes" chart="FCF_Line">Por Mês</option>
				<option value="ano" chart="FCF_Line">Por Ano</option>
				<option value="categoria" chart="FCF_Bar2D">Por Categoria</option>
				<option value="cliente" chart="FCF_Bar2D">Por Cliente</option>
				<option value="modulo" chart="FCF_Bar2D">Por Módulo</option>
				<option value="sprint" chart="FCF_Bar2D">Por Sprint</option>
				<option value="esforco" chart="FCF_Bar2D">Por Esforço</option>
				<option value="valor" chart="FCF_Bar2D">Por Valor de Negócio</option>
			</select>
			
			Tipo de Ticket:
			<select id="tipoDeTicketKey" name="tipoDeTicketKey">
				<option value="-1">Tudo</option>
				<option value="2">Estórias</option>
				<option value="3">Defeitos</option>
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
				var tipoDeRelatorio = $("#tipoDeRelatorio").val();
				var tipoDeTicketKey = $("#tipoDeTicketKey").val();
				var parametros = "?dataInicial=" + di + "&dataFinal=" + df +"&tipoDeRelatorio=" + tipoDeRelatorio + "&tipoDeTicketKey=" + tipoDeTicketKey;
				var url = encodeURIComponent("${raiz}relatorios/quantidades/gerar.xml" + parametros);
				var chartType = $('#tipoDeRelatorio').find('option:selected').attr('chart');
				var chart = new FusionCharts("${raiz}commons/charts/"+chartType+".swf", "chart", "920", "500");
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