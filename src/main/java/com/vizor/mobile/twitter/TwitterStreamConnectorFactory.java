package com.vizor.mobile.twitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.ConfigRule;
import com.vizor.mobile.ConfigTweet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

public class TwitterStreamConnectorFactory {

    private final static String URL_STREAM = "https://api.twitter.com/2/tweets/search/stream";
    private final static String URL_RULES = "https://api.twitter.com/2/tweets/search/stream/rules";
    private final static String URL_GET_TOKEN = "https://api.twitter.com/oauth2/token?grant_type=client_credentials";
    private static final Logger LOG = LoggerFactory.getLogger(TwitterStreamConnectorFactory.class);
    private String token = null;

    public TwitterStreamConnector createConnector(String apiKey, String secretKey) throws IOException {

        return new TwitterStreamConnector() {
            @Override
            public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) throws IOException, InterruptedException {
                if (token == null) {
                    token = getBearerToken(apiKey, secretKey);
                }
                String resultStatusRules = actionMethodGET(token, URL_RULES);
                if (resultStatusRules.contains("result_count\": 0")) {
                    String jsonRules = new Gson().toJson(ruleList);
                    jsonRules = "{\"add\": " + jsonRules + " }";
                    System.out.println(jsonRules);
                    String responseRules = actionMethodPostWithJson(URL_RULES, token, jsonRules);
                    System.out.println(responseRules);
                }
                System.out.println(resultStatusRules);
                getStream(token, streamConsumer);
            }
        };
    }

    private String getBearerToken(String apiKey, String secretKey) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(URL_GET_TOKEN);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(200);
            httpURLConnection.setReadTimeout(200);
            String auth = apiKey + ":" + secretKey;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();

            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.substring(39, builder.length() - 2);
    }

    private void getStream(String token, Consumer<Tweet> streamConsumer) {
        try {
            URL url = new URL(URL_STREAM);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(5000);
            String authHeaderValue = "Bearer " + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)){
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Tweet tweet = parsingResponseJsonToTweet(line);
                        System.out.println(tweet);
                        streamConsumer.accept(tweet);
                    }
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String actionMethodGET(String token, String urlAddress) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(300);
            String authHeaderValue = "Bearer " + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();

            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)){
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return result.toString();
    }

    private String actionMethodPostWithJson(String urlAddress, String token, String json) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(200);
            String authHeaderValue = "Bearer " + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            String jsonInputString = json;

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private Tweet parsingResponseJsonToTweet(String line) {
        JsonElement jsonObjectTweet = JsonParser.parseString(line).getAsJsonObject().get("data");
        JsonElement jsonObjectRules = JsonParser.parseString(line).getAsJsonObject().get("matching_rules");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type type = new TypeToken<List<ConfigRule>>() {
        }.getType();
        Tweet tweet = gson.fromJson(jsonObjectTweet, ConfigTweet.class);
        List<Rule> rules = new Gson().fromJson(jsonObjectRules, type);
        tweet.setMatchingRules(rules);
        return tweet;
    }
}
