package com.vizor.mobile;

import com.vizor.mobile.twitter.Rule;

import java.util.Optional;

public class ConfigRule implements Rule
{
    private String value;
    private String tag;

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
        return Optional.empty();
    }
}
