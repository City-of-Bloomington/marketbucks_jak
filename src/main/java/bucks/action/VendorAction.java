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

public class VendorAction extends TopAction{

    static final long serialVersionUID = 150L;	
    static boolean debug = false;
    static Logger logger = LogManager.getLogger(VendorAction.class);
    Vendor vendor = null;
    List<Vendor> vendors = null;
    String vendorsTitle = "Current Vendors";
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
	if(action.equals("Save")){
	    back = vendor.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		id = vendor.getId();
		addActionMessage("Saved Successfully");
	    }
	}
	else if(action.equals("setInactive")){
	    vendor = new Vendor(debug, id);
	    back = vendor.doSelect();
	    vendor.setActive(false);
	    back = vendor.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}	
	else if(action.equals("Update")){
	    back = vendor.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else if(!id.equals("")){
	    vendor = new Vendor(debug, id);
	    back = vendor.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	return ret;
    }
    @StrutsParameter
    public List<Vendor> getVendors(){
	if(vendors == null){
	    VendorList tl = new VendorList(debug);
	    String back = tl.find();
	    if(back.equals("")){
		vendors = tl.getVendors();
	    }
	}
	return vendors;
    }
    @StrutsParameter
    public void setVendor(Vendor val){
	if(val != null)
	    vendor = val;
    }
    @StrutsParameter
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    @StrutsParameter
    public Vendor getVendor(){
	if(vendor == null){
	    vendor = new Vendor();
	}
	return vendor;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && vendor != null){
	    id = vendor.getId();
	}
	return id;
    }
    @StrutsParameter
    public boolean hasVendors(){
	getVendors();
	return vendors != null && vendors.size() > 0;
    }
    public String populate(){
	String ret = SUCCESS;
	return ret;
    }
    @StrutsParameter
    public String getVendorsTitle(){
	return vendorsTitle;
    }
    /**
     * this is needed for systems that will not provide vendor separate database
     * this will allow to Add/Edit vendors through the interface
     * we are using the flag 'enableVendorListUpdate' to tell us that we
     * have an external database for vendors (when true). If false, that mean
     * we have to add vendors through the interface
     */
    @StrutsParameter
    public boolean canEdit(){
	return true;
	// disable this for now
	// return !enableVendorsAutoUpdate;
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





































