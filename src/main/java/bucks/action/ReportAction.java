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
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class ReportAction extends TopAction{

    static final long serialVersionUID = 80L;	
   
    static Logger logger = LogManager.getLogger(ReportAction.class);
    Report report = null;
    String format = "";
    List<String> years = null;
    List<Vendor> vendors = null;
    public String execute(){
	String ret = INPUT;
	doPrepare();
        // default
	if(action.equals("Submit")){
	    ret = SUCCESS;
	    String back = report.find();
	    if(!back.equals("")){
		addActionError(back);
		ret = INPUT;
	    }
	    else{
		if(!format.equals("")){
		    ret = "csv";
		}
	    }
	}
	return ret;
    }
    @StrutsParameter(depth=2)    
    public Report getReport(){
	if(report == null){
	    report = new Report(debug);
	}
	return report;
    }
    @StrutsParameter(depth=2)
    public void setReport(Report val){
	if(val != null)
	    report = val;
    }
    public void setFormat(boolean val){
	if(val)
	    format = "csv";
    }
    public boolean getFormat(){
	return !format.equals("");
    }
    @StrutsParameter(depth=1)
    public List<String> getYears(){
	if(years == null){
	    int start_year = 2014;
	    int yy = Helper.getCurrentYear();
	    years = new ArrayList<String>(yy-start_year+1);
	    years.add("");
	    for(int i=yy;i >= start_year;i--){
		years.add(""+i);
	    }
	}
	return years;
    }
    @StrutsParameter(depth=1)
    public List<Vendor> getVendors(){
	VendorList vl = new VendorList(debug);
	String back = vl.find();
	if(back.equals("")){
	    vendors = vl.getVendors();
	}
	return vendors;
    }
    public String getYear(){
	return report.getYear();
    }
    public String getPrev_year(){
	return report.getPrev_year();
    }
    public String getNext_year(){
	return report.getNext_year();
    }	
    public String getDay(){
	return report.getDay();
    }	
    public String getDate_from(){
	return report.getDate_from() ;
    }	
    public String getDate_to(){
	return report.getDate_to() ;
    }
    public String getBy(){
	return report.getBy() ;
    }
    public String getType(){
	return report.getType();
    }
    public boolean getDistributeMB(){
	return report.getDistributeMB();
    }
    public boolean getDistributeGC(){
	return report.getDistributeGC();
    }
    public boolean getDistributeRX(){
	return report.getDistributeRX();
    }
    public boolean getDistributeWic(){
	return report.getDistributeWic();
    }
    public boolean getDistributeSenior(){
	return report.getDistributeSenior();
    }
    public boolean getRedeemRX(){
	return report.getRedeemRX();
    }
    public boolean getRedeemWic(){
	return report.getRedeemWic();
    }
    public boolean getRedeemSenior(){
	return report.getRedeemSenior();
    }		
    public boolean getIssued(){
	return report.getIssued();
    }
    public boolean getUnissued(){
	return report.getUnissued();
    }
    public boolean getRedeem(){
	return report.getRedeem();
    }
    public boolean getParticipate(){
	return report.getParticipate();
    }
    public boolean getInventory(){
	return report.getInventory();
    }
    public boolean getRedeemOld(){
	return report.getRedeemOld();
    }
    public boolean getIssuedNotRedeemed(){
	return report.getIssuedNotRedeemed();
    }		
    public String getTitle(){
	return report.getTitle();
    }	
    public List<ReportRow> getRows(){
	return report.getRows();
    }
    public List<List<ReportRow>> getAll(){
	return report.getAll();
    }
    public List<ReportRow> getInventoryList(){
	return report.getInventoryList();
    }
    public ReportRow getColumnTitle(){
	return report.getColumnTitle();
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





































