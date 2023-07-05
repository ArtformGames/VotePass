package com.artformgames.plugin.votepass.core.database;

import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public class DataSerializer {

    private DataSerializer() {
    }

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static String serializeAnswers(Map<Integer, RequestAnswer> answers) {
        return GSON.toJson(answers);
    }

    public static Map<Integer, RequestAnswer> deserializeAnswers(String answersJSON) {
        return GSON.fromJson(answersJSON, new TypeToken<Map<Integer, RequestAnswer>>() {
        }.getType());
    }

    public static List<String> deserializeStringList(String json) {
        return GSON.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

}
