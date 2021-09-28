package com.vizor.mobile.twitter;

import com.vizor.mobile.ParserJson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

public class TwitterStreamConnectorFactory {

    private final static String URL_STREAM = "https://api.twitter.com/2/tweets/search/stream";
    private final static String URL_RULES = "https://api.twitter.com/2/tweets/search/stream/rules";
    private final static String URL_GET_TOKEN = "https://api.twitter.com/oauth2/token?grant_type=client_credentials";
    private final static String BASIC = "Basic ";
    private final static String BEARER = "Bearer ";
    private ParserJson parserJson = new ParserJson();
    private String token = null;
    private ControllerTweet controllerTweet = new ControllerTweet();

    public TwitterStreamConnector createConnector(String apiKey, String secretKey) throws IOException {
        return new TwitterStreamConnector() {
            @Override
            public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) throws IOException, InterruptedException {
                if (token == null) {
                    token = controllerTweet.postRequest(URL_GET_TOKEN, BASIC,
                            getEncodeSecret(apiKey,secretKey), null);
                    token = parserJson.parseGetToken(token);
                }
                String resultStatusRules = controllerTweet.getRequest(URL_RULES, BEARER, token);
                int countRules = -1;
                    countRules = parserJson.parseJsonResultStatusRules(resultStatusRules);
                    String addRulesRequestJson = parserJson.parseToCreateAddRulesRequest(ruleList);
                if (countRules == 0) {
                    controllerTweet.postRequest(URL_RULES, BEARER, token, addRulesRequestJson);
                }else {
                    String responseDelete = parserJson.parseToDeleteRulesRequest(resultStatusRules);
                    controllerTweet.postRequest(URL_RULES, BEARER, token, responseDelete);
                    controllerTweet.postRequest(URL_RULES, BEARER, token, addRulesRequestJson);
                }
                controllerTweet.getStream(URL_STREAM, BEARER, token, parserJson, streamConsumer);
            }
        };
    }

    private String getEncodeSecret(String apiKey, String secretKey) {
        String auth = apiKey + ":" + secretKey;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return new String(encodedAuth);
    }
}
