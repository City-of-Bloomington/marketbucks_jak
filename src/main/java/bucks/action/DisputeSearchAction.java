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
import com.opensymphony.xwork2.ModelDriven;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.struts2.action.SessionAware;  
import org.apache.struts2.action.ServletContextAware;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class DisputeSearchAction extends TopAction{

    static final long serialVersionUID = 29L;	
    static Logger logger = LogManager.getLogger(DisputeSearchAction.class);
    //
    DisputeList disputeList = null;
    List<Dispute> disputes = null;
    private User user;
    String bucksTitle = "Disputed Bucks";
    String disputesTitle = "Most Recent Disputes";
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
	if(action.equals("Find")){ 
	    ret = SUCCESS;			
	    back = disputeList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Dispute> list = disputeList.getDisputes();
		if(list == null || list.size() == 0){
		    disputesTitle = "No dispute cases found";
		}
		else{
		    disputesTitle = "Found "+list.size()+" record";
		    disputes = list;
		}
	    }
	}
	else{
	    findDisputes();
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public DisputeList getDisputeList(){ // starting a new redeem
	if(disputeList == null){
	    disputeList = new DisputeList(debug);
	}		
	return disputeList;
    }
    @StrutsParameter(depth=1)
    public void setDisputeList(DisputeList val){
	if(val != null)
	    disputeList = val;
    }
    @StrutsParameter(depth=2)
    public List<Dispute> getDisputes(){ // starting a new redeem
	if(disputes == null){
	    disputes = new ArrayList<Dispute>();
	}		
	return disputes;
    }
    public boolean hasDisputes(){
	getDisputes();
	return disputes != null && disputes.size() > 0;
    }
    public String getRedeem_id(){
	return disputeList.getRedeem_id();
    }	
    public String getWhich_date(){
	return disputeList.getWhich_date();
    }
    public String getDate_from(){
	return disputeList.getDate_from() ;
    }
    public String getDate_to(){
	return disputeList.getDate_to() ;
    }
    public String getSortBy(){
	return disputeList.getSortBy() ;
    }
    public String getStatus(){
	return disputeList.getStatus();
    }
    public String getReason(){
	return disputeList.getReason();
    }	    
    public String getBucksTitle(){
	return bucksTitle;
    }
    public String getDisputesTitle(){
	return disputesTitle;
    }
    public String getAction(){
	return action;
    }
    private void findDisputes(){
	if(disputes == null){
	    DisputeList dl = new DisputeList();
	    dl.setLimit("30");
	    String back = dl.find();
	    if(back.isEmpty()){
		List<Dispute> ones = dl.getDisputes();
		if(ones != null && ones.size() > 0);
		disputes = ones;
	    }
	}
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





































