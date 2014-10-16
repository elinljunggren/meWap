/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.net.URL;

/**
 *
 * @author Oskar
 */
public class GoogleAuth {
    
    private static GoogleAuth instance = null;
    
    private GoogleAuth() {
        
    }
    
    public static GoogleAuth getInstance() {
        instance = new GoogleAuth();
        return instance;
    }
    
    public URL getLoginURL() {
        return null;
    }
    
    public URL getLogoutURL() {
        return null;
    }
    
    public boolean isLoggedIn() {
        return false;
    }
    
}
