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

public class DisputeList implements java.io.Serializable{

    static final long serialVersionUID = 37L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(DisputeList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    String id="", which_date="r.date_time", redeem_id="", status="", reason="";

    String date_from="", date_to="", date_to_2="", sortBy="r.id DESC",
	buck_id="", limit="";
    boolean unresolved = false; // all
    List<Dispute> disputes = null;
	
    public DisputeList(){
    }	
    public DisputeList(boolean val){
	debug = val;
    }
    public DisputeList(boolean val, String val2){
	debug = val;
	setRedeem_id(val2);
    }	
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRedeem_id(String val){
	if(val != null)
	    redeem_id = val;
    }
    public void setBuck_id(String val){
	if(val != null)
	    buck_id = val;
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
    public void setReason(String val){
	if(val != null && !val.equals("-1"))
	    reason = val;
    }
    public void setUnresolved(){
	unresolved = true;
    }	
    //
    public String getId(){
	return id;
    }
    public String getRedeem_id(){
	return redeem_id;
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
	    return "-1"; // for all
	}
	return status;
    }
    public String getReason(){
	if(reason.equals("")){
	    return "-1"; // for all
	}
	return reason;
    }	
    public List<Dispute> getDisputes(){
	return disputes;
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;

    }		
    //
    public String find(){

	String qq = "select r.id, r.redeem_id,r.buck_id,r.status,r.reason,r.user_id,date_format(r.date_time,'%m/%d/%Y %H:%i'),r.suggestions,r.notes  ";		
	String qf = " from disputes r ";
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
	    if(unresolved){
		if(!qw.equals("")) qw += " and ";
		qw += " r.status != 'Resolved' ";
	    }
	    if(!redeem_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.redeem_id = ? ";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.status = ? ";
	    }
	    if(!reason.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.reason = ? ";
	    }			
	    if(!buck_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.buck_id = ? ";
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
		if(!redeem_id.equals("")){
		    pstmt.setString(jj++,redeem_id);
		}
		if(!status.equals("")){
		    pstmt.setString(jj++,status);
		}
		if(!reason.equals("")){
		    pstmt.setString(jj++,reason);
		}				
		if(!buck_id.equals("")){
		    pstmt.setString(jj++,buck_id);
		}				
		if(!which_date.equals("")){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setTimestamp(jj++, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    disputes = new ArrayList<Dispute>();
	    while(rs.next()){
		Dispute one = new Dispute(debug,
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4),
					  rs.getString(5),
					  rs.getString(6),
					  rs.getString(7),
					  rs.getString(8),
					  rs.getString(9)
					  );
		disputes.add(one);
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





































