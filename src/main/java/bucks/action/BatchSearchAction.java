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
import org.apache.struts2.dispatcher.HttpParameters;  
import org.apache.struts2.dispatcher.Parameter;  
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class BatchSearchAction extends TopAction{

    static final long serialVersionUID = 227L;	
    static Logger logger = LogManager.getLogger(BatchSearchAction.class);
    //
    List<Batch> batches = null;
    BatchList batchList = null;
    private List<Type> buck_types = null;
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
	    batchList.setNoLimit();
	    back = batchList.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		batches = batchList.getBatches();
		if(batches == null || batches.size() == 0){
		    batchesTitle = "No match found ";
		}
		else{
		    batchesTitle = "Found "+batches.size()+" records";
		}
	    }
	    if(action.startsWith("Printable")){
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    String all = "";
		    for(String key:paramMap.keySet()){
			if(key.equals("action")) continue;			
			Parameter param = paramMap.get(key);
			if(param.isMultiple()){
			    String[] vals = param.getMultipleValues();
			    if(vals != null && !vals[0].equals("")){
				all +="&"+key.substring(key.indexOf(".")+1)+"="+vals[0];
				// System.err.println(" key "+key+" "+vals[0]);
			    }			    
			}
		    }
		    System.err.println(all);
		    String str = url+"AuditSheet.do?"+all;
		    res.sendRedirect(str);
		    return super.execute();
		}catch(Exception ex){
		    System.err.println(ex);
		}					
	    }
	}		
	return ret;
    }
    @StrutsParameter
    public BatchList getBatchList(){ 
	if(batchList == null){
	    batchList = new BatchList(debug);
	}		
	return batchList;
    }
    @StrutsParameter
    public List<Batch> getBatches(){
	return batches;
    }
    @StrutsParameter
    public String getBucksTitle(){
	return bucksTitle;
    }
    @StrutsParameter
    public String getBatchesTitle(){
	return batchesTitle;
    }
    @StrutsParameter
    public List<Type> getBuck_types(){
	if(buck_types == null){
	    TypeList bcl = new TypeList(debug, "buck_types");
	    String back = bcl.find();
	    if(back.equals("")){
		buck_types = bcl.getTypes();
	    }
	}
	Type tt = new Type(debug, "-1","All");
	buck_types.add(0,tt);
	return buck_types;
    }	
    // we need this for auto_complete
    @StrutsParameter
    public void setVendorName(String val){
	// just ignore 
    }	
    public String populate(){
	String ret = SUCCESS;
	batchList = new BatchList(debug);
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





































