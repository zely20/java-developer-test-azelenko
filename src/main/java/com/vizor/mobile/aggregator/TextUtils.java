package com.vizor.mobile.aggregator;

public class TextUtils
{
    static int wordCount(String text) {
        return text.split("\\s[.,:;()?!-]\\s|\\s+").length;
    }
    static int charCount(String text) {
        String result = text.replaceAll("\\s+","");
        return result.length();
    }
}
