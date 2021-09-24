package com.vizor.mobile;

import com.vizor.mobile.twitter.Rule;

import java.util.Objects;
import java.util.Optional;

public class ConfigRule implements Rule {

    private String id;
    private String value;
    private String tag;

    public ConfigRule() {
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Optional<String> getId() {
        return Optional.of(id);
    }

    @Override
    public String toString() {
        return "ConfigRule{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigRule that = (ConfigRule) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, tag);
    }
}
