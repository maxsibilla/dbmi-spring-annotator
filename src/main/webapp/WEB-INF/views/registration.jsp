<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<myTags:imports title="Create an account"/>
<link href="${contextPath}/resources/css/auth.css" rel="stylesheet">

<body>
<myTags:navbar></myTags:navbar>
<main role="main" class="container">
    <div class="wrapper fadeInDown">
        <div id="formContent">
            <!-- Tabs Titles -->

            <!-- Icon -->
            <div class="fadeIn first">
                <h4>Create an account</h4>
                <%--                    <img src="http://danielzawadzki.com/codepen/01/icon.svg" id="icon" alt="User Icon"/>--%>
            </div>

            <!-- Login Form -->
            <form:form method="POST" modelAttribute="userForm">
                <spring:bind path="username">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="text" path="username" required="required" class="fadeIn second"
                                    placeholder="Username"
                                    autofocus="true"></form:input>
                        <form:errors path="username"></form:errors>
                    </div>
                </spring:bind>

                <spring:bind path="email">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="email" path="email" required="required" class="fadeIn third"
                                    placeholder="Email"></form:input>
                        <form:errors path="email"></form:errors>
                    </div>
                </spring:bind>

                <spring:bind path="password">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="password" path="password" required="required" class="fadeIn fourth"
                                    placeholder="Password"></form:input>
                        <form:errors path="password"></form:errors>
                    </div>
                </spring:bind>

                <spring:bind path="passwordConfirm">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <form:input type="password" path="passwordConfirm" required="required" class="fadeIn fourth"
                                    placeholder="Confirm your password"></form:input>
                        <form:errors path="passwordConfirm"></form:errors>
                    </div>
                </spring:bind>

                <input type="submit" class="fadeIn fourth" value="Submit">
            </form:form>

            <div id="formFooter">
                <a class="underlineHover" href="${contextPath}/login">Already have an account?</a>
            </div>

        </div>
    </div>
</main>
</body>
</html>