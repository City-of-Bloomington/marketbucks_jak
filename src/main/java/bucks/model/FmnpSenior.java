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

public class FmnpSenior implements java.io.Serializable{

    static final long serialVersionUID = 13L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(FmnpSenior.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String amountStr="", id="",
	buck_type_id="1", // always MB of $3
	cancelled="", 
	dispute_resolution="";
    String ticket_num = "";
    // change in TopAction class
    int senior_max_amount = 45;
    int senior_max_count = 2; 
    int amount = 0, total = 0; 
    String user_id="", date_time="";
    String buck_id = ""; // adding one buck at a time
    boolean needMoreIssue = false;
    BuckConf conf = null;
    Batch batch = null;
    Buck buck = null;
    Type buck_type = null;
    List<Buck> bucks = null;
    User user = null;
    String[] cancel_buck_id = null; // bucks that need to be cancelled
    public FmnpSenior(){
    }	
    public FmnpSenior(boolean val){
	debug = val;
    }
    public FmnpSenior(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public FmnpSenior(boolean deb,
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
	setTicketNum(val2);
	setAmount(val3);
	setSenior_max_amount(val4);
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
		if(amount > senior_max_amount){
		    amount = senior_max_amount;
		}
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }
    public void setTicketNum(String val){
	if(val != null)
	    ticket_num = val;
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
    public void setSenior_max_amount(Integer val){
	if(val != null && val > 0)
	    senior_max_amount = val;
    }
    public void setSenior_max_amount(String val){
	if(val != null && !val.equals("")){
	    try{
		senior_max_amount = Integer.parseInt(val);
	    }catch(Exception ex){

	    }								
	}
    }
    public int getAmountInt(){
	return amount;
    }    
    public String getUser_id(){
		
	return user_id;
    }

    public String getAmount(){
	if(id.equals("")){
	    return ""+senior_max_amount;
	}
	return amountStr;
    }
    public String getDate_time(){
		
	return date_time;
    }
    public String getTicketNum(){
	if(id.equals("")){
	    return "";
	}
	return ticket_num;
    }		
    public String getBuck_type_id(){
		
	return buck_type_id;
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
	
    public String getCancelled(){
		
	return cancelled;
    }
    public String getDispute_resolution(){
	return dispute_resolution;
    }
    public String toString(){
	return ticket_num+" $"+amountStr+" "+date_time;
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
    public boolean hasBucks(){
	getBucks();
	return bucks != null && bucks.size() > 0;
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
	if(!buck.getBuck_type_id().equals("1")){
	    msg = "Only MB's are allowed ";
	    return msg;
	}
	getBucksTotal(); 
	if(total < amount){
	    if(isAlreadyIssued()){
		msg = "Already in the system";
		return msg;
	    }
	    if(bucks.contains(buck)){
		msg = "Already added ";
		return msg;
	    }
	    int excess = (total + buck.getValue_int()) - amount;
	    if(excess <= 0){
		msg = addNewBuck(buck);
		if(!msg.equals("")){
		    bucks.remove(0);
		    msg = "Error: The buck is already in the system";
		}
		else{
		    bucks.add(0, buck); // make it first
		    total += buck.getValue_int();
		}
	    }
	    else{
		msg = " Rx value exceeds requested amount by $"+excess;
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
	String qq = " select sum(total) from                                               (select count(*) total from ebt_bucks where buck_id=?                           union select count(*) total from senior_bucks where buck_id=?                   union select count(*) total from rx_bucks where buck_id=?                       union select count(*) total from senior_bucks where buck_id=?                   union select count(*) total from gift_bucks where buck_id=? )tt ";
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
    String addNewBuck(Buck buck){
	String msg="";
	//
	// expire date on gifts is one year from issue date
	//
	buck.setExpire_date("12/31/"+Helper.getNextYear());
	buck.setFund_type("senior");
	msg = buck.doUpdate();
	if(!msg.equals("")){
	    msg =" Could not save data "+msg;
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into senior_bucks values(?,?)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect ";
	    return msg;
	}
	try{
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
	    BuckList bl = new BuckList(debug);
	    bl.setSenior_id(id);
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
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String msg = "";
	if(amount % 3 > 0){
	    msg = "Requested amount is not multiple of 3";
	    return msg;
	}
	if(ticket_num.isEmpty()){
	    msg = "Ticket # is required ";
	    return msg;
	}
	date_time = Helper.getToday();
	String qq = "select count(*) from fmnp_seniors where ticket_num = ? and cancelled is null";
	String qq2 = "insert into fmnp_seniors values(0,?,?,?,?,now(),null,null)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, ticket_num);
	    rs = pstmt.executeQuery();
	    int cnt = 0;
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(cnt >= senior_max_count){
		msg = "Maximum number of tickets exceeded "+senior_max_count+" allowed, no more transactions are allowed for this ticket";
		return msg;
	    }
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq);						
	    fillData(pstmt2, 1);
	    pstmt2.executeUpdate();
	    //
	    qq = "select LAST_INSERT_ID() ";
	    logger.debug(qq);
	    pstmt3 = con.prepareStatement(qq);
	    rs = pstmt3.executeQuery();
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
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}
	return msg;
    }
    public String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(amount % 3 > 0){
	    msg = "Requested amount is not multiple of 3";
	    return msg;
	}		
	date_time = Helper.getToday();
	String qq = "update fmnp_seniors set amount=?,senior_max_amount=?,user_id=?,dispute_resolution=? where id=?";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    fillData(pstmt, 1);
	    pstmt.setString(5, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.isEmpty()){
	    msg = checkForUpdatedAmount();
	    msg = doSelect();
	}
	return msg;
    }	
    String fillData(PreparedStatement pstmt, int c){
	String msg="";
	try{
	    // all these are required
	    if(id.isEmpty()){
		pstmt.setString(c++, ticket_num);
	    }
	    pstmt.setString(c++, amountStr);
	    pstmt.setInt(c++, senior_max_amount);
	    if(!user_id.equals(""))
		pstmt.setString(c++, user_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!id.equals("")){
		if(!dispute_resolution.isEmpty())
		    pstmt.setString(c++, "y");
		else
		    pstmt.setNull(c++, Types.CHAR);
	    }
	}
	catch(Exception ex){
	    msg += ex;
	    logger.error(msg);
	}
	return msg;					
    }
    //
    public String doSelect(){
		
	String qq = "select r.id, r.ticket_num,r.amount ,r.senior_max_amount,r.user_id,date_format(r.date_time,'%m/%d/%Y %H:%i'),r.cancelled,r.dispute_resolution from fmnp_seniors r where r.id=? ";
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
	// the amount is greater than
	// we need to delete the bucks from this trans
	//
	if(id.isEmpty()){
	    msg = "No id entered";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "delete from senior_bucks where buck_id=?";
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
	    msg = "Some of the MB are already redeemed, therefore this transaction can not be cancelled ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "update bucks set fund_type=null,expire_date=null where id in (select buck_id from senior_bucks where senior_id=?) ";
	String qq2 = "delete from senior_bucks where senior_id=? ";				
	String qq3 = "update fmnp_seniors set cancelled='y' where id=?";
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
	    pstmt2 = con.prepareStatement(qq);
	    pstmt2.setString(1, id);
	    pstmt2.executeUpdate();
	    qq = qq3;
	    logger.debug(qq);			
	    pstmt3 = con.prepareStatement(qq);
	    pstmt3.setString(1, id);
	    pstmt3.executeUpdate();						

	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}
	return msg;				

    }		   

    /**
     * in case we want to cancel/void some Rx's
     */
    public String doCancelSelected(){
	String msg = "";		
	if(id.equals("")){
	    msg = "FMNP SENIOR transaction id not set ";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null,pstmt3=null;
	ResultSet rs = null;
	if(cancel_buck_id == null){
	    return msg;
	}
	// make sure this GC is not redeemed
	//
	String qq = "select count(*) from redeem_bucks r where r.buck_id=? "; 
	String qq2 = "update bucks b, senior_bucks r set b.expire_date=null, b.fund_type=null where b.id=r.buck_id and b.id=? and r.senior_id=?";
	String qq3 = "delete from senior_bucks where buck_id=? ";
	//
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);						
	    //
	    for(String str: cancel_buck_id){
		pstmt.setString(1, str);
		rs = pstmt.executeQuery();
		int cnt = 0;
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt > 0){
		    msg += " MB "+str+" is already redeemed and can not be voided";
		}
		else{
		    pstmt2.setString(1, str);
		    pstmt2.setString(2, id);
		    pstmt2.executeUpdate();
		    //
		    pstmt3.setString(1, str);
		    pstmt3.executeUpdate();
		    //
		}
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}
	doSelect();
	return msg;				

    }
    //
    private boolean isAnyRedeemed(){
	boolean ret = false;
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select count(*) from senior_bucks g,redeem_bucks r where r.buck_id=g.buck_id and g.senior_id=? ";
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





































