/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class FmnpSearch implements java.io.Serializable{

    static final long serialVersionUID = 35L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(FmnpSearch.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    String id="", which_date="date_time", limit= " 30 ";

    String date_from="", date_to="", sortBy="id DESC ";
    String buck_id="",ticket_num="", date_to_2="",
	dispute_resolution="", amount="", type="", status="";
    boolean cancelled = false, active = false;
    List<FmnpWic> wics = null;
    List<FmnpSenior> seniors = null; 
    public FmnpSearch(){
    }	
    public FmnpSearch(boolean val){
	debug = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAmount(String val){
	if(val != null)
	    amount = val;
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
    public void setDispute_resolution(String val){
	if(val != null && !val.equals("-1"))
	    dispute_resolution = val;
    }		
    public void setTicketNum(String val){
	if(val != null)
	    ticket_num = val;
    }
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val;
    }
    public void setStatus(String val){
	if(val != null && !val.equals("-1")){
	    status = val;
	    if(status.equals("Cancelled"))
		cancelled = true;
	    else if(status.equals("Active"))
		active = true;
	}
    }		
    public void setNoLimit(){
	limit = "";
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;
    }
    public void setSortBy(String val){
	if(val != null && !val.equals("-1"))
	    sortBy = val;
    }
    //
    public String getId(){
	return id;
    }
    public String getTicketNum(){
	return ticket_num;
    }		
    public String getBuck_id(){
	return buck_id;
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
    public String getType(){
	if(type.isEmpty())
	    return "-1";
	return type ;
    }
    public String getStatus(){
	if(status.isEmpty())
	    return "-1";
	return status ;
    }		
    public String getSortBy(){
	if(sortBy.equals("")){
	    return "-1";
	}
	return sortBy ;
    }
    public String getDispute_resolution(){
	if(dispute_resolution.equals("")){
	    return "-1";
	}
	return dispute_resolution ;
    }		
    public List<FmnpWic> getWics(){
	return wics;
    }
    public List<FmnpSenior> getSeniors(){
	return seniors;
    }
    //
    public String find(){
	String qq = "select 'wic' as type,w.id, w.ticket_num, w.amount,w.wic_max_amount,w.user_id,date_format(w.date_time,'%m/%d/%Y %H:%i'),w.cancelled,w.dispute_resolution  from fmnp_wics w ";
	String qq2 = "select 'senior' as type,s.id, s.ticket_num, s.amount,s.senior_max_amount,s.user_id,date_format(s.date_time,'%m/%d/%Y %H:%i'),s.cancelled,s.dispute_resolution  from fmnp_seniors s ";
	String qw = "", qw2="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " w.id = ? ";
	    qw2 += " s.id = ? ";						
	}
	else {
	    if(!buck_id.equals("")){
		qq += " left join wic_bucks wb on wb.wic_id=w.id ";
		qq2 += " left join senior_bucks sb on sb.wic_id=s.id ";
		if(!qw.equals("")) qw += " and ";
		if(!qw2.equals("")) qw2 += " and ";								
		qw += " wb.buck_id = ? ";
		qw2 += " sb.buck_id = ? ";								
	    }
	    if(cancelled){
		if(!qw.equals("")) qw += " and ";					
		qw += " w.cancelled is not null ";
		if(!qw2.equals("")) qw2 += " and ";					
		qw2 += " s.cancelled is not null ";								
	    }
	    if(active){
		if(!qw.equals("")) qw += " and ";					
		qw += " w.cancelled is null ";
		if(!qw2.equals("")) qw2 += " and ";					
		qw2 += " s.cancelled is null ";								
	    }
	    if(dispute_resolution.equals("y")){
		if(!qw.equals("")) qw += " and ";					
		qw += " w.dispute_resolution is not null ";
		if(!qw2.equals("")) qw2 += " and ";					
		qw2 += " s.dispute_resolution is not null ";								
	    }
	    else if(dispute_resolution.equals("n")){
		if(!qw.equals("")) qw += " and ";					
		qw += " w.dispute_resolution is null ";
		if(!qw2.equals("")) qw2 += " and ";					
		qw2 += " s.dispute_resolution is null ";								
	    }						
	    if(!amount.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.amount = ? ";
		if(!qw2.equals("")) qw2 += " and ";
		qw2 += " s.amount = ? ";								
	    }
	    if(!ticket_num.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.ticket_num = ? ";
		if(!qw2.equals("")) qw2 += " and ";
		qw2 += " s.ticket_num = ? ";
	    }						
	    if(!which_date.equals("")){
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "w."+which_date+" >= ? ";
		    if(!qw2.equals("")) qw2 += " and ";
		    qw2 += "s."+which_date+" >= ? ";										
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "w."+which_date+" <= ? ";
		    if(!qw2.equals("")) qw2 += " and ";
		    qw2 += "s."+which_date+" <= ? ";										
		}
	    }
	}
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!qw2.equals(""))
	    qq2 += " where "+qw2;				
	boolean both = true;
	if(type.isEmpty()){
	    qq = qq+" union "+qq2;
	}
	else if(type.equals("wic")){
	    both = false;
	    if(!sortBy.equals("")){
		qq += " order by w."+sortBy;
	    }
	}
	else {
	    qq = qq2;
	    both = false;
	    if(!sortBy.equals("")){
		qq += " order by s."+sortBy;
	    }
	}
	if(!limit.equals("")){
	    qq += " limit "+limit;
	}
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
		if(both)
		    pstmt.setString(jj++,id);										
	    }
	    else{
		if(!buck_id.equals("")){
		    pstmt.setString(jj++,buck_id);
		}				
		if(!amount.equals("")){
		    pstmt.setString(jj++,amount);
		}
		if(!ticket_num.equals("")){
		    pstmt.setString(jj++,ticket_num);
		}
		if(!which_date.equals("")){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setTimestamp(jj++, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    }
		}
		if(both){
		    if(!buck_id.equals("")){
			pstmt.setString(jj++,buck_id);
		    }				
		    if(!amount.equals("")){
			pstmt.setString(jj++,amount);
		    }
		    if(!ticket_num.equals("")){
			pstmt.setString(jj++,ticket_num);
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
	    }
	    wics = new ArrayList<>();
	    seniors = new ArrayList<>();
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String type = rs.getString(1);
		if(type.equals("wic")){
		    FmnpWic one = new FmnpWic(debug,
					      rs.getString(2),
					      rs.getString(3),
					      rs.getString(4),
					      rs.getString(5),
					      rs.getString(6),
					      rs.getString(7),
					      rs.getString(8),
					      rs.getString(9)
					      );
		    wics.add(one);
		}
		else{
		    FmnpSenior one = new FmnpSenior(debug,
						    rs.getString(2),
						    rs.getString(3),
						    rs.getString(4),
						    rs.getString(5),
						    rs.getString(6),
						    rs.getString(7),
						    rs.getString(8),
						    rs.getString(9)
						    );
		    seniors.add(one);
		}
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





































