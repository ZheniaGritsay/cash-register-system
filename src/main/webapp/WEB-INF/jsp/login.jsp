<%--
  Created by IntelliJ IDEA.
  User: Zhenia
  Date: 1/5/2018
  Time: 6:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <%@include file="parts/set-lang.jspf" %>
    <title><fmt:message key="title.login" bundle="${bundle}"/></title>
    <%@include file="parts/header.jspf" %>
</head>
<body>
<c:if test="${not empty redirect}">
    <c:redirect url="${pageContext.request.contextPath}/app/home"/>
</c:if>
<%@include file="parts/jumborton.jspf" %>
<div class="container mt-sm-5">
</div>
<div class="row">
    <div class="col-sm-4"></div>
    <div class="col-sm-4">
        <div class="card">
            <div class="card-header" style="position: relative">
                <b><fmt:message key="title.login" bundle="${bundle}"/></b>
                <form class="form-inline" style="left: 70%; position: relative; display: inline">
                    <select class="form-control" id="language" name="language" onchange="submit()">
                        <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>
                            <fmt:message key="label.english" bundle="${bundle}"/>
                        </option>
                        <option value="ru_RU" ${language == 'ru_RU' ? 'selected' : ''}>
                            <fmt:message key="label.russian" bundle="${bundle}"/>
                        </option>
                    </select>
                </form>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                        <fmt:message key="error.login.or.password" bundle="${bundle}"/>
                    </div>
                </c:if>
                <form action="${pageContext.request.contextPath}/app/login" method="post">
                    <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text"><i class="fa fa-user"></i></span></span>
                        <input type="text" class="form-control" id="login" name="login"
                               placeholder="<fmt:message key="label.login" bundle="${bundle}" />">
                    </div>
                    <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text"><i class="fa fa-lock"></i></span></span>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="<fmt:message key="label.password" bundle="${bundle}" />">
                    </div>
                    <input type="submit" class="btn btn-outline-primary"
                           value="<fmt:message key="button.log.in" bundle="${bundle}" />">
                    <button type="button" class="btn btn-outline-primary" style="margin-left: 10px"
                            onclick="location.href='${pageContext.request.contextPath}/app/registration'">
                        <fmt:message key="button.register" bundle="${bundle}"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
<%@include file="parts/footer.jspf" %>
</body>
</html>
