<%--
  Created by IntelliJ IDEA.
  User: Zhenia
  Date: 1/15/2018
  Time: 4:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit</title>
    <%@include file="parts/header.jspf"%>
    <style>
        .card {
            border-color: #28a745;
        }

        .card > .card-header {
            color: #3c763d;
            background-color: #dff0d8;
            border-color: #28a745;
        }

        nav[aria-label='Page navigation'] {
            position: relative;
            left: 35%;
            padding: 10px
        }
    </style>
</head>
<body>
<%@include file="parts/set-lang.jspf" %>
<%@include file="parts/nav-bar.jspf" %>
<div class="container mt-md-5">
    <div class="row">
        <div class="col-sm-5">
            <div>
                <form class="form-inline">
                    <div class="input-group">
                        <label for="recordsPerPage"><fmt:message key="label.records.per.page" bundle="${bundle}"/></label>
                        <select id="recordsPerPage" name="recordsPerPage" class="form-control-sm ml-md-2"
                                onchange="submit()">
                            <option value="5" ${recordsPerPage == 5 ? 'selected' : ''} selected>5</option>
                            <option value="10" ${recordsPerPage == 10 ? 'selected' : ''}>10</option>
                            <option value="20" ${recordsPerPage == 20 ? 'selected' : ''}>20</option>
                        </select>
                    </div>
                </form>
                <c:if test="${entity eq 'products'}">
                    <div class="input-group">
                        <input id="productSearch" type="text" class="form-control" style="border-color: #a0b1ff"
                               onkeyup="productSearchHandler(event)"
                               placeholder="<fmt:message key="label.product.search" bundle="${bundle}"/>">
                    </div>
                </c:if>
                <c:if test="${entity eq 'checks'}">
                    <div class="input-group">
                        <input id="checkSearch" type="text" class="form-control" style="border-color: #a0b1ff"
                               onkeyup="checkSearchHandler(event)"
                               placeholder="<fmt:message key="label.check.search" bundle="${bundle}"/>">
                    </div>
                </c:if>
                <c:if test="${entity eq 'reports'}">
                    <form class="form-inline">
                        <div class="input-group">
                            <label for="reportTypeSearch"><fmt:message key="label.report.type" bundle="${bundle}"/></label>
                            <select id="reportTypeSearch" class="form-control-sm ml-md-2"
                                    onchange="onReportTypeChange(event)">
                                <option value="ALL" selected>all</option>
                                <option value="CLOSED_CHECKS">closed checks</option>
                                <option value="MODIFIED_CHECKS">modified checks</option>
                                <option value="REFUNDED_CHECKS">refunded checks</option>
                                <option value="MIXED_CHECKS">mixed checks</option>
                            </select>
                        </div>
                    </form>
                </c:if>
            </div>
            <button class="btn btn-outline-success add-form" style="margin-bottom: 10px">
                <i class="fa fa-plus-circle"></i> <fmt:message key="button.add" bundle="${bundle}"/>
            </button>
            <div class="card add-form-content" style="display: none">
            </div>
        </div>
        <div class="col-md-7"></div>
    </div>
</div>
<div class="container mt-md-3 main">
    <div class="row">
        <div class="col-md-12">
            <div class="well">
                <%@include file="parts/pagination.jspf" %>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="quantityModal" tabindex="-1" role="dialog" aria-labelledby="quantityModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="label.quantity" bundle="${bundle}"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="quantity"></label>
                        <input type="number" class="form-control" id="quantity" placeholder="0">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
            </div>
        </div>
    </div>
</div>
<%@include file="parts/footer.jspf" %>
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
<script type="text/javascript" src="../../resources/js/common.js"></script>
<script type="text/javascript">
    numberOfPages = ${numberOfPages};
    lastPage = numberOfPages;
    entity = '${entity}';
    recordsPerPage = ${recordsPerPage};
</script>
</body>
</html>
