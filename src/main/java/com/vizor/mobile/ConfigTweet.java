package com.vizor.mobile;

import com.google.gson.annotations.SerializedName;
import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.Tweet;

import java.util.ArrayList;
import java.util.List;

public class ConfigTweet implements Tweet {
    private String text;
    @SerializedName(value = "matching_rules")
    private List<Rule> matchingRules = new ArrayList<>();

    @Override
    public String toString() {
        return "ConfigTweet{" +
                "text='" + text + '\'' +
                ", rules=" + matchingRules +
                '}';
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<Rule> getMatchingRules() {
        return matchingRules;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMatchingRules(List<Rule> matchingRules) {
        this.matchingRules = matchingRules;
    }
}
