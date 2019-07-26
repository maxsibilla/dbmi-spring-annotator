<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<myTags:imports title="Analytics"/>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>
<body>
<myTags:navbar></myTags:navbar>
<main role="main" class="container-fluid">
    <div class="navbar-body">
        <table id="analytics" class="table table-striped table-bordered" style="width:100%">
            <thead>
            <tr>
                <th>
                    Username
                </th>
                <th>
                    Annotated Word
                </th>
                <th>
                    Page
                </th>
                <th>
                    Word Type
                </th>
                <th>
                    Page Loaded At
                </th>
                <th>
                    Annotation Selected At
                </th>
                <th>
                    English Phrase Filter
                </th>
                <th>
                    Science Phrase Filter
                </th>
                <th>
                    Word Difficulty Filter
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${annotationTrackers}" var="annotationTracker" varStatus="status">
                <tr>
                    <td>
                            ${annotationTracker.annotation.user.username}
                    </td>
                    <td>
                            ${annotationTracker.annotation.quote}
                    </td>
                    <td>
                            ${annotationTracker.annotation.uri}
                    </td>
                    <td>
                            ${annotationTracker.annotation.wordType}
                    </td>
                    <td>
                            ${annotationTracker.pageLoadTime}
                    </td>
                    <td>
                            ${annotationTracker.annotationClickTime}
                    </td>
                    <td>
                            ${annotationTracker.englishPhraseFilter}
                    </td>
                    <td>
                            ${annotationTracker.sciencePhraseFilter}
                    </td>
                    <td>
                            ${annotationTracker.wordDifficultyFilter}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <script>
        $(document).ready(function () {
            $('#analytics').DataTable();
        });
    </script>
</main>
</body>
</html>