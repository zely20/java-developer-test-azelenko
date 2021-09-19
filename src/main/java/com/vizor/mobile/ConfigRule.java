package com.vizor.mobile;

import com.google.gson.annotations.SerializedName;
import com.vizor.mobile.twitter.Rule;

import java.util.Optional;

public class ConfigRule implements Rule
{
    private String id;
    private String value;
    private String tag;

    public ConfigRule() {
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public String getTag()
    {
        return tag;
    }

    @Override
    public Optional<String> getId()
    {
        return Optional.of(id);
    }
}
