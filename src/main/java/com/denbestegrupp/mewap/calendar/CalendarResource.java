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
* RESTful resource which makes calendar data accessible by the frontend
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Path("calendar")
@RequestScoped
public class CalendarResource {
    
    private GoogleCalendar calendar = new GoogleCalendar();
    
    @GET
    @Path("eventsForDate")
    public Response eventsForDate(@Context HttpHeaders hh, 
            @QueryParam(value = "startDate") long start, @QueryParam(value = "endDate") long end) {
        List<String> events = calendar.getEvents(start, end, hh);
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (String event : events) {
            InputStream stream = new ByteArrayInputStream(event.getBytes(StandardCharsets.UTF_8));
            JsonObject array = Json.createReader(stream).readObject();
            json.add(array);
        }
        return Response.accepted(json.build()).build();
    }
    
}
