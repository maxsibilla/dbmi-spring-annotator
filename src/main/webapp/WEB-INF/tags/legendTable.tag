<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="myTags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="table table-sm legend-font" style="margin-bottom: 3px;">
    <tbody>
    <tr>
        <td>
            <div class="color-box" style="background-color: yellow"></div>
        </td>
        <td>Scientific phrase</td>
    </tr>
    <tr>
        <td>
            <div class="color-box" style="background-color: blue"></div>
        </td>
        <td>English phrase</td>
    </tr>

    <tr>
        <td>Word Difficulty</td>
        <td>
            <div class="form-check-inline">
                <label class="form-check-label">
                    <input id="t6-check" type="checkbox" class="form-check-input" value="" checked
                           onclick="showHideWordDifficulty(this.id, 'T6')">Moderate
                </label>
            </div>
            <div class="form-check-inline">
                <label class="form-check-label">
                    <input id="d-check" type="checkbox" class="form-check-input" value="" checked
                           onclick="showHideWordDifficulty(this.id, 'D')">Difficult
                </label>
            </div>
        </td>
    </tr>

    <tr>
        <td colspan="2" style="text-align:center">
            <button id="hide-scientific-phrases" class="btn btn-outline-dark btn-xxs"
                    onclick="hidePhrases('scientific', this.id, 'show-scientific-phrases')">
                Hide Scientific Phrases
            </button>
            <button id="show-scientific-phrases" class="btn btn-outline-dark btn-xxs display-none"
                    onclick="showPhrases('scientific', this.id, 'hide-scientific-phrases')">
                Show Scientific
                Phrases
            </button>

            <button id="hide-english-phrases" class="btn btn-outline-dark btn-xxs"
                    onclick="hidePhrases('english', this.id, 'show-english-phrases')">
                Hide English Phrases
            </button>
            <button id="show-english-phrases" class="btn btn-outline-dark btn-xxs display-none"
                    onclick="showPhrases('english', this.id, 'hide-english-phrases')">
                Show English Phrases
            </button>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            Parent concept:
            <select class="parent-select" multiple="multiple" style="width: 100%">
                <c:forEach items="${concepts}" var="concept">
                    <optgroup label="${concept.key}">
                        <c:forEach items="${concept.value}" var="baseWord">
                            <option selected value="${baseWord}">${baseWord}</option>
                        </c:forEach>
                    </optgroup>
                </c:forEach>
            </select>
        </td>
    </tr>

    </tbody>
</table>

<script>
    $(document).ready(function () {
        $.fn.select2.amd.require(["optgroup-data", "optgroup-results"], function (OptgroupData, OptgroupResults) {
            $('.parent-select').find('option').prop('selected', 'selected').end().select2({
                dataAdapter: OptgroupData,
                resultsAdapter: OptgroupResults,
            })
            // $('.parent-select').select2();
        });
    });


    $('.parent-select').on('change', function (e) {
        showHideConcept('quote', 'parent-select')
    });
</script>