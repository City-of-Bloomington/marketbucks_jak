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


public class IssueAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(IssueAction.class);
    //
    Ebt ebt = null;
    String bucksTitle = "Market Bucks issued to this custmer";
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
	getEbt();
	if(action.equals("Next")){ // carried over from ebtAction
	    ebt.doSelect();
	    ret = SUCCESS;
	}
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = ebt.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = ebt.handleAddingBuck();
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
    @StrutsParameter(depth = 1)
    public boolean hasId(){
	if(id.isEmpty()){
	    getEbt();
	    id = ebt.getId();
	}
	return !id.isEmpty();
    }
    @StrutsParameter(depth = 1)
    public boolean hasBucks(){
	if(ebt == null)
	    getEbt();
	return ebt.hasBucks();
    }    
    @StrutsParameter(depth=1)    
    public Ebt getEbt(){ // starting a new ebt
	if(ebt == null){
	    ebt = new Ebt(debug);
	}		
	return ebt;
    }
    @StrutsParameter(depth=1)    
    public void setEbt(Ebt val){
	if(val != null)
	    ebt = val;
    }
    @StrutsParameter(depth=1)    
    @Override
    public String getId(){
	if(id.equals("") && ebt != null){
	    id = ebt.getId();
	}
	return id;
    }
    @StrutsParameter(depth=1)    
    public String getBucksTitle(){
	return bucksTitle;
    }	
    @StrutsParameter(depth = 1)    
    public String getAmount(){
	return ebt.getAmount();
    }
    @StrutsParameter(depth = 1)
    public String getDmb_amount(){
	return ebt.getDmb_amount();
    }
    @StrutsParameter(depth = 1)    
    public String getApprove(){

	return ebt.getApprove();
    }
    @StrutsParameter(depth = 1)
    public String getCard_last_4(){
	return ebt.getCard_last_4();
    }
    public String getPaid_amount(){
	return ebt.getPaid_amount();
    }
    public String getDonated_amount(){
	return ebt.getDonated_amount();
    }    
    public User getEbt_user(){
	return ebt.getUser();
    }
    public String getDate_time(){
	return ebt.getDate_time();
    }
    
    public String getIncludeDouble(){
	return ebt.getIncludeDouble();
    }
    public boolean isCancelled(){
	return ebt.isCancelled();
    }
    public boolean isDispute_resolution(){
	return ebt.isDispute_resolution();
    }
    public String getNotes(){
	return ebt.getNotes();
    }
    public Integer getEbt_donor_max(){
	return ebt_donor_max;
    }
    public Integer getEbt_buck_value(){
	return ebt_buck_value;
    }
    public boolean hasBalance(){
	return ebt.hasBalance();
    }
    public boolean needMoreIssue(){
	return ebt.needMoreIssue();
    }    
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    ebt = new Ebt(debug, id);
	    String back = ebt.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	else{
	    ebt = new Ebt(debug);
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





































