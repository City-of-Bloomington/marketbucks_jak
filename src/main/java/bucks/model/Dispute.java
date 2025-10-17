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

public class Dispute implements java.io.Serializable{

    static final long serialVersionUID = 17L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Dispute.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", suggestions="", notes=""; 
    String redeem_id ="", buck_id="", user_id="",date_time="", status="Waiting", reason="";
    Buck buck = null;
    Redeem redeem = null;
    User user = null;
    List<Dispute> disputes = null;
    Resolution resolution = null;
    public Dispute(){
    }	
    public Dispute(boolean val){
	debug = val;
    }
    public Dispute(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Dispute(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9
		   ){
	setValues( val,
		   val2,
		   val3,
		   val4,
		   val5,
		   val6,
		   val7,
		   val8,
		   val9
		   );
	debug = deb;
		
    }
    void setValues(
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9
				   
		   ){
	setId(val);
	setRedeem_id(val2);
	setBuck_id(val3);
	setDisputeStatus(val4);
	setReason(val5);
	setUser_id(val6);		
	setDate_time(val7);
	setSuggestions(val8);
	setNotes(val9);
    }
				 

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setBuck_id(String val){
	if(val != null)
	    buck_id = val;
    }	
    public void setRedeem_id(String val){
	if(val != null)
	    redeem_id = val;
    }
    public void setDisputeStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }    
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }	
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setReason(String val){
	if(val != null)
	    reason = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }	
    public void setSuggestions(String val){
	if(val != null)
	    suggestions = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getRedeem_id(){
	return redeem_id;
    }
    public String getBuck_id(){
	return buck_id;
    }	
    public String getDate_time(){
		
	return date_time;
    }
    public String getUser_id(){
		
	return user_id;
    }
    public String getDisputeStatus(){
	return status;
    }
    public String getStatus(){
	return status;
    }    
    public String getReason(){
	return reason;
    }
    public String getSuggestions(){
	return suggestions;
    }
    public String getNotes(){
	return notes;
    }
    public String getResolution_id(){
	if(hasResolution()){
	    return resolution.getId();
	}
	return "";
    }
    public boolean hasNotes(){
	return !notes.equals("");
    }
    public boolean canEdit(){
	return id.equals("") || !status.equals("Resolved");
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
    public Resolution getResolution(){
	if(resolution == null && !id.equals("")){
	    ResolutionList rl = new ResolutionList(debug);
	    rl.setDispute_id(id);
	    String back = rl.find();
	    if(back.equals("")){
		List<Resolution> list = rl.getResolutions();
		if(list != null && list.size() > 0){
		    resolution = list.get(0);
		}
	    }
	}
	return resolution;
    }
    public boolean hasResolution(){
	getResolution();
	return resolution != null;
    }
    public Buck getBuck(){
	if(!buck_id.equals("") && buck == null){
	    Buck one = new Buck(debug, buck_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		buck = one;
	    }
	}
	return buck;
    }
    public Redeem getRedeem(){
	if(redeem == null && !redeem_id.equals("")){
	    redeem = new Redeem(debug, redeem_id);
	    String back = redeem.doSelect();
	}
	return redeem;
    }
    public List<Dispute> getDisputes(){
	if(!redeem_id.equals("")){
	    DisputeList dl = new DisputeList(debug);
	    dl.setRedeem_id(redeem_id);
	    String back = dl.find();
	    if(back.equals("")){
		List<Dispute> list = dl.getDisputes();
		if(list != null && list.size() > 0){
		    disputes = list;
		}
	    }
	}
	return disputes;
    }
    public String toString(){
	return id+" "+redeem_id+" "+buck_id;
    }
    public boolean isResolved(){
	return status.equals("Resolved");
    }
    public boolean isWaiting(){
	return status.equals("Waiting");
    }    
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null;
	ResultSet rs = null;
	String msg = "";
	if(redeem_id.equals("") || buck_id.equals("")){
	    msg = "Redeem ID, or Buck ID not set";
	    return msg;
	}
	date_time = Helper.getToday();
	String qq = "insert into disputes values(0,?,?,?,?,?,now(),?,null)";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, redeem_id);
	    pstmt.setString(2, buck_id);
	    pstmt.setString(3, status);
	    if(!reason.equals(""))
		pstmt.setString(4, reason);
	    else
		pstmt.setNull(4, Types.VARCHAR);				
	    if(!user_id.equals(""))
		pstmt.setString(5, user_id);
	    else
		pstmt.setNull(5, Types.VARCHAR);
	    if(!suggestions.equals(""))
		pstmt.setString(6, suggestions);
	    else
		pstmt.setNull(6, Types.VARCHAR);				
	    pstmt.executeUpdate();
	    //
	    qq = "select LAST_INSERT_ID() ";
	    logger.debug(qq);
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }			
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return msg;
    }
    //
    // 
    //
    public String doUpdate(){
	//
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update disputes set status=?,user_id=?,date_time=now(),notes=? where id=?";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, status);
	    if(!user_id.equals(""))
		pstmt.setString(2, user_id);
	    else
		pstmt.setNull(2, Types.VARCHAR);
	    if(!notes.equals(""))
		pstmt.setString(3, notes);
	    else
		pstmt.setNull(3, Types.VARCHAR);			
	    pstmt.setString(4, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.equals("")){
	    doSelect();
	}
	return msg;
			
    }
    public String doDelete(){
	//
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	// if there is a resolution we need to delete it first
	// for dependency issue
	//
	if(hasResolution()){
	    msg = resolution.doDelete();
	    if(!msg.equals("")){
		return msg;
	    }
	}
	String qq = "delete from disputes where id=?";
	//
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
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
    public String doRefresh(){
	String msg = "";
		
	return msg;
    }
    public String doSelect(){
		
	String qq = "select r.id,r.redeem_id,r.buck_id,r.status,r.reason,r.user_id, date_format(r.date_time,'%m/%d/%Y %H:%i'),r.suggestions,r.notes from disputes r where r.id=? ";
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
			  rs.getString(4),
			  rs.getString(5),
			  rs.getString(6),
			  rs.getString(7),
			  rs.getString(8),
			  rs.getString(9)
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
    /**
     * check if certain buck_id is in disputed list
     */
    public boolean isInDispute(){
	String qq = "select count(*) from disputes where buck_id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	boolean found = false;
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		return found;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, buck_id);
	    rs = pstmt.executeQuery();	
	    if(rs.next()){
		if(rs.getInt(1) > 0){
		    found = true;
		}
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return found;
    }

}





































