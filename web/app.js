$(document).ready(function () {
    var respondent = "";
    var qList;
    var quenrName = "";
    var questions;
    var questionNumber;
    var answers;

    // Start

    var dataString = "type=getUsername";
    $.ajax({
        type: "POST",
        url: "Login",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            if (data.result != "OK") {
                clearLogin();
                showLogIn();
            } else {
                respondent = data.username;
                setLoginAs(respondent);
                clearLogin();
                qList = showUserPanel();
            }
            ;
        },
        error: function (jqXHR, textStatus) {
            errorShow(jqXHR, textStatus);
        }
    });

    // Login

    $("#LoginForm").submit(function (e) {
        e.preventDefault();
    });

    $(":input[name='respondent']").keyup(function () {
        if ($("#respondent").val() == "" || $("#pass").val() == "") {
            $("#sendName").prop('disabled', true);
        } else {
            $("#sendName").prop('disabled', false);
        }

        if ($("#respondent").val() == "" || $("#pass").val() == "" || $("#retype").val() == "") {
            $("#submit").prop('disabled', true);
        } else {
            if ($("#pass").val() == $("#retype").val()) {
                $("#submit").prop('disabled', false);
            } else {
                $("#submit").prop('disabled', true);
            }
        }
    });

    $("#sendName").click(function () {
        respondent = $("input#respondent").val();
        var pass = $("input#pass").val();
        pass = getHash(respondent + pass);

        var dataString = "type=login";
        dataString += "&respondent=" + respondent;
        dataString += "&password=" + pass;

        $.ajax({
            type: "POST",
            url: "Login",
            cache: false,
            async: false,
            data: dataString,
            dataType: "json",
            success: function (data) {
                if (data.result != "OK") {
                    respondent = "";
                    alert("User with your password not found. Try again or register.");
                    $("#pass").attr("value", "");
                    $("#sendName").prop('disabled', true);
                } else {
                    $("#respondent").attr("value", "");
                    $("#pass").attr("value", "");
                    setLoginAs(respondent);
                    clearLogin();
                    qList = showUserPanel();
                }
                ;
            },
            error: function (jqXHR, textStatus) {
                errorShow(jqXHR, textStatus);
            }
        });
    });

    // Registration

    $("#register").click(function () {
        showRegistration();
    });

    $("#submit").click(function () {

        respondent = $("input#respondent").val();
        var pass = $("input#pass").val();
        pass = getHash(respondent + pass);

        var dataString = "type=register";
        dataString += "&respondent=" + respondent;
        dataString += "&password=" + pass;

        $.ajax({
            type: "POST",
            url: "Login",
            cache: false,
            async: false,
            data: dataString,
            dataType: "json",
            success: function (data) {
                if (data.result != "OK") {
                    alert("User not added. Try other username.");
                    respondent = "";
                    clearRegistration();
                } else {
                    setLoginAs(respondent);
                    clearRegistration();
                    qList = showUserPanel();
                }
                ;
            },
            error: function (jqXHR, textStatus) {
                errorShow(jqXHR, textStatus);
            }
        });
    });

    $("#backToLogin").click(function () {
        showLogIn();
    });

    // Logout

    $("#logOut").click(function () {
        var dataString = "type=logout";
        respondent = "";

        $.ajax({
            type: "POST",
            url: "Login",
            cache: false,
            async: false,
            data: dataString,
            dataType: "json",
            success: function (data) {
                if (data.result == "OK") {
                    showLogIn();
                } else {
                    hideAll();
                    $("#Error").show(300);
                    console.log("Something really bad happened " + data.result);
                }
                ;
            },
            error: function (jqXHR, textStatus) {
                errorShow(jqXHR, textStatus);
            }
        });
    });

    // Start dialog

    $("#Questionnaire").on('click', '.start', function () {
        quenrName = $(this).attr("name");
        if (!checkRespAnswer(respondent, quenrName)) {
            if (!confirm("You've already answered the questions. Your answers will be replaced.\nWould you like to continue?")) {
                return;
            }
        }
        questions = getQuestion(quenrName);
        questionNumber = 0;
        answers = [];
        showQuestion(questions, questionNumber++);
    });

    // Dialog

    $("#question").on('change', ":radio[name='answer']", function () {
        $("#acceptAnswer").prop('disabled', false);
    });

    $("#acceptAnswer").click(function () {
        answers[questionNumber - 1] = Number($('input[name=answer]:checked').val());
        if (questionNumber < questions.length) {
            showQuestion(questions, questionNumber++);
        } else {
            sendAnswer(respondent, quenrName, answers);
            var stat = getStatistic(respondent, quenrName);
            showStatistic(stat, questions);
        }
    });

    // Statistic

    $("#Questionnaire").on('click', '.statistic', function () {
        quenrName = $(this).attr("name");
        var stat = getStatistic(respondent, quenrName);
        questions = getQuestion(quenrName);
        showStatistic(stat, questions);
    });

    $("#repeat").click(function () {
        clearStatictic();
        showUserPanel();
    });
});

