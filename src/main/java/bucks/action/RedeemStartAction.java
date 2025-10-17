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


public class RedeemStartAction extends TopAction{

    static final long serialVersionUID = 27L;	
    static Logger logger = LogManager.getLogger(RedeemStartAction.class);
    //
    // Note: This flag is intended for other organization that need to
    // use this app but do not have similar New World database
    // to check for Vendor list, in this case the user must update
    // the vendor list manually or provide another mechanism to do that
    // for those organizations they can turn this flag off by setting
    // it to false
    //
    //
    Redeem redeem = null;
    List<Redeem> redeems = null;
    List<Vendor> vendors = null;
    List<Buck> bucks = null;
    List<Dispute> disputes = null;
    String bucksTitle = "Redeemed MB & GC";
    String redeemsTitle = "Most Recent Redemptions";	
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
	getRedeem();
	if(action.equals("Next")){
	    ret = SUCCESS;
	    redeem.setUser_id(user.getId());
	    if(redeem.isVendorInactive()){
		back = "This vendor is not active and can not redeem bucks";
		addActionError(back);
		return ret;
	    }
	    //
	    // we do this as needed espacially if the vendor is not
	    // in the list, but once a day
	    //
	    /**
	       //ignore this now
	    if(enableVendorsAutoUpdate){						
		if(!redeem.isVendorAvailable()){
		    RefreshVendors rv = new RefreshVendors(debug,
							   vendorsCheckUrl,
							   vendorsDatabase,
							   vendorsUser,
							   vendorsPassword
							   );
		    //
		    // if we have not checked this today
		    //
		    if(!rv.isCurrent()){ 
			back = rv.consolidate();
			if(!back.equals("")){
			    addActionError(back);					
			}
			else{
			    back = rv.addUpdate();
			}
		    }
		}
	    }
	    */
	    if(redeem.isVendorAvailable()){
		System.err.println(" saving redeem");
		back = redeem.doSave();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    action="";
		    ret ="redeem"; // add bucks
		    addActionMessage("Saved Successfully");
		}
	    }
	    else{
		back = "This vendor is not available or not active and can not redeem bucks";
		addActionError(back);
	    }
	}
	else if(!id.equals("")){
	    ret = populate();
	}
	return ret;
    }
    @StrutsParameter(depth=1)
    public Redeem getRedeem(){ // starting a new redeem
	if(redeem == null){
	    redeem = new Redeem(debug);
	    if(!id.isEmpty())
		redeem.setId(id);
	}		
	return redeem;
    }
    @StrutsParameter(depth=1)
    public List<Redeem> getRedeems(){
	RedeemList bl = new RedeemList(debug);
	bl.setLimit("50");
	String back = bl.find();
	if(back.equals("") && bl.getRedeems() != null){
	    redeems = bl.getRedeems();
	}
	return redeems;
    }
    @StrutsParameter(depth=1)
    public List<Vendor> getVendors(){
	if(vendors == null){
	    VendorList vl = new VendorList(debug);
	    vl.setActiveOnly();
	    String back = vl.find();
	    if(back.equals("")){
		vendors = vl.getVendors();
	    }
	}
	return vendors;
    }
    public boolean hasRedeems(){
	getRedeems();
	return redeems != null && redeems.size() > 0;
    }
    @StrutsParameter(depth=1)
    public User getRedeemUser(){
	return redeem.getUser();
    }
	
    @StrutsParameter(depth=1)
    public boolean hasVendors(){
	getVendors();
	return vendors != null && vendors.size() > 0;
    }
    @StrutsParameter(depth=1)
    public void setRedeem(Redeem val){
	if(val != null)
	    redeem = val;
    }
    @StrutsParameter(depth=1)
    @Override
    public String getId(){
	if(id.equals("") && redeem != null){
	    id = redeem.getId();
	}
	return id;
    }
    @StrutsParameter(depth=1)
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter(depth=1)
    public String getRedeemsTitle(){
	return redeemsTitle;
    }	
    // we need this for auto_complete
    @StrutsParameter(depth=1)
    public void setVendorName(String val){
	// just ignore 
    }
    @StrutsParameter(depth=1)
    public String getCount(){
	return redeem.getCount();
    }
    @StrutsParameter(depth=1)
    public String getTotal(){
	return redeem.getTotal();
    }
    @StrutsParameter(depth=1)
    public String getNotes(){
	return redeem.getNotes();
    }
    @StrutsParameter(depth=1)    
    public boolean hasBucks(){
	if(redeem == null)
	    getRedeem();
	return redeem.hasBucks();
    }
    @StrutsParameter(depth=1)
    public List<Buck> getBucks(){
	if(hasBucks()){
	    if(bucks == null)
		bucks = redeem.getBucks();
	}
	return bucks;
    }
    @StrutsParameter(depth=1)    
    public boolean hasDisputes(){
	if(redeem == null)
	    getRedeem();
	return redeem.hasDisputes();
    }
    @StrutsParameter(depth=1)
    public List<Dispute> getDisputes(){
	if(hasDisputes()){
	    if(disputes == null)
		disputes = redeem.getDisputes();
	}
	return disputes;
    }        
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    redeem = new Redeem(debug, id);
	    String back = redeem.doSelect();
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





































