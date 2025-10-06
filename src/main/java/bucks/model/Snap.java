package bucks.model;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Snap implements java.io.Serializable{

    static final long serialVersionUID = 19L;	
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Snap.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dft = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    static DecimalFormat dblf = new DecimalFormat("0.00");
    final static double dbl_max_default = 27.0;
    String id="", date="", time="", user_id="", card_number="",
	authorization="";
    double dbl_amount=0, ebt_amount=0, snap_amount=0, dbl_max = 27.0;
    String cancelled="";
    String includeDouble = "y";
    User user = null;
    public Snap(){
    }	
    public Snap(boolean val){
	debug = val;
    }
    public Snap(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public Snap(boolean deb,
		String val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7,
		String val8,
		String val9,
		String val10,
		String val11,
		String val12
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
		   val10,
		   val11,
		   val12
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
		   String val9,
		   String val10,
		   String val11,
		   String val12
		   ){
	setId(val);
	setDate(val2);
	setTime(val3);
	setCardNumber(val4);
	setAuthorization(val5);
	setSnapAmount(val6);
	setEbtAmount(val7);
	setDblAmount(val8);
	setDblMax(val9);
	setIncludeDouble(val10);	
	setUser_id(val11);
	setCancelled(val12);

    }	
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setTime(String val){
	if(val != null)
	    time = val;
    }
    public void setIncludeDouble(String val){
	if(val != null && val.equals("y"))
	    includeDouble = "y";
	else
	    includeDouble = "";
    }    
    public void setCardNumber(String val){
	if(val != null)
	    card_number = val;
    }
    public void setAuthorization(String val){
	if(val != null)
	    authorization = val;
    }
    public void setSnapAmount(String val){
	if(val != null)
	    snap_amount = getDouble(val);
    }
    public void setEbtAmount(String val){
	if(val != null)
	    ebt_amount = getDouble(val);
    }
    public void setDblAmount(String val){
	if(val != null)
	    dbl_amount = getDouble(val);
    }
    public void setDblMax(String val){
	if(val != null)
	    dbl_max = getDouble(val);
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }    
    double getDouble(String val){
	double ret = 0;
	try{
	    ret = Double.parseDouble(val);
	}catch(Exception ex){
	    logger.error("invalid number "+val);
	}
	return ret;
    }
    public void setCancelled(String val){
	if(val != null && !val.isEmpty())
	    cancelled = "y";
    }
    public boolean isCancelled(){
	return !cancelled.isEmpty();
    }
    //
    public String getId(){
	return id;
    }
    public String getDate(){
	return date;
    }
    public String getTime(){
	return time;
    }
    public String getIncludeDouble(){
	return includeDouble.isEmpty()?"n":"y";
    }    
    public String getCardNumber(){
		
	return card_number;
    }
    public String getAuthorization(){
	return authorization;
    }
    public String getSnapAmount(){
	if(snap_amount > 0)
	    return ""+dblf.format(snap_amount);
	else
	    return "";
    }
    public double getSnapAmountDbl(){
	return snap_amount;
    }
    public String getEbtAmount(){
	if(ebt_amount > 0)
	    return ""+dblf.format(ebt_amount);
	return "";
    }
    public double getEbtAmountDbl(){
	return ebt_amount;
    }    
    public String getDblAmount(){
	if(dbl_amount > 0)
	    return ""+dblf.format(dbl_amount);
	return "0.00";
    }
    public double getDblAmountDbl(){
	return dbl_amount;
    }    
    public String getDblMax(){
	return ""+dblf.format(dbl_max);
    }    
    
    public String getUser_id(){
	return user_id;
    }
    public String getCancelled(){
	return cancelled;
    }
    public boolean hasUser(){
	getUser();
	return user != null;
    }
    public boolean canDouble(){
	return includeDouble != null && !includeDouble.isEmpty();
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
	return id+" "+card_number+" "+authorization;
    }
    public String doSplitSnapAmount(){
	String msg = "";
	if(!canDouble()){
	    ebt_amount = snap_amount;
	    dbl_amount = 0;
	    return msg;
	}
	if(dbl_max <= 0){
	    dbl_max = dbl_max_default;
	}
	if(snap_amount <= 0.0){
	    msg = "Invalid amount "+snap_amount;
	}
	else{
	    if(snap_amount > 2 * dbl_max){ 
		dbl_amount = dbl_max;
	    }
	    else{
		dbl_amount = (Math.round(100*snap_amount/2.))/100.;
	    }
	    ebt_amount = snap_amount - dbl_amount;	    
	}
	return msg;
    }
    private String checkEntries(){
	String msg = "";
	if(card_number.isEmpty()){
	    msg = " card number is required ";
	}
	if(authorization.isEmpty()){
	    if(!msg.isEmpty()) msg += ", ";
	    msg += " authorization is required ";
	}
	if(snap_amount <= 0){
	    if(!msg.isEmpty()) msg += ", ";
	    msg += " snap amount value not valid ";
	}
	if(user_id.isEmpty()){
	    if(!msg.isEmpty()) msg += ", ";	    
	    msg += " user not set ";
	}
	return msg;
    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg = "";
	msg = checkEntries();
	if(!msg.isEmpty()){
	    return msg;
	}
	doSplitSnapAmount();
	date = Helper.getToday();
	String qq = "insert into snap_purchases values(0,now(),?,?,?, ?,?,?,?,?,null)";
	String qq2 = "select LAST_INSERT_ID() ";
	//
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, card_number);
	    pstmt.setString(2, authorization);	    
	    pstmt.setDouble(3, snap_amount);
	    pstmt.setDouble(4, ebt_amount);
	    pstmt.setDouble(5, dbl_amount);
	    pstmt.setDouble(6, dbl_max);
	    if(includeDouble.isEmpty()){
		pstmt.setNull(7, Types.CHAR);
	    }
	    else{
		pstmt.setString(7, "y");
	    }
	    pstmt.setString(8, user_id);
	    pstmt.executeUpdate();
	    qq = qq2;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id  = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    msg += ex+" : "+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return msg;
    }
    public String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg = "";
	if(!msg.isEmpty()){
	    return msg;
	}
	doSplitSnapAmount();
	String qq = "update snap_purchases set date=?,card_number=?,authorization=?,snap_amount=?,ebt_amount=?,dbl_amount=?,dbl_max=?,include_double=?,user_id=?,cancelled=? where id=?";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    String date2 = "";
	    if(date.equals("")){
		date = Helper.getToday();
	    }
	    date2 = dateFormat2.format(dateFormat.parse(date));
	    if(time.equals(""))
		time = "00:00";
	    String dateTime = date2+" "+time;
	    System.err.println(" date time "+dateTime);
	    pstmt.setString(jj++, dateTime);
	    pstmt.setString(jj++, card_number);
	    pstmt.setString(jj++, authorization);	    
	    pstmt.setDouble(jj++, snap_amount);
	    pstmt.setDouble(jj++, ebt_amount);
	    pstmt.setDouble(jj++, dbl_amount);
	    pstmt.setDouble(jj++, dbl_max);
	    if(includeDouble.isEmpty()){
		pstmt.setNull(jj++, Types.CHAR);
	    }
	    else{
		pstmt.setString(jj++, "y");
	    }
	    pstmt.setString(jj++, user_id);
	    if(isCancelled()){
		pstmt.setString(jj++, "y");
	    }
	    else{
		pstmt.setNull(jj++, Types.CHAR);
	    }
	    pstmt.setString(jj++,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+" : "+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt);
	}
	return msg;
    }
    public String doCancel(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg = "";
	date = Helper.getToday();
	String qq = "update snap_purchases set cancelled=?,user_id=? where id=? ";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, "y");
	    pstmt.setString(2, user_id);
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+" : "+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt);
	}
	msg = doSelect();
	return msg;
    }        
    public String doSelect(){
		
	String qq = "select b.id, date_format(b.date,'%m/%d/%Y'),"+
	    " date_format(b.date,'%H:%i'),"+
	    "b.card_number,b.authorization,b.snap_amount,b.ebt_amount,b.dbl_amount,b.dbl_max,b.include_double,b.user_id,b.cancelled from snap_purchases b where b.id=? ";
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
			  rs.getString(9),
			  rs.getString(10),
			  rs.getString(11),
			  rs.getString(12)
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





































