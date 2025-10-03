/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.action;

import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;  
import jakarta.servlet.http.HttpServletResponse;  
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class GiftAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(GiftAction.class);
    //
    // we need the latest buckConf to get the
    // buck face value and expire date for the season
    //
    Gift gift = null;
    List<Gift> gifts = null;
    String bucksTitle = "GC in this transaction";
    String giftsTitle = "Most recent gift certificates";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.equals("")){
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
	    }	
	}		
	else if(action.equals("Next")){
	    ret = SUCCESS;
	    gift.setUser_id(user.getId());
	    back = gift.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		ret ="issue"; // add bucks
		addActionMessage("Saved Successfully");
	    }
	}
	else if(action.equals("Update")){
	    ret = SUCCESS;
	    gift.setUser_id(user.getId());
	    back = gift.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		ret ="issue"; 				
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.equals("Cancel")){
	    ret = SUCCESS;
	    back = gift.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Buck> bucks = gift.getBucks();
		if(bucks != null && bucks.size() > 0){
		    for(Buck one:bucks){
			if(one.isVoided()){
			    CancelledBuck two = new CancelledBuck(debug, one.getId(),user.getId());
			    back += two.doSave();
			}
		    }
		}
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Cancelled Successfully");
		    gift =new Gift();
		    id="";
		}
	    }
	}
	else if(action.startsWith("Void")){ // void selected GC
	    ret = SUCCESS;
	    back = gift.doCancelSelected();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Buck> bucks = gift.getBucks();
		if(bucks != null && bucks.size() > 0){
		    for(Buck one:bucks){
			if(one.isVoided()){
			    CancelledBuck two = new CancelledBuck(debug, one.getId(),user.getId());
			    back += two.doSave();
			}
		    }
		}
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Voided Successfully");
		}
	    }
	}		
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = gift.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = gift.handleAddingBuck();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Added Successfully");
		}
	    }
	}		
	else if(!id.equals("")){
	    ret = populate();
	}
	return ret;
    }
    @StrutsParameter
    public Gift getGift(){ // starting a new ebt
	if(gift == null){
	    gift = new Gift(debug);
	}		
	return gift;
    }
    @StrutsParameter
    public List<Gift> getGifts(){
	GiftList bl = new GiftList(debug);
	String back = bl.find();
	if(back.equals("") && bl.getGifts() != null){
	    gifts = bl.getGifts();
	}
	return gifts;
    }
    @StrutsParameter
    public boolean hasGifts(){
	getGifts();
	return gifts != null && gifts.size() > 0;
    }
    @StrutsParameter
    public void setGift(Gift val){
	if(val != null)
	    gift = val;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && gift != null){
	    id = gift.getId();
	}
	return id;
    }
    @StrutsParameter
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter
    public String getGiftsTitle(){
	return giftsTitle;
    }	

    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    gift = new Gift(debug, id);
	    String back = gift.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	return ret;
    }
    @Override
    public void withServletContext(ServletContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public void withParameters(HttpParameters pars) {
        paramMap = pars;
    }

}





































