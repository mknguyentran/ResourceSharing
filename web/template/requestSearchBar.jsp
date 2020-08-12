<%-- 
    Document   : searchBar
    Created on : Jul 7, 2020, 2:44:50 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
    <body>
        <s:form action="searchRequestHistory">
            <s:textfield name="searchResourceName" placeholder="search by resource name"/>
            From: <s:textfield type="date" name="searchFromDateString"/>
            To: <s:textfield type="date" name="searchToDateString"/>
            <s:submit value=">"/>
        </s:form>
    </body>
