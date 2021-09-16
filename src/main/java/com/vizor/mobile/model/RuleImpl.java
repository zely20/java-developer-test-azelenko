package com.vizor.mobile.model;

import com.vizor.mobile.aggregator.PrometheusTweetAggregator;
import com.vizor.mobile.twitter.Rule;

import java.util.Optional;

public class RuleImpl implements Rule {

    private String id;
    private String value;
    private String tag;

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public Optional<String> getId() {
        return Optional.of(this.id);
    }
}
