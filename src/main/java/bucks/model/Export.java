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

public class Export implements java.io.Serializable{

    static final long serialVersionUID = 14L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Export.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", status="Open"; 
    String nw_batch_name ="", user_id="",date_time="";
    List<Redeem> redeems = null;
    User user = null;
    public Export(){
    }	
    public Export(boolean val){
	debug = val;
    }
    public Export(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Export(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4
		  ){
	setValues( val,
		   val2,
		   val3,
		   val4
		   );
	debug = deb;
		
    }
    void setValues(
		   String val,
		   String val2,
		   String val3,
		   String val4
		   ){
	setId(val);
	setDate_time(val2);
	setUser_id(val3);
	setNw_batch_name(val4);
    }
				 

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setNw_batch_name(String val){
	if(val != null)
	    nw_batch_name = val;
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }	
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
   
    //
    public String getId(){
	return id;
    }
    public String getNw_batch_name(){
	return nw_batch_name;
    }
    public String getDate_time(){
		
	return date_time;
    }
    public String getUser_id(){
		
	return user_id;
    }
    public String getTotal(){
	int total = 0;
	if(redeems == null){
	    getRedeems();
	}
	if(redeems != null){
	    for(Redeem one:redeems){
		total += one.getTotalInt();
	    }
	}
	return ""+total;
    }
    public String getStatus(){
	if(!nw_batch_name.equals("")){
	    status = "Completed";
	}
	return status;
    }
    public boolean isOpen(){
	return getStatus().equals("Open");
    }
    public boolean hasDisputes(){
	if(redeems == null){
	    getRedeems();
	}
	if(redeems != null){
	    for(Redeem one:redeems){
		if(one.hasDisputes()){
		    return true;
		}
	    }
	}
	return false;
    }
    public User getUser(){
	if(!user_id.equals("") && user == null){
	    User one = new User(debug, null, user_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		user = one;
	    }
	}
	return user;

    }
    public String toString(){
	return id+" "+nw_batch_name+" "+" "+date_time;
    }
    public void setRedeems(List<Redeem> vals){
	if(vals != null)
	    redeems = vals;
    }
    public List<Redeem> getRedeems(){
	if(redeems == null){
	    RedeemList rl = new RedeemList(debug);
	    if(!id.equals("")){
		rl.setExport_id(id);
	    }
	    else{
		rl.setNotEportedYet();
		rl.setStatus("Completed");
	    }
	    rl.setSortBy("v.lname");
	    String back = rl.find();
	    if(back.equals("")){
		redeems = rl.getRedeems();
	    }
	    else{
		logger.error(back);
	    }
	}
	return redeems;
    }
    public String addRedeemsToExport(){
	String msg = "";
	if(id.equals("")){
	    msg = "Export id not set ";
	    return msg;
	}
	if(redeems == null){
	    msg = "No redeems to Export ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into export_redeems values(?,?)";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    for(Redeem one:redeems){
		pstmt.setString(1, id);
		pstmt.setString(2, one.getId());
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			

    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	date_time = Helper.getToday();
	String qq = "insert into exports values(0,now(),?,?)";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    fillData(pstmt, 1);
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
	    //
	    qq = "select LAST_INSERT_ID() ";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }			
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    String fillData(PreparedStatement pstmt, int c){
	String msg="";
	try{
	    if(!user_id.equals(""))
		pstmt.setString(c++, user_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);						
	    if(!nw_batch_name.equals(""))
		pstmt.setString(c++, nw_batch_name);
	    else
		pstmt.setNull(c++, Types.VARCHAR);

	}
	catch(Exception ex){
	    msg += ex;
	    logger.error(msg);
	}
		
	return msg;					
    }
    //
    // update the fund_type only
    //
    public String updateNwBatchName(){
	//
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	if(nw_batch_name.equals("")){
	    msg = " New world batch name not set ";
	    return msg;
	}
	String qq = "update exports set nw_batch_name=? where id=?";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, nw_batch_name);
	    pstmt.setString(2, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	msg = doSelect();
	return msg;
			
    }
    public String doSelect(){
		
	String qq = "select x.id, date_format(x.date_time,'%m/%d/%Y %H:%i'),x.user_id,x.nw_batch_name from exports x where x.id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();	
	    if(rs.next()){
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4)
			  );
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





































