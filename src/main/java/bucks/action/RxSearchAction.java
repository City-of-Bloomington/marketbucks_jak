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

public class RxSearchAction extends TopAction{

    static final long serialVersionUID = 237L;	
    static Logger logger = LogManager.getLogger(RxSearchAction.class);
    //
    List<MarketRx> rxes = null;
    MarketRxList rxList = null;
    String bucksTitle = "Bucks";
    String rxesTitle = "Issued MB's";
    int rxTotal = 0;
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
	if(action.equals("Search")){
	    ret = SUCCESS;
	    rxList.setNoLimit();
	    back = rxList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		rxes = rxList.getMarketRxes();
		if(rxes == null || rxes.size() == 0){
		    rxesTitle = "No match found ";
		}
		else{
		    rxesTitle = "Found "+rxes.size()+" records";
		}
	    }
	}		
	return ret;
    }
    public String getId(){
	return rxList.getId();
    }
    public String getBuck_id(){
	return rxList.getBuck_id();
    }
    public String getAmount(){
	return rxList.getAmount();
    }
    public String getWhich_date(){
	return rxList.getWhich_date();
    }
    public String getDate_from(){
	return rxList.getDate_from() ;
    }
    public String getDate_to(){
	return rxList.getDate_to() ;
    }

    public String getSortBy(){
	return rxList.getSortBy() ;
    }
    public String getCancelled(){
	return rxList.getCancelled() ;
    }
    public String getDispute_resolution(){
	return rxList.getDispute_resolution() ;
    }	
    
    @StrutsParameter(depth=1)
    public MarketRxList getRxList(){ 
	if(rxList == null){
	    rxList = new MarketRxList(debug);
	}		
	return rxList;
    }
    @StrutsParameter(depth=2)
    public List<MarketRx> getRxes(){
	return rxes;
    }
    public int getRxTotal(){
	if(hasRxes()){
	    if(rxTotal == 0){
		for(MarketRx one:rxes){
		    if(!one.isCancelled()){
			rxTotal += one.getAmountInt();
		    }
		}
	    }
	}
	return rxTotal;
    }
    public boolean hasRxes(){
				
	return rxes != null && rxes.size() > 0;

    }
    public String getBucksTitle(){
	return bucksTitle;
    }
    public String getRxesTitle(){
	return rxesTitle;
    }

    // we need this for auto_complete
    public void setVendorName(String val){
	// just ignore 
    }	
    public String populate(){
	String ret = SUCCESS;
	rxList = new MarketRxList(debug);
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





































