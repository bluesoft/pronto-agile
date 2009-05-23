<%@ include file="/commons/taglibs.jsp"%>
<html>
    <head>
        <title>Pronto Agile | <decorator:title/></title>
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/themes/simplicity/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/commons/themes/simplicity/print.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/themes/menu.css'/>" />
        <%@ include file="/commons/scripts/scripts.jsp"%>
        <decorator:head/>
    </head>
	<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
	
	    <div id="page">
	        <div id="header" class="clearfix">
	            <jsp:include page="/commons/header.jsp"/>
	        </div>
	
	        <div id="content" class="clearfix">
	            <div id="main">
	                <h1><decorator:getProperty property="page.heading"/></h1>
	                <decorator:body/>
	            </div>
	            <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
	
	            <div id="nav">
	                <div class="wrapper">
	                    <h2 class="accessibility">Navigation</h2>
	                    <jsp:include page="/commons/menu.jsp"/>
	                </div>
	                <hr />
	            </div><!-- end nav -->
	        </div>
	
	        <div id="footer" class="clearfix">
	            <jsp:include page="/commons/footer.jsp"/>
	        </div>
	    </div>
	</body>
</html>