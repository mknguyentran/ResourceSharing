<%-- 
    Document   : signUp
    Created on : Jun 29, 2020, 11:37:59 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Resource Sharing - Sign Up</title>
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/universal.css"/>
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <s:form action="signUp" method="POST">
            <s:label for="email">Email</s:label>
            <p class="fieldError"><s:property value="fieldErrors['email']"/></p>
            <s:textfield name="email"/><br/>
            <s:label for="email">Password<br/></s:label>
            <p class="fieldError"><s:property value="fieldErrors['password']"/></p>
            <s:password name="password"/><br/>
            <s:label for="email">Confirm Password<br/></s:label>
            <p class="fieldError"><s:property value="fieldErrors['confirmPassword']"/></p>
            <s:password name="confirmPassword"/><br/>
            <s:label for="email">Name<br/></s:label>
            <p class="fieldError"><s:property value="fieldErrors['name']"/></p>
            <s:textfield name="name"/><br/>
            <s:label for="email">Address<br/></s:label>
            <p class="fieldError"><s:property value="fieldErrors['address']"/></p>
            <s:textfield name="address"/><br/>
            <s:label for="email">Phone Number<br/></s:label>
            <p class="fieldError"><s:property value="fieldErrors['phoneNumber']"/></p>
            <s:textfield name="phoneNumber"/><br/>
            <s:submit value="Sign Up"/>
        </s:form>
    </body>
</html>
