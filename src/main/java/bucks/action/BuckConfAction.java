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

public class BuckConfAction extends TopAction{

    static final long serialVersionUID = 20L;	
    static Logger logger = LogManager.getLogger(BuckConfAction.class);
    BuckConf buckConf = null;
    List<Batch> batches = null;
    List<BuckConf> buckConfs = null; // most recent confs
    private List<Type> buck_types = null;
    private List<Type> gl_accounts = null;
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
	    buckConf.setUser_id(user.getId());
	    back = buckConf.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Saved Successfully");	
	    }
	}
	else if(action.equals("Update")){
	    buckConf.setUser_id(user.getId());			
	    back = buckConf.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		ret = ERROR;
	    }
	    else{
		addActionMessage("Updated Successfully");				
	    }
	}
	else if(action.equals("Refresh")){ // refresh vendor list
	    /*
	    // this part is automated on the redeem/vendor interface
	    ret = SUCCESS;
	    RefreshVendors rv = new RefreshVendors(debug,
	    vendorsCheckUrl,
	    vendorsDatabase,
	    vendorsUser,
	    vendorsPassword
	    );
	    back = rv.consolidate();
	    if(!back.equals("")){
	    addActionError(back);
	    }
	    else{
	    addActionMessage("Refreshed Successfully");
	    }
	    */
	}		
	else if(!id.equals("")){
	    buckConf = new BuckConf(debug, id);
	    back = buckConf.doSelect();
	    if(!back.equals("")){
		addActionError(back);
		ret = ERROR;
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public BuckConf getBuckConf(){
	if(buckConf == null){
	    buckConf = new BuckConf(debug);
	}		
	return buckConf;
    }
    @StrutsParameter(depth=2)
    public void setBuckConf(BuckConf val){
	if(val != null)
	    buckConf = val;
    }
    public String getName(){
	//
	return buckConf.getName();
    }	
    public String getValue(){
	return buckConf.getValue();
    }
    public String getType_id(){
	return buckConf.getType_id();
    }
    @StrutsParameter(depth=1)
    public Type getType(){
	return buckConf.getType();
    }	    
    @StrutsParameter(depth=1)
    @Override
    public String getId(){
	if(id.equals("") && buckConf != null){
	    id = buckConf.getId();
	}
	return id;
    }
    public String getDate(){
		
	return buckConf.getDate();
    }
    public String getDonor_max_value(){
		
	return buckConf.getDonor_max_value();
    }
    @StrutsParameter(depth=1)    
    public User getConf_user(){
	return buckConf.getUser();
    }	
    public boolean isCurrent(){
	return buckConf.isCurrent();
    }
    public boolean isExpired(){
	return buckConf.isExpired();
    }		
    public String getGl_account(){
	return buckConf.getGl_account();
    }	    
    public String getBatchesTitle(){
	return "Current batches in this Configuration";
    }
    public boolean hasBatches(){
	return buckConf.hasBatches();
    }
    public List<Batch> getBatches(){
	return buckConf.getBatches();
    }
    @StrutsParameter(depth=2)
    public List<BuckConf> getBuckConfs(){
	if(buckConfs == null){
	    BuckConfList bcl = new BuckConfList(debug);
	    String back = bcl.find();
	    if(back.equals("") && (bcl.getBuckConfs() != null)){
		buckConfs = bcl.getBuckConfs();
	    }
	}		
	return buckConfs;
    }
    @StrutsParameter(depth=2)
    public List<Type> getBuck_types(){
	if(buck_types == null){
	    TypeList bcl = new TypeList(debug, "buck_types");
	    bcl.setSortBy("id");
	    String back = bcl.find();
	    if(back.equals("")){
		buck_types = bcl.getTypes();
	    }
	}		
	return buck_types;
    }
    @StrutsParameter(depth=2)
    public List<Type> getGl_accounts(){
	if(gl_accounts == null){
	    TypeList bcl = new TypeList(debug, "gl_accounts");
	    bcl.setSortBy("id");
	    String back = bcl.find();
	    if(back.equals("")){
		gl_accounts = bcl.getTypes();
	    }
	}		
	return gl_accounts;
    }		
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    buckConf = new BuckConf(debug, id);
	    String back = buckConf.doSelect();
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





































