<%-- 
    Document   : searchBar
    Created on : Jul 7, 2020, 2:44:50 PM
    Author     : Mk
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
    <s:form action="searchRequest">
        <s:textfield name="searchKeyword" placeholder="search by resource name, requested user, or sent date(yyyy-mm-dd)" size="60"/>
        <s:select list="statusList" headerKey="0" headerValue="---Filter by Status---" name="searchStatus" listKey="id" listValue="name" label="Status"/>
        <s:submit value=">"/>
    </s:form>
</body>
