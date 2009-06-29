<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ tag body-content="empty"  %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="onclick" required="false" %>

<c:url var="iconsFolder" value="/commons/icons"/>
<img src="${iconsFolder}/${name}" title="${title}" alt="${title}" onclick="${onclick}" style="cursor:pointer"/>
