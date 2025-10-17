/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.model;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class MailUser extends User implements java.io.Serializable{

    String super_user="";
 
    static final long serialVersionUID = 133L;		
    static Logger logger = LogManager.getLogger(MailUser.class);

    public MailUser(){
	super();
    }		
    public MailUser(boolean deb){
	super(deb);
    }
    public MailUser(boolean deb, String val){
	super(deb, val);
    }	
    public MailUser(boolean deb, String val, String val2){
	super(deb, val, val2);
    }
    public MailUser(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    boolean val5,
		    String val6){
	super(deb, val, val2, val3, val4, val5);
	setSuper_user(val6);
    }
    //
    public boolean isSuper_user(){
	return !super_user.equals("");
    }
    public void setSuper_user(String val){
	if(val != null && !val.equals("")){
	    super_user="y";
	}
    }
    public String getId(){
	return super.getId();
    }
    public String getFullName(){
	return super.getFullName();
    }	
    //
    // getters
    //
    public String getSuper_user(){
	return super_user;
    }
    //
    // this is the same as user
    //
    @Override
    public int hashCode() {
	int hash = 7, id_int = 0;
	if(!id.equals("")){
	    try{
		id_int = Integer.parseInt(id);
	    }catch(Exception ex){}
	}
	hash = 67 * hash + id_int;
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
	final User other = (User) obj;
	return this.id.equals(other.id);
    }
    
    //
    @Override  	
    public String doSelect(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select u.id,u.userid,u.fullname,u.role,u.inactive,m.super_user from users u,mail_notifications m where u.id=m.id and ";
	if(!username.equals("")){
	    qq += " userid = ?";
	}
	else if(!id.equals("")){
	    qq += " id = ?";
	}
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!username.equals(""))
		pstmt.setString(1, username);
	    else
		pstmt.setString(1, id);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5) != null);
		setSuper_user(rs.getString(6));				
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
    public String doSave(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "insert into mail_notificatios values(?,?)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    if(super_user.equals(""))
		pstmt.setNull(2, Types.CHAR);
	    else
		pstmt.setString(2, "y");
	    pstmt.executeUpdate();
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
    public String doUpdate(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "update mail_notificatios set super_user=? where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    if(super_user.equals(""))
		pstmt.setNull(1, Types.CHAR);
	    else
		pstmt.setString(1, "y");
	    pstmt.setString(2, id);
	    pstmt.executeUpdate();
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
    public String doDelete(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "delete from mail_notificatios where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
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

}
