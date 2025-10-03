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

public class EbtList implements java.io.Serializable{

    static final long serialVersionUID = 33L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(EbtList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    String id="", which_date="e.date_time", limit="30" ;
    String card_last_4="", approve="", amount="", buck_id="";
    String date_from="", date_to="", date_to_2="", sortBy="e.id DESC ";
    String cancelled = "", dispute_resolution=""; // for all
    String exclude_id = "";
    boolean today_date = false;
    List<Ebt> ebts = null;
	
    public EbtList(){
    }	
    public EbtList(boolean val){
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
    public void setCard_last_4(String val){
	if(val != null)
	    card_last_4 = val;
    }
    public void setApprove(String val){
	if(val != null)
	    approve = val;
    }	
    public void setAmount(String val){
	if(val != null)
	    amount = val;
    }
    public void setCancelled(String val){
	if(val != null && !val.equals("-1"))
	    cancelled = val;
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
    public void excludeId(String val){
	if(val != null)
	    exclude_id = val;
    }		
    public void setSortBy(String val){
	if(val != null &&  !val.equals("-1"))
	    sortBy = val;
    }
    public void setDispute_resolution(String val){
	if(val != null &&  !val.equals("-1"))
	    dispute_resolution = val;
    }
    public void setNoLimit(){
	limit = "";
    }
    //
    public String getId(){
	return id;
    }
    public String getBuck_id(){
	return buck_id;
    }	
    public String getCard_last_4(){
	return card_last_4;
    }
    public String getApprove(){
	return approve;
    }
    public String getAmount(){
	return amount;
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
	if(sortBy.equals("")){
	    return "-1";
	}
	return sortBy ;
    }
    public String getCancelled(){
	if(cancelled.equals("")){
	    return "-1";
	}
	return cancelled;
    }
    public String getDispute_resolution(){
	if(dispute_resolution.equals("")){
	    return "-1";
	}
	return dispute_resolution;
    }
    public void setTodayDate(){
	today_date = true;
    }
    public List<Ebt> getEbts(){
	return ebts;
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;
    }
    //
    public String find(){

	String qq = "select e.id, e.amount,e.approve,e.card_last_4,e.user_id,date_format(e.date_time,'%m/%d/%Y %H:%i'),e.dmb_amount,e.cancelled,e.ebt_donor_max,e.ebt_buck_value,e.dispute_resolution,e.include_double ";		
	String qf = " from ebts e ";
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " e.id = ? ";
	}
	else {
	    if(!buck_id.equals("")){
		qf += " left join ebt_bucks eb on eb.ebt_id=e.id ";
		if(!qw.equals("")) qw += " and ";				
		qw += " eb.buck_id = ? ";				
	    }
	    if(cancelled.equals("y")){
		if(!qw.equals("")) qw += " and ";					
		qw += " e.cancelled is not null ";
	    }
	    else if(cancelled.equals("n")){
		if(!qw.equals("")) qw += " and ";					
		qw += " e.cancelled is null ";
	    }
	    if(dispute_resolution.equals("y")){
		if(!qw.equals("")) qw += " and ";					
		qw += " e.dispute_resolution is not null ";
	    }
	    else if(dispute_resolution.equals("n")){
		if(!qw.equals("")) qw += " and ";					
		qw += " e.dispute_resolution is null ";
	    }						
	    if(!card_last_4.equals("")){
		if(!qw.equals("")) qw += " and ";				
		qw += " e.card_last_4 = ? ";
	    }
	    if(!approve.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.approve = ? ";
	    }
	    if(!amount.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.amount = ? ";
	    }
	    if(!exclude_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.id <> ? ";
	    }						
	    if(today_date){
		// add today date 
		if(!qw.equals("")) qw += " and ";
		qw += " date(e.date_time) = curdate() ";								
	    }
	    else if(!which_date.equals("")){
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
		if(!buck_id.equals("")){
		    pstmt.setString(jj++,buck_id);
		}				
		if(!card_last_4.equals("")){
		    pstmt.setString(jj++,card_last_4);				
		}
		if(!approve.equals("")){
		    pstmt.setString(jj++,approve);					
		}
		if(!amount.equals("")){
		    pstmt.setString(jj++,amount);
		}
		if(!exclude_id.equals("")){
		    pstmt.setString(jj++, exclude_id);
		}								
		if(!today_date && !which_date.equals("")){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setTimestamp(jj++, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    }
		}
	    }
	    ebts = new ArrayList<Ebt>();			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Ebt one = new Ebt(debug,
				  rs.getString(1),
				  rs.getInt(2),
				  rs.getString(3),
				  rs.getString(4),
				  rs.getString(5),
				  rs.getString(6),
				  rs.getInt(7),
				  rs.getString(8),
				  rs.getInt(9),
				  rs.getInt(10),
				  rs.getString(11),
				  rs.getString(12) 
				  );
		ebts.add(one);
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





































