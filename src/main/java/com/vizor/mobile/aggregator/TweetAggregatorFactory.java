package com.vizor.mobile.aggregator;

public class TweetAggregatorFactory
{
    public TweetAggregator createPrometheusAggregator()
    {
        return new PrometheusTweetAggregator();
    }
}
