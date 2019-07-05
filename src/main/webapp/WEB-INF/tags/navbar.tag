<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<nav class="navbar navbar-expand-md navbar-light bg-light fixed-top">
    <a class="navbar-brand" href="#">Spring Annotator</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbars"
            aria-controls="navbars" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbars">
        <ul class="navbar-nav mr-auto">
<%--            <li class="nav-item">--%>
<%--                <a class="nav-link" href="${contextPath}/view?uri=Phenotype&showAnnotator=true">Phenotype <span class="sr-only">(current)</span></a>--%>
<%--            </li>--%>
<%--            <li class="nav-item">--%>
<%--                <a class="nav-link" href="${contextPath}/view?uri=PhenotypeVideo&showAnnotator=true">Phenotype Video</a>--%>
<%--            </li>--%>
<%--            <li class="nav-item">--%>
<%--                <a class="nav-link" href="${contextPath}/view?uri=Fibrodysplasia&showAnnotator=true">Fibrodysplasia</a>--%>
<%--            </li>--%>
        </ul>
        <c:choose>
            <c:when test="${pageContext.request.userPrincipal.name != null}">
                <form class="form-inline my-2 my-lg-0" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-outline-danger my-2 my-sm-0" type="submit">Logout</button>
                </form>
            </c:when>
            <c:otherwise>
                <form class="form-inline my-2 my-lg-0" method="GET" action="${contextPath}/login">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Login</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
