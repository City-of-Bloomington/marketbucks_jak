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


public class ExportStartAction extends TopAction{

    static final long serialVersionUID = 28L;	
    static Logger logger = LogManager.getLogger(ExportAction.class);
    //
    Export export = null;
    List<Export> exports = null;
    List<Redeem> redeems = null;
    String exportsTitle = "Most Recent Exports";
    String redeemsTitle = "Recent redemptions that need to be exported";
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
	else if(!id.equals("")){
	    ret = populate();
	}
	else if(action.startsWith("Start")){
	    ret = "export";
	}
	return ret;
    }
    @StrutsParameter
    public Export getExport(){ // starting a new redeem
	if(export == null){
	    export = new Export(debug);
	}		
	return export;
    }
    @StrutsParameter
    public void setExport(Export val){
	if(val != null)
	    export = val;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && export != null){
	    id = export.getId();
	}
	return id;
    }
    @StrutsParameter
    public String getRedeemsTitle(){
	return redeemsTitle;
    }
    @StrutsParameter
    public String getExportsTitle(){
	return exportsTitle;
    }
    @StrutsParameter
    public List<Export> getExports(){
	if(exports == null){
	    ExportList el = new ExportList(debug);
	    String back = el.find();
	    if(back.equals("")){
		List<Export> list = el.getExports();
		if(list != null && list.size() > 0){
		    exports = list;
		}
	    }
	}
	return exports;
    }
    @StrutsParameter
    public List<Redeem> getRedeems(){
	if(redeems == null){
	    RedeemList rl = new RedeemList(debug);
	    rl.setNotEportedYet();
	    rl.setStatus("Completed");
	    String back = rl.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		redeems = rl.getRedeems();
		if(redeems == null || (redeems.size() == 0)){
		    addActionMessage("No redeems available to export");
		}
		else{
		    addActionMessage("Found "+redeems.size()+" redemptions to export");
		}
	    }
	}
	return redeems;
    }
    @StrutsParameter
    public boolean hasDisputes(){
	if(redeems != null){
	    for(Redeem one:redeems){
		if(one.hasDisputes()){
		    return true;
		}
	    }
	}
	return false;
    }	
    public String populate(){
	String ret = SUCCESS;
	if(!id.equals("")){
	    export = new Export(debug, id);
	    String back = export.doSelect();
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





































