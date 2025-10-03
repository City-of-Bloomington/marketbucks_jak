package bucks.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
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

public class BuckConf implements java.io.Serializable{

    static final long serialVersionUID = 25L;	
    boolean debug = false;
    static Logger logger = LogManager.getLogger(BuckConf.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String 
	value="3", type_id="",
	name="", user_id="", gl_account="",
	date="", id="", donor_max_value="18";
    String gl_accounts[] = {"",
	"201-18-186503-47240", // MB
	"201-18-186503-47230", // GC 5
	"201-18-186503-47230"}; // GC 20
    boolean isCurrent = true, expired = false;
    User user = null;
    Type type = null;
    List<Batch> batches = null;
    public BuckConf(){
    }	
    public BuckConf(boolean val){
	debug = val;
    }
    public BuckConf(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public BuckConf(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    boolean val8,
		    String val9,
		    String val10
		    ){
	setValues( val,
		   val2,
		   val3,
		   val4,
		   val5,
		   val6,
		   val7,
		   val8,
		   val9,
		   val10
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
		   boolean val8,
		   String val9,
		   String val10
		   ){
	setId(val);
	setValue(val2);
	setType_id(val3);
	setDate(val4);
	setDonor_max_value(val5);
	setUser_id(val6);
	setName(val7);
	setIsCurrent(val8);
	if(val9 != null){
	    type = new Type(debug, type_id, val9);
	}
	setGl_account(val10);
    }	

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }	
    public void setValue(String val){
	if(val != null)
	    value = val;
    }
    public void setType_id(String val){
	if(val != null && !val.equals("-1"))
	    type_id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setDonor_max_value(String val){
	if(val != null)
	    donor_max_value = val;
    }	
    public void setIsCurrent(boolean val){
	isCurrent = val;
    }
    public void setGl_account(String val){
	if(val != null && !val.equals("-1"))
	    gl_account = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getName(){
	//
	// for new records
	if(id.equals("")){
	    name = "MB "+Helper.getCurrentYear();
	}
	return name;
    }	
    public String getValue(){
	return value;
    }
    public String getType_id(){
	if(type_id.equals("")){
	    return "-1";
	}
	return type_id;
    }
    public Type getType(){
	if(!type_id.equals("") && type == null){
	    type = new Type(debug, type_id, null, "buck_types");
	    type.doSelect();
	}
	return type;
    }	
    public int getValue_int(){
	int val = 0;
	try{
	    val = Integer.parseInt(value);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return val;
    }
    public int getDonor_max_value_int(){
	int val = 0;
	try{
	    val = Integer.parseInt(donor_max_value);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return val;
    }	
    public String getUser_id(){
	return user_id;
    }	
    public String getDate(){
		
	return date;
    }
    public String getDonor_max_value(){
		
	return donor_max_value;
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
    public boolean isCurrent(){
	return isCurrent && !expired;
    }
    public boolean isExpired(){
	return expired;
    }		
    public String getGl_account(){
	if(gl_account.equals("")){
	    return "-1";
	}
	return gl_account;
    }	
    public String toString(){
	String ret = "";
	if(type != null){
	    if(!ret.equals(""))ret += " ";
	    ret += type.getName();
	}
	if(!ret.equals(""))ret += " ";		
	ret += "$"+value;
	return ret;
    }
    public String getInfo(){
	return toString();
    }
    public boolean hasBatches(){
	getBatches();
	return batches != null && batches.size() > 0;
    }
    public List<Batch> getBatches(){
	if(id.equals("")) return null;
	if(batches == null){
	    BatchList bl = new BatchList(debug);
	    bl.setConf_id(id);
	    String back = bl.find();
	    if(back.equals("")){
		List<Batch> blist = bl.getBatches();
		if(blist != null && blist.size() > 0){
		    batches = blist;
		}
	    }
	}
	return batches;
    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	isCurrent = true;
	date = Helper.getToday();
	String msg = "";
	if(value.equals("") ||
	   user_id.equals("")){
	    msg = "buck value, and/or user not set";
	    return msg;
	}
	String qq = "insert into buck_confs values(0,?,?,now(),?,?,?,?)";
	String qs = "select LAST_INSERT_ID() ";
	// if(debug)
	logger.debug(qs);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, value);
	    pstmt.setString(2, type_id);
	    pstmt.setString(3, donor_max_value);
	    pstmt.setString(4, user_id);
	    if(name.equals("")){
		getName();
	    }
	    pstmt.setString(5, name);
	    if(gl_account.equals(""))
		pstmt.setNull(6, Types.VARCHAR);
	    else
		pstmt.setString(6, gl_account);			
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
	    //
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qs);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id  = rs.getString(1);
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
    public String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(name.equals("")){
	    msg = "Name is a required field";
	    return msg;
	}		
	date = Helper.getToday();
	String qq = "update buck_confs set value=?,type_id=?, name=?,gl_account=?,date=now(),donor_max_value=?,user_id=? where id=? ";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, value);
	    pstmt.setString(2, type_id);
	    pstmt.setString(3, name);
	    if(gl_account.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, gl_account);
	    pstmt.setString(5, donor_max_value);
	    pstmt.setString(6, user_id);
	    pstmt.setString(7, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	    System.err.println(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.equals("")){
	    doSelect();
	}
	return msg;
    }

    public String doSelect(){
		
	String qq = "select b.id, b.value,b.type_id,date_format(b.date,'%m/%d/%Y'),b.donor_max_value,b.user_id,b.name,year(date)-year(curdate()),t.name,b.gl_account  from buck_confs b left join buck_types t on t.id=b.type_id where b.id=? ";
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
		setValues(id,
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





































