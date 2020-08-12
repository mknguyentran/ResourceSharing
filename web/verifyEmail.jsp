<%-- 
    Document   : verifyEmail
    Created on : Jul 1, 2020, 11:42:17 AM
    Author     : Mk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="template/header.jsp" %>
        <h2>You need to verify your email account before continue using Resource Sharing</h2>
        <s:form action="verifyEmail" method="POST">
            <s:label for="code">Enter the 4-digit verification code sent to <span style="font-weight: bold"><s:property value="#session.ACCOUNT.email"/></span></s:label><br/>
            <s:if test="fieldErrors['code'] != null"><font color="red"><s:property value="fieldErrors['code']"/></font><br/></s:if>
            <s:textfield name="code"/>
            <s:submit value="Submit"/>
        </s:form>
            <p>Each verification code will expire 5 minutes after being sent, or if you requested a new one</p>
            <p>Did not get a verification code? <s:a action="requestNewCode">Request a new verification code</s:a></p>
            <p><s:property value="alert"/></p>
    </body>
</html>
