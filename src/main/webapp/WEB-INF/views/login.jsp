<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">

<myTags:imports title="Log in with your account"/>
<link href="${contextPath}/resources/css/auth.css" rel="stylesheet">

<body>
<myTags:navbar></myTags:navbar>
<main role="main" class="container">

    <div class="wrapper fadeInDown">
        <div id="formContent">
            <!-- Tabs Titles -->

            <%--        <!-- Icon -->--%>
            <%--        <div class="fadeIn first">--%>
            <%--            <img src="http://danielzawadzki.com/codepen/01/icon.svg" id="icon" alt="User Icon"/>--%>
            <%--        </div>--%>

            <!-- Login Form -->
            <form method="POST" action="${contextPath}/login">
                <div class="form-group ${error != null ? 'has-error' : ''}">
                    <h6>${message}</h6>

                    <input name="username" type="text" required="required" class="fadeIn second" placeholder="Username"
                           autofocus="true"/>

                    <input name="password" type="password" required="required" class="fadeIn third"
                           placeholder="Password"/>
                    <h6>${error}</h6>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <input type="submit" class="fadeIn fourth" value="Log In">
                </div>
            </form>

            <div id="formFooter">
                <a class="underlineHover" href="${contextPath}/registration">Create an account</a>
            </div>

        </div>
    </div>
</main>

</body>
</html>