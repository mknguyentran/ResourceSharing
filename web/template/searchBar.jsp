<%-- 
    Document   : searchBar
    Created on : Jul 7, 2020, 2:44:50 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
    <body>
        <s:form action="searchResource">
            <s:textfield name="searchName" placeholder="search by name..."/>
            <s:select list="categoryList" headerKey="0" headerValue="---Filter by Category---" name="searchCategory" listKey="id" listValue="name" label="Category"/>
            From: <s:textfield type="date" name="searchFromDateString"/>
            To: <s:textfield type="date" name="searchToDateString"/>
            <s:submit value=">"/>
        </s:form>
    </body>
