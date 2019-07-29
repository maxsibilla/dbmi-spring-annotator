<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<myTags:imports title="${uri}" wantJQXPanel="${true}"/>
<link href="${contextPath}/resources/css/auth.css" rel="stylesheet">
<link href="${contextPath}/resources/css/jqx.base.css" rel="stylesheet">
<link href="${contextPath}/resources/css/tags-annotator.css" rel="stylesheet">
<script src="${contextPath}/resources/js/splitter/jqxcore.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxbuttons.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxsplitter.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxpanel.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxscrollbar.js"></script>
<script src="${contextPath}/resources/js/searchhighlight.js"></script>
<script src="${contextPath}/resources/js/tags-annotator.js"></script>
<body>
<%--<myTags:navbar></myTags:navbar>--%>
<div id="main-splitter" class="main-splitter">
    <div id="content">
        <c:if test="${contentIsFile}">
            <c:out value="${fileContents}" escapeXml="false"/>
        </c:if>

        <c:if test="${contentIsVideo}">
            <%--            to set start and end time--%>
            <video controls src="/annotator-file-dir/videos/${fileContents}#t=${startTime},${endTime}"
                   class="video-player"
                   id="main-video">
                    <%--            <video controls src="/annotator-file-dir/videos/${fileContents}" class="video-player" id="main-video">--%>
                <track default src="/annotator-file-dir/videos/${subtitles}" label="English subtitles" kind="subtitles"
                       srclang="en"></track>
            </video>

            <br>
            <h4 id="my-subtitle-display" class="subtitles">

            </h4>
        </c:if>

    </div>
    <div id="new-annotator-viewer">
        <div id="annotation-definition">

        </div>
        <div id="nested-viewer">
            <div id="carouselControls" class="carousel slide" data-ride="carousel">
                <div class="carousel-inner" id="annotation-figure">

                </div>
                <a class="carousel-control-prev" href="#carouselControls" role="button"
                   data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="carousel-control-next" href="#carouselControls" role="button"
                   data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>

            <div id="annotation-video">

            </div>
        </div>
    </div>
</div>

<div class="col-md-4">
    <div class="legend-large d-none d-sm-block">
        <button class="btn btn-secondary btn-xs" id="complete">Complete</button>
        <br>
        <button id="show-legend" class="btn btn-secondary btn-xs" onclick="toggleLegend('show')">Show Legend</button>
        <div id="main-legend" class="legend display-none">
            <button id="legend-button" class="btn btn-outline-dark btn-xs" onclick="toggleLegend('hide')">Hide</button>
            <myTags:legendTable/>
        </div>
    </div>
</div>

