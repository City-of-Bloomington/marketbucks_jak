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
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class FmnpWicAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(FmnpWicAction.class);
    //
    FmnpWic wic = null;
    List<FmnpWic> fmnpWics = null;
    String fmnpWicsTitle = "FMNP WIC transactions";
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
	    wic.setUser_id(user.getId());
	    wic.setWic_max_amount(wic_max_amount);
	    back = wic.doSave();
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
	    wic.setUser_id(user.getId());
	    back = wic.doUpdate();
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
	    back = wic.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = wic.handleAddingBuck();
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
	    back = wic.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    ret="issue";
	}										
	else if(action.equals("Cancel")){
	    ret = SUCCESS;
	    back = wic.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Buck> bucks = wic.getBucks();
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
		    wic = new FmnpWic();
		    id="";
		}
	    }
	}
	else if(action.startsWith("Cancel Selected")){ // void selected GC
	    ret = SUCCESS;
	    back = wic.doCancelSelected();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Cancelled Successfully");
		if(wic.hasBalance()){
		    ret ="issue"; 			
		}
	    }
	}		
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = wic.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = wic.handleAddingBuck();
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
	else{
	    getWic();
	    wic.setWic_max_amount(wic_max_amount);						
	}
	return ret;
    }
    @StrutsParameter(depth = 2)
    public FmnpWic getWic(){ // starting a new wic
	if(wic == null){
	    wic = new FmnpWic(debug);
	    if(id.equals(""))
		wic.setWic_max_amount(wic_max_amount);
	    wic.setId(id);
	    if(!id.isEmpty()){
		wic.doSelect();
	    }
	}		
	return wic;
    }
    @StrutsParameter(depth = 2)
    public List<FmnpWic> getFmnpWics(){
	FmnpWicList bl = new FmnpWicList(debug);
	bl.setLimit("10");
	String back = bl.find();
	if(back.equals("")){
	    List<FmnpWic> ones =  bl.getFmnpWics();
	    if(ones != null && ones.size() > 0){
		fmnpWics = ones;
	    }
	}
	return fmnpWics;
    }
    public boolean hasFmnpWics(){
	getFmnpWics();
	return fmnpWics != null && fmnpWics.size() > 0;
    }
    @StrutsParameter(depth = 2)
    public void setWic(FmnpWic val){
	if(val != null)
	    wic = val;
	if(!id.isEmpty()){
	    wic.setId(id);
	    wic.doSelect();
	}
    }
    @Override
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }    
    @Override
    public String getId(){
	if(id.equals("") && wic != null){
	    id = wic.getId();
	}
	return id;
    }
    public String getAmount(){
	return wic.getAmount();
    }
    public String getDate_time(){
		
	return wic.getDate_time();
    }
    public User getWic_user(){
		
	return wic.getUser();
    }
    public String getTicketNum(){
	return wic.getTicketNum();
    }		
    public String getBuck_type_id(){
		
	return wic.getBuck_type_id();
    }
    public String getCancelled(){
		
	return wic.getCancelled();
    }
    public boolean isDispute_resolution(){
	return wic.isDispute_resolution();
    }	    
    public boolean hasId(){
	return !id.isEmpty();
    }
    public String getTotal(){
	return wic.getTotal();
    }
    public String getBalance(){
	return wic.getBalance();
    }
    public boolean hasBalance(){
	return wic.hasBalance();
    }
    public boolean hasBucks(){
	return wic.hasBucks();
    }
    public List<Buck> getBucks(){
	return wic.getBucks();
    }
    public String getBucksTotal(){
	return wic.getBucksTotal();
    }
    //	
    public boolean needMoreIssue(){
	//
	return wic.needMoreIssue();
    }    
    public String getFmnpWicsTitle(){
	return fmnpWicsTitle;
    }
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    wic = new FmnpWic(debug, id);
	    String back = wic.doSelect();
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





































