/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author emma
 */

//wrapper for answer class
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="Answer", propOrder = {
    "user",
    "dates"
})
public class AnswerWrapper {
    
    private MWAnswer answer;
    
    protected AnswerWrapper(){
    }
    
    public AnswerWrapper(MWAnswer answer){
        this.answer = answer;
    }
    
    @XmlElement
    public UserWrapper getUser(){
        return new UserWrapper(answer.getUser());
    }
    @XmlElement
    public Collection<Long> getDates(){
        return answer.getDates();
    }
}
