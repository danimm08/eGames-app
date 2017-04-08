package es.egames.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;

import es.egames.R;

/**
 * Created by daniel on 6/04/17.
 */

public class RestTemplateManager {

    public static RestTemplate create() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                boolean res = false;
                try {
                    res = !response.getStatusCode().equals(HttpStatus.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
        return restTemplate;
    }

    public static HttpEntity authenticateRequest(Activity instance) {
        HttpEntity httpEntity;
        SharedPreferences sharedPref = instance.getSharedPreferences(
                instance.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("access_token", null);

        if (access_token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + access_token);
            httpEntity = new HttpEntity(headers);
        } else {
            httpEntity = new HttpEntity(null);
        }
        return httpEntity;
    }

    public static String getUrl(Activity instance, String partialUrl) {
        return instance.getString(R.string.ip_server) + partialUrl;
    }
}
