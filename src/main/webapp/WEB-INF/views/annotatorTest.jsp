<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<myTags:imports title="Lorem Ipsum"/>
<link href="${contextPath}/resources/css/auth.css" rel="stylesheet">
<link href="${contextPath}/resources/css/jqx.base.css" rel="stylesheet">
<script src="${contextPath}/resources/js/splitter/jqxcore.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxbuttons.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxsplitter.js"></script>
<script src="${contextPath}/resources/js/splitter/jqxscrollbar.js"></script>


<body>
<myTags:navbar></myTags:navbar>
<main id="main" role="main" class="container">
    <div id="main-splitter">
        <div id="content" style="overflow-y: auto;">
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et
                dolore
                magna
                aliqua. Et netus et malesuada fames ac turpis. Porttitor rhoncus dolor purus non enim praesent
                elementum.
                Sagittis
                id consectetur purus ut faucibus pulvinar elementum integer enim. In dictum non consectetur a erat nam
                at
                lectus
                urna. Lorem ipsum dolor sit amet. Dignissim diam quis enim lobortis scelerisque. Imperdiet dui accumsan
                sit
                amet
                nulla facilisi morbi tempus iaculis. Pretium quam vulputate dignissim suspendisse in est. Eget dolor
                morbi
                non
                arcu
                risus quis varius. Mollis nunc sed id semper risus. Sit amet aliquam id diam maecenas ultricies mi eget
                mauris.
                Id
                semper risus in hendrerit gravida rutrum quisque non tellus. Id porta nibh venenatis cras sed felis
                eget.
                Purus
                semper eget duis at tellus at. Ultricies mi eget mauris pharetra et ultrices. Ut consequat semper
                viverra
                nam
                libero
                justo.</p>

            <p>Rutrum quisque non tellus orci. Felis eget velit aliquet sagittis id consectetur purus ut. Phasellus
                vestibulum
                lorem sed risus. Aliquet bibendum enim facilisis gravida. Velit sed ullamcorper morbi tincidunt.
                Scelerisque
                viverra
                mauris in aliquam. Turpis egestas maecenas pharetra convallis posuere morbi leo. In metus vulputate eu
                scelerisque
                felis imperdiet. Nisi est sit amet facilisis magna etiam tempor orci eu. Faucibus purus in massa tempor
                nec.</p>

            <p>Dolor purus non enim praesent elementum facilisis leo. Malesuada fames ac turpis egestas. Cras ornare
                arcu
                dui
                vivamus arcu. Mattis pellentesque id nibh tortor id aliquet lectus proin nibh. Placerat orci nulla
                pellentesque
                dignissim enim sit amet venenatis urna. Id venenatis a condimentum vitae sapien pellentesque habitant
                morbi.
                Sagittis nisl rhoncus mattis rhoncus urna. Felis bibendum ut tristique et. Sit amet nisl purus in
                mollis.
                Viverra
                maecenas accumsan lacus vel facilisis. Nunc aliquet bibendum enim facilisis gravida neque convallis a.
                Tincidunt
                lobortis feugiat vivamus at augue eget. Rutrum tellus pellentesque eu tincidunt tortor aliquam
                nulla.</p>

            <p>Sapien pellentesque habitant morbi tristique senectus et netus et. Eu ultrices vitae auctor eu. Enim diam
                vulputate
                ut pharetra sit amet aliquam. Ultrices gravida dictum fusce ut. Ac tortor vitae purus faucibus ornare
                suspendisse
                sed. Est ullamcorper eget nulla facilisi. Vel facilisis volutpat est velit egestas dui id ornare arcu.
                Dictum at
                tempor commodo ullamcorper a lacus vestibulum sed arcu. Dictum sit amet justo donec enim. Sed tempus
                urna et
                pharetra pharetra. Diam vulputate ut pharetra sit amet aliquam id diam maecenas. Sodales ut eu sem
                integer
                vitae
                justo. Lorem dolor sed viverra ipsum nunc aliquet bibendum. Congue mauris rhoncus aenean vel elit
                scelerisque.
                Malesuada fames ac turpis egestas.</p>

            <p>Eu lobortis elementum nibh tellus molestie. Arcu bibendum at varius vel pharetra vel turpis. Vestibulum
                rhoncus
                est
                pellentesque elit ullamcorper. Eget mi proin sed libero enim sed faucibus. Nibh mauris cursus mattis
                molestie a
                iaculis. Eget mauris pharetra et ultrices neque ornare aenean euismod. Ornare massa eget egestas purus
                viverra
                accumsan in nisl nisi. Tempor id eu nisl nunc mi ipsum faucibus. Leo vel fringilla est ullamcorper eget
                nulla
                facilisi etiam dignissim. Volutpat sed cras ornare arcu dui vivamus arcu.</p>
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
    $(function () {
        var uri = getUrlParameter('file', document.location.href);
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

        $('#main-splitter').jqxSplitter({
            width: $('#main').width(),
            height: $(window).height() - 100,
            // width: '100%',
            // height: '100%',
            orientation: 'horizontal',
            panels: [{size: '70%', collapsible: false}, {size: '30%', collapsible: true}]
        });

    });

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
