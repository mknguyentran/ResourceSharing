<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="pageLinkBar">
    <s:if test="%{pagesAmount != null}">
        <s:if test="%{pagesAmount > 0}">
            <s:if test="%{page != 1 && page != null}">
                <s:url var="pageLink" action="newRequestStep2" escapeAmp="false">
                    <s:param name="searchName" value="searchName"/>
                    <s:param name="searchCategory" value="searchCategory"/>
                    <s:param name="searchFromDateString" value="searchFromDateString"/>
                    <s:param name="searchToDateString" value="searchToDateString"/>
                    <s:param name="page" value="page - 1"/>
                </s:url>
                <a href="<s:property value="#pageLink"/>" class="pageLink"><</a>
            </s:if>
            <s:else>
                <a class="pageLink"><</a>
            </s:else>
            <s:iterator begin="1" end="pagesAmount" status="counter">
                <s:url var="pageLink" action="newRequestStep2" escapeAmp="false">
                    <s:param name="searchName" value="searchName"/>
                    <s:param name="searchCategory" value="searchCategory"/>
                    <s:param name="searchFromDateString" value="searchFromDateString"/>
                    <s:param name="searchToDateString" value="searchToDateString"/>
                    <s:param name="page" value="#counter.count"/>
                </s:url>
                <a href="<s:property value="#pageLink"/>" class="pageLink" <s:if test="%{page == #counter.count || (page == null && #counter.count == 1)}">id="currentPage"</s:if>><s:property value="#counter.count"/></a>
            </s:iterator>
            <s:if test="%{page != pagesAmount && page != null}">
                <s:url var="pageLink" action="newRequestStep2" escapeAmp="false">
                    <s:param name="searchName" value="searchName"/>
                    <s:param name="searchCategory" value="searchCategory"/>
                    <s:param name="searchFromDateString" value="searchFromDateString"/>
                    <s:param name="searchToDateString" value="searchToDateString"/>
                    <s:param name="page" value="page + 1"/>
                </s:url>
                <a href="<s:property value="#pageLink"/>" class="pageLink">></a>
            </s:if>
            <s:elseif test="%{page == null && pagesAmount > 1}">
                <s:url var="pageLink" action="newRequestStep2" escapeAmp="false">
                    <s:param name="searchName" value="searchName"/>
                    <s:param name="searchCategory" value="searchCategory"/>
                    <s:param name="searchFromDateString" value="searchFromDateString"/>
                    <s:param name="searchToDateString" value="searchToDateString"/>
                    <s:param name="page" value="2"/>
                </s:url>
                <a href="<s:property value="#pageLink"/>" class="pageLink">></a>
            </s:elseif>
            <s:else>
                <a class="pageLink">></a>
            </s:else>
        </s:if>
    </s:if>
</div>
