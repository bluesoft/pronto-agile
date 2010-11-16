<%@ include file="/commons/taglibs.jsp"%>
<c:url var="chart" value="/commons/charts/open-flash-chart.swf"/>
<c:url var="data" value="/burndown/data/${sprint.sprintKey}?considerarFimDeSemana=${considerarFimDeSemana}"/>
<html>
	<head>
		<title>Burndown Chart</title>
		<script type="text/javascript">
		swfobject.embedSWF(
		  "${chart}", "my_chart", "800", "480",
		  "9.0.0", "expressInstall.swf",
		  {"data-file":"${data}"}
		  );

		function recarregar() {
			var sprintKey = $("#sprintKey").val();
			var considerarFimDeSemana = $('#considerarFimDeSemana').val();
			goTo(pronto.raiz + 'burndown/' + sprintKey + '?considerarFimDeSemana='+considerarFimDeSemana);
		}
		  
		</script>
	</head>
	<body>
		<h1>
			Burndown Chart do Sprint ${sprint.nome}
			<%@ include file="/commons/sprintLinks.jsp" %>
		</h1>
		
		<c:if test="${fn:length(sprints) gt 1}">
			<div align="right">
				Sprint: 
				<form:select path="sprint.sprintKey" onchange="recarregar()" id="sprintKey">
					<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
				</form:select>
				Considerar Finais de Semana: 
				<select name="considerarFimDeSemana" onchange="recarregar()"  id="considerarFimDeSemana">
					<option ${considerarFimDeSemana eq true ? 'selected' : ''} value="true">Sim</option>
					<option ${considerarFimDeSemana eq false ? 'selected' : ''} value="false">Não</option>
				</select>
			</div>
		</c:if>
		
		<div align="center">
			<div id="my_chart"></div>
		</div>
	</body>
</html>