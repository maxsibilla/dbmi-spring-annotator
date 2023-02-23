<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<myTags:imports title="Unauthorized Request"/>

<body>
<myTags:navbar></myTags:navbar>
<main role="main" class="container">
    <div class="navbar-body">
        <div class="row">

            <h3 class="text-center">Status 401: You do not currently have the authorization to view this.</h3>

        </div>
    </div>
</main>
</body>
</html>