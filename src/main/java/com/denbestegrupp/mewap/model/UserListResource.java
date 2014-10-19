/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author josefinondrus
 */
@Path("users")
@RequestScoped
public class UserListResource {

    private final static Logger log = Logger.getAnonymousLogger();

    @Inject
    private IMeWap meWap;

    @Context
    private UriInfo uriInfo;
    
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response create(JsonObject ev) {
        log.log(Level.INFO, "{0}:create", this);
        log.log(Level.INFO, "Json{0}", ev.toString());
        
        MWUser user = new MWUser(ev.getString("name"), ev.getString("email"));
        meWap.getUserList().create(user);   
        
        return Response.created(null).build();

    }
 @DELETE
    @Path(value = "{email}")
    public Response delete(@PathParam(value = "email") final String email) {
        log.log(Level.INFO, "Delete {0}", email);

        meWap.getUserList().delete(email);
        return Response.ok().build();
    }
    
    @GET
    @Path(value = "{email}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response find(@PathParam(value = "email") String email) {

        MWUser user = meWap.getUserList().find(email);
        if (user != null) {
            //EventWrapper ew = new EventWrapper(event);
            return Response.ok(user).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {

        Collection<MWUser> us = meWap.getUserList().findAll();
        GenericEntity<Collection<MWUser>> gu = new GenericEntity<Collection<MWUser>>(us) {
        };
        
        return Response.ok(gu).build();
    }
    
    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count() {

        log.log(Level.INFO, "Count");
        int c = meWap.getUserList().count();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
}
