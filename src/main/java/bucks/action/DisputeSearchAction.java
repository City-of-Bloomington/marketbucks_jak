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
	return ret;
    }
    @StrutsParameter
    public DisputeList getDisputeList(){ // starting a new redeem
	if(disputeList == null){
	    disputeList = new DisputeList(debug);
	}		
	return disputeList;
    }
    @StrutsParameter
    public void setDisputeList(DisputeList val){
	if(val != null)
	    disputeList = val;
    }
    @StrutsParameter
    public List<Dispute> getDisputes(){ // starting a new redeem
	if(disputes == null){
	    disputes = new ArrayList<Dispute>();
	}		
	return disputes;
    }
    @StrutsParameter
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter
    public String getDisputesTitle(){
	return disputesTitle;
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





































