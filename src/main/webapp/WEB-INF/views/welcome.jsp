<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<myTags:imports title="Welcome"/>
<body>
<myTags:navbar></myTags:navbar>
<main role="main" class="container">
    <div class="navbar-body">
        <h2>Welcome ${pageContext.request.userPrincipal.name}</h2>
        <c:forEach items="${userFileInfoList}" var="userFileInfo" varStatus="status">
            <div class="card">
                <div class="card-header">
                    File ${status.count}
                </div>
                <div class="card-body">
                    <c:set var="filename" value="${userFileInfo.filename}"></c:set>
                    <c:set var="hasAssistance" value="${userFileInfo.hasAssistance}"></c:set>
                    <c:url value="/view" var="url">
                        <c:param name="uri" value="${filename}"/>
                        <c:param name="showAnnotator" value="${hasAssistance}"/>
                    </c:url>

                    <h5 class="card-title">${userFileInfo.publicFilename}</h5>
                <%--                    <p class="card-text">With supporting text below as a natural lead-in to additional content.</p>--%>
                    <a href="${url}" class="btn btn-primary
                        <c:forEach items="${uncompletedFiles}" var="file" varStatus="status">
                            <c:if test="${file eq userFileInfo.filename}">
                                disabled btn-secondary
                            </c:if>
                        </c:forEach>
                     ">View</a>
                </div>
            </div>
            <br>
        </c:forEach>
    </div>
</main>
</body>
</html>