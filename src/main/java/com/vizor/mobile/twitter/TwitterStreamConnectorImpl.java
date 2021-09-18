package com.vizor.mobile.twitter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class TwitterStreamConnectorImpl implements TwitterStreamConnector{

    private final static String URL_STREAM = "https://api.twitter.com/2/tweets/search/stream";
    private final static String URL_GET_TOKEN = "https://api.twitter.com/2/tweets/search/stream";
    private final static String CONTENT_TYPE = "Content-type: application/json";
    private final static String BEARER = "Authorization: Bearer ";

    @Override
    public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) throws IOException, InterruptedException {

    }
}
