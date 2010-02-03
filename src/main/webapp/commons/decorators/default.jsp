<%@ include file="/commons/taglibs.jsp"%>
<html>
    <head>
        <title>Pronto Agile | <decorator:title/></title>
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/themes/simplicity/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/commons/themes/simplicity/print.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/themes/menu.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/commons/scripts/markitup/skins/markitup/style.css'/>" />
        <%@ include file="/commons/scripts/scripts.jsp"%>
        <decorator:head/>
    </head>
	<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
	    <div id="page">
	        <div id="header" class="clearfix">
	            <jsp:include page="/commons/header.jsp"/>
	        </div>
			<jsp:include page="/commons/menuRapido.jsp"/>
	        <div id="content" class="clearfix">

	            <c:if test="${mensagem ne null || param.mensagem ne null}">
	            	<br>
					<div class="ui-widget"> 
						<div class="ui-state-highlight ui-corner-all" style="padding-left: 10px;"> 
							<p><br><span class="ui-icon ui-icon-info" style="float: left;"> </span> 
							&nbsp; ${mensagem}${param.mensagem}</p> 
						</div> 
					</div>
	            </c:if>
					            
	            <c:if test="${erro ne null || param.erro ne null}">
					<br>
					<div class="ui-widget">
						<div class="ui-state-error ui-corner-all" style="padding-left: 10px;">
							<p><br><span class="ui-icon ui-icon-alert" style="float: left;"> </span>
							&nbsp; ${erro}${param.erro}</p>
						</div>
					</div>
	            </c:if>
	            
	             <div id="nav">
	                <div class="wrapper">
	                    <h2 class="accessibility">Navigation</h2>
	                    <jsp:include page="/commons/menu.jsp"/>
	                </div>
	                <hr />
	            </div><!-- end nav -->
	            
	            <div id="main">
	                <h1><decorator:getProperty property="page.heading"/></h1>
	                <decorator:body/>
	            </div>
	            <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
	        </div>

	        <div id="footer" class="clearfix">
	            <jsp:include page="/commons/footer.jsp"/>
	        </div>
	    </div>
	</body>
</html>