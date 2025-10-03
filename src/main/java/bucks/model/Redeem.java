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
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Redeem implements java.io.Serializable{

    static final long serialVersionUID = 13L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Redeem.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id=""; 
    String vendor_id="", vendor_num ="", user_id="",date_time="", notes="";
    String buck_id="", status="Open";
    //
    // we use this flag to know if we need to check for
    // expired bucks, since there is difference in the expire dates on
    // MB configure file and expire date expression stated on the buck
    // we are disabling this check for now
    //
    boolean ignore_expire_date = false;
    int count = 0;
    List<Buck> bucks = null;
    List<Buck> bk_bucks = null;
    List<Buck> gc5_bucks = null;
    List<Buck> gc20_bucks = null;
    List<Buck> gc_bucks = null; // gc5 + gc20
    List<Dispute> disputes = null;
    List<String> buck_ids = null;
    Vendor vendor = null;
    User user = null;
    public Redeem(){
    }	
    public Redeem(boolean val){
	debug = val;
    }
    public Redeem(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Redeem(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
		  String val6,
		  String val7,
		  String val8,
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
		   String val8,
		   String val9,
		   String val10
		   ){
	setId(val);
	setDate_time(val2);
	setVendor_id(val3);
	setUser_id(val4);
	if(!vendor_id.equals("")){
	    vendor = new Vendor(debug, vendor_id, val10, val5, val6, val7);
	}
	setStatus(val8);
	setNotes(val9);
    }
    @StrutsParameter(depth=1)
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setVendor_id(String val){
	if(val != null)
	    vendor_id = val;
    }
    public void setVendorNum(String val){
	if(val != null)
	    vendor_num = val;
    }
    public void setVendorNum2(String val){
	if(val != null && !val.equals("-1")){
	    if(vendor_num.isEmpty()){
		vendor_num = val;
	    }
	}
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
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setIgnore_expire_date(boolean val){
	ignore_expire_date = val;
    }
    // @StrutsParameter(depth=1)
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getVendor_id(){
	return vendor_id;
    }
    public String getDate_time(){
		
	return date_time;
    }
    public String getDate(){
	return date_time.substring(0,10);
    }
    public String getUser_id(){
		
	return user_id;
    }
    public String getStatus(){
		
	return status;
    }
    public String getCount(){
		
	return ""+count;
    }
    public String getNotes(){
		
	return notes;
    }
    public boolean hasNotes(){
		
	return !notes.equals("");
		
    }
    public boolean hasBucks(){
	getBucks();
	return bucks != null && bucks.size() > 0;
		
    }    
    public boolean canFinalize(){
	if(!status.equals("Open")) return false;
	return getTotalInt() > 0;
    }
    /**
     * we can cancel only if no bucks were redeemed
     */
    public boolean canCancel(){
	return getTotalInt() == 0;

    }
    public Vendor getVendor(){
	if(!vendor_id.equals("") && vendor == null){
	    Vendor vv = new Vendor(debug, vendor_id);
	    String back = vv.doSelect();
	    if(back.equals("")){
		vendor = vv;
	    }
	}
	return vendor;
    }
    public String findVendor(){
	String back = "";
	if(!vendor_num.equals("")){
	    VendorList vl = new VendorList(debug, vendor_num);
	    // we will check later if the vendor is inactive 
	    back = vl.find();
	    if(back.equals("")){
		List<Vendor> ones = vl.getVendors();
		if(ones != null && ones.size() > 0){
		    vendor = ones.get(0);
		    vendor_id = vendor.getId();
		}
	    }
	}
	return back;
    }
    public User getRedeemUser(){
	return getUser();
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
    public boolean hasDisputes(){
	if(disputes == null){
	    getDisputes();
	}
	return disputes != null;
    }
    public List<Dispute> getDisputes(){
	if(disputes == null){
	    DisputeList dl  = new DisputeList(debug, id);
	    dl.setUnresolved();
	    String back = dl.find();
	    if(back.equals("")){
		List<Dispute> list = dl.getDisputes();
		if(list != null && list.size() > 0){
		    disputes = list;
		}
	    }
	}
	return disputes;
    }
    public String toString(){
	return id+" "+getVendor()+" "+date_time;
    }
    public String getTotal(){
	int total = 0;
	if(bucks == null){
	    getBucks();
	}
	if(bucks != null){
	    for(Buck one:bucks){
		total += one.getValue_int();
	    }
	}
	return ""+total;
    }
    public int getTotalInt(){
	int total = 0;
	if(bucks == null){
	    getBucks();
	}
	if(bucks != null){
	    for(Buck one:bucks){
		total += one.getValue_int();
	    }
	}
	return total;
    }
    public List<Buck> getBucks(){
	if(!id.equals("") && bucks == null){
	    BuckList bl = new BuckList(debug, null, null, id, null, null);// redeem.id
	    bl.setSortBy("b.id ASC");
	    String back = bl.find();
	    if(back.equals("")){
		bucks = bl.getBucks();
		count = bucks.size();
	    }
	    else{
		logger.error(back);
	    }
	}
	return bucks;
    }
    public List<Buck> getBk_bucks(){
	if(bucks == null)
	    getBucks();
	if(bk_bucks == null)
	    classifyBucks();
	return bk_bucks;
    }
    public List<Buck> getGc5_bucks(){
	if(bucks == null)
	    getBucks();		
	if(gc5_bucks == null)
	    classifyBucks();
	return gc5_bucks;
    }
    public List<Buck> getGc20_bucks(){
	if(bucks == null)
	    getBucks();
	if(gc20_bucks == null)
	    classifyBucks();
	return gc20_bucks;
    }
    public List<Buck> getGc_bucks(){
	if(bucks == null)
	    getBucks();
	if(gc_bucks == null)
	    classifyBucks();
	return gc_bucks;
    }	
    private void classifyBucks(){
	bk_bucks = new ArrayList<Buck>(); // ebt and rx
	gc5_bucks = new ArrayList<Buck>();
	gc20_bucks = new ArrayList<Buck>();
	gc_bucks = new ArrayList<Buck>();
	buck_ids = new ArrayList<String>();
	if(bucks != null && bucks.size() > 0){
	    for(Buck one:bucks){
		String type_id = one.getBuck_type_id();
		if(type_id.equals("1"))
		    bk_bucks.add(one);
		else if(type_id.equals("2")){
		    gc5_bucks.add(one);
		    gc_bucks.add(one);
		}
		else if(type_id.equals("3")){
		    gc20_bucks.add(one);
		    gc_bucks.add(one);
		}
	    }
	}
    }
    public String redeemBuck(){
	String msg = "";
	if(id.equals("")){
	    msg = "Redeem id not set ";
	    return msg;
	}
	if(buck_id.equals("")){
	    msg = "no bucks set for Redeem ";
	    return msg;
	}
	// check if the buck is expired or redeemed before
	//
	Buck buck = new Buck(debug, buck_id);
	//
	if(bucks == null){
	    getBucks();
	}
	if(bucks != null){
	    //
	    // if we already redeemed this buck we just ignore it
	    //
	    if(bucks.contains(buck)) return "Already redeemed"; 
	}
	Dispute dispute = new Dispute(debug);
	dispute.setBuck_id(buck_id);
	dispute.setRedeem_id(id);
	dispute.setUser_id(user_id);		
	//

	msg = buck.doSelect();
	if(!msg.isEmpty()){
	    if(!dispute.isInDispute()){
		dispute.setReason("Not Exist");
		// dispute.setSuggestions("Add this MB or GC by using Generate & Print. No need for printing");				
		dispute.setStatus("Waiting");
		String back = dispute.doSave();
		msg = " This buck is not in the system. This buck is added to the dispute list.";
	    }
	    else{
		msg= "This buck is not in the system yet and is still in the dispute list";
	    }			
	    return msg;
	}
	if(msg.isEmpty()){
	    msg = buck.findOtherBuckInfo();
	}
	if(!buck.isIssued()){
	    if(!dispute.isInDispute()){
		dispute.setReason("Not Issued");
		// dispute.setSuggestions("Need to issue this MB or GC");
		dispute.setStatus("Waiting");
		String back = dispute.doSave();			
		msg = "This buck or GC is not issued yet and cannot be redeemed. This Buck is added to the dispute list ";
		return msg;
	    }
	    else{
		msg = "This buck or GC is not issued yet and cannot be redeemed. This Buck is still in the dispute list ";
		return msg;
	    }
						
	}
	// expire date is written on the buck, it is not the same
	// as the expire date in the config file, therefore we
	// are not going to check for expire date and let the employee
	// check for that (for now).
	if(!ignore_expire_date){
	    if(buck.isExpired()){
		//
		// check if the buck is in the disputed list
		//
		if(!dispute.isInDispute()){
										
		    dispute.setReason("Expired");
		    // dispute.setSuggestions("Change the expire date in the configuration ID "+buck.getConf_id());
		    dispute.setStatus("Waiting");
		    String back = dispute.doSave();
		    msg = " This buck is expired since "+buck.getDays_to_expire()+" days. This buck is added to the dispute list.";
		    return msg;				
		}
		else{
		    return "This buck is expired since "+buck.getDays_to_expire()+" and is still in the dispute list";
		}
	    }
	}
	if(buck.isVoided()){
	    if(!dispute.isInDispute()){

		dispute.setReason("Voided");
		// dispute.setSuggestions("Voided can not be redeemed "+buck.getConf_id());
		dispute.setStatus("Rejected");
		String back = dispute.doSave();
		msg = " This MB/GC is voided and can not be redeemed ";
		return msg;				
	    }
	    else{
		msg= "This MB/GC is voided and is already in the dispute list";
		return msg;
	    }
	}
	if(!buck.exists()){
	    if(!dispute.isInDispute()){
		dispute.setReason("Not Exist");
		// dispute.setSuggestions("This MB or GC need to be generate and then issue it.");				
		dispute.setStatus("Waiting");
		String back = dispute.doSave();
		msg = " This buck is not in the system. This buck is added to the dispute list.";
		return msg;				
	    }
	    else{
		return "This buck is not in the system yet and is still in the dispute list";
				
	    }
	}
	/*
	  boolean exist = false;
	  if(true){
	  BuckList  bl = new BuckList(debug);
	  bl.setId(buck_id);
	  msg = bl.findEbtGiftBucks();
	  if(msg.equals("")){
	  List<Buck> bls = bl.getBucks();
	  if(bls != null && bls.size() > 0){
	  exist = true;
	  }
	  }
	  }
	  if(!exist){
	  if(!dispute.isInDispute()){
	  dispute.setReason("Not Issued");
	  // dispute.setSuggestions("Need to issue this MB or GC");
	  dispute.setStatus("Waiting");
	  String back = dispute.doSave();			
	  msg = "This buck or GC is not issued yet and cannot be redeemed. This Buck is added to the dispute list ";
	  return msg;
	  }
	  else{
	  msg = "This buck or GC is not issued yet and cannot be redeemed. This Buck is still in the dispute list ";
	  return msg;
	  }
	  }
	*/
	if(buck.isRedeemed()){
	    msg = "This buck or GC is already redeemed. ";
	    // we do not do anything for already redeemed bucks						
	    return msg;
	}
	if(vendor == null){
	    getVendor();
	}
	if(vendor != null){
	    String type_id = buck.getBuck_type_id();
	    String pay = "GC";
	    if(type_id.equals("1")){
		pay= "MB";
	    }
	    if(!vendor.canReceive(pay)){
		msg = "This vendor can not redeem "+pay;
		return msg;
	    }
	}
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
	    pstmt.setString(1, id);
	    pstmt.setString(2, buck_id);
	    count++;
	    pstmt.executeUpdate();
	    bucks.add(buck);
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
    public boolean isVendorInactive(){
	if(vendor == null){
	    findVendor();
	}
	return vendor != null && vendor.isInActive();
    }
    public boolean isVendorAvailable(){
	if(vendor == null){
	    findVendor();
	}
	return vendor != null && !vendor.isInActive();
    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String msg = "";
	date_time = Helper.getToday();
	if(vendor_id.equals("")){
	    msg = "vendor not set ";
	    return msg;
	}
	if(user_id.equals("")){
	    msg = "user not set ";
	    return msg;
	}		
	String qq = "insert into redeems values(0,now(),?,?,'Open',null)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, vendor_id);
	    pstmt.setString(2, user_id);
	    pstmt.executeUpdate();
	    //
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
    public String doFinalize(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	date_time = Helper.getToday();

	String qq = "update redeems set status = 'Completed' where id=?";
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
	    status="Completed";
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
    public String updateNotes(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	System.err.println(" updating notes ");
	String qq = "update redeems set notes = ? where id=?";
	System.err.println(" notes "+notes);
	System.err.println(" id "+id);	
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    if(notes.equals(""))
		pstmt.setNull(1, Types.VARCHAR);
	    else
		pstmt.setString(1, notes);
	    pstmt.setString(2, id);
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
    public String doCancel(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";

	String qq = "delete from redeems where id=?";
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
    //
    public String doSelect(){
		
	String qq = "select r.id, date_format(r.date_time,'%m/%d/%Y %H:%i'),r.vendor_id,r.user_id,v.lname,v.fname,v.payType,r.status,r.notes,v.vendor_num from redeems r join vendors v on v.id=r.vendor_id where r.id=? ";
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





































