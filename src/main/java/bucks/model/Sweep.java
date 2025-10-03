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

public class Sweep implements java.io.Serializable{

    static final long serialVersionUID = 15L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Sweep.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id=""; 
    String sweep_name ="", user_id="",date_time="";
    List<Buck> bucks = null;
    User user = null;
    public Sweep(){
    }	
    public Sweep(boolean val){
	debug = val;
    }
    public Sweep(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Sweep(boolean deb,
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
	setSweep_name(val4);
    }
				 

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setSweep_name(String val){
	if(val != null)
	    sweep_name = val;
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
    public String getSweep_name(){
	return sweep_name;
    }
    public String getDate_time(){
		
	return date_time;
    }
    public String getUser_id(){
		
	return user_id;
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
	return id+" "+sweep_name+" "+date_time;
    }
    public List<Buck> getBucks(){
	if(!id.equals("") && bucks == null){
	    BuckList bl = new BuckList(debug, null, null, null, null, id);
	    String back = bl.find();
	    if(back.equals("")){
		bucks = bl.getBucks();
	    }
	    else{
		logger.error(back);
	    }
	}
	return bucks;
    }
    public void setBucks(List<Buck> vals){
	if(vals != null){
	    bucks = vals;
	}
    }
    public String addBucksToSweep(){
	String msg = "";
	if(id.equals("")){
	    msg = "Sweep id not set ";
	    return msg;
	}
	if(bucks == null){
	    msg = "no bucks set for Redeem ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into sweep_bucks values(?,?)";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    for(Buck buck:bucks){
		pstmt.setString(1, id);
		pstmt.setString(2, buck.getId());
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
	String qq = "insert into sweeps values(0,now(),?,?)";
	if(debug)
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
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
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
	    if(!sweep_name.equals(""))
		pstmt.setString(c++, sweep_name);
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
    public String updateSweepName(){
	//
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(sweep_name.equals("")){
	    msg = " New World sweep name not set ";
	    return msg;
	}
	String qq = "update sweeps set sweep_name=? where id=?";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, sweep_name);
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
	return msg;
			
    }
    public String doSelect(){
		
	String qq = "select s.id, date_format(s.date_time,'%m/%d/%Y %H:%i'),s.user_id,s.sweep_name from sweeps s where s.id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(debug)
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





































