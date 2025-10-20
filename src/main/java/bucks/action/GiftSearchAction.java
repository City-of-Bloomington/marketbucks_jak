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
	if(!action.isEmpty()){
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
    @Override
    public String getId(){
	return giftList.getId();
    }
    public String getBuck_id(){
	return giftList.getBuck_id();
    }
    public String getAmount(){
	return giftList.getAmount();
    }
    public String getCheck_no(){
	return giftList.getCheck_no();
    }	
    public String getWhich_date(){
	return giftList.getWhich_date();
    }
    public String getDate_from(){
	return giftList.getDate_from() ;
    }
    public String getDate_to(){
	return giftList.getDate_to() ;
    }
    public String getPay_type(){
	return giftList.getPay_type() ;
    }	
    public String getSortBy(){
	return giftList.getSortBy() ;
    }
    public String getCancelled(){
	return giftList.getCancelled() ;
    }
    public String getDispute_resolution(){
	return giftList.getDispute_resolution() ;
    }		    
    @StrutsParameter(depth=2)
    public GiftList getGiftList(){ 
	if(giftList == null){
	    giftList = new GiftList(debug);
	}		
	return giftList;
    }
    @StrutsParameter(depth=2)
    public List<Gift> getGifts(){
	return gifts;
    }
    public String getBucksTitle(){
	return bucksTitle;
    }
    public String getGiftsTitle(){
	return giftsTitle;
    }
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





































