package com.devianta.simplequestions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonWorker {
    public static <T> JsonObject toJson(T object) {
        Gson gson = new GsonBuilder().create();
        JsonObject myObj = new JsonObject();
        JsonElement qObj = gson.toJsonTree(object);
        myObj.add("result", qObj);
        return myObj;
    }

    public static <T> JsonObject addProp(JsonObject myObj, String property, T object) {
        Gson gson = new GsonBuilder().create();
        myObj.add(property, gson.toJsonTree(object));
        return myObj;
    }
}
