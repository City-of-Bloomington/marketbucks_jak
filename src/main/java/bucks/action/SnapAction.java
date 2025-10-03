/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.action;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import com.opensymphony.xwork2.ModelDriven;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;  
import jakarta.servlet.http.HttpServletResponse;  
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.action.SessionAware;  
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class SnapAction extends TopAction{

    static final long serialVersionUID = 25L;	
    static Logger logger = LogManager.getLogger(SnapAction.class);
    static DecimalFormat dblf = new DecimalFormat("0.00");
    //
    Snap snap = null;
    List<Snap> snaps = null;
    String snapsTitle = "Most Recent Purchases";
    double snapTotal= 0, dblTotal = 0, ebtTotal=0;
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	getSnap();
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
	if(action.equals("Next")){
	    ret = SUCCESS;
	    back = snap.doSplitSnapAmount();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}	
	if(action.equals("Save")){ 
	    ret = SUCCESS;
	    snap.setUser_id(user.getId());
	    back = snap.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		addActionMessage("Saved Successfully");
	    }
	}
	else if(action.equals("Update")){
	    ret = SUCCESS;
	    snap.setUser_id(user.getId());
	    back = snap.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		action="";
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.startsWith("Cancel")){
	    ret = SUCCESS;
	    snap.setUser_id(user.getId());
	    back = snap.doCancel();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Cancelled Successfully");
	    }
	}
	else if(action.startsWith("snapStart")){
	    ret = "snapStart";
	}	
	else if(!id.isEmpty()){
	    ret = populate();
	}
	else{ // start snap entry total amount
	    ret = "snapStart";
	}
	return ret;
    }
    @StrutsParameter(depth = 1)
    public Snap getSnap(){ // starting a new snap
	if(snap == null){
	    if(id.equals(""))
		snap = new Snap(debug);
	    else{
		snap = new Snap(debug, id);
		String back = snap.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		}
	    }
	}		
	return snap;
    }
    @StrutsParameter(depth = 1)
    public List<Snap> getSnaps(){
	if(snaps == null){
	    SnapList bl = new SnapList(debug);
	    bl.setTodayDate();
	    String back = bl.find();
	    if(back.equals("") && bl.getSnaps() != null){
		snaps = bl.getSnaps();
	    }
	}
	return snaps;
    }
    @StrutsParameter(depth = 1)
    public boolean hasSnaps(){
	getSnaps();
	return snaps != null && snaps.size() > 0;
    }
    @StrutsParameter(depth = 1)
    public void setSnap(Snap val){
	if(val != null)
	    snap = val;
    }
    @StrutsParameter(depth = 1)
    public String getSnapsTitle(){
	return snapsTitle;
    }	
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    snap = new Snap(debug, id);
	    String back = snap.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	return ret;
    }
    @StrutsParameter(depth = 1)
    public String getSnapTotal(){
	if(hasSnaps()){
	    if(snapTotal == 0){
		for(Snap one:snaps){
		    if(!one.isCancelled()){
			snapTotal += one.getSnapAmountDbl();
			dblTotal += one.getDblAmountDbl();
			ebtTotal += one.getEbtAmountDbl();
		    }
		}
	    }
	}
	return dblf.format(snapTotal);
    }
    @StrutsParameter(depth = 1)
    public String getDblTotal(){
	if(snapTotal == 0)
	    getSnapTotal();
	return dblf.format(dblTotal);
    }
    @StrutsParameter(depth = 1)
    public String getEbtTotal(){
	if(snapTotal == 0)
	    getSnapTotal();
	return dblf.format(ebtTotal);
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





































