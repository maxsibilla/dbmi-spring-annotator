<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<myTags:imports title="${uri}"/>
<link href="${contextPath}/resources/css/auth.css" rel="stylesheet">
<link href="${contextPath}/resources/css/jqx.base.css" rel="stylesheet">
<script src="${contextPath}/resources/js/splitter/jqxcore.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxbuttons.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxsplitter.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxpanel.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxscrollbar.js"></script>
<script src="${contextPath}/resources/js/searchhighlight.js"></script>

<body>
<%--<myTags:navbar></myTags:navbar>--%>
<main id="main" role="main" class="container-fluid">
    <div id="main-splitter">
        <div id="content">
            <c:out value="${fileContents}" escapeXml="false"/>

        </div>
        <div id="new-annotator-viewer">
            <div id="annotation-definition">

            </div>
            <div id="nested-viewer">
                <div id="annotation-figure">

                </div>
                <div id="annotation-video">

                </div>
            </div>
        </div>
    </div>

</main>

<script>
    $(document).ready(function () {
        var uri = getUrlParameter('uri', document.location.href);
        if (uri == null || uri == undefined) {
            uri = 'default';
        }
        //enable basic annotator functionality
        var annotator = $('#content').annotator();

        //enable ability to save annotations
        annotator.annotator('addPlugin', 'Store', {
            prefix: '${contextPath}/annotation',
            annotationData: {
                'uri': uri,
                'timestamp': new Date().getTime()
            },
            loadFromSearch: {
                'limit': 100,
                'uri': uri,
                'timestamp': new Date().getTime()
            }
        });

        $('#content').jqxPanel({width: '100%', height: '100%'});
        $('#main-splitter').jqxSplitter({
            width: $('#main').width(),
            height: $(window).height() - 35,
            // width: '100%',
            // height: '100%',
            orientation: 'horizontal',
            panels: [{size: '66%', collapsible: false}, {size: '34%', collapsible: true}]
        });
        $('#main-splitter').jqxSplitter('collapse');


        // testing
<%--        <c:forEach items="${phrases}" var="entry">--%>
<%--        createDynamicAnnotation("${entry.key}", "${entry.value}", uri);--%>
<%--        </c:forEach>--%>

    });

    function createDynamicAnnotation(searchWord, definition, uri) {
        // clear previous search
        console.log("Searching for..." + searchWord);
        var allElements = Array.from(document.querySelectorAll('.highlighted'))
        allElements.forEach(function (element) {
            element.classList.remove('highlighted')
        });
        highlightSearchTerms(searchWord, true);

        for (var i = 0; i < 2; i++) {
            // for (var i = 0; i < document.getElementsByClassName('highlighted').length; i++) {

            (function (i) {
                //for testing
                if (document.getElementsByClassName('highlighted')[i] != undefined) {

                    var rootXPath = getXpathOfNode(document.getElementsByClassName('annotator-wrapper')[0]);
                    var xPath = getXpathOfNode(document.getElementsByClassName('highlighted')[i]);
                    xPath = xPath.replace(rootXPath, '').replace('/FONT', '').toLowerCase();
                    xPath = xPath.replace('tbody', 'tbody[1]').replace('td', 'td[1]');

                    var parentElement = document.getElementsByClassName('highlighted')[i].parentElement;
                    var startOffset = parentElement.innerText.indexOf(searchWord);
                    var endOffset = startOffset + searchWord.length;

                    var annotation = {};
                    var range = {};

                    annotation.quote = searchWord;
                    annotation.text = definition;
                    annotation.uri = uri;

                    range.start = xPath;
                    range.end = xPath;
                    range.startOffset = startOffset;
                    range.endOffset = endOffset;
                    annotation.range = range;

                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "${contextPath}/annotation/newAnnotation",
                        data: JSON.stringify(annotation),
                        dataType: 'json',
                        async: false,
                        timeout: 600000,
                        success: function (data) {

                        },
                        error: function (e) {

                        }
                    });
                }
            })(i);
        }
    }


    function getUrlParameter(name, url) {
        if (!url) url = location.href
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(url);
        return results == null ? null : results[1];
    }
</script>
</body>
</html>