// For everithing

function hideAll() {
    $("form").hide(300);
}

function errorShow(jqXHR, textStatus) {
    hideAll();
    $("#Error").show(300);
    console.log("Something really bad happened " + textStatus);
    $("#err").html(jqXHR.responseText);
}

// LoginForm functions

function showLogIn() {
    hideAll();
    setTimeout(function () {
        $("#loginLgnd").html("Login to questionnaires");
        $("#sendName").show();
        $("#register").show();
        $("#backToLogin").hide();
        $("#submit").hide();
        $("#retype").hide();
        $("#retypeLabel").hide();
        $("#LoginForm").show(300);
    }, 300);
}

function clearLogin() {
    $("#loginLgnd").html("");
    $("#submit").prop('disabled', true);
    $("#sendName").prop('disabled', true);
    $("#register").prop('disabled', false);
    $("#respondent").attr("value", "");
    $("#pass").attr("value", "");
    $("#retype").attr("value", "");
}

function getHash(str) {
    var hash = 0;
    var i = 0;
    var len = str.length;
    while (i < len) {
        hash = ((hash << 5) - hash + str.charCodeAt(i++)) << 0;
    }
    return (hash + 2147483647) + 1;
}

// Registration function

function showRegistration() {
    hideAll();
    setTimeout(function () {
        $("#loginLgnd").html("Registration");
        $("#submit").show();
        $("#retype").show();
        $("#retypeLabel").show();
        $("#backToLogin").show();
        $("#sendName").hide();
        $("#register").hide();
        $("#LoginForm").show(300);
    }, 300);
}

function clearRegistration() {
    clearLogin();
}

function setLoginAs(respondent) {
    $("span[name='loginAs']").html(respondent);
}

// User panel function

function showUserPanel() {
    var qList;
    var dataString = "type=getQList";

    $.ajax({
        type: "POST",
        url: "SimpleQuestions",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            if (data.result == "OK") {
                qList = data.qList;
            } else {
                hideAll();
                $("#Error").show(300);
                console.log("Something really bad happened " + data.result);
            }
            ;
        },
        error: function (jqXHR, textStatus) {
            errorShow(jqXHR, textStatus);
        }
    });

    hideAll();
    setTimeout(function () {
        $("#qList").html("");
        for (var key in qList) {
            $("#qList").append("<tr><td>" + qList[key] + "</td><td><a href='#' class='start' name='" + key + "'>Start</a></td><td><a href='#' class='statistic' name='" + key + "'>Statistic</a></td></tr>");
        }
        $("#Questionnaire").show(300);
    }, 300);
    return qList;
}

// Question manage

function getQuestion(quenrName) {
    var questions = null;
    dataString = "type=getQuestion";
    dataString += "&questionnaire=" + quenrName;

    $.ajax({
        type: "POST",
        url: "SimpleQuestions",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            questions = data.questionnaire;
        },
        error: function (jqXHR, textStatus) {
            errorShow(jqXHR, textStatus);
        }
    });
    return questions;
}

