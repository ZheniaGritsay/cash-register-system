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
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="parts/set-lang.jspf" %>
    <title><fmt:message key="title.registration" bundle="${bundle}"/></title>
    <%@include file="parts/header.jspf"%>
</head>
<body>
<c:if test="${not empty redirect}">
    <c:redirect url="${pageContext.request.contextPath}/app/login"/>
</c:if>
<%@include file="parts/jumborton.jspf" %>
<div class="container">
    <div class="card mt-md-3" style="width: 50%">
        <div class="card-header" style="position: relative">
            <fmt:message key="title.registration" bundle="${bundle}"/>
            <form class="form-inline" style="position: relative; left: 70%; display: inline">
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
            <c:if test="${errors || loginExists}">
                <div class="alert alert-danger">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <c:if test="${not empty loginExists}">
                        <fmt:message key="error.exists.login" bundle="${bundle}"/>
                    </c:if>
                    <div>
                        <c:if test="${not empty loginError}">
                            <fmt:message key="label.login" bundle="${bundle}"/>&nbsp;
                            <fmt:message key="${loginError}" bundle="${bundle}"/>
                        </c:if>
                    </div>
                    <div>
                        <c:if test="${not empty passwordError}">
                            <fmt:message key="label.password" bundle="${bundle}"/>&nbsp;
                            <fmt:message key="${passwordError}" bundle="${bundle}"/> 5
                        </c:if>
                    </div>
                    <div>
                        <c:if test="${not empty firstNameError}">
                            <fmt:message key="label.first.name" bundle="${bundle}"/>&nbsp;
                            <fmt:message key="${firstNameError}" bundle="${bundle}"/>
                        </c:if>
                    </div>
                    <div>
                        <c:if test="${not empty lastNameError}">
                            <fmt:message key="label.last.name" bundle="${bundle}"/>&nbsp;
                            <fmt:message key="${lastNameError}" bundle="${bundle}"/>
                        </c:if>
                    </div>
                    <div>
                        <c:if test="${not empty emailError}">
                            <fmt:message key="label.email" bundle="${bundle}"/>&nbsp;
                            <fmt:message key="${emailError}" bundle="${bundle}"/>
                        </c:if>
                    </div>
                </div>
            </c:if>
            <form action="${pageContext.request.contextPath}/app/registration" method="post">
                <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-user fa-1x"></i>
                                </span>
                            </span>
                    <input type="text" class="form-control" id="login" name="login"
                           placeholder="<fmt:message key="label.login" bundle="${bundle}" />">
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text">
                            <i class="fa fa-lock fa-1x"></i>
                        </span>
                    </span>
                    <input type="password" class="form-control" name="password" value="<c:out value="${password}"/>"
                           placeholder="<fmt:message key="label.password" bundle="${bundle}"/>"/>
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text">
                            <i class="fa fa-user fa-1x"></i>
                        </span>
                    </span>
                    <input type="text" class="form-control" name="firstName" value="<c:out value="${firstName}"/>"
                           placeholder="<fmt:message key="label.first.name" bundle="${bundle}"/>">
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text">
                            <i class="fa fa-user fa-1x"></i>
                        </span>
                    </span>
                    <input type="text" class="form-control" name="lastName" value="<c:out value="${lastName}"/>"
                           placeholder="<fmt:message key="label.last.name" bundle="${bundle}"/>">
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text">
                            <i class="fa fa-envelope fa-1x"></i>
                        </span>
                    </span>
                    <input type="email" class="form-control" name="email" value="<c:out value="${email}"/>"
                           placeholder="<fmt:message key="label.email" bundle="${bundle}"/>">
                </div>
                <input type="submit" class="btn btn-outline-primary"
                       value="<fmt:message key="button.register" bundle="${bundle}"/>">
                <input type="reset" class="btn btn-outline-primary"
                       value="<fmt:message key="button.reset" bundle="${bundle}"/>"
                       style="margin-left: 10px">
            </form>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf" %>
</body>
</html>
