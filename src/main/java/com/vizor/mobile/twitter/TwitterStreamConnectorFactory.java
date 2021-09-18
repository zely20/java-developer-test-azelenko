package com.vizor.mobile.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TwitterStreamConnectorFactory {

    private final static String URL_STREAM = "https://api.twitter.com/2/tweets/search/stream";
    private final static String URL_GET_TOKEN = "https://api.twitter.com/oauth2/token?grant_type=client_credentials";
    private final static String CONTENT_TYPE = "Content-type: application/json";
    private final static String BEARER = "Authorization: Bearer ";
    private InputStreamReader inputStream = null;
    private BufferedReader bufferedReader = null;
    private StringBuilder builder = new StringBuilder();


    public TwitterStreamConnector createConnector(String apiKey, String secretKey) throws IOException {
        String token = getBearerToken(apiKey, secretKey);
        return null;
    }

    private String getBearerToken(String apiKey, String secretKey) {
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

            try {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                    bufferedReader = new BufferedReader(inputStream);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.substring(39, builder.length()-2);
    }
}
