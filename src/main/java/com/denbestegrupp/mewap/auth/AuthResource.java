/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.auth;

import com.denbestegrupp.mewap.model.MWUser;
import com.denbestegrupp.mewap.model.MeWap;
import com.denbestegrupp.mewap.model.UserWrapper;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Oskar
 */
@Path("auth")
@RequestScoped
public class AuthResource {
    
    private final static GoogleAuth gauth = GoogleAuth.getInstance();
    
    @Inject
    private MeWap meWap;
    
    @GET
    @Path(value = "login")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response login() {
        JsonObject url = Json.createObjectBuilder().add("url", gauth.getLoginURL()).build();
        return Response.ok(url).build();
    }
    
    @GET
    @Path(value = "isLoggedIn")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response isLoggedIn(@Context HttpHeaders hh) {
        JsonObject loggedIn = Json.createObjectBuilder().add("loggedIn", gauth.isLoggedIn(hh)).build();
        return Response.ok(loggedIn).build();
    }
    
    @GET
    @Path(value = "getLoggedInUser")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLoggedInUser(@Context HttpHeaders hh) {
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        UserWrapper uw = new UserWrapper(user);
        return Response.ok(uw).build();
    }
    
}
