package bucks.action;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
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

public class BuckAction extends TopAction{

    static final long serialVersionUID = 22L;	
    static Logger logger = LogManager.getLogger(BuckAction.class);
    //
    Buck buck = null;
    public String execute(){
	String ret = INPUT;
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
	if(!id.equals("")){
	    getBuck();
	    buck.setId(id);
	    back = buck.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		/**
		   back = buck.findOtherBuckInfo();
		   if(!back.equals("")){
		   addActionError(back);
		   }
		*/
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=1)
    public Buck getBuck(){ 
	if(buck == null){
	    buck = new Buck(debug);
	}		
	return buck;
    }
    @StrutsParameter(depth=1)
    public void setBuck(Buck val){
	if(val != null)
	    buck = val;
    }
    public String getValue(){
	if(buck == null)
	    getBuck();
	return buck.getValue();
    }
    public String getExpire_date(){
	if(buck == null)
	    getBuck();
	return buck.getExpire_date();
    }
    public String getFund_typeStr(){
	if(buck == null)
	    getBuck();
	return buck.getFund_typeStr();
    }
    
    public boolean isVoided(){
	if(buck == null)
	    getBuck();
	return buck.isVoided();
    }
    public boolean isEbtType(){
	return buck.isEbtType();
    }
    public boolean isDmbType(){
	return buck.isDmbType();
    }
    public boolean isRxType(){
	return buck.isRxType();
    }
    public boolean isWicType(){
	return buck.isWicType();
    }
    public boolean isSeniorType(){
	return buck.isSeniorType();
    }
    public boolean hasEbt(){
	return buck.hasEbt();
    }
    public boolean hasGift(){
	return buck.hasGift();				

    }
    public boolean hasRedeem(){
	return buck.hasRedeem();				

    }        
    @Override
    @StrutsParameter(depth=1)
    public String getId(){
	if(id.equals("") && buck != null){
	    id = buck.getId();
	}
	return id;
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






































