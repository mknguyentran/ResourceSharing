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
        <%@include file="/template/header.jsp"%>
        <%@include file="/template/searchBar.jsp"%>
        <%@include file="/template/pageLink.jsp"%>
        <br/>
        <s:if test="%{resourceList != null}">
            <s:if test="%{resourceList.size() > 0}">
                <s:iterator value="resourceList">
                    <div class="resourceCard">
                        <h2><s:property value="name"/></h2>
                        <p>Category: <s:property value="category"/></p>
                        <p>Color: <s:property value="color"/></p>
                        <s:if test="%{searchFromDateString != null && searchToDateString != null}">
                            <s:if test="%{!searchFromDateString.isEmpty() && !searchToDateString.isEmpty()}">
                                <p>Availability among <s:property value="searchFromDateString"/> - <s:property value="searchToDateString"/>: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                            </s:if>
                            <s:elseif test="%{!searchFromDateString.isEmpty()}">
                                <p>Availability on <s:property value="searchFromDateString"/>: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                            </s:elseif>
                            <s:elseif test="%{!searchToDateString.isEmpty()}">
                                <p>Availability on <s:property value="searchToDateString"/>: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                            </s:elseif>
                            <s:else>
                                <p>Current Availability: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                            </s:else>
                        </s:if>
                        <s:else>
                            <p>Current Availability: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                        </s:else>
                    </div>
                    <br/>
                </s:iterator>
            </s:if>
            <s:else>
                <p>No result found</p>
            </s:else>
        </s:if>
        <s:else>
            <p>No result found</p>
        </s:else> 
    </body>
</html>
