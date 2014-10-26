
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.auth.GoogleAuth;
import com.denbestegrupp.mewap.model.MWEvent.AnswerNotification;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service for the EventList, link between backend and frontend
 * Gets requests from services.js.
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 */
@Path("events")
@RequestScoped
public class EventListResource {
 
    private final static boolean sendEmail = false;
    
    private final static Logger log = Logger.getAnonymousLogger();
    private final static GoogleAuth gauth = GoogleAuth.getInstance();
    
    @Inject
    private MeWap meWap;

    @Context
    private UriInfo uriInfo;
    
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response create(JsonObject ev, @Context HttpHeaders hh) {
        log.log(Level.INFO, "{0}:create", this);
        log.log(Level.INFO, "Json{0}", ev.toString());
        
        MWUser creator = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        
        AnswerNotification answerNotification = MWEvent.AnswerNotification
                .valueOf(ev.getString("notification"));

        List<Long> dates = new ArrayList<>();
        for (JsonValue date : ev.getJsonArray("dates")) {
            dates.add(Long.parseLong(date.toString()));
        }
        
        List<MWUser> participators = new ArrayList<>();
        participators.add(creator);
        for (JsonValue p : ev.getJsonArray("participators")) {
            String email = p.toString();
            MWUser participator = meWap.getUserList().find(email);
            if(participator == null) {
                participator = new MWUser(email, email);
                meWap.getUserList().create(participator);
            }
            participators.add(participator);
        }

        long durationValue = -1;
        if(!ev.isNull("duration")) {
            durationValue = (long) ev.getInt("duration");
        }
        
        String name = ev.getString("name");
        MWEvent event = new MWEvent(name,
                creator,
                ev.getString("description"),
                dates,
                ev.getBoolean("allDayEvent"), 
                durationValue, 
                Long.parseLong(ev.getString("deadline")), 
                ev.getBoolean("deadlineReminder"), 
                answerNotification,
                participators);
        meWap.getEventList().create(event);
        
        for (MWUser participator : participators) {
            if (!participator.equals(creator)) {
                URL url = null;
                try {
                    url = new URL("http://oskarnyberg.com/annat/mewap/mail.php?to=" + participator.getEmail()
                            + "&from=" + URLEncoder.encode(creator.getName(), "UTF-8")
                            + "&event=" + URLEncoder.encode(name, "UTF-8")
                            + "&id=" + event.getId()
                            + "&type=invite");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                sendEmail(url);
                log.log(Level.INFO, "------- Email sent? -------");
                log.log(Level.INFO, url.toString());
            }
        }
        
        return Response.created(null).build();
    }

    @DELETE
    @Path(value = "{id}")
    public Response delete(@PathParam(value = "id") final Long id) {
        log.log(Level.INFO, "Delete {0}", id);

        meWap.getEventList().delete(id);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("deleteHistory")
    public Response deleteHistory(@Context HttpHeaders hh) {
        
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        List<MWEvent> es = meWap.getEventList()
                .getRelatedToUser(user, meWap.getEventList().findAll());
        es = meWap.getEventList().getHistory(es);
        
        for (MWEvent e : es) {
            if (e.getCreator().equals(user)) {
                meWap.getEventList().delete(e.getId());
            }
        }
        
        return Response.ok().build();
    }

    @PUT
    @Path(value = "{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response update(@PathParam(value = "id") final Long id, JsonObject ev) {
        log.log(Level.INFO, "{0}:update{1}", new Object[]{this, id});
        log.log(Level.INFO, "Json{0}", ev.toString());
                
        AnswerNotification answerNotification = MWEvent.AnswerNotification
                .valueOf(ev.getString("notification"));
        
        List<Long> dates = new ArrayList<>();
        for (JsonValue date : ev.getJsonArray("dates")) {
            dates.add(Long.parseLong(date.toString()));
        }
        
        List<MWUser> participators = new ArrayList<>();
        for (JsonValue p : ev.getJsonArray("participators")) {
            String email = p.toString();
            MWUser participator = meWap.getUserList().find(email);
            if(participator == null) {
                participator = new MWUser(email, email);
                meWap.getUserList().create(participator);
            }
            participators.add(participator);
        }
        
        MWEvent event = meWap.getEventList().find(id);
        event.setName(ev.getString("name"));
        event.setDescription(ev.getString("description"));
        event.setDates(dates);
        event.setAllDayEvent(ev.getBoolean("allDayEvent"));
        event.setDuration((long) ev.getInt("duration"));
        event.setDeadline(Long.parseLong(ev.getString("deadline")));
        event.setDeadlineReminder(ev.getBoolean("deadlineReminder"));
        event.setNotification(answerNotification);
        event.setParticipators(participators);
                
        meWap.getEventList().update(event);
        return Response.created(null).build();
    }
    
    @PUT
    @Path(value = "/answer/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response addAnswer(@PathParam(value = "id") final long id, JsonObject a) {
        log.log(Level.INFO, "{0}:update{1}", new Object[]{this, id});
        log.log(Level.INFO, "Json{0}", a.toString());
        
        MWEvent event = meWap.getEventList().find(id);
        MWUser user = meWap.getUserList().find(a.getString("user"));

        List<Long> dates = new ArrayList<>();
        for (JsonValue date : a.getJsonArray("dates")) {
            dates.add(Long.parseLong(date.toString()));
        }

        event.addAnswer(user, dates);
        
        meWap.getEventList().update(event);
        
        log.log(Level.INFO, event.getNotification().toString());
        
        StringBuilder result = new StringBuilder();
        for (MWUser u : event.getParticipators()) {
            result.append(", " + u.getEmail());
        }
        
        if (event.getNotification() == MWEvent.AnswerNotification.EACH_ANSWER) {
                URL url = null;
                try {
                    url = new URL("http://oskarnyberg.com/annat/mewap/mail.php?to=" + event.getCreator().getEmail()
                            + "&from=" + URLEncoder.encode(user.getName(), "UTF-8")
                            + "&event=" + URLEncoder.encode(event.getName(), "UTF-8")
                            + "&id=" + event.getId()
                            + "&type=eachanswer");
                } catch (MalformedURLException|UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                sendEmail(url);
                log.log(Level.INFO, "------- Email sent? -------");
        } else if (event.getNotification() == MWEvent.AnswerNotification.LAST_ANSWER 
                && event.getParticipators().size() - event.getAnswers().size()  <= 1) {
                URL url = null;
                try {
                    url = new URL("http://oskarnyberg.com/annat/mewap/mail.php?to=" + event.getCreator().getEmail()
                            + "&event=" + URLEncoder.encode(event.getName(), "UTF-8")
                            + "&id=" + event.getId()
                            + "&type=lastanswer");
                } catch (MalformedURLException|UnsupportedEncodingException ex) {
                    Logger.getLogger(EventListResource.class.getName()).log(Level.SEVERE, null, ex);
                }
                sendEmail(url);
                log.log(Level.INFO, "------- Email sent? -------");
        }
        
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
    public Response findAll(@Context HttpHeaders hh) {
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        Collection<MWEvent> es = meWap.getEventList()
                .getRelatedToUser(user, meWap.getEventList().findAll());
        Collection<EventWrapper> ews = wrapEvents(es);
        
        GenericEntity<Collection<EventWrapper>> ge = 
                new GenericEntity<Collection<EventWrapper>>(ews) {
        };
        
        return Response.ok(ge).build();
    }

    @GET
    @Path(value = "range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRange(@QueryParam(value = "first") int first,
            @QueryParam(value = "count") int count, @Context HttpHeaders hh) {
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        List<MWEvent> es = meWap.getEventList()
                .getRelatedToUser(user, meWap.getEventList().findAll());
        es = meWap.getEventList().getUpcomingEvents(es);
        Collection<MWEvent> es2 = getRange(es, first, count);
        Collection<EventWrapper> ews = wrapEvents(es2);

        GenericEntity<Collection<EventWrapper>> ge = 
                new GenericEntity<Collection<EventWrapper>>(ews) {
        };
        
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "history")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findHistory(@QueryParam(value = "first") int first,
            @QueryParam(value = "count") int count, @Context HttpHeaders hh) {
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        List<MWEvent> es = meWap.getEventList()
                .getRelatedToUser(user, meWap.getEventList().findAll());
        es = meWap.getEventList().getHistory(es);
        Collection<MWEvent> es2 = getRange(es, first, count);
        Collection<EventWrapper> ews = wrapEvents(es2);

        GenericEntity<Collection<EventWrapper>> ge = 
                new GenericEntity<Collection<EventWrapper>>(ews) {
        };
        
        return Response.ok(ge).build();
    }

    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count(@Context HttpHeaders hh) {
        log.log(Level.INFO, "Count");
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        int c = meWap.getEventList().getRelatedToUser(user, meWap.getEventList().getUpcomingEvents(meWap.getEventList().findAll())).size();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @GET
    @Path(value = "countHistory")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countHistory(@Context HttpHeaders hh) {
        log.log(Level.INFO, "Count");
        MWUser user = meWap.getUserList().find(gauth.getLoggedInUser(hh));
        int c = meWap.getEventList().getRelatedToUser(user, meWap.getEventList().getHistory(meWap.getEventList().findAll())).size();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    private Collection<EventWrapper> wrapEvents(Collection<MWEvent> events) {
        Collection<EventWrapper> ews = new ArrayList<>();
        
        for(MWEvent e : events) {
            EventWrapper ew = new EventWrapper(e);
            ews.add(ew);
        }
        return ews;
    }

    private Collection<MWEvent> getRange(List<MWEvent> events, int first, int count) {
        Collection<MWEvent> es = new ArrayList<>();
        int last = first+count;
        
        for(int i = first; i < last; i++ ) {
            try {
                es.add(events.get(i));
            } catch(IndexOutOfBoundsException e) {
                break;
            }
        }
        return es;
    }
    
    private void sendEmail(URL url) {
        if (sendEmail) {
            try {
                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
