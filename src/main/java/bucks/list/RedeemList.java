/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.utils.*;

public class RedeemList implements java.io.Serializable{

    static final long serialVersionUID = 35L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(RedeemList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		
    String id="", which_date="r.date_time", vendor_id="", status="", payType="";

    String date_from="", date_to="", date_to_2="", sortBy="r.id DESC", export_id="", vendor_num = "",
	export="", buck_id="", limit="";
    boolean not_exported = false, exported=false;
    List<Redeem> redeems = null;
	
    public RedeemList(){
    }	
    public RedeemList(boolean val){
	debug = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setBuck_id(String val){
	if(val != null)
	    buck_id = val;
    }	
    public void setVendor_id(String val){
	if(val != null && !val.equals("-1"))
	    vendor_id = val;
    }
    public void setVendor_num(String val){
	if(val != null)
	    vendor_num = val;
    }    
    public void setExport_id(String val){
	if(val != null)
	    export_id = val;
    }	
    public void setWhich_date(String val){
	if(val != null)
	    which_date = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null){
	    date_to = val;
	    date_to_2 = val+" 23:59";
	}
    }
    public void setSortBy(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))
	    sortBy = val;
    }
    public void setPayType(String val){
	if(val != null && !val.equals("-1"))
	    payType = val;
    }	
    public void setStatus(String val){
	if(val != null && !val.equals("-1"))
	    status = val;
    }
    public void setNotEportedYet(){
	not_exported = true;
    }
    public void setExport(String val){
	if(!val.equals("-1")){
	    if(val.equals("exported"))
		exported = true;
	    else if(val.startsWith("not"))
		not_exported = true;
	    export = val;
	}
    }	
    //
    public String getId(){
	return id;
    }
    public String getBuck_id(){
	return buck_id;
    }	
    public String getVendor_id(){
	return vendor_id;
    }
    public String getVendor_num(){
	return vendor_num;
    }	    
    public String getWhich_date(){
	return which_date;
    }
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getSortBy(){
	return sortBy ;
    }
    public String getPayType(){
	if(payType.equals("")){
	    return "-1"; // for all;
	}
	return payType ;
    }
    public String getExport(){
	if(export.equals("")){
	    return "-1"; // for all;
	}
	return export ;
    }	
    public String getStatus(){
	if(status.equals("")){
	    return "-1";
	}
	return status;
    }	
    public List<Redeem> getRedeems(){
	return redeems;
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;
    }
    //
    public String find(){

	String qq = "select r.id, date_format(r.date_time,'%m/%d/%Y %H:%i'),r.vendor_id,r.user_id,v.lname,v.fname,v.payType,r.status,r.notes,v.vendor_num  ";		
	String qf = " from redeems r join vendors v on v.id=r.vendor_id ";
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " r.id = ? ";
	}
	else {
	    if(!buck_id.equals("")){
		qf += " left join redeem_bucks rb on r.id=rb.redeem_id ";
		if(!qw.equals("")) qw += " and ";
		qw += " rb.buck_id = ? ";
	    }
	    if(!vendor_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.vendor_id = ? ";
	    }
	    else if(!vendor_num.isEmpty()){
		qf += " left join vendors v on r.vendor_id=v.id ";
		if(!qw.equals("")) qw += " and ";
		qw += " v.vendor_num = ? ";
		
	    }
	    if(!payType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " v.payType = ? ";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.status = ? ";
	    }			
	    if(!export_id.equals("")){
		qf += " join export_redeems x on x.redeem_id=r.id ";
		if(!qw.equals("")) qw += " and ";
		qw += " x.export_id = ? ";
	    }
	    if(not_exported){
		if(!qw.equals("")) qw += " and ";				
		qw += " r.id not in (select redeem_id from export_redeems) ";
	    }
	    else if(exported){
		if(!qw.equals("")) qw += " and ";				
		qw += " r.id in (select redeem_id from export_redeems) ";
	    }
	    if(!which_date.equals("")){
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";					
		    qw += which_date+" >= ? ";					
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += which_date+" <= ? ";					
		}
	    }
	}
	qq += qf;
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	if(!limit.equals("")){
	    qq += " limit "+limit;
	}
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }
	    else{
		if(!buck_id.equals("")){
		    pstmt.setString(jj++,buck_id);
		}
		if(!vendor_id.equals("")){
		    pstmt.setString(jj++,vendor_id);
		}
		else if(!vendor_num.isEmpty()){
		    pstmt.setString(jj++,vendor_num);
		}
		if(!payType.equals("")){
		    pstmt.setString(jj++,payType);
		}
		if(!status.equals("")){
		    pstmt.setString(jj++,status);
		}				
		if(!export_id.equals("")){
		    pstmt.setString(jj++,export_id);
		}				
		if(!which_date.equals("")){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dfTime.parse(date_to_2).getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    redeems = new ArrayList<Redeem>();
	    while(rs.next()){
		Redeem one = new Redeem(debug,
					rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10)
					);
		redeems.add(one);
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
}





































