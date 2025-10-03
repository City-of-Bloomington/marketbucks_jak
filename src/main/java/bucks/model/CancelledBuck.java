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

public class CancelledBuck implements java.io.Serializable{

    static final long serialVersionUID = 13L;	
    boolean debug = false;
    static Logger logger = LogManager.getLogger(CancelledBuck.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", user_id="", date_time="";
    User user = null;
    Buck buck = null;
    public CancelledBuck(){
    }	
    public CancelledBuck(boolean val){
	debug = val;
    }
    public CancelledBuck(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public CancelledBuck(boolean deb,
			 String val,
			 String val2
			 ){
	setId(val);
	setUser_id(val2);
	debug = deb;
		
    }	
    public CancelledBuck(boolean deb,
			 String val,
			 String val2,
			 String val3
			 ){
	setValues( val,
		   val2,
		   val3
		   );
	debug = deb;
		
    }
	
    void setValues(
		   String val,
		   String val2,
		   String val3
		   ){
	setId(val);
	setUser_id(val2);
	setDate_time(val3);
    }
				 

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }
    //
    public String getId(){
	return id;
    }
    public String getUser_id(){
	return user_id;
    }
    public String getDate_time(){
	return date_time;
    }
    @Override
    public int hashCode() {
	int hash = 3, id_int = 0;
	if(!id.equals("")){
	    try{
		id_int = Integer.parseInt(id);
	    }catch(Exception ex){}
	}
	hash = 53 * hash + id_int;
	return hash;
    }
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final CancelledBuck other = (CancelledBuck) obj;
	return this.id.equals(other.id);
    }
    public Buck getBuck(){
	if(buck == null && !id.equals("")){
	    Buck one = new Buck(debug, id);
	    String back = one.doSelect();
	    if(back.equals("")){
		buck = one;
	    }
	}
	return buck;
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
	return id;
    }

    /**
     * bucks are saved in the batch class in a group
     */
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = "select count(*) from cancelled_bucks where id=?";
	String qq2 = "insert into cancelled_bucks values(?,?,null)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    //
	    // check if it is not cancelled before
	    //
	    pstmt = con.prepareStatement(qq);
	    int cnt = 0;
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(cnt == 0){
		pstmt = con.prepareStatement(qq2);
		int c=1;
		pstmt.setString(c++, id);
		pstmt.setString(c++, user_id);
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
	if(msg.equals("")){
	    msg = doSelect();
	}
	return msg;
    }
	

    public String insertBucks(List<String> seq_list){

	if(seq_list == null || seq_list.size() < 1){
	    return "No bucks list to add ";
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
			
	String qq = "insert into cancelled_bucks values(?,?,null)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    for(String one:seq_list){
		pstmt.setString(1, one);
		pstmt.setString(2, user_id);
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
    //
    // 
    //
    public String doSelect(){
	// need fix
	String qq = "select b.id, b.user_id,date_format(b.date_time,'%m/%d/%Y %H:%i') from cancelled_bucks b where b.id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(id.equals("")){
	    msg = "Buck id not set";
	    return msg;
	}
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
			  rs.getString(3)
			  );
	    }
	    else{
		msg = "Not found";
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





































