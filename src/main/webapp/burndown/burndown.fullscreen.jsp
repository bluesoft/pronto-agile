<%@ include file="/commons/taglibs.jsp"%>
<c:url var="chart" value="/open-flash-chart.swf"/>
<c:url var="data" value="/burndown/data"/>
<html>
	<head>
		<meta http-equiv="refresh" content="120" />
		<title>Pronto! Burndown Chart</title>
		<%@ include file="/commons/scripts/scripts.jsp"%>
		<script type="text/javascript">
		swfobject.embedSWF(
		  "${chart}", "my_chart", "100%", "100%",
		  "9.0.0", "expressInstall.swf",
		  {"data-file":"${data}/${sprintKey}"}
		  );
		</script>
		<c:url var="swfObject" value="/commons/scripts/swfobject.js"/>
	</head>
	<body>
		<div align="center">
			<div id="my_chart"></div>
		</div>
	</body>
</html>