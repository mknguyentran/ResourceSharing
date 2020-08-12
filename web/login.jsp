<%-- 
    Document   : index
    Created on : Jun 29, 2020, 12:10:01 PM
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
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <script>
            function onSignIn(googleUser) {
                var id_token = googleUser.getAuthResponse().id_token;
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut()
                document.getElementById("loginWithGoogleToken").setAttribute("value", id_token);
                document.getElementById("loginWithGoogleForm").submit();
            }
        </script>
        <meta name="google-signin-client_id" content="405075026780-p9fg5nvcmq3pc8gh0epplui40o3ut7hr.apps.googleusercontent.com">
    </head>
    <body>
        <%@include file="/template/header.jsp" %>
        <s:form action="login" method="POST" id="loginForm">
            Email
            <br/>
            <p class="fieldError"><s:property value="fieldErrors['email']"/></p>
            <s:textfield name="email"/>
            <br/>
            Password
            <br/>
            <p class="fieldError"><s:property value="fieldErrors['password']"/></p>
            <s:password name="password"/>
            <br/>
            <p class="fieldError"><s:property value="fieldErrors['recaptcha']"/></p>
            <div class="g-recaptcha" data-sitekey="6LeZXasZAAAAAKQ2sMh7lDG54wdj8cRJebvoPViC"></div>
            <button type="submit">Login</button>
        </s:form>
        <s:form action="loginWithGoogle" method="POST" id="loginWithGoogleForm">
            <h2 style="color: gray">or</h2>
            <s:hidden name="token" id="loginWithGoogleToken"/>
            <div class="g-signin2" data-onsuccess="onSignIn"></div>
        </s:form>
        <a href="signUp.jsp">Sign Up</a>
    </body>
</html>
