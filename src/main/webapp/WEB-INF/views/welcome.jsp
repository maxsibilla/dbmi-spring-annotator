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
        <h3>
            Please visit this <a href="">link</a> to be instructed on how the following tool functions.
        </h3>
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

                    <c:set var="preTest" value="preTest${userFileInfo.filename}"/>
                    <c:set var="postTest" value="postTest${userFileInfo.filename}"/>

                    <h5 class="card-title">${userFileInfo.publicFilename}</h5>

                    <button value="${userFileInfo.preTest}" id="${preTest}" onclick="markComplete(this.value, this.id)"
                            class="btn btn-primary
                        <c:forEach items="${uncompletedFiles}" var="file" varStatus="status">
                            <c:if test="${file eq preTest}">
                                disabled btn-secondary
                            </c:if>
                        </c:forEach>
                     ">Pre-Test
                    </button>

                    <a href="${url}" class="btn btn-primary
                        <c:forEach items="${uncompletedFiles}" var="file" varStatus="status">
                            <c:if test="${file eq userFileInfo.filename}">
                                disabled btn-secondary
                            </c:if>
                        </c:forEach>
                     ">View</a>

                    <button value="${userFileInfo.postTest}" id="${postTest}"
                            onclick="markComplete(this.value, this.id)" class="btn btn-primary
                        <c:forEach items="${uncompletedFiles}" var="file" varStatus="status">
                            <c:if test="${file eq postTest}">
                                disabled btn-secondary
                            </c:if>
                        </c:forEach>
                     ">Post-Test
                    </button>
                </div>
            </div>
            <br>
        </c:forEach>

        <p>
            Once you have completed all of the tasks please fill out this <a href="https://pitt.co1.qualtrics.com/jfe/form/SV_ba0KkRh3CfAao29">System Usability Scale questionnaire</a>.
        </p>
    </div>

    <script>
        function markComplete(testUrl, uri) {
            $.post("complete", {
                uri: uri,
            }, function () {
                window.open(testUrl, "_blank");
                location.reload();
            });
        }
    </script>
</main>
</body>
</html>