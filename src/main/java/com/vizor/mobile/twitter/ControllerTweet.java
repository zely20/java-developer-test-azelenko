package com.vizor.mobile.twitter;

import com.vizor.mobile.ParserJson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class ControllerTweet {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerTweet.class);

    public String postRequest(String address, String prefixToken, String token, String payload) {
        StringBuilder builder = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(address);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(200);
            String authHeaderValue = prefixToken + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            if (payload != null) {
                try (OutputStream os = httpURLConnection.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode() || httpURLConnection.HTTP_CREATED == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            httpURLConnection.disconnect();
        }
        return builder.toString();
    }

    public void getStream(String urlAddress, String prefixToken, String token, ParserJson parserJson, Consumer<Tweet> streamConsumer) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(5000);
            String authHeaderValue = prefixToken + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.equals("")) {
                            Tweet tweet = parserJson.parsingResponseJsonToTweet(line);
                            streamConsumer.accept(tweet);
                            System.out.println(tweet);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public String getRequest(String urlAddress, String prefixToken, String token) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(300);
            String authHeaderValue = prefixToken + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            httpURLConnection.disconnect();
        }
        return result.toString();
    }
}
