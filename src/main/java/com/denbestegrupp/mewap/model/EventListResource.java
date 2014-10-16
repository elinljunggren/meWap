/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.model.MWEvent.AnswerNotification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author elin
 */
@Path("events")
public class EventListResource {
 
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
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	String dateInString = ev.getString("deadline");
 
        Date deadline = null;
	try {
            deadline = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
	}

        AnswerNotification answerNotification = MWEvent.AnswerNotification.valueOf(ev.getString("notification"));

        List<Date> dates = new ArrayList<Date>();
        for (JsonValue date : ev.getJsonArray("dates")) {
            try {
                dates.add(formatter.parse(date.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        MWEvent event = new MWEvent(ev.getString("name"), 
                dates, 
                (long) ev.getInt("duration"), 
                deadline, ev.getBoolean("deadlineReminder"), 
                answerNotification);
        meWap.getEventList().create(event);   
        
        return Response.created(null).build();

    }

    @DELETE
    @Path(value = "{id}")
    public Response delete(@PathParam(value = "id") final Long id) {
        log.log(Level.INFO, "Delete {0}", id);

        meWap.getEventList().delete(id);
        return Response.ok().build();
    }

    /**
     * Updates event after getting an answer with dates.
     * @param id
     * @param ev
     * @return 
     */
    @PUT
    @Path(value = "{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response update(@PathParam(value = "id") final Long id, JsonObject ev) {
        log.log(Level.INFO, "{0}:update{1}", new Object[]{this, id});
        log.log(Level.INFO, "Json{0}", ev.toString());

        MWEvent event = meWap.getEventList().find(id);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        List<Date> dates = new ArrayList<Date>();
        for (JsonValue date : ev.getJsonArray("dates")) {
            try {
                dates.add(formatter.parse(date.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        event.setDates(dates);
        meWap.getEventList().update(event);
        return Response.created(null).build();
    }
    
    @GET
    @Path(value = "{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response find(@PathParam(value = "id") Long id) {

        MWEvent event = meWap.getEventList().find(id);
        if (event != null) {
            EventWrapper ew = new EventWrapper(event);
            return Response.ok(ew).build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {

        Collection<MWEvent> es = meWap.getEventList().findAll();
        Collection<EventWrapper> ews = new ArrayList<>();
        
        for(MWEvent e: es){
            EventWrapper ew = new EventWrapper(e);
            ews.add(ew);
        }
        
        GenericEntity<Collection<EventWrapper>> ge = new GenericEntity<Collection<EventWrapper>>(ews) {
        };
        
        return Response.ok(ge).build();
    }

    @GET
    @Path(value = "range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRange(@QueryParam(value = "first") int first, @QueryParam(value = "count") int count) {

        Collection<MWEvent> es = meWap.getEventList().findRange(first, count);
        Collection<EventWrapper> ews = new ArrayList<>();
        
        for(MWEvent e: es){
            EventWrapper ew = new EventWrapper(e);
            ews.add(ew);
        }
        
        GenericEntity<Collection<EventWrapper>> ge = new GenericEntity<Collection<EventWrapper>>(ews) {
        };
        
        return Response.ok(ge).build();
    }

    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count() {

        log.log(Level.INFO, "Count");
        int c = meWap.getEventList().count();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
}
