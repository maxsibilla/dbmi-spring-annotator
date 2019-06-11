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
    <h2>Welcome ${pageContext.request.userPrincipal.name}</h2>
</main>
</body>
</html>