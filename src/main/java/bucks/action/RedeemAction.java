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

public class RedeemAction extends TopAction{

    static final long serialVersionUID = 26L;	
    static Logger logger = LogManager.getLogger(RedeemAction.class);
    //
    Redeem redeem = null;
    List<Redeem> redeems = null;
    List<Vendor> vendors = null;
    List<Buck> bucks = null;
    List<Dispute> disputes = null;
    String bucksTitle = "Redeemed MB & GC";
    String redeemsTitle = "Most Recent Redemptions";
    String disputesTitle = "Disputes in This Redemption";

    public String execute(){
	String ret = SUCCESS;
	getRedeem();    
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
	if(action.equals("Next")){
	    // do nothing here
	    ret = SUCCESS;
	}
	else if(action.equals("Finalize")){ 
	    ret = SUCCESS;			
	    back = redeem.doFinalize();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = redeem.doSelect();				
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Finalized Successfully");
		}
	    }
	}		
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = redeem.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = redeem.redeemBuck();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Added Successfully");
		}
	    }
	}
	else if(action.equals("Cancel")){ // adding a buck
	    ret = SUCCESS;			
	    back = redeem.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		ret = "cancel";
		addActionMessage("Transaction Cancelled Successfully");
	    }
	}		
	else if(action.startsWith("Scan")){ // go to adding more bucks
	    populate();
	    ret = "redeem";
	}		
	else if(action.equals("Update")){ // adding a buck
	    ret = SUCCESS;			
	    back = redeem.updateNotes();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	    redeem.doSelect();
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
	}
	if(!id.isEmpty()){
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
	VendorList vl = new VendorList(debug);
	String back = vl.find();
	if(back.equals("")){
	    vendors = vl.getVendors();
	}
	return vendors;
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
    @StrutsParameter(depth=1)
    public String getDisputesTitle(){
	return disputesTitle;
    }
    @StrutsParameter(depth=1)
    public String getStatus(){
	getRedeem();
	return  redeem.getStatus();
    }    
    @Override
    @StrutsParameter(depth=1)
    public void setId(String val) {
        if (val != null){
	    id = val;
	    getRedeem();
	    redeem.setId(id);
	}
    }    
    // we need this for auto_complete
    @StrutsParameter(depth=1)
    public void setVendorName(String val){
	// just ignore 
    }
    @StrutsParameter(depth=1)    
    public Vendor getVendor(){
	return redeem.getVendor();
    }
    @StrutsParameter(depth=1)    
    public User getRedeemUser(){
	return redeem.getUser();
    }    
    @StrutsParameter(depth=1)
    public String getDate_time(){
	return redeem.getDate_time();
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
    public void setNotes(String val){
	if(val != null){
	    if(redeem == null)
		getRedeem();
	    redeem.setNotes(val);
	}
    }
    public boolean hasBucks(){
	if(redeem == null){
	    populate();
	}
	return redeem.hasBucks();
    }
    @StrutsParameter(depth=2)
    public List<Buck> getBucks(){
	if(hasBucks()){
	    if(bucks == null)
		bucks = redeem.getBucks();
	}
	return bucks;
    }
    public boolean hasDisputes(){
	if(redeem == null)
	    populate();
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
    public boolean canFinalize(){
	return redeem.canFinalize();
    }
    public boolean canCancel(){
	return redeem.canCancel();
    }    
    public String populate(){
	String ret = SUCCESS;
	getRedeem();	
	if(!id.equals("")){
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





































