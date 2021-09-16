package com.vizor.mobile.aggregator;

import com.vizor.mobile.twitter.Tweet;

/**
 * Базовый интерфейс для обработки твитов.
 *
 */
public interface TweetAggregator
{
    void aggregateTweet(Tweet tweet);
}
