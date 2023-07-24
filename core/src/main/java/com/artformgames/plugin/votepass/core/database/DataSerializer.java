package com.artformgames.plugin.votepass.core.database;

import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataSerializer {

    private DataSerializer() {
    }

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static String serializeAnswers(Map<Integer, RequestAnswer> answers) {
        return GSON.toJson(answers);
    }

    public static Map<Integer, RequestAnswer> deserializeAnswers(String answersJSON) {
        JsonObject obj = JsonParser.parseString(answersJSON).getAsJsonObject();
        Map<Integer, RequestAnswer> answerMap = new TreeMap<>();
        obj.entrySet().forEach(entry -> {
            int id = Integer.parseInt(entry.getKey());
            JsonObject answerObj = entry.getValue().getAsJsonObject();

            String question = answerObj.get("question").getAsString();
            List<String> answers = new ArrayList<>();

            answerObj.get("answers").getAsJsonArray().forEach(s -> answers.add(s.getAsString()));

            RequestAnswer answer = new RequestAnswer(question, answers);
            answerMap.put(id, answer);
        });
        return answerMap;
    }

    public static List<String> deserializeStringList(String json) {
        return GSON.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

}
