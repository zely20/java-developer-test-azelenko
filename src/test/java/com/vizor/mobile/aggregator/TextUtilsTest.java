package com.vizor.mobile.aggregator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextUtilsTest
{

    @Test
    void wordCount()
    {
        assertEquals(1, TextUtils.wordCount("word"));
        assertEquals(2, TextUtils.wordCount("word word"));
        assertEquals(2, TextUtils.wordCount("word  word"));
        assertEquals(2, TextUtils.wordCount("word , word"));
        assertEquals(2, TextUtils.wordCount("Word\nWord."));
        assertEquals(2, TextUtils.wordCount("Word - Word"));
        assertEquals(3, TextUtils.wordCount("Word123 123 Word"));
        assertEquals(3, TextUtils.wordCount("Word123 123 Word "));
        assertEquals(1, TextUtils.wordCount("three-part-word"));

        assertEquals(14, TextUtils.charCount("Word123 123 Word "));
        assertEquals(8, TextUtils.charCount("word  word"));
        assertEquals(8, TextUtils.charCount("word word"));
        assertEquals(9, TextUtils.charCount("word , word"));
    }
}