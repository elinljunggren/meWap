/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;

/**
 *
 * @author josefinondrus
 */
@Path("users")
public class UserListResource {

    private final static Logger log = Logger.getAnonymousLogger();

    @Inject
    private IMeWap meWap;

    @Context
    private UriInfo uriInfo;

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

        MWUser email = meWap.getUserList().find(email);
        if (email != null) {
            //EventWrapper ew = new EventWrapper(event);
            return Response.ok(email).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {

        Collection<MWUser> us = meWap.getUserList().findAll();
        //Collection<EventWrapper> ews = new ArrayList<>();
        
       // for(MWUser u: us){
         //   EventWrapper ew = new EventWrapper(e);
          //  ews.add(ew);
        //}
        
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
