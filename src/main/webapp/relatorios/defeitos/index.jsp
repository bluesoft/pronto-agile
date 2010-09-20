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
				<option value="categoria">Por Categoria</option>
				<option value="cliente">Por Cliente</option>
				<option value="modulo">Por Módulo</option>
				<option value="sprint">Por Sprint</option>
			</select>
			
			<fmt:formatDate var="strDataInicial" value="${dataInicial}"/>
			Data Inicial: <input type="text" id="dataInicial" class="required dateBr" value="${strDataInicial}" size="12"/>
			<fmt:formatDate var="strDataFinal" value="${dataFinal}"/>
			Data Final: <input type="text" id="dataFinal" class="required dateBr" value="${strDataFinal}" size="12"/>
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
				var chart = new FusionCharts("${raiz}/commons/charts/FCF_Bar2D.swf", "chart", "920", "500");
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