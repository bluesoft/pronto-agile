<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Backlog</title>
		<%@ include file="/commons/scripts/scripts.jsp" %>
	</head>
	<body>
		<h1>Backlog</h1>
		<table style="width: 100%">
			<thead>
			<tr>
				<th style="width: 100%">Nome</th>
				<th style="width: 16px"></th>
				<th style="width: 16px"></th>
			</tr>
			</thead>
			<tbody>
			<c:set var="cor" value="${true}"/>
			<c:forEach items="${tickets}" var="t">
				<c:set var="cor" value="${!cor}"/>
				<tr style="height: 18px" class="${cor ? 'even' : 'odd'}">
					
				</tr>
			</c:forEach>
			</tbody>
		</table>	
		
		
	</body>
</html>