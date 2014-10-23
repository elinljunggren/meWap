/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.auth;

import com.denbestegrupp.mewap.model.MWUser;
import com.denbestegrupp.mewap.model.MeWap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Oskar
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/authcallback"})
public class AuthCallback extends HttpServlet {

    @Inject
    private MeWap mewap;
    
    private Logger log = Logger.getAnonymousLogger();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.log(Level.INFO, "---------- Callback requested ----------");
        if (req.getParameter("error") != null) {
            resp.getWriter().println(req.getParameter("error"));
            return;
        }

        String code = req.getParameter("code");

        // get the access token by post to Google
        String body = post("https://accounts.google.com/o/oauth2/token", ImmutableMap.<String, String>builder()
                .put("code", code)
                .put("client_id", AuthConstants.CLIENT_ID)
                .put("client_secret", AuthConstants.CLIENT_SECRET)
                .put("redirect_uri", AuthConstants.CALLBACK_URL)
                .put("grant_type", "authorization_code").build());

        JSONObject jsonObject = null;

        // get the access token from json and request info from Google
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse json " + body);
        }

        // google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
        String accessToken = (String) jsonObject.get("access_token");

        Cookie cookie = new Cookie("access_token", accessToken);
        resp.addCookie(cookie);

        // get some info about the user with the access token
        String jsonString = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(accessToken).toString());
        
        HashMap<String,String> json = new HashMap<>();
        try {
            json = new ObjectMapper().readValue(jsonString, HashMap.class);
        } catch (IOException ex) {
            Logger.getLogger(GoogleAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MWUser user = mewap.getUserList().find(json.get("email"));
        if (user == null) {
            user = new MWUser(json.get("email"), json.get("name"));
            mewap.getUserList().create(user);
        }
        
        log.log(Level.INFO, "---------- Token retreived ----------");
        log.log(Level.INFO, jsonString);
        
        resp.sendRedirect(AuthConstants.FINAL_URL);
    }

    // makes a GET request to url and returns body as a string
    private String get(String url) throws ClientProtocolException, IOException {
        return execute(new HttpGet(url));
    }

    // makes a POST request to url with form parameters and returns body as a string
    private String post(String url, Map<String, String> formParameters) throws ClientProtocolException, IOException {
        HttpPost request = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<>();

        for (String key : formParameters.keySet()) {
            nvps.add(new BasicNameValuePair(key, formParameters.get(key)));
        }

        request.setEntity(new UrlEncodedFormEntity(nvps));

        return execute(request);
    }

    // makes request and checks response code for 200
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
