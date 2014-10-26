package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.IDAO;
import java.util.List;
import javax.ejb.Local;

/**
* Interface for an UserList. An UserList makes users accessible from the database.
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Local
public interface IUserList extends IDAO<MWUser, String> {

    public List<MWUser> getByName(String name);
    
}
