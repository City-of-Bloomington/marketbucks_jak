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
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.action.SessionAware;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class EbtAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(EbtAction.class);
    //
    Ebt ebt = null;
    List<Ebt> ebts = null;
    String bucksTitle = "Bucks in this Request";
    String ebtsTitle = "Most Recent Requests";
    int ebtTotal= 0, dmbTotal = 0;
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
	if(action.equals("Next")){ // Save new ebt
	    ret = SUCCESS;
	    ebt.setEbt_donor_max(ebt_donor_max);
	    ebt.setEbt_buck_value(ebt_buck_value);
	    ebt.setUser_id(user.getId());
	    back = ebt.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		ret ="issue"; // add bucks
		addActionMessage("Saved Successfully");
		id = ebt.getId();
	    }
	}
	else if(action.equals("Update")){
	    ret = SUCCESS;
	    ebt.setUser_id(user.getId());
	    back = ebt.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		ret ="issue"; // add bucks				
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.startsWith("Cancel")){
	    ret = SUCCESS;
	    back = ebt.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		ebt = new Ebt();
		id="";
		addActionMessage("Cancelled Successfully");
	    }
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
    public Ebt getEbt(){ // starting a new ebt
	if(ebt == null){
	    ebt = new Ebt(debug);
	}		
	return ebt;
    }
    @StrutsParameter(depth = 1)
    public List<Ebt> getEbts(){
	if(ebts == null){
	    EbtList bl = new EbtList(debug);
	    String back = bl.find();
	    if(back.equals("") && bl.getEbts() != null){
		ebts = bl.getEbts();
	    }
	}
	return ebts;
    }
    @StrutsParameter(depth = 1)
    public boolean hasEbts(){
	getEbts();
	return ebts != null && ebts.size() > 0;
    }
    @StrutsParameter(depth = 1)
    public void setEbt(Ebt val){
	if(val != null)
	    ebt = val;
    }
    @StrutsParameter(depth = 1)
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter(depth = 1)
    public String getEbtsTitle(){
	return ebtsTitle;
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
    public String getIncludeDouble(){
	return ebt.getIncludeDouble();
    }
    public boolean isCancelled(){
	return ebt.isCancelled();
    }
    public boolean isDispute_resolution(){
	return ebt.isDispute_resolution();
    }
    public Integer getEbt_buck_value(){
	return ebt_buck_value;
    }
    public Integer getEbt_donor_max(){
	return ebt_donor_max;
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
	return ret;
    }
    @StrutsParameter(depth = 1)
    public int getEbtTotal(){
	if(hasEbts()){
	    if(ebtTotal == 0){
		for(Ebt one:ebts){
		    if(!one.isCancelled()){
			ebtTotal += one.getAmountInt();
			dmbTotal += one.getDmb_amountInt();
		    }
		}
	    }
	}
	return ebtTotal;
    }
    @StrutsParameter(depth = 1)
    public int getDmbTotal(){
	if(ebtTotal == 0)
	    getEbtTotal();
	return dmbTotal;
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





































