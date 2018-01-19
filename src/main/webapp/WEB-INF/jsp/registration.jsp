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
    <%@include file="parts/set-lang.jspf"%>
    <title><fmt:message key="title.registration" bundle="${bundle}"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css"
          integrity="sha384-Zug+QiDoJOrZ5t4lssLdxGhVrurbmBWopoEl+M6BdEfwnCJZtKxi1KgxUyJq13dy" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"
            integrity="sha384-a5N7Y/aK3qNeh15eJKGWxsqtnX/wWdSZSKp+81YjTmS15nvnvxKHuzaWwXHDli+4"
            crossorigin="anonymous"></script>
    <script src="https://use.fontawesome.com/61c4da9bba.js"></script>
    <link rel="stylesheet" href="../../resources/css/common.css">
</head>
<body>
<c:if test="${not empty redirect}">
    <c:redirect url="${pageContext.request.contextPath}/app/login"/>
</c:if>
<%@include file="parts/jumborton.jspf"%>
<div class="container">
    <div class="card" style="width: 50%">
        <div class="card-header"><fmt:message key="title.registration" bundle="${bundle}"/></div>
        <div class="card-body">
            <c:if test="${errors || loginExists}">
                <div class="alert alert-danger">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <c:if test="${not empty loginExists}">
                        <fmt:message key="label.login.exists" bundle="${bundle}" />
                    </c:if>
                    <c:if test="${not empty loginError}">
                        <fmt:message key="label.login" bundle="${bundle}" />&nbsp;
                        <fmt:message key="${loginError}" bundle="${bundle}" />
                    </c:if>
                    <c:if test="${not empty passwordError}">
                        <fmt:message key="label.password" bundle="${bundle}" />&nbsp;
                        <fmt:message key="${passwordError}" bundle="${bundle}" />
                    </c:if>
                    <c:if test="${not empty firstNameError}">
                        <fmt:message key="label.first.name" bundle="${bundle}" />&nbsp;
                        <fmt:message key="${firstNameError}" bundle="${bundle}" />
                    </c:if>
                    <c:if test="${not empty lastNameError}">
                        <fmt:message key="label.last.name" bundle="${bundle}" />&nbsp;
                        <fmt:message key="${lastNameError}" bundle="${bundle}" />
                    </c:if>
                    <c:if test="${not empty emailError}">
                        <fmt:message key="label.email" bundle="${bundle}" />&nbsp;
                        <fmt:message key="${emailError}" bundle="${bundle}" />
                    </c:if>
                </div>
            </c:if>
            <form action="${pageContext.request.contextPath}/app/registration" method="post">
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text"><i class="fa fa-user"></i></span></span>
                    <input type="text" class="form-control" name="login" value="<c:out value="login"/>"
                           placeholder="<fmt:message key="label.login" bundle="${bundle}"/>"/>
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text"><i class="fa fa-lock"></i></span></span>
                    <input type="password" class="form-control" name="password" value="<c:out value="password"/>"
                           placeholder="<fmt:message key="label.password" bundle="${bundle}"/>"/>
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text"><i class="fa fa-user"></i></span></span>
                    <input type="text" class="form-control" name="firstName" value="<c:out value="firstName"/>"
                           placeholder="<fmt:message key="label.first.name" bundle="${bundle}"/>">
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text"><i class="fa fa-user"></i></span></span>
                    <input type="text" class="form-control" name="lastName" value="<c:out value="lastName"/>"
                           placeholder="<fmt:message key="label.last.name" bundle="${bundle}"/>">
                </div>
                <div class="input-group">
                    <span class="input-group-prepend">
                        <span class="input-group-text"><i class="fa fa-envelope"></i></span></span>
                    <input type="email" class="form-control" name="email" value="<c:out value="email"/>"
                           placeholder="<fmt:message key="label.email" bundle="${bundle}"/>">
                </div>
                <input type="submit" class="btn btn-outline-primary"
                       value="<fmt:message key="button.register" bundle="${bundle}"/>">
                <input type="reset" class="btn btn-outline-primary" value="<fmt:message key="button.reset" bundle="${bundle}"/>"
                       style="margin-left: 10px">
            </form>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf"%>
</body>
</html>
