/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.calendar;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Oskar
 */
@Path("cal")
@RequestScoped
public class CalendarResource {
    
    private GoogleCalendar calendar = new GoogleCalendar();
    
    @GET
    @Path("eventsForDate")
    public Response eventsForDate(@Context HttpHeaders hh, @QueryParam(value = "date") long date) {
        List<String> events = calendar.getEvents(date, hh);
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String event : events) {
            InputStream stream = new ByteArrayInputStream(event.getBytes(StandardCharsets.UTF_8));
            JsonObject array = Json.createReader(stream).readObject();
            json.add(array);
        }
        return Response.accepted(json.build()).build();
    }
    
}
