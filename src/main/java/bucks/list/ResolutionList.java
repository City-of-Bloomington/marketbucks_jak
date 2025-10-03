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

public class ResolutionList implements java.io.Serializable{

    static final long serialVersionUID = 235L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(ResolutionList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		
    String id="", which_date="r.date_time", vendor_id="", status="",
	dispute_id="";
    String date_from="", date_to="", date_to_2="", sortBy="r.id DESC",
	redeem_id="";
    List<Resolution> resolutions = null;
	
    public ResolutionList(){
    }	
    public ResolutionList(boolean val){
	debug = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setDispute_id(String val){
	if(val != null)
	    dispute_id = val;
    }	
    public void setRedeem_id(String val){
	if(val != null)
	    redeem_id = val;
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
	if(val != null)
	    sortBy = val;
    }
    public void setStatus(String val){
	if(val != null && !val.equals("-1"))
	    status = val;
    }
    //
    public String getId(){
	return id;
    }
    public String getRedeem_id(){
	return redeem_id;
    }
    public String getDispute_id(){
	return dispute_id;
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
    public String getStatus(){
	if(status.equals("")){
	    return "-1";
	}
	return status;
    }	
    public List<Resolution> getResolutions(){
	return resolutions;
    }
    //
    public String find(){

	String qq = "select r.id, r.dispute_id,r.conf_id,r.value,r.approve,r.card_last_4,r.pay_type,r.check_no,date_format(r.expire_date,'%m/%d/%Y'),r.user_id,date_format(r.date_time,'%m/%d/%Y %H:%i'),r.status,r.new_buck_id  ";		
	String qf = " from resolutions r ";
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
	    if(!dispute_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.dispute_id = ? ";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.status = ? ";
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
	qq += " limit 10 ";		
	logger.debug(qq);
	// System.err.println(qq);
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
		if(!dispute_id.equals("")){
		    pstmt.setString(jj++,dispute_id);
		}
		if(!status.equals("")){
		    pstmt.setString(jj++,status);
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
	    resolutions = new ArrayList<Resolution>();
	    while(rs.next()){
		Resolution one = new Resolution(debug,
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13)
						);
		resolutions.add(one);
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





































