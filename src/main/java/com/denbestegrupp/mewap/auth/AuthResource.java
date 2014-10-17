/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.auth;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Oskar
 */
@Path("auth")
public class AuthResource {
    
    private final static GoogleAuth gauth = GoogleAuth.getInstance();
    
    @GET
    @Path(value = "login")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response login() {
        if (!gauth.isLoggedIn()) {
            JsonObject url = Json.createObjectBuilder().add("url", gauth.getLoginURL().toString()).build();
            return Response.ok(url).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Path(value = "logout")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response logout() {
        if (gauth.isLoggedIn()) {
            JsonObject url = Json.createObjectBuilder().add("url", gauth.getLogoutURL().toString()).build();
            return Response.ok(url).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Path(value = "isLoggedIn")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response isLoggedIn() {
        JsonObject loggedIn = Json.createObjectBuilder().add("loggedIn", gauth.isLoggedIn()).build();
        return Response.ok(loggedIn).build();
    }
    
}
