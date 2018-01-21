<%--
  Created by IntelliJ IDEA.
  User: Zhenia
  Date: 1/21/2018
  Time: 12:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="et" uri="http://com.projects/tags/entity/table" %>
<html>
<head>
    <title>Title</title>
    <%@include file="parts/header.jspf" %>
</head>
<body>
<%@include file="parts/set-lang.jspf" %>
<%@include file="parts/nav-bar.jspf" %>
<div class="container">
    <div class="row">
        <div class="col-md-5">
            <form class="form-inline my-2 my-lg-0 mr-md-3 mb-md-3">
                <select class="form-control" id="language" name="language" onchange="submit()">
                    <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>
                        <fmt:message key="label.english" bundle="${bundle}"/>
                    </option>
                    <option value="ru_RU" ${language == 'ru_RU' ? 'selected' : ''}>
                        <fmt:message key="label.russian" bundle="${bundle}"/>
                    </option>
                </select>
            </form>
            <div class="card mt-md-3" style="width: 50%">
                <div class="card-header"><fmt:message key="title.registration" bundle="${bundle}"/></div>
                <div class="card-body">
                    <c:if test="${errors}">
                        <div class="alert alert-danger">
                            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
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
                    <form action="${pageContext.request.contextPath}/app/edit-account" method="post">
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-user"></i>
                                </span>
                            </span>
                            <label for="login"><fmt:message key="label.login" bundle="${bundle}"/></label>
                            <input type="text" class="form-control" id="login" name="login"
                                   value="<c:out value="${user.login}"/>" disabled/>
                        </div>
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-user"></i>
                                </span>
                            </span>
                            <label for="firstName"><fmt:message key="label.first.name" bundle="${bundle}"/></label>
                            <input type="text" class="form-control" id="firstName" name="firstName"
                                   value="<c:out value="${employee.firstName}"/>">
                        </div>
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-user"></i>
                                </span>
                            </span>
                            <label for="lastName"><fmt:message key="label.last.name" bundle="${bundle}"/></label>
                            <input type="text" class="form-control" id="lastName" name="lastName"
                                   value="<c:out value="${employee.lastName}"/>">
                        </div>
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-envelope"></i>
                                </span>
                            </span>
                            <label for="email"><fmt:message key="label.email" bundle="${bundle}"/></label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<c:out value="${employee.email}"/>">
                        </div>
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-money"></i>
                                </span>
                            </span>
                            <label for="salary"><fmt:message key="label.email" bundle="${bundle}"/></label>
                            <input type="number" class="form-control" id="salary" name="salary"
                                   value="<c:out value="${employee.salary}"/>" disabled>
                        </div>
                        <div class="input-group">
                            <span class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="fa fa-briefcase"></i>
                                </span>
                            </span>
                            <label for="email"><fmt:message key="label.email" bundle="${bundle}"/></label>
                            <input type="text" class="form-control" id="position" name="position"
                                   value="<c:out value="${employee.position}"/>" disabled>
                        </div>
                        <input type="submit" class="btn btn-outline-primary"
                               value="<fmt:message key="button.save" bundle="${bundle}"/>">
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-7" style="max-height: 500px; overflow: auto">
            <et:entityTable entities="${employeesChecks}" excludeFields="${excludeFields}"/>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf" %>
</body>
</html>
