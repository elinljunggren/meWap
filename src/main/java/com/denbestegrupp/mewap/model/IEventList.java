/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.IDAO;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Oskar
 */
@Local
public interface IEventList extends IDAO<MWEvent, Long> {
    
    public List<MWEvent> getByName(String name);
    
    public List<MWEvent> getRelatedToUser(MWUser user, Collection<MWEvent> es);
    
}
