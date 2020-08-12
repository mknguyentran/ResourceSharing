<%-- 
    Document   : requestDetail
    Created on : Jul 19, 2020, 5:42:10 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <div id="content">
            <s:if test="%{request != null}">
                <h2><s:property value="request.requestedUser"/>'s Request</h2>
                <p>Sent at: <s:property value="request.sentAt"/></p>
                <p>Booking date: <s:property value="request.displayFromDate"/> - <s:property value="request.displayToDate"/></p>
                <p>Status: <s:property value="request.status"/></p>
                <p>Detail</p>
                <s:if test="%{!error.isEmpty()}">
                    <font color="red"><s:property value="error"/></font>
                </s:if>
                <table border="1" cellpadding="5">
                    <thead>
                        <tr>
                            <th>Resource</th>
                            <th>Amount Requested</th>
                                <s:if test="%{request.status.equals('new')}">
                                <th>Available</th>
                                </s:if>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="request.detail">
                            <tr>
                                <td><s:property value="key.name"/> (ID: <s:property value="key.id"/>)</td>
                                <td><s:property value="value"/></td>
                                <s:if test="%{request.status.equals('new')}">
                                    <td><s:property value="key.availableAmount"/></td>
                                </s:if>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
                <s:if test="%{request.status.equals('new')}">
                    <s:form action="deleteRequest" method="POST">
                        <s:hidden name="id" value="%{request.id}"/>
                        <s:submit value="Delete"/>
                    </s:form>
                    <s:form action="acceptRequest" method="POST">
                        <s:hidden name="id" value="%{request.id}"/>
                        <s:submit value="Accept"/>
                    </s:form>
                </s:if>
            </s:if>
            <s:else>
                <p>Can not load this request detail</p>
            </s:else>
        </div>
    </body>
</html>
