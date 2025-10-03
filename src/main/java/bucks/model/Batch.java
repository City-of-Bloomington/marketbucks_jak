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
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Batch implements java.io.Serializable{

    static final long serialVersionUID = 19L;	
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Batch.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    int batch_size = 0, last_seq_printed = 0, pages = 0;
    String id="", status="Waiting", date="", user_id="",
	type="buck", conf_id="";
    int start_seq = 0, end_seq = 0;
    List<String> seq_list = null;
    User user = null;
    BuckConf conf = null;
    public Batch(){
    }	
    public Batch(boolean val){
	debug = val;
    }
    public Batch(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public Batch(boolean deb,
		 String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 String val6,
		 String val7
		 ){
	setValues( val,
		   val2,
		   val3,
		   val4,
		   val5,
		   val6,
		   val7
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
		   String val7
		   ){
	setId(val);
	setConf_id(val2);
	setBatch_size(val3);
	setStatus(val4);
	setStart_seq(val5);
	setDate(val6);
	setUser_id(val7);
    }	
    public void setPages(String val){
	if(val != null){
	    try{
		pages = Integer.parseInt(val);
		batch_size = pages * 3;
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setBatch_size(String val){
	if(val != null){
	    try{
		batch_size = Integer.parseInt(val);
		pages = batch_size/3;
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setLast_seq_printed(String val){
	if(val != null){
	    try{
		last_seq_printed = Integer.parseInt(val);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setConf_id(String val){
	if(val != null)
	    conf_id = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }	
    public void setStart_seq(String val){
	if(val != null){
	    try{
		start_seq = Integer.parseInt(val);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }	
    //
    public String getId(){
	return id;
    }
    public String getConf_id(){
	return conf_id;
    }
    public String getBatch_size(){
		
	return ""+batch_size;
    }
    public String getPages(){
		
	return ""+pages;
    }	
    public int getBatch_size_int(){
		
	return batch_size;
    }	
    public String getStart_seq(){
	if(start_seq == 0){
	    if(!id.equals(""))
		findStartSeq();
	    else
		findNextStartSeq();
	}
	return ""+start_seq;
    }
    public int getStart_seq_int(){
	return start_seq;
    }
    public String getEnd_seq(){
	if(seq_list == null){
	    getSeq_list();
	}
	return ""+end_seq;
    }
    public void findStartSeq(){
	if(seq_list == null){
	    getSeq_list();
	}
    }	
    public String getStatus(){
	return status;
    }
    public String getDate(){
	return date;
    }
    public String getUser_id(){
	return user_id;
    }	
    public List<String> getSeq_list(){
	if(seq_list == null && !id.equals("")){
	    findBuckListSeq();
	}
	return seq_list;
    }
    public BuckConf getConf(){
	if(conf == null && !conf_id.equals("")){
	    BuckConf one = new BuckConf(debug, conf_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		conf = one;
	    }
	}
	return conf;
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
    public String getValue(){
	String value="";
	if(conf == null){
	    getConf();
	}
	if(conf != null){
	    value = conf.getValue();
	}
	return value;
    }
    public int getTotal(){
	String val = getValue();
	int total = 0;
	try{
	    total = batch_size * (Integer.parseInt(val));
	}catch(Exception ex){
	    System.err.println(ex);
	}
	return total;
    }
    public Type getType(){
	Type type = null;
	if(conf == null){
	    getConf();
	}
	if(conf != null){
	    type = conf.getType();
	}
	return type;

    }
    public String toString(){
	return id+" "+batch_size+" "+status;
    }
    public String findNextStartSeq(){
	String msg = "";
	if(!id.equals("") || start_seq > 0) return msg;
	String qq = " select max(id)+1 from buck_seq ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		start_seq = rs.getInt(1);
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
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(batch_size < 1){
	    msg = " Requested batch size not set";
	    return msg;
	}
	date = Helper.getToday();
	String qq = "insert into batches values(0,?,?,?,?,now(),?)";
	String qs = "select LAST_INSERT_ID() ";
	String qq2 = "insert into buck_seq values(?,?)";
	// if(debug)
	logger.debug(qs);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, conf_id);
	    pstmt.setInt(2, batch_size);
	    pstmt.setString(3, status);
	    pstmt.setInt(4, start_seq);
	    pstmt.setString(5,user_id);
	    pstmt.executeUpdate();
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qs);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id  = rs.getString(1);
	    }
	    if(!id.equals("")){
		con.setAutoCommit(false);
		pstmt = con.prepareStatement(qq2);				
		try{
		    for(int i=0;i<batch_size;i++){
			pstmt.setInt(1, (start_seq+i));
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			con.commit();						
		    }
		}catch(Exception ex2){
		    msg += ex2+": "+qq2;
		    if(con != null){
			con.rollback();
		    }
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
	return msg;
    }
    public String doInsert(){
	String msg = "";
	msg = insertBucks();
	if(msg.equals("")){
	    msg = updateStatus();
	}
	return msg;
    }
    public String insertBucks(){

	String msg = "", value="";
		
	if(seq_list == null){
	    findBuckListSeq();
	}
	if(conf == null){
	    getConf();
	}
	if(conf != null){
	    value = conf.getValue();
	}		
	if(seq_list == null || seq_list.size() < 1){
	    msg = "No record found to be inserted ";
	    return msg;
	}
	if(value.equals("")){
	    msg = "Value not set ";
	    return msg;			
	}
	Buck bb = new Buck(debug);
	bb.setValue(value);
	msg = bb.insertBucks(seq_list);
	if(msg.equals("")){
	    status = "Printed";
	}
	return msg;
    }
    /**
     * if we printed less than we supposed to
     * we change the size and delete some of the sequences
     */ 
    public String updateStatus(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = "update batches set status=?, batch_size=? where id=? ";
	String qq2 = "delete from buck_seq where batch_id = ? and id > ? ";
	date = Helper.getToday();
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, status);
	    pstmt.setInt(2, batch_size);
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	    if(last_seq_printed > 0){
		pstmt = con.prepareStatement(qq2);
		pstmt.setString(1, id);
		pstmt.setInt(2, last_seq_printed);
	    }
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
     * given the batch_id and last seq we will create the buck records
     */
    public String findBuckListSeq(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";		
	if(id.equals("")){
	    msg = "Batch id not set ";
	    return msg;
	}
	String qq = "select id from buck_seq where batch_id=?";
	if(last_seq_printed > 0){
	    qq += " and id <= ? ";
	}
	//if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    if(last_seq_printed > 0){
		pstmt.setInt(2, last_seq_printed);
	    }			
	    rs = pstmt.executeQuery();
	    seq_list = new ArrayList<String>();
	    int jj = 0;
	    while(rs.next()){
		int seq = rs.getInt(1);
		if(jj == 0){
		    start_seq = seq;
		}
		else
		    end_seq = seq;
		seq_list.add(""+seq);
		jj++;
	    }
	    batch_size = seq_list.size();
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
    public String doSelect(){
		
	String qq = "select b.id, b.conf_id ,b.batch_size, b.status,b.start_seq,date_format(b.date,'%m/%d/%Y'),b.user_id from batches b where b.id=? ";
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
			  rs.getString(7)
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





































