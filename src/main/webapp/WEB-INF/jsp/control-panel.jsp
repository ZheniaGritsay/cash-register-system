<%--
  Created by IntelliJ IDEA.
  User: Zhenia
  Date: 1/15/2018
  Time: 11:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <%@include file="parts/header.jspf"%>
</head>
<body>
<%@include file="parts/set-lang.jspf"%>
<%@include file="parts/nav-bar.jspf"%>
<div class="container mt-sm-5">
    <div class="row">
        <div class="col-md-4">
            <div class="well" style="width: 300px">
                <img src="../../resources/image/users.png" class="img-responsive" style="height: 300px; width: 100%">
                <div class="container">
                    <h5 class="mt-1"><fmt:message key="label.users" bundle="${bundle}"/></h5>
                    <button type="button" class="btn btn-outline-primary mb-sm-2"
                            onclick="location.href='${pageContext.request.contextPath}/app/users/view'">
                        <fmt:message key="button.edit" bundle="${bundle}"/>
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="well" style="width: 300px">
                <img src="../../resources/image/employees.png" class="img-responsive" style="height: 300px; width: 100%">
                <div class="container">
                    <h5 class="mt-1"><fmt:message key="label.employees" bundle="${bundle}"/></h5>
                    <button type="button" class="btn btn-outline-primary mb-sm-2"
                            onclick="location.href='${pageContext.request.contextPath}/app/employees/view'">
                        <fmt:message key="button.edit" bundle="${bundle}"/>
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="well" style="width: 300px">
                <img src="../../resources/image/products.png" class="img-responsive" style="height: 300px; width: 100%">
                <div class="container">
                    <h5 class="mt-1"><fmt:message key="label.products" bundle="${bundle}"/></h5>
                    <button type="button" class="btn btn-outline-primary mb-sm-2"
                            onclick="location.href='${pageContext.request.contextPath}/app/products/view'">
                        <fmt:message key="button.edit" bundle="${bundle}"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container mt-sm-5">
    <div class="row">
        <div class="col-md-4">
            <div class="well" style="width: 300px">
                <img src="../../resources/image/checks.png" class="img-responsive" style="height: 300px; width: 100%">
                <div class="container">
                    <h5 class="mt-1"><fmt:message key="label.checks" bundle="${bundle}"/></h5>
                    <button type="button" class="btn btn-outline-primary mb-sm-2"
                            onclick="location.href='${pageContext.request.contextPath}/app/checks/view'">
                        <fmt:message key="button.edit" bundle="${bundle}"/>
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="well" style="width: 300px">
                <img src="../../resources/image/reports.png" class="img-responsive" style="height: 300px; width: 100%">
                <div class="container">
                    <h5 class="mt-1"><fmt:message key="label.reports" bundle="${bundle}"/></h5>
                    <button type="button" class="btn btn-outline-primary mb-sm-2"
                            onclick="location.href='${pageContext.request.contextPath}/app/reports/view'">
                        <fmt:message key="button.edit" bundle="${bundle}"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf" %>
</body>
</html>
