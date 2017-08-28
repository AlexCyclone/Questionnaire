package com.devianta.simplequestions;

import com.devianta.dbworker.DBWorker;
import com.devianta.registration.User;
import com.devianta.registration.UserDB;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class Login extends HttpServlet {

    private static final long serialVersionUID = 2L;
    UserDB users;

    public Login() {
        loadDB();
    }

    private final void loadDB() {
        users = DBWorker.readSerialDB(Properties.DB_PATH, "users.db", UserDB.class);
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
            case "getUsername":
                myObj = getUsername(request);
                break;
            case "login":
                myObj = login(request);
                break;
            case "register":
                myObj = register(request);
                break;
            case "logout":
                myObj = logout(request);
                break;
            default:
                myObj = null;
                break;
        }
        out.println(myObj.toString());
        out.close();
    }

    private JsonObject getUsername(HttpServletRequest request) {
        JsonObject myObj = null;

        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("user_login");

        if (username == null || username.equals("")) {
            myObj = JsonWorker.toJson("undefined");
        } else {
            myObj = JsonWorker.toJson("OK");
            JsonWorker.addProp(myObj, "username", username);
        }
        return myObj;
    }

    private JsonObject login(HttpServletRequest request) {
        JsonObject myObj = null;

        User user = new User();
        user.setUsername(request.getParameter("respondent"));
        user.setPassword(request.getParameter("password"));

        if (users.hasUser(user)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user_login", user.getUsername());
            myObj = JsonWorker.toJson("OK");
        } else {
            myObj = JsonWorker.toJson("undefined");
        }

        return myObj;
    }

    private JsonObject register(HttpServletRequest request) {
        JsonObject myObj = null;

        User user = new User();
        user.setUsername(request.getParameter("respondent"));
        user.setPassword(request.getParameter("password"));

        if (users.addUser(user)) {
            DBWorker.writeSerialDB(Properties.DB_PATH, "users.db", users);
            myObj = login(request);
        } else {
            myObj = JsonWorker.toJson("duplicate");
        }
        return myObj;
    }

    private JsonObject logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user_login");
        }

        return JsonWorker.toJson("OK");
    }
}
