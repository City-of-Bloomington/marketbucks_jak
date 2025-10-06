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

public class SnapList implements java.io.Serializable{

    static final long serialVersionUID = 33L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(SnapList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    String id="", limit="30" ;
    String card_number="", authorization="", amount="";
    String date_from="", date_to="", date_to_2="", sortBy="b.date DESC ";
    boolean today_date = false, active_only=false, inactive_only=false;
    boolean included_only = false, not_included_only = false;
    String status="", doubleRequest="";
    List<Snap> snaps = null;
	
    public SnapList(){
    }	
    public SnapList(boolean val){
	debug = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAuthorization(String val){
	if(val != null)
	    authorization = val;
    }	
    public void setCardNumber(String val){
	if(val != null)
	    card_number = val;
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
    public void setStatus(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Active")) setActiveOnly();
	    else if(val.equals("Cancelled")) setInactiveOnly();
	    status = val;
	}
    }
    public void setDoubleRequest(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("Included")) setIncludedOnly();
	    else if(val.equals("Not Included")) setNotIncludedOnly();
	    doubleRequest = val;
	}
    }    
    public String getStatus(){
	if(status.isEmpty()) return "-1";
	return status;
    }
    public String getDoubleRequest(){
	if(doubleRequest.isEmpty()) return "-1";
	return doubleRequest;
    }    
    public void setAmount(String val){
	if(val != null)
	    amount = val;
    }    
    public void setSortBy(String val){
	if(val != null &&  !val.equals("-1"))
	    sortBy = val;
    }
    public void setNoLimit(){
	limit = "";
    }
    //
    public String getId(){
	return id;
    }
    public String getCardNumber(){
	return card_number;
    }
    public String getAuthorization(){
	return authorization;
    }
    public String getAmount(){
	return amount;
    }	
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getSortBy(){
	if(sortBy.equals("")){
	    return "-1";
	}
	return sortBy ;
    }
    public void setTodayDate(){
	today_date = true;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setInactiveOnly(){
	inactive_only = true;
    }    
    public void setIncludedOnly(){
	included_only = true;
    }
    public void setNotIncludedOnly(){
	not_included_only = true;
    }    
    public List<Snap> getSnaps(){
	return snaps;
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;
    }
    //
    public String find(){

	String qq = "select b.id, date_format(b.date,'%m/%d/%Y'),"+
	    "date_format(b.date,'%H:%i'),"+
	    "b.card_number,b.authorization,b.snap_amount,b.ebt_amount,b.dbl_amount,b.dbl_max,b.include_double,b.user_id,b.cancelled from snap_purchases b ";
	
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " b.id = ? ";
	}
	else {
	    if(active_only){
		qw += " b.cancelled is null ";
	    }
	    else if(inactive_only){
		qw += " b.cancelled is not null ";
	    }
	    if(included_only){
		if(!qw.equals("")) qw += " and ";				
		qw += " b.include_double is not null ";
	    }
	    else if(not_included_only){
		if(!qw.equals("")) qw += " and ";				
		qw += " b.include_double is null ";
	    }
	    if(!card_number.equals("")){
		if(!qw.equals("")) qw += " and ";				
		qw += " b.card_mumber = ? ";
	    }
	    if(!authorization.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.authorization = ? ";
	    }
	    if(today_date){
		// add today date 
		if(!qw.equals("")) qw += " and ";
		qw += " date(b.date) = curdate() ";
	    }
	    else{
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "date(b.date) >= ? ";					
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "date(b.date) <= ? ";					
		}
	    }
	}
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	if(!limit.equals("")){
	    qq += " limit "+limit;
	}
	//
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
		if(!card_number.equals("")){
		    pstmt.setString(jj++,card_number);				
		}
		if(!authorization.equals("")){
		    pstmt.setString(jj++,authorization);					
		}
		if(!today_date){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dfTime.parse(date_to_2).getTime()));
		    }
		}
	    }
	    snaps = new ArrayList<>();			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Snap one = new Snap(debug,
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
				    rs.getString(12)
				    
				    );
		snaps.add(one);
	    }
	}catch(Exception e){
	    msg += e+": "+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
}





































