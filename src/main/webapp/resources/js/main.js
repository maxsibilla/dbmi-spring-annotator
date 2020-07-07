function toggleLegend(cmd) {
    if (cmd === "hide") {
        $('#main-legend').hide();
        $('#show-legend').show();
    } else if (cmd === "show") {
        $('#show-legend').hide();
        $('#main-legend').show();
    }
}

function hidePhrases(tag, buttonToHide, buttonToShow) {
    var elems = document.querySelectorAll("[data-annotation-tag='" + tag + "']");
    for (var i = 0; i < elems.length; i++) {
        elems[i].setAttribute("style", "background: none");
    }

    $("#" + buttonToHide).hide();
    $("#" + buttonToShow).show();

    if (tag == 'scientific') {
        sciencePhraseFilter = 'hidden';
    }
    if (tag == 'english') {
        englishPhraseFilter = 'hidden';
    }
}

function showPhrases(tag, buttonToHide, buttonToShow) {
    var backgroundColor;
    if (tag === "english") {
        backgroundColor = 'rgba(0, 0, 255, 0.3)';
    }
    if (tag === "scientific") {
        backgroundColor = 'rgba(255, 255, 0, 0.3)';
    }
    var elems = document.querySelectorAll("[data-annotation-tag='" + tag + "']");
    for (var i = 0; i < elems.length; i++) {
        elems[i].setAttribute("style", "background: " + backgroundColor);
    }

    $("#" + buttonToHide).hide();
    $("#" + buttonToShow).show();

    if (tag == 'scientific') {
        sciencePhraseFilter = 'visible';
    }
    if (tag == 'english') {
        englishPhraseFilter = 'visible';
    }
}

function showHideWordDifficulty(checkbox, difficulty) {
    var backgroundColor = 'rgba(0, 0, 255, 0.3)';

    var elems = document.querySelectorAll("[data-annotation-difficulty='" + difficulty + "']");

    if (document.getElementById(checkbox).checked) {
        for (var i = 0; i < elems.length; i++) {
            elems[i].setAttribute("style", "background: " + backgroundColor);
        }

        if (difficulty == 'T6') {
            wordDifficultyFilter.push('moderate');
        }

        if (difficulty == 'D') {
            wordDifficultyFilter.push('difficult');
        }
    } else {
        for (var i = 0; i < elems.length; i++) {
            elems[i].setAttribute("style", "background: none");
        }

        if (difficulty == 'T6') {
            wordDifficultyFilter.splice(wordDifficultyFilter.indexOf('moderate'), 1);
        }

        if (difficulty == 'D') {
            wordDifficultyFilter.splice(wordDifficultyFilter.indexOf('difficult'), 1);
        }
    }
}

function showHideConcept(conceptLineage, selectId) {
    var backgroundColor = 'rgba(255, 255, 0, 0.3)';
    var selected = $('.' + selectId).find(':selected');

    if (selected.length > 0) {
        var hideElems = document.querySelectorAll("[data-annotation-" + conceptLineage + "]");
        for (var i = 0; i < hideElems.length; i++) {
            hideElems[i].setAttribute("style", "background: none");
        }

        for (var i = 0; i < selected.length; i++) {
            var elems = document.querySelectorAll("[data-annotation-" + conceptLineage + "='" + selected[i].text + "']");

            for (var j = 0; j < elems.length; j++) {
                elems[j].setAttribute("style", "background: " + backgroundColor);
            }
        }
    } else {
        var showElems = document.querySelectorAll("[data-annotation-" + conceptLineage + "]");
        for (var i = 0; i < showElems.length; i++) {
            showElems[i].setAttribute("style", "background: " + backgroundColor);
        }
    }

}