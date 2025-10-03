/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.list;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.utils.*;

public class MailUserList{

    boolean debug = false;
    static final long serialVersionUID = 1123L;		
    static Logger logger = LogManager.getLogger(MailUserList.class);
    List<MailUser> mailUsers = null;
    List<User> users = null;
    String[] add_user_id=null;
    String[] del_user_id=null;
	
    String name = "", next_fire_time="";

    public MailUserList(){
    }	
    public MailUserList(boolean deb){
	debug = deb;
    }	
    //
    // setters
    //
    public void setAdd_user_id(String[] vals){
	if(vals != null){
	    add_user_id = vals;
	}
    }
    public void setDel_user_id(String[] vals){
	if(vals != null){
	    del_user_id = vals;
	}
    }	
    public List<MailUser> getMailUsers(){
	return mailUsers;
    }
    public List<User> getUsers(){
	return users;
    }
    public String getNext_fire_time(){
	return next_fire_time;
    }
    public String find(){
	String msg = "";
	String qq = " select u.id,u.userid,u.fullname,u.role,u.inactive,m.super_user from users u, mail_notifications m where u.id=m.id ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qo = " order by u.fullname ";
	qq += qo;
	// System.err.println(qq);
	logger.debug(qq);
	try{
	    mailUsers = new ArrayList<MailUser>();
	    users = new ArrayList<User>();
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();	
	    while(rs.next()){
		MailUser one = new MailUser(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5) != null,
					    rs.getString(6)
					    );
		User two = new User(debug,
				    rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4),
				    rs.getString(5) != null);
		mailUsers.add(one);
		users.add(two);
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
    public String addUsers(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	if(add_user_id == null || add_user_id.length == 0){
	    return msg;
	}
	String qq = "insert into mail_notifications values(?,null)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    for(String str:add_user_id){
		pstmt.setString(1, str);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String deleteUsers(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	if(del_user_id == null || del_user_id.length == 0){
	    return msg;
	}
	String qq = "delete from mail_notifications where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    for(String str:del_user_id){
		pstmt.setString(1, str);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }		
    public String doClean(){
		
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	// we need to delete the old records from the database
	// otherwise we get exception and avoid duplication in scheduling
	//
	/*
	  String[] qq = {"delete from qrtz_cron_triggers",
	  "delete from qrtz_simple_triggers",
	  "delete from qrtz_fired_triggers",
	  "delete from qrtz_triggers",
	  "delete from qrtz_job_details"};
	*/
	// apps mysql is case sensetive
	String[] qq = {"delete from QRTZ_CRON_TRIGGERS",
	    "delete from QRTZ_SIMPLE_TRIGGERS",
	    "delete from QRTZ_FIRED_TRIGGERS",
	    "delete from QRTZ_TRIGGERS",
	    "delete from QRTZ_JOB_DETAILS"};		
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect to DB";
		System.err.println(" could not connect to DB");
		return msg;
	    }
	    for(String str:qq){
		logger.debug(str);
		pstmt = con.prepareStatement(str);
		pstmt.executeUpdate();
	    }
	}catch(Exception ex){
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String findNextFireTime(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//		
	String qq = " select date_format(from_unixtime(next_fire_time/1000),'%W %m/%d/%Y %H:%i') from QRTZ_TRIGGERS"; // we will have only one record
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect to DB";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){ //  all what we need is one record
		next_fire_time = rs.getString(1);
	    }
	}catch(Exception ex){
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
		
		
    }
	
}
