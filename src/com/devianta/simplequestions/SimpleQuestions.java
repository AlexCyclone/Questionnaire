package com.devianta.simplequestions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devianta.dbworker.DBWorker;
import com.devianta.questionnaire.Answer;
import com.devianta.questionnaire.AnswerDB;
import com.devianta.questionnaire.Questionnaire;
import com.google.gson.JsonObject;

public class SimpleQuestions extends HttpServlet {

    private static final long serialVersionUID = 2L;
    private Map<String, AnswerDB> answerDBMap = new HashMap<>();

    public SimpleQuestions() {
        super();
        startServlet();
    }

    private final void startServlet() {
        String[] xmlFilesArray = DBWorker.listFiles(Properties.DB_PATH, "xml");
        for (String file : xmlFilesArray) {
            String dbFile = file.substring(0, file.lastIndexOf('.'));

            Questionnaire quenr = DBWorker.readXMLDB(Properties.DB_PATH, file, Questionnaire.class);
            AnswerDB db = DBWorker.readSerialDB(Properties.DB_PATH, dbFile + ".dat", AnswerDB.class);

            if (db.getQuenr() == null) {
                db.setQuenr(quenr);
            } else if (!db.getQuenr().equals(quenr)) {
                throw new IllegalArgumentException("Incorrect database file " + dbFile + ".dat");
            }

            answerDBMap.put(dbFile, db);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("type");
        PrintWriter out = response.getWriter();
        Properties.setResponseParameters(response);
        JsonObject myObj;

        switch (reqType) {
            case "getQList":
                myObj = getQList();
                break;
            case "checkRespondent":
                myObj = checkRespondent(request);
                break;
            case "getQuestion":
                myObj = getQuestion(request);
                break;
            case "addAnswer":
                myObj = addAnswer(request);
                break;
            case "getStatistic":
                myObj = getStatistic(request);
                break;
            default:
                myObj = null;
                break;
        }
        out.println(myObj.toString());
        out.close();
    }

    private JsonObject getQList() {
        Map<String, String> qList = new HashMap<>();
        Set<String> qSet = answerDBMap.keySet();
        for (String qName : qSet) {
            String qDesc = answerDBMap.get(qName).getQuenr().getName();
            qList.put(qName, qDesc);
        }
        JsonObject myObj = JsonWorker.toJson("OK");
        JsonWorker.addProp(myObj, "qList", qList);
        return myObj;
    }

    private JsonObject checkRespondent(HttpServletRequest request) {
        AnswerDB db = answerDBMap.get(request.getParameter("questionnaire"));
        if (db.contains(new Answer(request.getParameter("respondent")))) {
            return JsonWorker.toJson("reiteration");
        }
        return JsonWorker.toJson("OK");
    }


    private JsonObject getQuestion(HttpServletRequest request) {
        AnswerDB db = answerDBMap.get(request.getParameter("questionnaire"));
        Questionnaire questnr = db.getQuenr();

        JsonObject myObj = JsonWorker.toJson("OK");

        JsonWorker.addProp(myObj, "questionnaire", questnr.getQList());
        return myObj;
    }

    private JsonObject addAnswer(HttpServletRequest request) {
        String respondent = request.getParameter("respondent");
        AnswerDB db = answerDBMap.get(request.getParameter("questionnaire"));

        List<Integer> answers = new ArrayList<>();
        String[] arr = request.getParameter("answers").split("[,]");

        for (String answer : arr) {
            answers.add(Integer.parseInt(answer));
        }
        db.addAnswer(new Answer(respondent, answers));
        DBWorker.writeSerialDB(Properties.DB_PATH, request.getParameter("questionnaire") + ".dat", db);

        return JsonWorker.toJson("OK");
    }

    private JsonObject getStatistic(HttpServletRequest request) {
        String respondent = request.getParameter("respondent");
        AnswerDB db = answerDBMap.get(request.getParameter("questionnaire"));

        JsonObject myObj = JsonWorker.toJson("OK");

        Integer respondentCount = db.getRespCount();
        Integer[][] answerStatistic = db.getStatistic();
        Integer[] userStatistic = db.getUserAnswer(new Answer(respondent));

        JsonWorker.addProp(myObj, "respondentCount", respondentCount);
        JsonWorker.addProp(myObj, "answerStatistic", answerStatistic);
        JsonWorker.addProp(myObj, "userStatistic", userStatistic);

        return myObj;
    }

}
