package com.vizor.mobile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.twitter.Rule;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ParserJson {

    public Integer parseJsonResultStatusRules(String resultStatusRules) {
        if(resultStatusRules == null) {
            throw new NullPointerException("resultStatusRules is null");
        }
        JsonObject jsonObject = new Gson().fromJson(resultStatusRules, JsonObject.class);
        jsonObject = jsonObject.getAsJsonObject("meta");
        JsonElement count = jsonObject.get("result_count");
        return count.getAsInt();
    }

    public String parseToCreateAddRulesRequest(List<Rule> rules) {
        JsonObject jsonObject = new JsonObject();
        JsonElement element = new Gson().toJsonTree(rules, new TypeToken<List<Rule>>() {}.getType());
        JsonArray requestArray = element.getAsJsonArray();
        jsonObject.add("add", requestArray);
        return new Gson().toJson(jsonObject);
    }

    public String parseToDeleteRulesRequest(String dataRules) {
        JsonObject jsonRequestIds = new JsonObject();
        JsonObject jsonRequestDelete = new JsonObject();

        Type type = new TypeToken<List<ConfigRule>>(){}.getType();
        JsonObject jsonObject = new Gson().fromJson(dataRules, JsonObject.class);
        JsonArray jsonRules = jsonObject.getAsJsonArray("data");
        List<Rule> rules = new Gson().fromJson(jsonRules,type);
        List<String> jsonListRules = rules.stream()
                .map(rule -> rule.getId().get())
                        .collect(Collectors.toList());
        JsonElement element = new Gson().toJsonTree(jsonListRules, new TypeToken<List<String>>() {}.getType());
        JsonArray requestArray = element.getAsJsonArray();
        jsonRequestIds.add("ids", requestArray);
        jsonRequestDelete.add("delete", jsonRequestIds);
        String result = new Gson().toJson(jsonRequestDelete);
        return result;
    }
}
