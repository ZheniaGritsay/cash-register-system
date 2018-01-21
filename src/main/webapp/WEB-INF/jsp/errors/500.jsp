<%--
  Created by IntelliJ IDEA.
  User: Zhenia
  Date: 1/24/2017
  Time: 10:09 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <%@include file="../parts/set-lang.jspf" %>
    <title>500</title>
    <%@include file="../parts/header.jspf"%>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <h3 style="position: relative; left: 80px"><fmt:message key="error.500" bundle="${bundle}"/></h3>
            <img src="../../../resources/image/errors/500.png" width="300" height="200">
        </div>
        <div class="col-md-4"></div>
    </div>
</div>
</body>
</html>
