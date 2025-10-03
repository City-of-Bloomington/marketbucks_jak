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

public class Ebt implements java.io.Serializable{

    static final long serialVersionUID = 11L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Ebt.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static int default_dmb_amount=27, default_buck_value=3;
    static String default_buck_type = "1";
    int ebt_donor_max=27, ebt_buck_value=3;
    String amountStr="0", id="", buck_type_id="1", cancelled="",
	dispute_resolution="";
    int amount = 0, dmb_amount=0, donor_max=0, paid_amount=0, 
	donated_amount=0, 
	total=0;
    // int buck_value = 0; // we get from conf from first buck
    String approve ="", card_last_4="",user_id="", date_time="";
    String buck_id = ""; // adding one buck at a time
    String includeDouble = "y";    
    boolean needMoreIssue = false;
    // BuckConf conf = null;
    Type buck_type = null;
    List<Buck> bucks = null;
    User user = null;
    public Ebt(){
    }	
    public Ebt(boolean val){
	debug = val;
    }
    public Ebt(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Ebt(boolean deb,
	       String val,
	       int val2,
	       String val3,
	       String val4,
	       String val5,
	       String val6,
	       int val7,
	       String val8,
	       int val9,
	       int val10,
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
		   int val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   int val7,
		   String val8,
		   int val9,
		   int val10,
		   String val11,
		   String val12
		   ){
	setId(val);
	setAmountInt(val2);
	setApprove(val3);
	setCard_last_4(val4);
	setUser_id(val5);
	setDate_time(val6);
	setDmb_amountInt(val7);
	setCancelled(val8);
	setEbt_donor_max(val9);
	setEbt_buck_value(val10);
	setDispute_resolution(val11);
	setIncludeDouble(val12);	
	getBucks();
	if(!isCancelled() && !isDispute_resolution()){
	    if((bucks == null || bucks.size() == 0) && amount > 0){
		needMoreIssue = true;
	    }
	}
	getBucksTotal();
    }

    public void setEbt_donor_max(int val){
	if(val > 0)
	    ebt_donor_max = val;
    }
    public void setEbt_buck_value(int val){
	if(val > 0)
	    ebt_buck_value = val;
    }		
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAmountInt(int val){
	if(val > 0){
	    amount = val;
	    amountStr = ""+amount;
	}
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
    void setDmb_amount(){
	if(!canDouble()){
	    dmb_amount = 0;
	    return;
	}
	if(!isDispute_resolution()){
	    dmb_amount = ebt_donor_max; 
	    if(dmb_amount > amount){
		dmb_amount = amount;
	    }
	    buck_type_id = default_buck_type;
	    checkFrequentEbtToday();
	}
    }
    public void setDmb_amount(String val){
	if(val != null && !val.equals("")){
	    try{
		dmb_amount = Integer.parseInt(val);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setDmb_amountInt(int val){
	if(val > 0)
	    dmb_amount = val;
    }
    public void setApprove(String val){
	if(val != null)
	    approve = val;
    }	
    public void setCard_last_4(String val){
	if(val != null)
	    card_last_4 = val;
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
	if(val != null)
	    cancelled = val;
    }
    public void setDispute_resolution(String val){
	if(val != null)
	    dispute_resolution = val;
    }
    public void setIncludeDouble(String val){
	if(val != null && val.equals("y"))
	    includeDouble = "y";
	else
	    includeDouble = "";
    }        
    //
    public String getId(){
	return id;
    }
    public String getAmount(){
	if(id.equals(""))
	    return "";
	return amountStr;
    }
    public int getAmountInt(){
	return amount;
    }
    public String getDmb_amount(){
	return ""+dmb_amount;
    }
    public int getDmb_amountInt(){
	return dmb_amount;
    }
    public String getApprove(){
	return approve;
    }
    public String getCard_last_4(){
		
	return card_last_4;
    }
    public String getPaid_amount(){
	return ""+paid_amount;
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
    public String getEbt_donor_max(){
	return ""+ebt_donor_max;
    }
    public String getEbt_buck_value(){
	return ""+ebt_buck_value;
    }		
    public String getCancelled(){
		
	return cancelled;
    }
    public String getDispute_resolution(){
		
	return dispute_resolution;
    }
    public boolean isDispute_resolution(){
	return dispute_resolution != null && !dispute_resolution.equals("");
    }
    public boolean isCancelled(){
	return !cancelled.equals("");
    }
    public String getIncludeDouble(){
	if(!includeDouble.isEmpty()){
	    return "y";
	}
	else{
	    return "n";
	}
    }
    public String getNotes(){
	String notes = "";
	if(isDispute_resolution())
	    notes = "Dispute resolution created record";
	return notes;
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
	return "$"+amount+" "+approve+" "+card_last_4+" "+date_time;
    }
    public String getDonated_amount(){
	return ""+donated_amount;
    }
    public String getTotal(){
	return ""+total;
    }
    public String getBucksTotal(){
	if(total == 0){
	    getBucks();
	    if(bucks != null){
		donated_amount = bucks.stream().filter(Buck::isDmbType).filter(Buck::isNotVoided).mapToInt(Buck::getValue_int).sum();
		paid_amount = bucks.stream().filter(Buck::isEbtType).filter(Buck::isNotVoided).mapToInt(Buck::getValue_int).sum();
		total = paid_amount + donated_amount;
	    }
	}
	return ""+total;
    }		
    public String getBalance(){
	int balance = (amount+dmb_amount) - paid_amount - donated_amount;		
	return ""+balance;
    }
    public boolean hasBalance(){
	int balance = (amount+dmb_amount) - paid_amount - donated_amount;
	return balance > 0;
    }
    public boolean hasBucks(){
	getBucks();
	return bucks != null && bucks.size() > 0;
    }
    public boolean canDouble(){
	return includeDouble != null && !includeDouble.isEmpty();
    }    
    //	
    public boolean needMoreIssue(){
	return (amount + dmb_amount)  >  total;
    }
    //
    public String handleAddingBuck(){
	boolean added = false;
	String msg = "";
	getBucksTotal();
	if(bucks == null){
	    bucks = new ArrayList<Buck>();
	}
	Buck buck = new Buck(debug, buck_id);
	msg = buck.doSelect();
	if(!msg.equals("")){
	    return msg;
	}
	if(isAlreadyIssued()){
	    msg =" This buck is already issued ";
	    return msg;
	}
	//
	// market bucks expire on the Dec 31 of the issued year
	//
	buck.setExpire_date("12/31/"+Helper.getNextYear());
	if(amount > paid_amount){
	    buck.setFund_type("ebt");
	    msg = buck.doUpdate();
	    if(msg.equals("")){
		if(!bucks.contains(buck)){
		    added = true;
		    msg = addNewBuckToEbt();
		    if(!msg.equals("")){
			bucks.remove(0);
			msg = "Error: The buck is already in the system";
		    }
		    else{
			paid_amount += buck.getValue_int();
			total += buck.getValue_int();
			bucks.add(0, buck); // make it first
		    }
		}
		else{
		    // adding the same buck to the pack
		    // we do not show error
		}
	    }
	}
	else if(dmb_amount > donated_amount){
	    buck.setFund_type("dmb");
	    msg = buck.doUpdate();
	    if(msg.equals("")){
		if(!bucks.contains(buck)){				
		    added = true;
		    msg = addNewBuckToEbt();
		    if(!msg.equals("")){
			msg = "Error: The buck is already in the system";
		    }
		    else{
			donated_amount += buck.getValue_int();
			total += buck.getValue_int();
			bucks.add(0, buck); // make it first
		    }
		}
		else{
		    // ignore this as it is already in the pack
		    // we do not show error 
		}
	    }
	}
	else if(dmb_amount < donated_amount){
	    msg = " Count exceeded "; // should not happen
	}
	//
	return msg;
    }
    /**
     * check if this buck was already issued
     */
    private boolean isAlreadyIssued(){
	boolean ret = false;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = " select sum(total) from                                               (select count(*) total from ebt_bucks where buck_id=?                           union select count(*) total from rx_bucks where buck_id=?                       union select count(*) total from wic_bucks where buck_id=?                      union select count(*) total from senior_bucks where buck_id=?                   union select count(*) total from gift_bucks where buck_id=? )tt ";
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
    /**
     * each customer can have max of 27 DMB per day
     * if a trans has been processed before we need to find the amount
     * of dmb and if 27 or more, no more dmb will be given and the
     * dmb_amount will be set to 0, if less than 27 was issued we
     * process with the difference.
     */
    public void checkFrequentEbtToday(){
	String back =  "";
	int dmb_amount_received = 0;
	if(card_last_4.equals("")){
	    back = " card last 4 digits are not provided ";
	    logger.error(back);
	    return ;
	}
	EbtList el = new EbtList(debug);
	el.setCard_last_4(card_last_4);
	el.setCancelled("n"); // ignore cancelled 
	el.setTodayDate();
	if(!id.equals("")){
	    el.excludeId(id);
	}
	back = el.find();
	if(back.equals("")){
	    List<Ebt> ones = el.getEbts();
	    if(ones != null){
		for(Ebt one:ones){
		    dmb_amount_received += one.getDmb_amountInt();
		}
	    }
	}
	else{
	    logger.error(back);
	}
	if(dmb_amount_received > 0){
	    if(dmb_amount_received < ebt_donor_max){
		dmb_amount = ebt_donor_max - dmb_amount_received;
		if(dmb_amount > amount){
		    dmb_amount = amount;
		}
	    }
	    else{
		dmb_amount = 0;
	    }
	}
	return ;
    }
    public String addNewBuckToEbt(){
	String msg="";
	if(buck_id.equals("") || id.equals("")){
	    msg = "No buck number or ebt id entered";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into ebt_bucks values(?,?)";
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
	//		
	if(!id.equals("") && bucks == null){
	    BuckList bl = new BuckList(debug,id,null,null,null,null); // ebt.id
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
	setDmb_amount();		
	String msg = "";
	if(amount % ebt_buck_value > 0){ // amount % 3
	    msg = "Requested amount is not divisible by "+ebt_buck_value;
	    return msg;
	}
	date_time = Helper.getToday();
	String qq = "insert into ebts values(0,?,?,?,?,now(),?,null,?,?,?,?)";// cancelled = null				
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
	if(amount % ebt_buck_value > 0){
	    msg = "Requested amount is not divisible by "+ebt_buck_value;
	    return msg;
	}
	setDmb_amount();				
	date_time = Helper.getToday();
	String qq = "update ebts set amount=?,approve=?,card_last_4=?,user_id=?,dmb_amount=?,dispute_resolution=?,include_double=? where id=?";
	//	if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    fillData(pstmt, 1);
	    pstmt.setString(8, id);
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
	msg = doSelect();
	return msg;
    }

    String fillData(PreparedStatement pstmt, int c){
	String msg="";
	try{
	    // all these are required
	    pstmt.setString(c++, amountStr);
	    if(!approve.equals(""))
		pstmt.setString(c++, approve);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!card_last_4.equals(""))
		pstmt.setString(c++, card_last_4);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!user_id.equals(""))
		pstmt.setString(c++, user_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    pstmt.setString(c++, ""+dmb_amount);
	    if(id.equals("")){
		pstmt.setInt(c++, ebt_donor_max);
		pstmt.setInt(c++, ebt_buck_value);
	    }
	    if(!dispute_resolution.equals(""))
		pstmt.setString(c++, "y");
	    else
		pstmt.setNull(c++, Types.CHAR);
	    if(includeDouble.isEmpty()){
		pstmt.setNull(c++, Types.CHAR);
	    }
	    else{
		pstmt.setString(c++, "y");
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

	String qq = "select e.id, e.amount ,e.approve,e.card_last_4,e.user_id,date_format(e.date_time,'%m/%d/%Y %H:%i'),e.dmb_amount,e.cancelled,e.ebt_donor_max,e.ebt_buck_value,e.dispute_resolution,e.include_double from ebts e where e.id=? ";
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
			  rs.getInt(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5),
			  rs.getString(6),
			  rs.getInt(7),
			  rs.getString(8),
			  rs.getInt(9),
			  rs.getInt(10),
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
    /**
     * after an update we check the total paid amount is less or greater
     * than the requested amount
     */
    public String checkForUpdatedAmount(){
	String msg = "";		
	getBucksTotal();
	if(paid_amount <= amount){
	    return msg;
	}
	// the amount is greater, then
	// we need to delete all the bucks from this ebt
	//
	if(id.equals("")){
	    msg = "No ebt id entered";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update bucks b, ebt_bucks e set b.fund_type=null where b.id=e.buck_id and e.ebt_id=? ";
	String qq2 = "delete from ebt_bucks where ebt_id=?";
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
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	bucks = new ArrayList<Buck>();
	donated_amount = 0;
	paid_amount = 0;
	total = 0;
	return msg;				

    }
    /**
     * if the customer changed his/her mind and wants to cancel the trans
     * we return the bucks to the pool and mark the ebt is cancelled
     */
    public String doCancel(){
	String msg = "";
	if(id.equals("")){
	    msg = "Ebt id not set ";
	    return msg;
	}
	if(isAnyRedeemed()){
	    msg = "Some of these MB's are already redeemed and can not be voided";
	    return msg;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update bucks set fund_type=null,expire_date=null where id in (select buck_id from ebt_bucks where ebt_id=?) ";
	String qq2 = "delete from ebt_bucks where ebt_id=? ";
	String qq3 = "update ebts set cancelled='y' where id=?";
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
    private boolean isAnyRedeemed(){
	boolean ret = false;
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select count(*) from ebt_bucks e,redeem_bucks r where e.buck_id=r.buck_id and e.ebt_id=? ";
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





































