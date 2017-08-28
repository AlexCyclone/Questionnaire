package com.devianta.simplequestions;

import javax.servlet.http.HttpServletResponse;

public interface Properties {
    public static String DB_PATH = "D:\\tcdata\\Questionnaire";

    public static void setResponseParameters(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
    }
}
