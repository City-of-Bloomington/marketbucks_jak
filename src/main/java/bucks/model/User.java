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
/**
 * User class
 *
 */

public class User implements java.io.Serializable{

    String username="", fullName="", dept="", role="", id="", inactive="";
    boolean debug = false, userExists = false;
    static final long serialVersionUID = 130L;		
    static Logger logger = LogManager.getLogger(User.class);
    String errors = "";
    public User(){
    }		
    public User(boolean deb){
	debug = deb;
    }
    public User(boolean deb, String val){
	debug = deb;
	setUsername(val);
    }	
    public User(boolean deb, String val, String val2){
	debug = deb;
	setUsername(val);
	setId(val2);
    }
    public User(boolean deb, String val, String val2,
		String val3, String val4, boolean val5){
	debug = deb;
	setValues(val, val2, val3, val4, val5);
    }
    void setValues(String val,
		   String val2,
		   String val3,
		   String val4,
		   boolean val5
		   ){
	setId(val);
	setUsername(val2);
	setFullName(val3);
	setRole(val4);
	setInactive(val5);
	userExists = true;
    }
    //
    public boolean hasRole(String val){
	if(role.indexOf(val) > -1) return true;
	return false;
    }
    public boolean canEdit(){
	return hasRole("Edit");
    }
    public boolean canDelete(){
	return (hasRole("Delete") || isAdmin());
    }
    public boolean isAdmin(){
	return hasRole("Admin");
    }
		
    public boolean hasUsername(){
	return !username.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }
    public boolean getInactive(){
	return !inactive.equals("");
    }
				
    public void setDebug(){
	debug = true;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }	
    public String getUsername(){
	return username;
    }
    public String getFullName(){
	return fullName;
    }
    public String getDept(){
	return dept;
    }
    public String getRole(){
	return role;
    }
    public String getRoleText(){
	String ret = "None";
	if(role.equals("Edit"))
	    ret = "Edit Only";
	else if(role.equals("Edit:Delete"))
	    ret = "Edit and Delete";
	else if(role.indexOf("Admin") > -1){
	    ret = "All";
	}
	return ret;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }	
    public void setUsername (String val){
	if(val != null)
	    username = val;
    }
    public void setFullName (String val){
	if(val != null)
	    fullName = val;
    }
    public void setRole (String val){
	if(val != null)
	    role = val;
    }
	
    public void setDept (String val){
	if(val != null)
	    dept = val;
    }
    public void setInactive (boolean val){
	if(val)
	    inactive = "y";
    }		
    public boolean userExists(){
	return userExists;
    }
    public String toString(){
	if(fullName == null || fullName.equals("")) return username;
	else return fullName;
    }
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
    public String doSave(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "insert into users values(0,?,?,?,null)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, username);
	    pstmt.setString(2, fullName);
	    if(role.equals(""))
		role="Edit:Delete";
	    pstmt.setString(3, role);
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
	String qq = "update users set userid=?,fullname=?,role=?,inactive=? where id=? ";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, username);
	    pstmt.setString(2, fullName);
	    if(role.equals(""))
		pstmt.setNull(3, Types.INTEGER);
	    else
		pstmt.setString(3, role);
	    if(inactive.equals("")){
		pstmt.setNull(4, Types.CHAR);
	    }
	    else{
		pstmt.setString(4, "y");
	    }
	    pstmt.setString(5, id);
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
		
    public String doSelect(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select * from users ", qw="";
	if(!username.equals("")){
	    qw += " where userid = ?";
	}
	else if(!id.equals("")){
	    qw += " where id = ?";
	}
	qq += qw;
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    // System.err.println(msg);
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
		String str = rs.getString(1);
		id = str;
		str = rs.getString(2);
		if(str != null)
		    username = str;
		str = rs.getString(3);
		if(str != null)
		    fullName = str;
		str = rs.getString(4);
		if(str != null)
		    role = str;
		str = rs.getString(5);
		if(str != null && !str.equals(""))
		    inactive = "y";								
		userExists = true;
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

		
}
