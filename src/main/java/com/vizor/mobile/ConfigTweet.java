package com.vizor.mobile;

import com.google.gson.annotations.SerializedName;
import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigTweet tweet = (ConfigTweet) o;
        return Objects.equals(text, tweet.text) && Objects.equals(matchingRules, tweet.matchingRules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, matchingRules);
    }
}
