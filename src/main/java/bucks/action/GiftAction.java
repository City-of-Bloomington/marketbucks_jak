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
	    populate();
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
	    back = populate();
	    if(!back.equals("")){
		addActionError(back);
	    }	    
	    ret ="issue"; 	
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public Gift getGift(){ // starting a new ebt
	if(gift == null){
	    gift = new Gift(debug);
	}		
	return gift;
    }
    @StrutsParameter(depth=2)
    public List<Gift> getGifts(){
	GiftList bl = new GiftList(debug);
	String back = bl.find();
	if(back.equals("") && bl.getGifts() != null){
	    gifts = bl.getGifts();
	}
	return gifts;
    }
    @StrutsParameter(depth=1)
    public boolean hasGifts(){
	getGifts();
	return gifts != null && gifts.size() > 0;
    }
    @StrutsParameter(depth=2)
    public void setGift(Gift val){
	if(val != null)
	    gift = val;
    }
    @StrutsParameter(depth=1)
    @Override
    public String getId(){
	if(id.equals("") && gift != null){
	    id = gift.getId();
	}
	return id;
    }
    @StrutsParameter(depth=1)
    @Override
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }    
    @StrutsParameter(depth=2)
    public List<Buck> getBucks(){
	return gift.getBucks();
    }
    public boolean hasBucks(){
	List<Buck> bucks = gift.getBucks();
	return bucks != null && bucks.size() > 0;
    }
    
    public String getAmount(){
	return gift.getAmount();
    }
    public String getPay_type(){
	return gift.getPay_type();
    }
    public String getCheck_no(){
		
	return gift.getCheck_no();
    }
    public boolean hasCheck_no(){
	return gift.hasCheck_no();
    }
    public String getDate_time(){
		
	return gift.getDate_time();
    }

    public boolean isCancelled(){
	return gift.isCancelled();
    }
    public boolean isDispute_resolution(){
	return gift.isDispute_resolution();
    }
    public User getGift_user(){
	return gift.getUser();
    }
    public Type getBuck_type(){
	return gift.getBuck_type();
    }
    public String getTotal(){
	return gift.getTotal();
    }
    public String getBucksTotal(){
	return gift.getBucksTotal();
    }
    public String getBalance(){
	return gift.getBalance();
    }
    public boolean hasBalance(){
	return gift.hasBalance();
    }
    //	
    public boolean needMoreIssue(){
	return gift.needMoreIssue();
    }
    
    public String getBucksTitle(){
	return bucksTitle;
    }
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





































