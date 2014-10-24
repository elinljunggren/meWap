/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Oskar
 */
@ApplicationScoped
public class GoogleAuth {
    
    private static GoogleAuth instance = null;
    
    private GoogleAuth() {
        
    }
    
    public static GoogleAuth getInstance() {
        instance = new GoogleAuth();
        return instance;
    }
    
    public String getLoginURL() {
        StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
        .append("?client_id=").append(AuthConstants.CLIENT_ID)
        .append("&response_type=code")
        .append("&scope=openid%20email")
        .append("&redirect_uri=").append(AuthConstants.CALLBACK_URL)
        .append("&state=")
        .append("&access_type=offline")
        .append("&approval_prompt=force");
        
        return oauthUrl.toString();
    }
    
    public boolean isLoggedIn(HttpHeaders hh) {
        try {
            String access_token = hh.getCookies().get("access_token").getValue();
            String jsonString = "";
            try {
                jsonString = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(access_token).toString());
            } catch (IOException ex) {
                Logger.getLogger(GoogleAuth.class.getName()).log(Level.SEVERE, null, ex);
            }

            HashMap<String,String> json = new HashMap<>();
            try {
                json =
                        new ObjectMapper().readValue(jsonString, HashMap.class);
            } catch (IOException ex) {
                Logger.getLogger(GoogleAuth.class.getName()).log(Level.SEVERE, null, ex);
            }

            return json.get("email") != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getLoggedInUser(HttpHeaders hh) {
        String access_token = hh.getCookies().get("access_token").getValue();
        String jsonString = "";
        try {
            jsonString = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(access_token).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        HashMap<String,String> json = new HashMap<>();
        try {
            json =
                    new ObjectMapper().readValue(jsonString, HashMap.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return json.get("email");
    }
    
    private String get(String url) throws ClientProtocolException, IOException {
        return execute(new HttpGet(url));
    }
    
    private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
        }

        return body;
    }
    
}
