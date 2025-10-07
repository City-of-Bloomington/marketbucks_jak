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


public class FmnpSeniorAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(FmnpSeniorAction.class);
    //
    FmnpSenior senior = null;
    List<FmnpSenior> fmnpSeniors = null;
    String fmnpSeniorsTitle = "FMNP Senior transactions";
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
	    senior.setUser_id(user.getId());
	    senior.setSenior_max_amount(senior_max_amount);
	    back = senior.doSave();
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
	    senior.setUser_id(user.getId());
	    back = senior.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		ret ="issue"; 				
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = senior.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = senior.handleAddingBuck();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Added Successfully");
		}
	    }
	    ret ="issue"; 			
	}
	else if(action.equals("Add Bucks")){ // when there is a balance
	    ret = SUCCESS;			
	    back = senior.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    ret="issue";
	}										
	else if(action.equals("Cancel")){
	    ret = SUCCESS;
	    back = senior.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Buck> bucks = senior.getBucks();
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
		    senior = new FmnpSenior();
		    id="";
		}
	    }
	}
	else if(action.startsWith("Cancel Selected")){ // void selected GC
	    ret = SUCCESS;
	    back = senior.doCancelSelected();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Cancelled Successfully");
		if(senior.hasBalance()){
		    ret ="issue"; 			
		}
	    }
	}		
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = senior.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = senior.handleAddingBuck();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    addActionMessage("Added Successfully");
		}
	    }
	}		
	else if(!id.equals("")){
	    populate();
	    return "issue";
	}
	else{
	    getSenior();						
	    senior.setSenior_max_amount(senior_max_amount);
	}
	return ret;
    }
    @StrutsParameter(depth = 2)
    public FmnpSenior getSenior(){ // starting a new senior
	if(senior == null){
	    senior = new FmnpSenior(debug);
	    if(id.equals(""))
		senior.setSenior_max_amount(senior_max_amount);
	    senior.setId(id);
	    if(!id.isEmpty()){
		senior.doSelect();
	    }
	}		
	return senior;
    }
    @StrutsParameter(depth = 2)
    public List<FmnpSenior> getFmnpSeniors(){
	if(fmnpSeniors == null){
	    FmnpSearch bl = new FmnpSearch(debug);
	    bl.setLimit("10");
	    bl.setType("senior");
	    String back = bl.find();
	    if(back.equals("")){
		List<FmnpSenior> ones =  bl.getSeniors();
		if(ones != null && ones.size() > 0){
		    fmnpSeniors = ones;
		}
	    }
	}
	return fmnpSeniors;
    }
    public boolean hasFmnpSeniors(){
	getFmnpSeniors();
	return fmnpSeniors != null && fmnpSeniors.size() > 0;
    }
    @StrutsParameter(depth = 2)
    public void setSenior(FmnpSenior val){
	if(val != null)
	    senior = val;
	if(!id.isEmpty()){
	    senior.setId(id);
	    senior.doSelect();
	}
    }
    @Override
    public String getId(){
	if(id.equals("") && senior != null){
	    id = senior.getId();
	}
	return id;
    }
    @Override
    @StrutsParameter(depth = 1)
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }

    public boolean hasId(){
	return !id.isEmpty();
    }
    public String getAmount(){
	return senior.getAmount();
    }
    public String getDate_time(){
		
	return senior.getDate_time();
    }
    public String getTicketNum(){
	return senior.getTicketNum();
    }
    public boolean isCancelled(){
	return senior.isCancelled();
    }
    public boolean isDispute_resolution(){
	return senior.isDispute_resolution();
    }
    public User getSenior_user(){
	return senior.getUser();
    }
    public Type getBuck_type(){
	return senior.getBuck_type();
    }
    public String getTotal(){
	return senior.getTotal();
    }
    public String getBucksTotal(){
	return senior.getBucksTotal();
    }
    public String getBalance(){
	return senior.getBalance();
    }
    public boolean hasBalance(){
	return senior.hasBalance();
    }
    public boolean hasBucks(){
	return senior.hasBucks();
    }
    public List<Buck> getBucks(){
	return senior.getBucks();
    }
    //	
    public boolean needMoreIssue(){
	//
	return senior.needMoreIssue();
    }

    
    public String getFmnpSeniorsTitle(){
	return fmnpSeniorsTitle;
    }
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    senior = new FmnpSenior(debug, id);
	    String back = senior.doSelect();
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





































