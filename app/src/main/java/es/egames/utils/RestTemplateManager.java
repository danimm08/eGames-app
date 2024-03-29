package es.egames.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

    public static HttpEntity authenticateRequest(Object activityOrContext) {
        HttpEntity httpEntity;
        SharedPreferences sharedPref = getSharedPreferences(activityOrContext);
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

    public static HttpEntity authenticateRequestWithObject(Object activityOrContext, Object object) {
        HttpEntity httpEntity;
        SharedPreferences sharedPref = getSharedPreferences(activityOrContext);
        String access_token = sharedPref.getString("access_token", null);

        if (access_token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + access_token);
            httpEntity = new HttpEntity(object, headers);
        } else {
            httpEntity = new HttpEntity(null);
        }
        return httpEntity;
    }

    public static URLConnection getConnection(Object activityOrContext, String auxUrl) {
        SharedPreferences sharedPref = getSharedPreferences(activityOrContext);
        String access_token = sharedPref.getString("access_token", null);

        URL url = null;
        URLConnection connection = null;
        try {
            url = new URL(auxUrl);
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (access_token != null) {
            HttpHeaders headers = new HttpHeaders();
            connection.setRequestProperty("Authorization", "Bearer " + access_token);
        }
        return connection;
    }

    public static String getUrl(Object activityOrContext, String partialUrl) {
        String url;
        if (activityOrContext instanceof Activity) {
            Activity activity = (Activity) activityOrContext;
            url = activity.getString(R.string.ip_server) + partialUrl;
        } else {
            Context context = (Context) activityOrContext;
            url = context.getString(R.string.ip_server) + partialUrl;
        }
        return url;
    }

    private static SharedPreferences getSharedPreferences(Object activityOrContext) {
        SharedPreferences sharedPref = null;
        if (activityOrContext instanceof Activity) {
            Activity instance;
            instance = (Activity) activityOrContext;
            sharedPref = instance.getSharedPreferences(
                    instance.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        } else if (activityOrContext instanceof Context) {
            Context instance;
            instance = (Context) activityOrContext;
            sharedPref = instance.getSharedPreferences(
                    instance.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        }
        return sharedPref;
    }

    public static String getToken(Object activityOrContext) {
        String token;
        SharedPreferences sharedPref = getSharedPreferences(activityOrContext);
        String access_token = sharedPref.getString("access_token", null);

        if (access_token != null) {
            token = access_token;
        } else {
            token = null;
        }
        return token;
    }

    public static void logout(Object activityOrContext) {
        SharedPreferences sharedPref = getSharedPreferences(activityOrContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("access_token");
        editor.commit();
    }
}
