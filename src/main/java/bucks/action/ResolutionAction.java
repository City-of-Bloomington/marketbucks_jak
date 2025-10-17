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


public class ResolutionAction extends TopAction{

    static final long serialVersionUID = 227L;	
    String dispute_id="";
    static Logger logger = LogManager.getLogger(ResolutionAction.class);
    //
    List<Resolution> resolutions = null;
    Resolution resolution = null;
    List<BuckConf> confs = null; 
    String bucksTitle = "Redeemed Bucks";
    String resolutionsTitle = "Most Recent Resolutions";	
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
	if(action.equals("Submit")){
	    ret = SUCCESS;
	    resolution.setUser_id(user.getId());
	    back = resolution.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Saved Successfully");
		ret = "view";
	    }
	}
	else if(!id.equals("")){
	    ret = populate();
	}
	else {
	    resolution = new Resolution();
	    if(!dispute_id.isEmpty()){
		resolution.setDispute_id(dispute_id);
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public Resolution getResolution(){ // starting a new redeem
	if(resolution == null){
	    resolution = new Resolution(debug);
	    if(!dispute_id.equals("")){
		resolution.setDispute_id(dispute_id);
	    }
	}		
	return resolution;
    }
    public boolean hasResolutions(){
	getResolutions();
	return resolutions != null && resolutions.size() > 0;
    }
    @StrutsParameter(depth=2)
    public List<Resolution> getResolutions(){
	ResolutionList bl = new ResolutionList(debug);
	String back = bl.find();
	if(back.equals("") && bl.getResolutions() != null){
	    resolutions = bl.getResolutions();
	}
	return resolutions;
    }
    @StrutsParameter(depth=2)
    public List<BuckConf> getConfs(){
	if(confs == null){
	    BuckConfList bcl = new BuckConfList(debug);
	    bcl.setExcludeOldYears();
	    String back = bcl.find();
	    if(back.equals("") && (bcl.getBuckConfs() != null)){
		confs = bcl.getBuckConfs();
	    }
	}		
	return confs;
    }
    public void setResolution(Resolution val){
	if(val != null)
	    resolution = val;
    }
    @Override
    public String getId(){
	if(id.equals("") && resolution != null){
	    id = resolution.getId();
	}
	return id;
    }
    @Override
    @StrutsParameter(depth=1)    
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    @StrutsParameter(depth=1)
    public void setDispute_id(String val){
	if(val != null){
	    dispute_id = val;
	}
    }        
    public String getNew_buck_id(){
	return resolution.getNew_buck_id();
    }
    public String getBuck_id(){
	return resolution.getBuck().getId();
    }	
    public String getDispute_id(){
	return resolution.getDispute_id();
    }
    public String getConf_id(){
	return resolution.getConf_id();
    }	
    public String getDate_time(){
		
	return resolution.getDate_time();
    }
    public String getValue(){
	return resolution.getValue();
    }
    public String getApprove(){
	return resolution.getApprove();
    }	
    public String getCard_last_4(){
	return resolution.getCard_last_4();
    }
    public String getPay_type(){
	return resolution.getPay_type();
    }
    public String getCheck_no(){
	return resolution.getCheck_no();
    }
    public String getStatus(){
	return resolution.getStatus();
    }
    public String getInfo(){
	return resolution.getConf().getInfo();
    }    
    @StrutsParameter(depth=2)
    public User getResolve_user(){
	return resolution.getUser();
    }
    @StrutsParameter(depth=2)
    public Buck getBuck(){
	return resolution.getBuck();
    }
    public String getBuck_type_id(){
	return resolution.getBuck().getBuck_type_id();
    }	    
    @StrutsParameter(depth=2)	
    public Dispute getDispute(){
	return resolution.getDispute();
    }
    public String getReason(){
	return resolution.getDispute().getReason();
    }
    public String getExpire_date(){
	return resolution.getExpire_date();
    }    
    @StrutsParameter(depth=2)
    public BuckConf getConf(){
	return resolution.getConf();
    }	    
    public String getBucksTitle(){
	return bucksTitle;
    }
    public String getResolutionsTitle(){
	return resolutionsTitle;
    }
    // we need this for auto_complete
    public void setVendorName(String val){
	// just ignore 
    }	
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    resolution = new Resolution(debug, id);
	    String back = resolution.doSelect();
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





































