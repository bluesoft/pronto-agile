<%@ include file="/commons/taglibs.jsp"%>
<c:url var="chart" value="/open-flash-chart.swf"/>
<c:url var="data" value="/burndown/data.action"/>
<html>
	<head>
		<title>Burndown Chart</title>
		<script type="text/javascript">
		swfobject.embedSWF(
		  "${chart}", "my_chart", "550", "200",
		  "9.0.0", "expressInstall.swf",
		  {"data-file":"${data}"}
		  );
		</script>
	</head>
	<body>
		
		<div id="my_chart"></div>
 	
	</body>
</html>
