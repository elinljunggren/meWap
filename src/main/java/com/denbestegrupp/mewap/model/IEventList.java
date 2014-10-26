package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.IDAO;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
* Interface for an EventList. An EveltList makes events accessible from the database.
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Local
public interface IEventList extends IDAO<MWEvent, Long> {
    
    public List<MWEvent> getByName(String name);
    
    public List<MWEvent> getRelatedToUser(MWUser user, Collection<MWEvent> es);
    
    public List<MWEvent> getHistory(Collection<MWEvent> es);
    
    public List<MWEvent> getUpcomingEvents(Collection<MWEvent> es);
    
}
