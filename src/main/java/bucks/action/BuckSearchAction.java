package bucks.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class BuckSearchAction extends TopAction{

    static final long serialVersionUID = 227L;	
    static Logger logger = LogManager.getLogger(BuckSearchAction.class);
    //
    BuckList buckList = null;
    List<Buck> bucks = null;
    String bucksTitle = "Bucks";
    String batchesTitle = "Batches";
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
	if(!action.equals("")){
	    ret = SUCCESS;
	    buckList.setNoLimit();
	    back = buckList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		bucks = buckList.getBucks();
		if(bucks == null || bucks.size() == 0){
		    bucksTitle = "No match found ";
		}
		else{
		    bucksTitle = "Found "+bucks.size()+" records";
		}
	    }
	}		
	return ret;
    }
    @Override
    public String getId(){
	if(id.isEmpty())
	    return buckList.getId();
	return id;
    }
    public String getEbt_id(){
	return buckList.getEbt_id();
    }
    public String getRx_id(){
	return buckList.getRx_id();
    }
    public String getWic_id(){
	return buckList.getWic_id();
    }
    public String getSenior_id(){
	return buckList.getSenior_id();
    }
    public String getBatch_id(){
	return buckList.getBatch_id();
    }		
    public String getGift_id(){
	return buckList.getGift_id();
    }	
    public String getRedeem_id(){
	return buckList.getRedeem_id();
    }	
    public String getFund_type(){
	return buckList.getFund_type();
    }
    public String getExport_id(){
	return buckList.getExport_id();
    }
    public String getWhich_date(){
	return buckList.getWhich_date(); // bs.date is batch date
    }
    public String getDate_from(){
	return buckList.getDate_from() ;
    }
    public String getDate_to(){
	return buckList.getDate_to() ;
    }
    public String getSortBy(){
	return buckList.getSortBy() ;
    }
    public String getType(){
	return buckList.getType();
    }    
    @StrutsParameter(depth = 1)
    public BuckList getBuckList(){ 
	if(buckList == null){
	    buckList = new BuckList(debug);
	}		
	return buckList;
    }
    @StrutsParameter(depth = 1)
    public List<Buck> getBucks(){
	return bucks;
    }
    @StrutsParameter(depth = 1)
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter(depth = 1)
    public String getBatchesTitle(){
	return batchesTitle;
    }

    public String populate(){
	String ret = SUCCESS;
	buckList = new BuckList(debug);
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





































