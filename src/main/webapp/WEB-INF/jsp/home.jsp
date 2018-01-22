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
    <title><fmt:message key="title.main" bundle="${bundle}"/></title>
    <%--<%@include file="parts/header.jspf"%>--%>
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
<%@include file="parts/set-lang.jspf" %>
<%@include file="parts/nav-bar.jspf" %>
<div class="container mt-sm-3">
    <div class="row">
        <div class="col-sm-4">
            <c:if test="${not empty employeeId}">
                <input type="hidden" id="employeeId" value="<c:out value="${employeeId}"/>">
                <div class="input-group">
                    <input type="text" class="form-control" id="employeeFirstName" name="employeeFirstName"
                           value="<c:out value="${employeeFirstName}"/>" disabled>
                </div>
                <div class="input-group">
                    <input type="text" class="form-control" id="employeeLastName" name="employeeLastName"
                           value="<c:out value="${employeeLastName}"/>" disabled>
                </div>
            </c:if>
        </div>
        <div class="col-sm-4">
            <div class="input-group">
                <input id="productSearch" type="text" class="form-control" style="border-color: #a0b1ff"
                       placeholder="<fmt:message key="label.product.search" bundle="${bundle}"/>">
            </div>
        </div>
        <div class="col-sm-4"></div>
    </div>
</div>
<div class="container mt-sm-3">
    <div class="row">
        <c:forEach var="product" items="${products}" end="2">
            <div class="col-sm-4">
                <div class="card">
                    <div class="card-header"><c:out value="${product.title}"/> : <c:out value="${product.code}"/></div>
                    <div class="card-body"><img src="data:image/jpg;base64,${images.get(product.title)}"
                                                class="img-responsive"
                                                style=" width: 100%"></div>
                    <div class="card-footer">
                        <form>
                            <div class="form-row mb-md-1">
                                <label class="col-sm-4" for="quantity">
                                    <fmt:message key="label.quantity" bundle="${bundle}"/>
                                </label>
                                <input type="number" class="form-control col-sm-8" id="quantity" name="quantity"
                                       placeholder="0">
                            </div>
                            <div class="form-row">
                                <label class="col-sm-4" for="price">
                                    <fmt:message key="label.price" bundle="${bundle}"/>
                                </label>
                                <input type="number" class="form-control col-sm-8" id="price" name="price"
                                       value="<c:out value="${product.price}"/>" disabled>
                            </div>
                        </form>
                        <input id="id" name="id" type="hidden" value="<c:out value="${product.id}"/>">
                        <input type="hidden" name="title" id="title"
                               value="<c:out value="${product.title}"/>">
                        <input type="hidden" name="code" id="code"
                               value="<c:out value="${product.code}"/>">
                        <input type="hidden" name="quantityStock" id="quantityStock"
                               value="<c:out value="${product.quantityOnStock}"/>">
                        <input type="hidden" name="quantityType" id="quantityType"
                               value="<c:out value="${product.quantityType}"/>">
                        <button type="button" onclick="addToHandler(event)"
                                class="btn btn-outline-primary mt-sm-3">
                            <fmt:message key="button.add" bundle="${bundle}"/>
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<div class="container mt-sm-4">
    <div class="row">
        <c:forEach var="product" items="${products}" begin="3">
            <div class="col-sm-4">
                <div class="card">
                    <div class="card-header"><c:out value="${product.title}"/> : <c:out value="${product.code}"/></div>
                    <div class="card-body"><img src="data:image/jpg;base64,${images.get(product.title)}"
                                                class="img-responsive"
                                                style=" width: 100%"></div>
                    <div class="card-footer">
                        <form>
                            <div class="form-row mb-md-1">
                                <label class="col-sm-4" for="quantity-2">
                                    <fmt:message key="label.quantity" bundle="${bundle}"/>
                                </label>
                                <input type="number" class="form-control col-sm-8" id="quantity-2" name="quantity"
                                       placeholder="0">
                            </div>
                            <div class="form-row">
                                <label class="col-sm-4" for="price-2">
                                    <fmt:message key="label.price" bundle="${bundle}"/>
                                </label>
                                <input type="number" class="form-control col-sm-8" id="price-2" name="price"
                                       value="<c:out value="${product.price}"/>" disabled>
                            </div>
                        </form>
                        <input id="id-2" name="id" type="hidden" value="<c:out value="${product.id}"/>">
                        <input type="hidden" name="title" id="title-2"
                               value="<c:out value="${product.title}"/>">
                        <input type="hidden" name="code" id="code-2"
                               value="<c:out value="${product.code}"/>">
                        <input type="hidden" name="quantityStock" id="quantityStock-2"
                               value="<c:out value="${product.quantityOnStock}"/>">
                        <input type="hidden" name="quantityType" id="quantityType-2"
                               value="<c:out value="${product.quantityType}"/>">
                        <button type="button" onclick="addToHandler(event)"
                                class="btn btn-outline-primary mt-sm-3">
                            <fmt:message key="button.add" bundle="${bundle}"/>
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<div class="container mt-sm-3">
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <%@include file="parts/pagination.jspf" %>
        </div>
        <div class="col-md-4"></div>
    </div>
</div>
<div class="modal fade" id="checkClosed" tabindex="-1" role="dialog" aria-labelledby="checkClosedLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="label.check.closed" bundle="${bundle}"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal"><fmt:message key="label.close" bundle="${bundle}"/></button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="productUnavailable" tabindex="-1" role="dialog" aria-labelledby="productUnavailableLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" style="color: red"><fmt:message key="label.unavailable" bundle="${bundle}"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="color: red">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal"><fmt:message key="label.close" bundle="${bundle}"/></button>
            </div>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf" %>
<script type="text/javascript" src="../../resources/js/common.js"></script>
<script type="text/javascript">
    numberOfPages = ${numberOfPages};
    recordsPerPage = ${recordsPerPage};
    lastPage = numberOfPages;
    entity = 'products';
    allMessages.closeCheck = '${closeCheck}';
    allMessages.sum = '${sum}';
</script>
</body>
</html>
