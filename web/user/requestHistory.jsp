<%-- 
    Document   : requestHistory
    Created on : Jul 19, 2020, 12:38:43 AM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing - History</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <%@include file="/template/requestSearchBar.jsp" %>
        <s:if test="%{requestList != null}">
            <s:if test="%{!requestList.isEmpty()}">
                <table border="1" cellpadding="5">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Sent At</th>
                            <th>Booking Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="requestList" status="counter">
                            <tr>
                                <td><s:property value="#counter.count"/></td>
                                <td><s:property value="displaySentAt"/></td>
                                <td><s:property value="displayFromDate"/> - <s:property value="displayToDate"/></td>
                                <td><s:property value="status"/></td>
                                <td>
                                    <s:form action="invalidateRequest" method="POST">
                                        <s:hidden name="id"/>
                                        <s:hidden name="searchResourceName" />
                                        <s:hidden name="searchFromDateString" />
                                        <s:hidden name="searchToDateString" />
                                        <s:submit cssClass="removeButton" value="Remove"/>
                                    </s:form>
                                </td>
                                <td>
                                    <s:form action="searchRequestHistory" method="POST">
                                        <s:hidden name="id" />
                                        <s:hidden name="searchResourceName" />
                                        <s:hidden name="searchFromDateString" />
                                        <s:hidden name="searchToDateString" />
                                        <s:submit value="View Detail"/>
                                    </s:form>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </s:if>
        </s:if>
        <s:if test="%{requestList == null || requestList.isEmpty()}">
            <p>You have no request yet</p>
        </s:if>
        <s:if test="%{request != null}">
            <table border="1" cellpadding="5">
                <thead>
                    <tr>
                        <th>Resource</th>
                        <th>Amount Requested</th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator value="request.detail">
                        <tr>
                            <td><s:property value="key.name"/></td>
                            <td><s:property value="value"/></td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </s:if>
    </body>
</html>