function checkRespAnswer(respondent, quenrName) {
    var dataString = "type=checkRespondent";
    dataString += "&respondent=" + respondent;
    dataString += "&questionnaire=" + quenrName;

    var bool;
    $.ajax({
        type: "POST",
        url: "SimpleQuestions",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            if (data.result == "OK") {
                bool = true;
            } else {
                bool = false;
            }
            ;
        },
        error: function (jqXHR, textStatus) {
            errorShow(jqXHR, textStatus);
        }
    });
    return bool;
}

// Dialog

function showQuestion(questions, questionNumber) {
    hideAll();
    setTimeout(function () {
        clearQuestion();

        $("#questionNumber").append(questionNumber + 1);
        $("#question").append("<label>" + questions[questionNumber].question + "</label></br></br>");
        questions[questionNumber].answer.forEach(function (answ, i) {
            $("#question").append("<input name=\"answer\" type=\"radio\" value=\"" + i + "\">" + answ + "</br>");
        });
        $("#question").append("</br>");

        $("#QuestionForm").show(300);
    }, 300);
}

function clearQuestion() {
    $("#questionNumber").html("");
    $("#question").html("");
}

// Answer send

function sendAnswer(respondent, quenrName, answers) {
    var dataString = "type=addAnswer";
    dataString += "&respondent=" + respondent;
    dataString += "&questionnaire=" + quenrName;
    dataString += "&answers=" + answers;
    $("#QuestionForm").hide(300);

    $.ajax({
        type: "POST",
        url: "SimpleQuestions",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            if (data.result != "OK") {
                hideAll();
                $("#Error").show(300);
                console.log("Something really bad happened " + data.result);
            }
            ;
        },
        error:
            function (jqXHR, textStatus) {
                $("#StatisticForm").hide(300);
                $("#Error").show(300);
                console.log("Something really bad happened: " + textStatus);
                $("#err").html(jqXHR.responseText);
            }
    });
}

// Statistic

function getStatistic(respondent, quenrName) {
    var stat = null;
    var dataString = "type=getStatistic";
    dataString += "&respondent=" + respondent;
    dataString += "&questionnaire=" + quenrName;

    $.ajax({
        type: "POST",
        url: "SimpleQuestions",
        cache: false,
        async: false,
        data: dataString,
        dataType: "json",
        success: function (data) {
            stat = data;
        },
        error: function (jqXHR, textStatus) {
            errorShow(jqXHR, textStatus);
        }
    });
    return stat;
}

function showStatistic(stat, questions) {
    hideAll();
    setTimeout(function () {
        clearStatictic();
        if(stat.userStatistic == null) {
            $("#userStatistic").append("<tr><td>You have not answered these questions yet</td></tr>");
        } else {
            for (i = 0; i < questions.length; i++) {
                $("#userStatistic").append("<tr><td>" + questions[i].question + "</td><td>" + questions[i].answer[stat.userStatistic[i]] + "</td></tr>");
            }
        }
        $("#answerCount").append(stat.respondentCount);
        for (i = 0; i < questions.length; i++) {
            $("#statistic").append("<tr><td>" + questions[i].question + "</td></tr>");
            $("#statistic").append("<tr><td>Answer:</td><td>Count:</td><td>Response share:</td></tr>");
            for (j = 0; j < questions[i].answer.length; j++) {
                $("#statistic").append("<tr><td>" + questions[i].answer[j] + "</td><td>" + stat.answerStatistic[i][j] + "</td><td>" + Number(stat.answerStatistic[i][j] / stat.respondentCount * 100).toFixed(2) + " %</td></tr>");
            }
            $("#statistic").append("<tr><td></br></td></tr>");
        }
        $("#StatisticForm").show(300);
    }, 300);
}

function clearStatictic() {
    $("#answerCount").html("");
    $("#userStatistic").html("");
    $("#statistic").html("");
}