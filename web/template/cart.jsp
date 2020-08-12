<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/ResourceSharing/css/cart.css"/>
    </head>
    <body>
        <s:if test="%{displayCart != null}"> 
            <div id="content">
                <p><span style="font-weight: bold">Request Date: </span><s:property value="#session.REQUEST.displayFromDate"/> - <s:property value="#session.REQUEST.displayToDate"/></p>
                <s:form action="newRequestStep1" method="POST">
                    <s:submit value="Change"/>
                </s:form>
                <s:if test="%{error != null}">
                    <font color="red"><s:property value="error"/></font><br/>
                </s:if>
                <s:if test="%{!displayCart.isEmpty()}">
                    <table cellpadding="5">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Color</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator value="displayCart">
                                <s:if test="%{key.isSufficient()}" >
                                    <tr>
                                        <td><s:property value="%{key.name}"/></td>
                                        <td><s:property value="%{key.category}"/></td>
                                        <td><s:property value="%{key.color}"/></td>
                                        <td>
                                            <s:if test="%{value == 1}">
                                                <s:form action="decreaseFromRequest" method="POST"  cssClass="minusButton" onclick="return confirm('Are you sure to remove this resource from the request?')">
                                                    <s:hidden name="id" value="%{key.id}"/>
                                                    <s:hidden name="searchName" />
                                                    <s:hidden name="searchCategory" />
                                                    <s:submit cssClass="button" value="-"/>
                                                </s:form>
                                            </s:if>
                                            <s:elseif test="%{value > 1}">
                                                <s:form action="decreaseFromRequest" method="POST"  cssClass="minusButton">
                                                    <s:hidden name="id" value="%{key.id}"/>
                                                    <s:hidden name="searchName" />
                                                    <s:hidden name="searchCategory" />
                                                    <s:submit cssClass="button" value="-"/>
                                                </s:form>
                                            </s:elseif >
                                            <p class="amount"><s:property value="%{value}"/></p>
                                            <s:form action="addToRequest" method="POST" cssClass="plusButton">
                                                <s:hidden name="id" value="%{key.id}"/>
                                                <s:hidden name="searchName" />
                                                <s:hidden name="searchCategory" />
                                                <s:submit cssClass="button" value="+"/>
                                            </s:form>
                                        </td>
                                        <td>
                                            <s:form action="removeFromRequest" method="POST" onclick="return confirm('Are you sure to remove this item from the cart?')">
                                                <s:hidden name="id" value="%{key.id}"/>
                                                <s:hidden name="searchName" />
                                                <s:hidden name="searchCategory" />
                                                <s:submit cssClass="removeButton" value="Remove"/>
                                            </s:form>
                                        </td>
                                    </tr>
                                </s:if>
                                <s:else>
                                <p>Resource '<s:property value="%{key.name}"/>' in your request is no longer available</p>
                                <tr class="unavailable">
                                    <td><s:property value="%{key.name}"/></td>
                                    <td><s:property value="%{key.category}"/></td>
                                    <td><s:property value="%{key.color}"/></td>
                                    <td><s:property value="%{value}"/></td>
                                    <td>
                                        <s:form action="removeFromRequest" method="POST">
                                            <s:hidden name="id" value="%{key.id}"/>
                                            <s:submit cssClass="removeButton" value="Remove"/>
                                        </s:form>
                                    </td>
                                </tr>
                            </s:else>
                        </s:iterator>
                        </tbody>
                    </table>
                    <s:form action="submitRequest">
                        <s:submit value="Submit"/>
                    </s:form>
                </s:if>
                <s:elseif test="%{displayCart == null || displayCart.isEmpty()}">
                    <p>Your cart is empty =((</p>
                </s:elseif>
            </s:if>
        </div>
    </body>
</html>
