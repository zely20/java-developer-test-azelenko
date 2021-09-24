package com.vizor.mobile.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ControllerTweet {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerTweet.class);

    public String postRequest(String address, String prefixToken, String token, String payload) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(200);
            String authHeaderValue = prefixToken + token;
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            httpURLConnection.connect();
            if(payload != null) {
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
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {
                if (httpURLConnection.HTTP_OK == httpURLConnection.getResponseCode() || httpURLConnection.HTTP_CREATED == httpURLConnection.getResponseCode()) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
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
        return builder.toString();
    }
}
