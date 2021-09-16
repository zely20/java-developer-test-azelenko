package com.vizor.mobile.aggregator;

public class TextUtils
{
    static int wordCount(String text) {
        return text.split("\\s[.,:;()?!-]\\s|\\s+").length;
    }
}
