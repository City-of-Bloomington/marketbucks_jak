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

public class Inventory implements java.io.Serializable{

    static final long serialVersionUID = 27L;
    static final int GCThreshold = 400; //GC should not be less than
    static final int MBThreshold = 2000; //MB should not be less than 	
    boolean debug = false;
    static Logger logger = LogManager.getLogger(Inventory.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Hashtable<String, Integer> table = new Hashtable<String, Integer>(3);
    String message = "";
    boolean isInventoryCritical = false;
    public Inventory(){
    }	
    public Inventory(boolean val){
	debug = val;
    }
    void setValues(
		   String val,
		   String val2,
		   String val3
		   ){
    }

    public String getMessage(){
	return message;
    }

    public String toString(){
	return " ";
    }
    public boolean isInventoryCritical(){
	return isInventoryCritical;
    }
    String find(){
		
	String qq = "",qq2="",qq3="",qw="",qw2="",qw3="", qg="", msg="";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	ResultSet rs = null;
	String which_date = "b.date ";
	qq = " select t.name type,count(*) amount from buck_seq s, batches b, buck_confs c,buck_types t ";
	qw = " where b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' ";		
	qq2 = " select t.name type,count(*) amount from gift_bucks eb, buck_seq s, batches b, buck_confs c,buck_types t,gifts e ";
	qw2 = " where eb.buck_id=s.id and b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' and e.id=eb.gift_id ";		
	qq3 = " select t.name type,count(*) amount from ebt_bucks eb, buck_seq s, batches b, buck_confs c,buck_types t,ebts e ";
	qw3 = " where eb.buck_id=s.id and b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' and e.id = eb.ebt_id ";
	qg = " group by type having amount > 0 order by type ";
	//
	// start of printing and issuing of printed MB and GC
	String year = "2015-01-01"; 
	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";				
		qw2 += " and ";
		qw3 += " and ";				
	    }
	    qw += which_date+" > '"+year+"'";
	    qw2 += which_date+" > '"+year+"'";
	    qw3 += which_date+" > '"+year+"'";			
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw2;
	    qq3 += qw3;			
	}
		
	qq += qg;
	qq2 += qg;
	qq3 += qg;		

	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);			

	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
						
	    rs = pstmt.executeQuery();
	    int val=0, old_val=0;
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		table.put(str, new Integer(val));
	    }
	    rs = pstmt2.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		if(table.containsKey(str)){
		    old_val = table.get(str).intValue();
		    old_val = old_val - val;
		    table.put(str, new Integer(old_val));
		}
	    }
	    //
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		if(table.containsKey(str)){
		    old_val = table.get(str).intValue();
		    old_val = old_val - val;
		    table.put(str, new Integer(old_val));
		}
	    }
	    //
	    // current inventory
	    //
	    List<String> list = new ArrayList<String>(table.keySet());
	    Collections.sort(list);
	    message = "Current inventory as of "+Helper.getToday()+"\n";
	    for(String key:list){
		val = table.get(key).intValue();
		message += key+" "+val+"\n";
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}		
	return msg;

    }	
    public String checkInventory(){
	String msg = "";
	msg = find();
	if(!msg.equals("")) return msg;
	//
	if(table == null || table.size() == 0){
	    msg = " could not find invetory";
	    return msg;
	}
	List<String> list = new ArrayList<String>(table.keySet());
	Collections.sort(list);
	for(String key:list){
	    int val = table.get(key).intValue();
	    if(key.startsWith("GC")){
		if(val < GCThreshold){
		    message += key+" "+val+" is below inventory threshold of "+GCThreshold+"\n";
		    isInventoryCritical = true;
		}
	    }
	    else if(key.startsWith("MB")){
		if(val < MBThreshold){
		    message += key+" "+val+" is below inventory threshold of "+MBThreshold+"\n";
		    isInventoryCritical = true;
		}
	    }
	}
	return msg;
    }
}





































