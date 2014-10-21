/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.auth;

import com.denbestegrupp.mewap.model.MWUser;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;

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
    
    public URL getLoginURL() {
        try {
            return new URL("http://raksooo.se/login");
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleAuth.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public URL getLogoutURL() {
        try {
            return new URL("http://raksooo.se/logout");
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleAuth.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean isLoggedIn() {
        return false;
    }
    
    public String getLoggedInUser() {
        return "a@a.a";
    } 
    
}
