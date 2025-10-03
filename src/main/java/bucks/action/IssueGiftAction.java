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


public class IssueGiftAction extends TopAction{

    static final long serialVersionUID = 27L;	
    static Logger logger = LogManager.getLogger(IssueGiftAction.class);
    //
    // we need the latest buckConf to get the
    // buck face value and expire date for the season
    //
    Gift gift = null;
    String bucksTitle = "Gift Certificates issued";
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
	if(action.equals("Next")){ //  Save carried over from giftAction
	    gift.doSelect();
	    ret = SUCCESS;
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
    public Gift getGift(){ // starting a new gift
	if(gift == null){
	    gift = new Gift(debug);
	}		
	return gift;
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





































