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
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class DisputeAction extends TopAction{

    static final long serialVersionUID = 28L;	
    static Logger logger = LogManager.getLogger(DisputeAction.class);
    //
    Dispute dispute = null;
    List<Dispute> disputes = null;
    String disputesTitle = "Most Recent Waiting Disputes";
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
	if(action.equals("Refresh")){ 
	    back = dispute.doRefresh();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	else if(action.equals("Update")){ 
	    dispute.setUser_id(user.getId());
	    back = dispute.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.equals("Delete")){ 
	    back = dispute.doDelete();
	    if(!back.equals("")){
		// back to the same page 
		addActionError(back);
	    }
	    else{
		ret = "search";
	    }
	}		
	else if(!id.equals("id")){ 
	    dispute = new Dispute(debug, id);
	    back = dispute.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}		
	return ret;
    }
    @StrutsParameter(depth=2)
    public Dispute getDispute(){ // starting a new redeem
	if(dispute == null){
	    if(!id.equals("")){
		dispute = new Dispute(debug, id);
	    }
	    else{
		dispute = new Dispute(debug);
	    }
	}		
	return dispute;
    }
    @StrutsParameter(depth=2)
    public void setDispute(Dispute val){
	if(val != null)
	    dispute = val;
    }
    public String getDisputesTitle(){
	return disputesTitle;
    }
    @Override
    public String getId(){
	if(id.equals("") && dispute != null){
	    id = dispute.getId();
	}
	return id;
    }
    public boolean hasResolution(){
	return dispute.hasResolution();
    }
    public boolean isWaiting(){
	return dispute.isWaiting();
    }
    public String getResolution_id(){
	return dispute.getResolution_id();
    }
    public String getRedeem_id(){
	return dispute.getRedeem_id();
    }
    public String getBuck_id(){
	return dispute.getBuck_id();
    }        
    public boolean hasNotes(){
	return dispute.hasNotes();
    }
    public boolean canEdit(){
	return dispute.canEdit();
    }
    @StrutsParameter(depth=2)
    public User getDispute_user(){
	return dispute.getUser();
    }
    @StrutsParameter(depth=2)
    public Buck getBuck(){
	return dispute.getBuck();
    }
    @StrutsParameter(depth=2)
    public Redeem getRedeem(){
	return dispute.getRedeem();
    }        
    public String getReason(){
	return dispute.getReason();
    }
    public String getDate_time(){
	return dispute.getDate_time();
    }    
    public String getStatus(){
	return dispute.getStatus();
    }    
    public String getSuggestions(){
	return dispute.getSuggestions();
    }
    public String getNotes(){
	return dispute.getNotes();
    }
    public boolean hasDisputes(){
	getDisputes();
	return disputes != null && disputes.size() > 0;
    }
    @StrutsParameter(depth=2)
    public List<Dispute> getDisputes(){ // starting a new redeem
	if(disputes == null){
	    DisputeList dl = new DisputeList(debug);
	    dl.setStatus("Waiting");
	    String back = dl.find();
	    disputes = dl.getDisputes();
	}		
	return disputes;
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





































