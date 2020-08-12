<%-- 
    Document   : userHome
    Created on : Jul 1, 2020, 9:57:04 AM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing - Home</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <%@include file="/template/managerRequestSearchBar.jsp" %>
        <%@include file="/template/managerPageLink.jsp" %>
        <s:if test="%{requestList != null}">
            <s:if test="%{!requestList.isEmpty()}">
                <s:iterator value="requestList">
                    <s:url action="loadRequestDetail" var="requestDetailLink" escapeAmp="false">
                        <s:param name="id" value="id"/>
                    </s:url>
                    <s:a href="%{requestDetailLink}">
                        <div class="requestCard">
                            <h2>Requested by <s:property value="requestedUser"/></h2>
                            <p>at <s:property value="displaySentAt"/></p>
                            <p><span style="font-weight: bold">Booking date: </span><s:property value="displayFromDate"/> - <s:property value="displayToDate"/></p>
                            <p><span style="font-weight: bold">Status: </span><s:property value="status"/></p>
                        </div>
                    </s:a>
                    <br/>
                </s:iterator>
            </s:if>
        </s:if>
        <s:if test="%{requestList == null || requestList.isEmpty()}">
            <p>No request found!</p>
        </s:if>
    </body>
</html>
