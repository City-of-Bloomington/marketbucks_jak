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

public class BuckConfList implements java.io.Serializable{

    static final long serialVersionUID = 32L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(BuckConfList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	

    String id="", type_id="", date_from="", date_to="", sortBy="b.id DESC";
    boolean excludeOldYears = false;
    List<BuckConf> buckConfs = null;
	
    public BuckConfList(){
    }	
    public BuckConfList(boolean val){
	debug = val;
    }

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setType_id(String val){
	if(val != null)
	    type_id = val;
    }	
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
	
    //
    public String getId(){
	return id;
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
    public void setExcludeOldYears(){
	excludeOldYears = true;		
    }
    public List<BuckConf> getBuckConfs(){
	return buckConfs;
    }
    //
    public String find(){

	String qq = "select b.id, b.value ,b.type_id,date_format(b.date,'%m/%d/%Y'),b.donor_max_value,b.user_id,b.name,year(b.date)-year(curdate()),t.name,b.gl_account ";		
	String qf = " from buck_confs b left join buck_types t on t.id=b.type_id";
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
	    if(!type_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.type_id = ? ";
	    }
	    if(!date_from.equals("")){
		qw += "b.date >= ? ";					
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")) qw += " and ";				
		qw += "b.date <= ? ";					
	    }
	}
	if(excludeOldYears){
	    if(!qw.equals("")) qw += " and ";
	    qw += " year(b.date) >= (year(curdate()) - 5) "; 
	}
	qq += qf;
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	qq += " limit 20 ";		
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
		if(!type_id.equals("")){
		    pstmt.setString(jj++, type_id);
		}
		if(!date_from.equals("")){
		    pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		}
		if(!date_to.equals("")){
		    pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));
		}
	    }
	    rs = pstmt.executeQuery();
	    buckConfs = new ArrayList<BuckConf>();
	    while(rs.next()){
		BuckConf one = new BuckConf(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7),
					    (rs.getInt(8) >= 0),
					    rs.getString(9),
					    rs.getString(10)
					    );
		buckConfs.add(one);
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	    System.err.println(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
    //
    public String findLatest(){

	String qq = "select b.id, b.value ,b.type_id,date_format(b.date,'%m/%d/%Y'),b.donor_max_value,b.user_id,b.name,year(b.date)-year(curdate()),t.name,b.gl_account ";		
	String qf = " from buck_confs b left join buck_types t on t.id=b.type_id";
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!type_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " b.type_id = ? ";
	}
	qq += qf;
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!sortBy.equals("")){
	    qq += " order by b.id desc ";
	}
	qq += " limit 1 ";		
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!type_id.equals("")){
		pstmt.setString(jj++, type_id);
	    }
	    rs = pstmt.executeQuery();
	    buckConfs = new ArrayList<>();
	    while(rs.next()){
		BuckConf one = new BuckConf(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7),
					    (rs.getInt(8) >= 0),
					    rs.getString(9),
					    rs.getString(10)
					    );
		buckConfs.add(one);
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	    System.err.println(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
		
}





































