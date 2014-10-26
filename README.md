<h1>meWap</h1>

<b>Group:</b> 1
<br>
<b>Authors: </b>Emma Gustafsson, Elin Ljunggren, Josefin Ondrus, Oskar Nyberg
<br>
<b>Course:</b> DAT076, Web applications, CTH 2014

A web application to plan time for a meeting. The creator creates an event where the dates and times are set and invites participators. An email is sent to the participator to notify them on the upcoming event. They enter the detail page of the event and fills in the dates and times they can participate, either by autogenerating from their google calendar or manually.

<ul>Required services and libraries:

 <li> Glassfish 4.0 </li>
 <li> Maven</li>
  <li>Angular.js</li>
  </ul>
<ul>Debugging and testing:

  <li>Java DB</li>
</ul>
<ul>Known bugs:
  
  <li>Edit and Delete buttons disappears when updating event detail-view as creator.</li>
  
  <li>When entering detail-view the answers made by user are dark-green in color. If page is refreshed however, they are light-green as the other participants answers.</li>
  
  <li>Edit view is a bit unreliable, mostly because the variable duration is unpredictable. </li>
  
  <li>Time zones are a bit hard to handle as well. We are not sure what the effects would be if an event suggests dates pre and post a summer/winter-time alteration.</li>
  
 <li>An event can be created with dates that have already past. Deadline can also be set after the event is supposed to take place.</li>
  
  <li>There are no error-messages when user input is wrong type or non-existing.</li>
  
  <li>Apparantly it is possible to set a date that does not exist in the date picker (e.g. 31st of November). This bug is however not in our application, but in google chrome. </li>
  </ul>
  
<ul>Features we wanted to implement but failed:

  <li>Send out dealine reminder to participants.</li>
  
 <li> Clearing history for all events, not only where you are the creator.</li>
  
  <li>Google Calendar preview when answering.</li>
  
  <li>Insertion of events in users Google Calendar.</li>
  
  <li>Setting an actual date for the event to take place, based upon which date most participators were availiable.</li>
  
  <li>GUI for creating an event in calendar view, with users own Google Calendar events represented in background of the calendar and the meWap-event in the foreground.</li>
</ul>
