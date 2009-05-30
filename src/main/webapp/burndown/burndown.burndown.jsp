<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Burndown Chart</title>
	</head>
	<body>
		
		
 	<c:url var="open-flash-chart" value="open-flash-chart.swf"/>
	<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
	        codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"
	        width="500"
			height="250" id="graph-2" align="middle">
	 
		<param name="allowScriptAccess" value="sameDomain" />
		<param name="movie" value="open-flash-chart.swf" />
		<param name="quality" value="high" />
		<embed src="${open-flash-chart}"
			   quality="high"
			   bgcolor="#FFFFFF"
			   width="500"
			   height="250"
			   name="open-flash-chart"
			   align="middle"
			   allowScriptAccess="sameDomain"
			   type="application/x-shockwave-flash"
			   pluginspage="http://www.macromedia.com/go/getflashplayer" />
	</object>
		
	</body>
</html>
