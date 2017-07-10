<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
    <title>书本</title>
</head>
<body>

<%--<form id="haha" action="/book/create" method="post">--%>
<form id="haha" action="/book/q" method="post">
    <%--<input type="text" name="name" value="" placeholder="字段" />--%>
    <input type="text" name="q" value="" placeholder="字段" />
    <input type="submit" value="提交">
</form>

<c:forEach items="${booklist}" var="book">
    <h4>${book.name}</h4><h4>${book.id}</h4><h4>${book.price}</h4><h4>${book.pic}</h4><h4>${book.description}</h4>
</c:forEach>

</body>
</html>
