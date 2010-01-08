<%@ include file="/commons/taglibs.jsp"%>
<c:url var="chart" value="/open-flash-chart.swf"/>
<c:url var="data" value="/burndown/data.action"/>
<c:url var="urlKanban" value="/kanban/kanban.action" />
<c:url var="retrospectivaUrl" value="/retrospectiva/ver.action"/>
<c:url var="urlSprint" value="/sprint/"/>
<html>
	<head>
		<title>Burndown Chart</title>
		<script type="text/javascript">
		swfobject.embedSWF(
		  "${chart}", "my_chart", "800", "480",
		  "9.0.0", "expressInstall.swf",
		  {"data-file":"${data}?sprintKey=${sprintKey}"}
		  );
		</script>
	</head>
	<body>
		<h1>
			Burndown Chart
			<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="goTo('${urlSprint}../ticket/listarPorSprint.action?sprintKey=${sprintKey}')"/>
			<pronto:icons name="kanban.png" title="Ver Kanban" onclick="goTo('${urlKanban}?sprintKey=${sprintKey}')"/>
			<pronto:icons name="retrospectiva.png" title="Retrospectiva" onclick="goTo('${retrospectivaUrl}?sprintKey=${sprintKey}')"/>
		</h1>
		<div align="center">
			<div id="my_chart"></div>
		</div>
	</body>
</html>