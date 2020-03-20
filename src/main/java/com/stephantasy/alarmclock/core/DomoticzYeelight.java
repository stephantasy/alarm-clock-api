package com.stephantasy.alarmclock.core;

import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * NOTES:
 *  - Domoticz HTTP Requests: https://www.domoticz.com/wiki/Domoticz_API/JSON_URL's
 *
 *  RGB:
 *      m = 3
 *      r, g, b = 0-255
 *      cw = t = ww = 0
 *
 *  White: (the brightness is brightest)
 *      m = 2
 *      cw = 0-255 (warm -> cold)
 *      t = ww = 255 - cw
 *      r = g = b = 0
*/

@Component
public class DomoticzYeelight {

    @Value("${alarmclock.url}")
    private String url;

    @Value("${alarmclock.port}")
    private int port;

    @Value("${alarmclock.idx}")
    private String idx;

    @Value("${alarmclock.username}")
    private String username;

    @Value("${alarmclock.password}")
    private String password;

    private OkHttpClient httpClient;

    public DomoticzYeelight(){
        try {
            httpClient = new OkHttpClient().newBuilder()
                    .followRedirects(false)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void sendGet() throws Exception {
//
//        Request request = new Request.Builder()
//                .url("https://www.google.com/search?q=mkyong")
//                .addHeader("custom-key", "mkyong")  // add request headers
//                .addHeader("User-Agent", "OkHttp Bot")
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            // Get response body
//            System.out.println(response.body().string());
//        }
//
//    }

    //http://192.168.1.44:8080/json.htm?type=command&param=switchlight&idx=120&switchcmd=Off

    public String sendStop() {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(url)
                .port(port)
                .addPathSegment("json.htm")
                .addQueryParameter("type", "command")
                .addQueryParameter("param", "switchlight")
                .addQueryParameter("idx", idx)
                .addQueryParameter("switchcmd", "Off")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Authorization", Credentials.basic(username, password))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            // Get response body
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonResponse);
            return jsonResponse;
        } catch (IOException e) {
            throw new CustomHttpException("Unable to control light!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String sendNewValues(LightParams lightParams) {

        String colorParams = getColorParams(lightParams);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(url)
                .port(port)
                .addPathSegment("json.htm")
                .addQueryParameter("type", "command")
                .addQueryParameter("param", "setcolbrightnessvalue")
                .addQueryParameter("idx", idx)
                .addQueryParameter("color", colorParams)
                .addQueryParameter("brightness", String.valueOf(lightParams.brightness))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Authorization", Credentials.basic(username, password))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            // Get response body
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonResponse);
            return jsonResponse;
        } catch (IOException e) {
            throw new CustomHttpException("Unable to control light!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String getColorParams(LightParams lightParams) {

        int r = 0, g = 0, b = 0;
        int cw = 0, t = 0, ww = 0;

        if(lightParams.mode == LightMode.WHITE) {
            cw = lightParams.color1;
            t = ww = 255 - cw;
        }else {
            r = lightParams.color1;
            g = lightParams.color2;
            b = lightParams.color3;
        }

        // {"b":0,"cw":128,"g":0,"m":2,"r":0,"t":128,"ww":128}
        StringBuilder value = new StringBuilder();
        value.append("{")
                .append("\"b\":").append(b)
                .append(",")
                .append("\"cw\":").append(cw)
                .append(",")
                .append("\"g\":").append(g)
                .append(",")
                .append("\"m\":").append(lightParams.mode.getValue())
                .append(",")
                .append("\"r\":").append(r)
                .append(",")
                .append("\"t\":").append(t)
                .append(",")
                .append("\"ww\":").append(ww)
                .append("}");

        return value.toString();
    }
}