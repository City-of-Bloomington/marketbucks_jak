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

public class Gift implements java.io.Serializable{

    static final long serialVersionUID = 13L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Gift.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String amountStr="", id="", buck_type_id="", cancelled="", dispute_resolution="";
    int amount = 0, total=0;
    String check_no ="", // RecTrac
	user_id="", date_time="";
    String pay_type = "Cash"; // check/money order, Credit Card, Honorary
    String buck_id = ""; // adding one buck at a time
    boolean needMoreIssue = false;
    BuckConf conf = null;
    Batch batch = null;
    Buck buck = null;
    Type buck_type = null;
    List<Buck> bucks = null;
    User user = null;
    String[] cancel_buck_id = null; // bucks that need to be voided
    public Gift(){
    }	
    public Gift(boolean val){
	debug = val;
    }
    public Gift(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Gift(boolean deb,
		String val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7,
		String val8
		){
	setValues( val,
		   val2,
		   val3,
		   val4,
		   val5,
		   val6,
		   val7,
		   val8
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
		   String val8
		   ){
	setId(val);
	setAmount(val2);
	setPay_type(val3);
	setCheck_no(val4);
	setUser_id(val5);
	setDate_time(val6);
	setCancelled(val7);
	setDispute_resolution(val8);
	getBucks();
	if((bucks == null || bucks.size() == 0) && amount > 0){
	    needMoreIssue = true;
	}
	else{
	    getBucksTotal();
	}
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAmount(String val){
	if(val != null && !val.equals("")){
	    amountStr = val;
	    try{
		amount = Integer.parseInt(val);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setPay_type(String val){
	if(val != null)
	    pay_type = val;
    }	
    public void setCheck_no(String val){
	if(val != null)
	    check_no = val;
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }	
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setBuck_id(String val){
	if(val != null && !val.equals("")){
	    if(val.matches("[0-9]+")){
		buck_id = val;
	    }
	    else{
		buck_id=val.substring(0, val.length()-1);
	    }
	}						
    }
	
    public void setBuck_type_id(String val){
	if(val != null)
	    buck_type_id = val;
    }
    public void setCancelled(String val){
	if(val != null){
	    cancelled = val;
	}
    }
    public void setDispute_resolution(String val){
	if(val != null){
	    dispute_resolution = val;
	}
    }		
    public void setCancel_buck_id(String[] vals){
	if(vals != null){
	    cancel_buck_id = vals;
	}
    }	
    //
    public String getId(){
	return id;
    }
    public String getAmount(){
	return amountStr;
    }
    public String getPay_type(){
	return pay_type;
    }
    public String getCheck_no(){
		
	return check_no;
    }

    public String getDate_time(){
		
	return date_time;
    }
    public String getUser_id(){
		
	return user_id;
    }
    public String getBuck_type_id(){
		
	return buck_type_id;
    }
    public String getCancelled(){
		
	return cancelled;
    }
    public String getDispute_resolution(){
	return dispute_resolution;
    }
    public boolean isCancelled(){
	boolean ret = !cancelled.equals("");
	return ret;
    }
    public boolean isDispute_resolution(){
	return !dispute_resolution.equals("");
    }		
    //
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
    public Type getBuck_type(){
	if(!buck_type_id.equals("") && buck_type == null){
	    Type one = new Type(debug, buck_type_id, null, "buck_types");
	    String back = one.doSelect();
	    if(back.equals("")){
		buck_type = one;
	    }
	}
	return buck_type;
    }
	
    public String toString(){
	return "$"+amountStr+" "+pay_type+" "+check_no+" "+date_time;
    }
    public String getTotal(){
	return ""+amount;
    }
    public String getBucksTotal(){
	total = 0;
	if(bucks == null)
	    getBucks();
	if(bucks != null){
	    for(Buck one:bucks){
		if(!one.isVoided()){
		    total += one.getValue_int();
		}
	    }
	}
	if(total < amount) needMoreIssue = true;
	else needMoreIssue = false;
	return ""+total;
    }
    public String getBalance(){
	return ""+(amount - total);
    }
    public boolean hasBalance(){
	return amount > total;
    }
    //	
    public boolean needMoreIssue(){
	//
	return needMoreIssue;
    }
    //
    public String handleAddingBuck(){

	String msg = "";
	if(bucks == null)
	    getBucks();
	if(bucks == null){
	    bucks = new ArrayList<Buck>();
	}
	Buck buck = new Buck(debug, buck_id);
	msg = buck.doSelect();
	if(!msg.equals("")){
	    return msg;
	}
	if(buck.getBuck_type_id().equals("1")){
	    msg = "Market bucks can not be used as GC ";
	    return msg;
	}
	getBucksTotal(); 
	if(total < amount){
	    if(isAlreadyIssued()){
		msg = "Error: this buck is already already issued";
	    }
	    else if(!bucks.contains(buck)){
		int excess = (total + buck.getValue_int()) - amount;
		if(excess <= 0){
		    msg = addNewBuckToGift(buck);
		    if(!msg.equals("")){
			msg = "Error: The buck is already in the system";
		    }
		    else{
			bucks.add(0, buck); // make it first
			total += buck.getValue_int();
		    }
		}
		else{
		    msg = " GC value exceeds requested amount by $"+excess;
		}
	    }
	    return msg;
	}
	//
	// find how many bucks we have so far
	//
	needMoreIssue = (amount - total) > 0;
	return msg;
    }
    private boolean isAlreadyIssued(){
	boolean ret = false;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = " select sum(total) from                                               (select count(*) total from ebt_bucks where buck_id=?                           union select count(*) total from rx_bucks where buck_id=?                       union select count(*) total from gift_bucks where buck_id=?                     union select count(*) total from wic_bucks where buck_id=?                      union select count(*) total from senior_bucks where buck_id=?                    )tt ";
	//
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return ret;
	}
				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, buck_id);
	    pstmt.setString(2, buck_id);
	    pstmt.setString(3, buck_id);
	    pstmt.setString(4, buck_id);
	    pstmt.setString(5, buck_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		if(rs.getInt(1) > 0) ret = true;
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return ret;
		
    }
    public String addNewBuckToGift(Buck buck){
	String msg="";
	//
	// expire date on gifts is one year from issue date
	//
	buck.setExpire_date("12/31/"+Helper.getNextYear());
	msg = buck.doUpdate();
	if(!msg.equals("")){
	    msg =" Could not save data "+msg;
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into gift_bucks values(?,?)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, buck_id);
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
    public String addNewBuckToGift(){
	String msg="";
	//
	// expire date on gifts is one year from issue date
	//
	buck = new Buck(debug, buck_id);
	msg = buck.doSelect();
	if(!msg.equals("")){
	    return msg;
	}
	buck.setExpire_date("12/31/"+Helper.getNextYear());
	msg = buck.doUpdate();
	if(!msg.equals("")){
	    msg =" Could not save data "+msg;
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into gift_bucks values(?,?)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, buck_id);
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
    public List<Buck> getBucks(){
	if(!id.equals("") && bucks == null){
	    BuckList bl = new BuckList(debug, null, id, null, null, null); // gift.id
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

    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(amount % 5 > 0){
	    msg = "Requested amount is not divisible by 5";
	    return msg;
	}
	date_time = Helper.getToday();
	String qq = "insert into gifts values(0,?,?,?,?,now(),null,?)";
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
	    needMoreIssue = true;
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
	if(amount % 5 > 0){
	    msg = "Requested amount is not divisible by 5";
	    return msg;
	}		
	date_time = Helper.getToday();
	String qq = "update gifts set amount=?,pay_type=?,check_no=?,user_id=?,dispute_resolution=? where id=?";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    fillData(pstmt, 1);
	    pstmt.setString(6, id);
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
	    msg = checkForUpdatedAmount();
	}
	return msg;
    }	
    String fillData(PreparedStatement pstmt, int c){
	String msg="";
	try{
	    // all these are required
	    pstmt.setString(c++, amountStr);
	    if(!pay_type.equals(""))
		pstmt.setString(c++, pay_type);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!check_no.equals(""))
		pstmt.setString(c++, check_no);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!user_id.equals(""))
		pstmt.setString(c++, user_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!dispute_resolution.equals(""))
		pstmt.setString(c++, "y");
	    else
		pstmt.setNull(c++, Types.CHAR);
	}
	catch(Exception ex){
	    msg += ex;
	    logger.error(msg);
	}
		
	return msg;					
    }
    //
    public String doSelect(){
		
	String qq = "select e.id, e.amount ,e.pay_type,e.check_no,e.user_id,date_format(e.date_time,'%m/%d/%Y %H:%i'),e.cancelled,e.dispute_resolution from gifts e where e.id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
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
			  rs.getString(8)
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
     * after an update we check the total paid amount is less or greater
     * than the requested amount
     */
    public String checkForUpdatedAmount(){
	String msg = "";		
	getBucksTotal();
	if(total <= amount){
	    return msg;
	}
	//
	// the amount is greater then
	// we need to delete the bucks from this trans
	//
	if(id.equals("")){
	    msg = "No gift id entered";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "delete from gift_bucks where gift_id=?";
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
    /**
     * in case we want to cancel the trans
     */
    public String doCancel(){
	String msg = "";		
	if(id.equals("")){
	    msg = "Transaction id not set ";
	    return msg;
	}
	if(isAnyRedeemed()){
	    msg = "Some of the GC are already redeemed, therefore this transaction can not be cancelled ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update bucks set fund_type=null,expire_date=null where id in (select buck_id from gift_bucks where gift_id=?) ";
	String qq2 = "delete from gift_bucks where gift_id=? ";				
	String qq3 = "update gifts set cancelled='y' where id=?";
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
	    qq = qq2;
	    logger.debug(qq);			
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	    qq = qq3;
	    logger.debug(qq);			
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

    /**
     * in case we want to cancel/void some GC's
     */
    public String doCancelSelected(){
	String msg = "";		
	if(id.equals("")){
	    msg = "Gift transaction id not set ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;		
	ResultSet rs = null;
	if(cancel_buck_id == null){
	    return msg;
	}
	// make sure this GC is not redeemed
	//
	String qq = "select count(*) from redeem_bucks r where r.buck_id=? "; 
	String qq2 = "update bucks b, gift_bucks gb set b.expire_date = null where b.id=gb.buck_id and b.id=? and gb.gift_id=?";
	//
	logger.debug(qq);

	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);			
	    for(String str:cancel_buck_id){
		pstmt.setString(1, str);
		rs = pstmt.executeQuery();
		int cnt = 0;
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt > 0){
		    msg += " GC "+str+" is already redeemed and can not be voided";
		}
		else{
		    pstmt2.setString(1, str);
		    pstmt2.setString(2, id);
		    pstmt2.executeUpdate();
		}
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
    private boolean isAnyRedeemed(){
	boolean ret = false;
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select count(*) from gift_bucks g,redeem_bucks r where r.buck_id=g.buck_id and g.gift_id=? ";
	//
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		logger.error(qq);
		return ret;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		if(rs.getInt(1) > 0){
		    ret = true;
		}
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return ret;
    }
}





































