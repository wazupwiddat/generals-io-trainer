package com.jdub.generals.trainer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameswarren on 4/10/17.
 */
public class Utils {
    public static List<Long> toLongArray(JSONArray array) {
        List<Long> data = new ArrayList<Long>();

        if (array == null) return data;
        int length = array.size();

        for (int i = 0; i < length; i++) {
            data.add(i, (Long)array.get(i));
        }
        return data;
    }

    public static List<String> toStringArray(JSONArray array) {
        List<String> data = new ArrayList<String>();

        if (array == null) return data;
        int length = array.size();
        for (int i = 0; i < length; i++) {
            data.add(i, (String)array.get(i));
        }
        return data;
    }

    public static List<GameMove> toGameMoveArray(JSONArray array) {
        List<GameMove> data = new ArrayList<>();
        if (array == null) return data;
        int length = array.size();
        for (int i = 0; i < length; i++) {
            data.add(i, new GameMove((JSONObject)array.get(i)));
        }
        return data;
    }
}