<script>
    var annotator;
    var currentTimeStamp;
    var wordDifficultyFilter = ['moderate', 'hard'];
    var englishPhraseFilter = 'visible';
    var sciencePhraseFilter = 'visible';

    $(document).ready(function () {
        currentTimeStamp = new Date().toLocaleString();
        var uri = getUrlParameter('uri', document.location.href);
        if (uri == null || uri == undefined) {
            uri = 'default';
        }
        if (${not disableAnnotations}) {
            createAnnotator(uri);
        }

        <c:if test="${contentIsVideo}">
        document.getElementById('main-video').textTracks[0].addEventListener('cuechange', function () {
            document.getElementById('my-subtitle-display').innerText = this.activeCues[0].text;
            annotator.annotator('destroy');
            createAnnotator(uri + this.activeCues[0].startTime);
        });

        document.getElementById('my-subtitle-display').addEventListener('click', function () {
            document.getElementById("main-video").pause();
        });
        </c:if>

        // to add pre annotations
        <c:if test="${addPreAnnotation}">
        <c:forEach items="${phrases}" var="entry">
            createDynamicPreAnnotation("${entry.key}", "${entry.value}", uri);
        </c:forEach>
        </c:if>

        //to add annotations
         <c:if test="${addAnnotation}">
        <c:forEach items="${annotations}" var="annotation">
            createDynamicAnnotation("${annotation.quote}", "${annotation.text}", "${annotation.video}", "${annotation.figure}", "${annotation.wordDifficulty}", uri);
        </c:forEach>
        </c:if>

        var video = document.getElementById('main-video');
        var startTime;
        var endTime;
        <c:choose>
            <c:when test="${not empty startTime}">
                startTime = ${startTime};
            </c:when>
            <c:otherwise>
                startTime = 0;
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${not empty endTime}">
                endTime = ${endTime};
            </c:when>
            <c:otherwise>
                endTime = 0;
            </c:otherwise>
        </c:choose>

        if($("#main-video").length) {

            video.addEventListener('timeupdate', function () {
                if (!video.seeking) {
                    supposedCurrentTime = video.currentTime;
                }
            });
            // prevent user from seeking
            video.addEventListener('seeking', function () {
                if (video.currentTime > endTime || video.currentTime < startTime) {
                    console.log("Seeking is disabled");
                    video.currentTime = supposedCurrentTime;
                }
            });
        }


        $('#content').jqxPanel({width: '100%', height: '100%'});
        $('#main-splitter').jqxSplitter({
            width: '100%',
            height: '100%',
            orientation: 'horizontal',
            panels: [{size: '66%', collapsible: false}, {size: '34%', collapsible: true}]
        });
        $('#main-splitter').jqxSplitter('collapse');
    });

    function createAnnotator(uri) {
        var optiontags = {
            tag: "scientific:yellow,english:blue"
        };

        //enable basic annotator functionality
        <c:choose>
        <c:when test="${contentIsFile}">
        annotator = $('#content').annotator({
            readOnly: ${readOnly}
        });
        </c:when>
        <c:otherwise>
        annotator = $('#my-subtitle-display').annotator({
            readOnly: ${readOnly}
        });
        </c:otherwise>
        </c:choose>
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
        }).annotator('addPlugin', 'HighlightTags', optiontags);
    }

    function createDynamicAnnotation(word, definition, video, figure, difficulty, uri) {
        console.log("Adding annotation for: " + word);
        var allElements = Array.from(document.querySelectorAll('.highlighted'))
        allElements.forEach(function (element) {
            element.classList.remove('highlighted')
        });
        highlightSearchTerms(word, true);

        for (var i = 0; i < document.getElementsByClassName('highlighted').length; i++) {
            (function (i) {
                var element = document.getElementsByClassName('highlighted')[i];

                var rootXPath = getXpathOfNode(document.getElementsByClassName('annotator-wrapper')[0]);
                var xPath = getXpathOfNode(element);
                xPath = xPath.replace(rootXPath, '').replace('/FONT', '').toLowerCase();
                xPath = xPath.replace('tbody', 'tbody[1]').replace('td', 'td[1]');
                xPath = xPath.replace(/]\[.*?\]/g, ']')

                var parentElement = element.parentElement;
                var startOffset = parentElement.textContent.indexOf(word);
                var endOffset = startOffset + word.length;

                var annotation = {};
                var range = {};

                annotation.quote = word;
                annotation.text = definition;
                annotation.uri = uri;
                annotation.wordType = "${preAnnotationType}";
                annotation.video = video;
                annotation.figure = figure;
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
                    timeout: 600000,
                    success: function (data) {

                    },
                    error: function (e) {

                    }
                });
            })(i);
        }
    }


    function createDynamicPreAnnotation(searchWord, definition, uri) {
        // clear previous search
        console.log("Searching for..." + searchWord);
        var allElements = Array.from(document.querySelectorAll('.highlighted'))
        allElements.forEach(function (element) {
            element.classList.remove('highlighted')
        });
        highlightSearchTerms(searchWord, true);

        for (var i = 0; i < document.getElementsByClassName('highlighted').length; i++) {
            (function (i) {
                var element = document.getElementsByClassName('highlighted')[i];

                var rootXPath = getXpathOfNode(document.getElementsByClassName('annotator-wrapper')[0]);
                var xPath = getXpathOfNode(element);
                xPath = xPath.replace(rootXPath, '').replace('/FONT', '').toLowerCase();
                xPath = xPath.replace('tbody', 'tbody[1]').replace('td', 'td[1]');
                xPath = xPath.replace(/]\[.*?\]/g, ']')

                var parentElement = element.parentElement;
                var startOffset = parentElement.textContent.indexOf(searchWord);
                var endOffset = startOffset + searchWord.length;

                var annotation = {};
                var range = {};

                annotation.quote = searchWord;
                annotation.text = definition;
                annotation.uri = uri;
                annotation.wordType = "${preAnnotationType}";
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
                    timeout: 600000,
                    success: function (data) {

                    },
                    error: function (e) {

                    }
                });
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

    document.getElementById('complete').addEventListener('click', function () {
        var c = confirm('Are you sure you are finished viewing this page?');
        if(c == true) {
            var uri = getUrlParameter('uri', document.location.href);
            $.post("complete", {
                    uri: uri,
                }, function (data) {
                     window.location.href = "${contextPath}";
                });
        }
    });
</script>
</body>
</html>
