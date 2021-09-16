package com.vizor.mobile.twitter;

import java.util.Optional;

/**
 * Описывает правило настройки потока твитов.
 *
 * {@see https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule}
 * {@see https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/api-reference/post-tweets-search-stream-rules}
 */
public interface Rule
{
    String getValue();
    String getTag();
    Optional<String> getId();
}
