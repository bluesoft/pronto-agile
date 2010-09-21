<%@ include file="/commons/taglibs.jsp" %>

<c:url var="raiz" value="/"/>
<script>
	var pronto = pronto? pronto : {};
	pronto.raiz = '${raiz}';
</script>

<script src="http://www.google.com/jsapi"></script>
<script>
 	google.load("jquery", "1.4.2");
 	google.load("jqueryui", "1.8.4");
</script>

<script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery.validate/1.7/jquery.validate.pack.js"></script>

<c:url var="jqueryValidatorBr" value="/commons/scripts/jquery.validate.br.js"/>
<script type="text/javascript" src="${jqueryValidatorBr}"></script>

<c:url var="markItUpSettingsUrl" value="/commons/scripts/markitup/set.js"/>
<script type="text/javascript" src="${markItUpSettingsUrl}"></script>

<c:url var="markItUpUrl" value="/commons/scripts/markitup/jquery.markitup.pack.js"/>
<script type="text/javascript" src="${markItUpUrl}"></script>

<c:url var="hotKeys" value="/commons/scripts/jquery.hotkeys-0.7.9.js"/>
<script type="text/javascript" src="${hotKeys}"></script>

<c:url var="shortKeys" value="/commons/scripts/jquery.shortkeys.js"/>
<script type="text/javascript" src="${shortKeys}"></script>

<c:url var="swfObject" value="/commons/scripts/swfobject.js"/>
<script type="text/javascript" src="${swfObject}"></script>

<c:url var="jqueryBlockUI" value="/commons/scripts/jquery.blockUI.js"/>
<script type="text/javascript" src="${jqueryBlockUI}"></script>

<c:url var="gritter" value="/commons/scripts/gritter/jquery.gritter.min.js"/>
<script type="text/javascript" src="${gritter}"></script>

<c:url var="metaDataUrl" value="/commons/scripts/jquery.metadata.js"/>
<script type="text/javascript" src="${metaDataUrl}"></script>

<c:url var="mbMenuJsUrl" value="/commons/scripts/mbMenu.js"/>
<script type="text/javascript" src="${mbMenuJsUrl}"></script>

<c:url var="menuJsUrl" value="/commons/scripts/menu.js"/>
<script type="text/javascript" src="${menuJsUrl}"></script>

<c:url var="prontoJsUrl" value="/commons/scripts/pronto.js"/>
<script type="text/javascript" src="${prontoJsUrl}"></script>

<c:url var="fusionCharts" value="/commons/scripts/FusionCharts.js"/>
<script type="text/javascript" src="${fusionCharts}"></script>

<script>
$(function(){
	$.ajaxSetup({ traditional: true });
});
</script>