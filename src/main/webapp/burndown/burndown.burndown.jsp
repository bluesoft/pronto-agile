<%@ include file="/commons/taglibs.jsp"%>
<c:url var="chart" value="/open-flash-chart.swf"/>
<c:url var="data" value="/burndown/data/${sprint.sprintKey}"/>
<html>
	<head>
		<title>Burndown Chart</title>
		<script type="text/javascript">
		swfobject.embedSWF(
		  "${chart}", "my_chart", "800", "480",
		  "9.0.0", "expressInstall.swf",
		  {"data-file":"${data}"}
		  );

		function recarregar(sprintKey) {
			goTo(pronto.raiz + 'burndown/' + sprintKey);
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
				<form:select path="sprint.sprintKey" onchange="recarregar(this.value)">
					<form:options items="${sprints}" itemLabel="nome" itemValue="sprintKey"/>
				</form:select>
			</div>
		</c:if>
		
		<div align="center">
			<div id="my_chart"></div>
		</div>
	</body>
</html>