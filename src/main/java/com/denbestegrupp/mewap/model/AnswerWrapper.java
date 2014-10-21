/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author emma
 */
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
    public MWUser getUser(){
        return answer.getUser();
    }
    @XmlElement
    public List <Date> getDates(){
        return answer.getDates();
    }
}
