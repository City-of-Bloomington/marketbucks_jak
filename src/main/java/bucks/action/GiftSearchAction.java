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
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class GiftSearchAction extends TopAction{

    static final long serialVersionUID = 247L;	
    static Logger logger = LogManager.getLogger(GiftSearchAction.class);
    //
    List<Gift> gifts = null;
    GiftList giftList = null;
    String bucksTitle = "Bucks";
    String giftsTitle = "Issued GC's";
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
	if(action.equals("Search")){
	    ret = SUCCESS;
	    giftList.setNoLimit();
	    back = giftList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		gifts = giftList.getGifts();
		if(gifts == null || gifts.size() == 0){
		    giftsTitle = "No match found ";
		}
		else{
		    giftsTitle = "Found "+gifts.size()+" records";
		}
	    }
	}		
	return ret;
    }
    @StrutsParameter
    public GiftList getGiftList(){ 
	if(giftList == null){
	    giftList = new GiftList(debug);
	}		
	return giftList;
    }
    @StrutsParameter
    public List<Gift> getGifts(){
	return gifts;
    }
    @StrutsParameter
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter
    public String getGiftsTitle(){
	return giftsTitle;
    }
    @StrutsParameter
    // we need this for auto_complete
    public void setVendorName(String val){
	// just ignore 
    }	
    public String populate(){
	String ret = SUCCESS;
	giftList = new GiftList(debug);
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





































