<%-- 
    Document   : header
    Created on : Jun 29, 2020, 6:05:21 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
    <s:if test="%{#session.ACCOUNT != null}">
        <s:if test="%{#session.ACCOUNT.role.equals('leader') || #session.ACCOUNT.role.equals('employee')}">
            <a href="searchResource.action"><h1>Resource Sharing</h1></a>
            <h1>Hello, <s:property value="%{#session.ACCOUNT.name}"/></h1>
            <a href="logout">Logout</a><br/>
            <s:if test="%{!#session.ACCOUNT.status.equals('new')}">
                <s:a href="newRequestStep1.action">New Request</s:a><br/>
                <s:a href="searchRequestHistory.action">Request History</s:a>
            </s:if>
        </s:if>
        <s:elseif test="%{#session.ACCOUNT.role.equals('manager')}">
            <a href="searchRequest.action"><h1>Resource Sharing</h1></a>
            <h1>Hello, <s:property value="%{#session.ACCOUNT.name}"/></h1>
            <a href="logout">Logout</a><br/>
        </s:elseif>
    </s:if>
    <s:else>
        <a href="logout"><h1>Resource Sharing</h1></a>
    </s:else>
</body>
