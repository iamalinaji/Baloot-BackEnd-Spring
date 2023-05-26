package Baloot.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

public class JsonParser {
    public static JSONObject parseJsonObject(String s) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(s);
            return (JSONObject) obj;
        } catch (ParseException pe) {
            System.out.println(pe);
        }
        return new JSONObject();
    }

    public static JSONArray parseJsonArray(String s) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(s);
            return (JSONArray) obj;
        } catch (ParseException pe) {
            System.out.println(pe);
        }
        return new JSONArray();
    }

    static ArrayList<String> parseCategory(String c) {
        c = c.replaceAll("[\\[\\]]", "");
        String[] spited = c.split(", ");
        return new ArrayList<>(Arrays.asList(spited));
    }

    public static ArrayList<String> parseCategory(JSONArray a) {
        ArrayList<String> categories = new ArrayList<>();
        for (Object obj : a) {
            String val = (String) obj;
            categories.add(val);
        }
        return categories;
    }
}

