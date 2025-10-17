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


public class ExportAction extends TopAction{

    static final long serialVersionUID = 28L;	
    static Logger logger = LogManager.getLogger(ExportAction.class);
    //
    Export export = null;
    List<Redeem> redeems = null;
    List<Export> exports = null;
    String redeemsTitle = "Redemptions in this Export";
    String exportsTitle = "Most Recent Exports";
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
	if(action.equals("Update")){
	    ret = SUCCESS;
	    export.setUser_id(user.getId());
	    back = export.updateNwBatchName();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Update Successfully");
	    }
	}
	else if(action.startsWith("Start")){ // carry over from ExportStartAction
	    ret = SUCCESS;
	    export.setUser_id(user.getId());
	    RedeemList rl = new RedeemList(debug);
	    rl.setNotEportedYet();
	    rl.setStatus("Completed");
	    back = rl.find();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		redeems = rl.getRedeems();
		if(redeems == null || (redeems.size() == 0)){
		    addActionMessage("No redeems available to export");
		}
		else{
		    back = export.doSave();
		    if(back.equals("")){
			export.setRedeems(redeems);
			back = export.addRedeemsToExport();
			if(!back.equals("")){
			    addActionError(back);
			}
			else{
			    addActionMessage("Found "+redeems.size()+" redemptions to export");
			}
		    }
		    else{
			addActionError(back);
		    }

		}
	    }
	}		
	else if(!id.equals("")){
	    ret = populate();
	}
	return ret;
    }
    @StrutsParameter(depth=2)
    public Export getExport(){ // starting a new redeem
	if(export == null){
	    export = new Export(debug);
	}		
	return export;
    }
    @StrutsParameter(depth=2)
    public void setExport(Export val){
	if(val != null)
	    export = val;
    }
    @StrutsParameter(depth=1)
    @Override
    public String getId(){
	if(id.equals("") && export != null){
	    id = export.getId();
	}
	return id;
    }
    @StrutsParameter(depth=1)
    @Override
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }        
    public String getRedeemsTitle(){
	return redeemsTitle;
    }
    
    public String getExportsTitle(){
	return exportsTitle;
    }
    @StrutsParameter(depth=2)
    public List<Redeem> getRedeems(){
	return redeems;
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
    public boolean hasExports(){
	getExports();
	return exports != null && exports.size() > 0;
    }
	
    @StrutsParameter(depth=2)
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
    public String getNw_batch_name(){
	return export.getNw_batch_name();
    }
    public String getDate_time(){
		
	return export.getDate_time();
    }
    public String getUser_id(){
		
	return export.getUser_id();
    }
    public String getTotal(){
	return export.getTotal();
    }
    public String getStatus(){
	return export.getStatus();
    }
    public boolean isOpen(){
	return export.isOpen();
    }
    public User getExport_user(){
	return export.getUser();

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





































