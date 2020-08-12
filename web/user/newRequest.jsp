<%-- 
    Document   : newRequest
    Created on : Jul 16, 2020, 8:17:56 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing - New Request</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <%@include file="/template/cart.jsp" %>
        <p>Step <s:property value="#session.REQUEST.step"/></p>
        <s:if test="%{#session.REQUEST != null}">
            <s:if test="%{#session.REQUEST.step == 1}">
                <s:form action="newRequestStep2" method="POST">
                    <p class="fieldError"><s:property value="error"/></p>
                    From: <s:textfield type="date" name="fromDateString"/>
                    Date: <s:textfield type="date" name="toDateString"/>
                    <s:submit value="Next"/>
                </s:form>
            </s:if>
            <s:elseif test="%{#session.REQUEST.step == 2}">
                <%@include file="/template/searchBarSmall.jsp" %>
                <%@include file="/template/requestPageLink.jsp" %>
                <s:if test="%{resourceList != null}">
                    <s:if test="%{resourceList.size() > 0}">
                        <s:iterator value="resourceList">
                            <div class="resourceCard">
                                <h2><s:property value="name"/></h2>
                                <p>Category: <s:property value="category"/></p>
                                <p>Color: <s:property value="color"/></p>
                                <p>Availability: <s:property value="availableAmount"/>/<s:property value="quantity"/></p>
                                <s:form action="addToRequest" method="POST">
                                    <s:hidden name="id" value="%{id}"/>
                                    <s:hidden name="searchName" />
                                    <s:hidden name="searchCategory" />
                                    <s:submit value="Add to Request"/>
                                </s:form>
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
            </s:elseif>
        </s:if>
    </body>
</html>
