/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.IDAO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Oskar
 */
@Local
public interface IUserList extends IDAO<MWUser, Long> {

    public List<MWUser> getByName(String name);
    
}
