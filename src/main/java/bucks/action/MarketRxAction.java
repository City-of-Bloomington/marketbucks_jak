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
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class MarketRxAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(MarketRxAction.class);
    //
    // we need the latest buckConf to get the
    // buck face value and expire date for the season
    //
    MarketRx rx = null;
    List<MarketRx> marketRxes = null;
    String bucksTitle = "Bucks in this transaction";
    String marketRxesTitle = "Most recent Market Rx Transactions";
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
	    rx.setUser_id(user.getId());
	    back = rx.doSave();
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
	    rx.setUser_id(user.getId());
	    back = rx.doUpdate();
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
	    back = rx.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = rx.handleAddingBuck();
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
	    back = rx.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    ret="issue";
	}										
	else if(action.equals("Cancel")){
	    ret = SUCCESS;
	    back = rx.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		List<Buck> bucks = rx.getBucks();
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
		    rx = new MarketRx();
		    id="";
		}
	    }
	}
	else if(action.startsWith("Cancel Selected")){ // void selected GC
	    ret = SUCCESS;
	    back = rx.doCancelSelected();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Cancelled Successfully");
		if(rx.hasBalance()){
		    ret ="issue"; 			
		}
	    }
	}		
	else if(action.equals("Add")){ // adding a buck
	    ret = SUCCESS;			
	    back = rx.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = rx.handleAddingBuck();
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
    @StrutsParameter
    public MarketRx getRx(){ // starting a new rx
	if(rx == null){
	    rx = new MarketRx(debug);
	    if(id.equals(""))
		rx.setRx_max_amount(rx_max_amount);
	    rx.setId(id);
	}		
	return rx;
    }
    @StrutsParameter
    public List<MarketRx> getMarketRxes(){
	MarketRxList bl = new MarketRxList(debug);
	bl.setLimit("10");
	String back = bl.find();
	if(back.equals("")){
	    List<MarketRx> ones =  bl.getMarketRxes();
	    if(ones != null && ones.size() > 0){
		marketRxes = ones;
	    }
	}
	return marketRxes;
    }
    @StrutsParameter
    public boolean hasMarketRxes(){
	getMarketRxes();
	boolean ret = marketRxes != null && marketRxes.size() > 0;
	System.err.println(" ret "+ret);
	return ret;
    }
    @StrutsParameter
    public void setMarketRx(MarketRx val){
	if(val != null)
	    rx = val;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && rx != null){
	    id = rx.getId();
	}
	return id;
    }
    @StrutsParameter
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter
    public String getMarketRxesTitle(){
	return marketRxesTitle;
    }	

    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    rx = new MarketRx(debug, id);
	    String back = rx.doSelect();
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





































