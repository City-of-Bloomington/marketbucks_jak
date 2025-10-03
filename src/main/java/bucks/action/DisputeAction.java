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
    @StrutsParameter
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
    @StrutsParameter
    public void setDispute(Dispute val){
	if(val != null)
	    dispute = val;
    }
    @StrutsParameter
    public String getDisputesTitle(){
	return disputesTitle;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && dispute != null){
	    id = dispute.getId();
	}
	return id;
    }
    @StrutsParameter
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





































