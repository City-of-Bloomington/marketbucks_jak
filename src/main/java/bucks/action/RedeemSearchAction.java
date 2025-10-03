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
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.action.ParametersAware;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class RedeemSearchAction extends TopAction {

    static final long serialVersionUID = 226L;	
    static Logger logger = LogManager.getLogger(RedeemSearchAction.class);
    //
    List<Redeem> redeems = null;
    RedeemList redeemList = null;
    List<Vendor> vendors = null;
    String bucksTitle = "Redeemed Bucks";
    String redeemsTitle = "Redemptions";
    String disputesTitle = "Dispute Cases in This Redemption";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.equals("")){
	    try{
		jakarta.servlet.http.HttpServletResponse res = ServletActionContext.getResponse();
		
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
	    }	
	}		
	if(action.equals("Search")){
	    ret = SUCCESS;			
	    back = redeemList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		redeems = redeemList.getRedeems();
		if(redeems == null || redeems.size() == 0){
		    redeemsTitle = "No match found ";
		}
		else{
		    redeemsTitle = "Found "+redeems.size()+" records";
		}
	    }
	}		
	return ret;
    }
    @StrutsParameter(depth=1)
    public RedeemList getRedeemList(){ // starting a new redeem
	if(redeemList == null){
	    redeemList = new RedeemList(debug);
	}		
	return redeemList;
    }
    @StrutsParameter(depth=1)
    public List<Redeem> getRedeems(){
	return redeems;
    }
    @StrutsParameter(depth=1)
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter(depth=1)
    public String getRedeemsTitle(){
	return redeemsTitle;
    }
    @Override
    public void withServletContext(ServletContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public void withParameters(HttpParameters pars) {
        paramMap = pars;
    }
    // we need this for auto_complete
    @StrutsParameter(depth=1)
    public void setVendorName(String val){
	// just ignore 
    }
    @StrutsParameter(depth=1)
    public List<Vendor> getVendors(){
	if(vendors == null){
	    VendorList vl = new VendorList(debug);
	    String back = vl.find();
	    if(back.equals("")){
		vendors = vl.getVendors();
	    }
	}
	return vendors;
    }    
    public String populate(){
	String ret = SUCCESS;
	redeemList = new RedeemList(debug);
	return ret;
    }

}





































