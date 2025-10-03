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

public class Resolution implements java.io.Serializable{

    static final long serialVersionUID = 18L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Resolution.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", dispute_id="", user_id="", date_time="";
    String expire_date=""; // for expired bucks
    String value="", conf_id=""; //for non existant
    String approve="", card_last_4="", pay_type="", check_no="";
    String status=""; // Success, Failure
    String reason = "", buck_id="", type_id="", new_buck_id="";
    Buck buck = null;
    BuckConf conf = null;
    Dispute dispute = null;
    User user = null;
	
    public Resolution(){
    }	
    public Resolution(boolean val){
	debug = val;
    }
    public Resolution(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Resolution(boolean deb,
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
		      String val12,
		      String val13
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
		   val12,
		   val13
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
		   String val12,
		   String val13
		   ){
	setId(val);
	setDispute_id(val2);
	setConf_id(val3);		
	setValue(val4);
	setApprove(val5);
	setCard_last_4(val6);
	setPay_type(val7);
	setCheck_no(val8);
	setExpire_date(val9);		
	setUser_id(val10);
	setDate_time(val11);
	setStatus(val12);
	setNew_buck_id(val13);
    }
				 

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setNew_buck_id(String val){
	if(val != null)
	    new_buck_id = val;
    }	
    public void setDispute_id(String val){
	if(val != null)
	    dispute_id = val;
    }	
    public void setExpire_date(String val){
	if(val != null)
	    expire_date = val;
    }
    public void setValue(String val){
	if(val != null)
	    value = val;
    }	
    public void setDate_time(String val){
	if(val != null)
	    date_time = val;
    }	
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setConf_id(String val){
	if(val != null && !val.equals("-1"))
	    conf_id = val;
    }
    public void setApprove(String val){
	if(val != null)
	    approve = val;
    }
    public void setCard_last_4(String val){
	if(val != null)
	    card_last_4 = val;
    }
    public void setPay_type(String val){
	if(val != null)
	    pay_type = val;
    }
    public void setCheck_no(String val){
	if(val != null)
	    check_no = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getNew_buck_id(){
	return new_buck_id;
    }	
    public String getDispute_id(){
	return dispute_id;
    }
    public String getConf_id(){
	if(conf_id.equals("")){
	    return "-1";
	}
	return conf_id;
    }	
    public String getDate_time(){
		
	return date_time;
    }
    public String getUser_id(){
		
	return user_id;
    }
    public String getValue(){
	return value;
    }
    public String getApprove(){
	return approve;
    }	
    public String getExpire_date(){
	return expire_date;
    }
    public String getCard_last_4(){
	return card_last_4;
    }
    public String getPay_type(){
	return pay_type;
    }
    public String getCheck_no(){
	return check_no;
    }
    public String getStatus(){
	return status;
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
    public Buck getBuck(){
	if(buck_id.equals("")){
	    if(dispute == null){
		getDispute();
	    }
	}
	if(!buck_id.equals("") && buck == null){
	    Buck one = new Buck(debug, buck_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		buck = one;
	    }
	}
	return buck;
    }	
	
    public Dispute getDispute(){
	if(dispute == null && !dispute_id.equals("")){
	    dispute = new Dispute(debug, dispute_id);
	    String back = dispute.doSelect();
	    if(!back.equals("")){
		System.err.println(back);
	    }
	    else{
		buck_id = dispute.getBuck_id();
	    }
	}
	return dispute;
    }
    public BuckConf getConf(){
	if(conf == null){
	    if(!conf_id.equals("")){
		conf = new BuckConf(debug, conf_id);
		String back = conf.doSelect();
		if(!back.equals("")){
		    System.err.println(back);
		}
	    }
	    else {
		getBuck();
		if(buck != null){
		    conf = buck.getConf();
		}
	    }
	}
	return conf;
    }	
    public String toString(){
	return id+" "+dispute_id;
    }
    private String validateNProcess(){
	String msg="";
	if(dispute == null){
	    getDispute();
	}
	if(dispute == null){
	    return "Dispute Info not available";
	}
	buck = dispute.getBuck();
	reason = dispute.getReason();
	if(reason.equals("Expired")){
	    expire_date = "12/31/"+Helper.getCurrentYear();
	    return changeExpireDate();
	}
	if(reason.startsWith("Not")){ // Not Exist, Not Issued
	    //
	    // a typo, then we have a new buck id
	    //
	    if(!new_buck_id.equals("")){
		return handleTypo();
	    }
	    getConf();
	    if(conf == null){
		return "Configuration is not set";
	    }
	    type_id = conf.getType_id();
	    if(value.equals("")){
		value = conf.getValue();
	    }
	    if(type_id.equals("1")){
		approve = "dis_res";
		card_last_4="1234";
	    }
	    else if(!type_id.equals("")){ // 2, 3
		if(pay_type.equals("")){
		    pay_type="Dispute Resolution";
		}
	    }
	    if(reason.equals("Not Exist")){
		msg = handleNotExist();
	    }
	    else{
		msg = handleNotIssued();
	    }
	}
	return msg;
    }
    private String changeExpireDate(){
		
	String msg = "";
	getDispute();
	if(buck == null){
	    return "No buck ";
	}
	buck.setExpire_date(expire_date);
	msg = buck.doUpdate();
	if(msg.equals("")){
	    msg = updateDisputeAndRedemption();
	    if(!msg.equals("")){
		System.err.println(msg);
	    }
	}
	return msg;
    }
    private String handleTypo(){
	String back = "";
	Buck one = new Buck(debug, new_buck_id);
	back = one.doSelect();
	if(!back.equals("")){
	    back = "Not exist or not issued yet";
	    return back; // no such buck or not issued
	}
	buck_id = new_buck_id;
	return updateDisputeAndRedemption();
    }
    private String updateDisputeAndRedemption(){
	String msg = "";
	if(dispute == null){
	    return "No dispute found ";
	}
	String red_id = dispute.getRedeem_id();
	if(!red_id.isEmpty()){ 
	    dispute.setStatus("Resolved");
	    dispute.setUser_id(user_id);
	    msg = dispute.doUpdate();
	    if(msg.isEmpty()){
		msg = redeemBuck(buck_id, red_id);
	    }
	}
	else{
	    msg = "No redemption record found ";
	}
	return msg;
    }
    private String redeemBuck(String b_id, String red_id){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into redeem_bucks values(?,?)";
		
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, red_id);
	    pstmt.setString(2, b_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	    msg = "Error: This buck is probably already redeemed ";
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
    private String handleNotExist(){
	String msg = "";
	//
	// create a new batch that include this buck only
	//
	String batch_id="";
	Batch batch = new Batch(debug);
	batch.setConf_id(conf_id);
	batch.setBatch_size("1");
	batch.setStatus("Printed");
	batch.setStart_seq(buck_id);
	batch.setUser_id(user_id);
	msg = batch.doSave();

	if(msg.equals("")){
	    batch_id = batch.getId();
	}
	else{
	    System.err.println(msg);
	}
	Buck one = new Buck(debug);
	one.setId(buck_id);
	one.setValue(value);
	msg = one.doSave();
	if(!msg.equals("")){
	    return msg;
	}
	msg += handleNotIssued();
	return msg;
    }
    //
    // we issue a new Ebt or Gift
    // we mark with dispute_resolution flag to distinguish
    //
    private String handleNotIssued(){
	String msg = "";
	if(type_id.equals("1")){ // MB
	    Ebt ebt = new Ebt(debug);
	    approve = "dis_res";
	    ebt.setApprove(approve); // dispute resolution flag
	    card_last_4="1234";
	    ebt.setCard_last_4(card_last_4); // dummy card
	    ebt.setAmount(value);
	    ebt.setUser_id(user_id);
	    ebt.setBuck_id(buck_id);
	    ebt.setDispute_resolution("y");
	    ebt.setIncludeDouble("");
	    msg = ebt.doSave();
	    msg += ebt.addNewBuckToEbt();
	    getBuck();
	    if(buck != null){
		buck.setFund_type("ebt"); 
		buck.setExpire_date("12/01/"+Helper.getCurrentYear());
		msg += buck.doUpdate();
	    }
	}
	else{ // GC
	    Gift gift = new Gift(debug);
	    gift.setPay_type(pay_type); // Dispute Resolution
	    gift.setCheck_no(check_no);
	    gift.setAmount(value);
	    gift.setUser_id(user_id);
	    gift.setBuck_id(buck_id);
	    gift.setDispute_resolution("y");
	    msg = gift.doSave();			
	    msg += gift.addNewBuckToGift();
	}
	if(msg.equals("")){
	    msg = updateDisputeAndRedemption();
	    if(!msg.equals("")){
		System.err.println(msg);
	    }
	}
	return msg;
    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg = "";
	if(dispute_id.equals("")){
	    msg = "Dispute ID not set";
	    return msg;
	}
	msg = validateNProcess();
	if(!msg.equals("")){
	    status = "Failure";
	}
	else{
	    status = "Success";
	}
	date_time = Helper.getToday();
	String qq = "insert into resolutions values(0,?,?,?,?, ?,?,?,?,?,now(),?,?)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, dispute_id);
	    setParams(pstmt, 2);
	    pstmt.executeUpdate();
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
    String setParams(PreparedStatement pstmt, int c){
	String msg = "";
	try{
	    if(!conf_id.equals(""))
		pstmt.setString(c++, conf_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!value.equals(""))
		pstmt.setString(c++, value);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!approve.equals(""))
		pstmt.setString(c++, approve);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!card_last_4.equals(""))
		pstmt.setString(c++, card_last_4);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!pay_type.equals(""))
		pstmt.setString(c++, pay_type);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!check_no.equals(""))
		pstmt.setString(c++, check_no);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!expire_date.equals(""))
		pstmt.setDate(c++, new java.sql.Date(dateFormat.parse(expire_date).getTime()));
	    else
		pstmt.setNull(c++, Types.DATE);
	    if(!user_id.equals(""))
		pstmt.setString(c++, user_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!status.equals(""))
		pstmt.setString(c++, status);
	    else
		pstmt.setNull(c++, Types.VARCHAR);
	    if(!new_buck_id.equals(""))
		pstmt.setString(c++, new_buck_id);
	    else
		pstmt.setNull(c++, Types.VARCHAR);						
	}
	catch(Exception ex){
	    msg += ex;
	    logger.error(msg);
	}
	return msg;
    }
    //
    // update the fund_type only
    //
    public String doUpdate(){
	//
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update resolutions set conf_id=?,value=?,approve=?,card_last_4=?,pay_type=?,check_no=?,expire_date=?,user_id=?,date_time=now(),status=?,new_buck_id=? where id=?";

	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    setParams(pstmt, 1);
	    pstmt.setString(11, id);
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
	String qq = "delete from resolutions where id=?";

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
    String doRefresh(){
	String msg = "";
		
	return msg;
    }
    public String doSelect(){
		
	String qq = "select r.id,r.dispute_id,r.conf_id,r.value,r.approve,r.card_last_4,r.pay_type,r.check_no,date_format(r.expire_date,'%m/%d/%Y'),r.user_id,date_format(date_time,'%m/%d/%Y %H:%i'),r.status,r.new_buck_id from resolutions r where r.id=? ";
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
			  rs.getString(8),
			  rs.getString(9),
			  rs.getString(10),
			  rs.getString(11),
			  rs.getString(12),
			  rs.getString(13)
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





































