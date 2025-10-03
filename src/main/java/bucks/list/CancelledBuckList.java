/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.utils.*;


public class CancelledBuckList implements java.io.Serializable{

    static final long serialVersionUID = 33L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(CancelledBuckList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String id="", user_id="", limit=" limit 30",
	which_date="b.date_time";
    String date_from="", date_to="", sortBy="b.id desc";
    List<CancelledBuck> bucks = null;
	
    public CancelledBuckList(){
    }	
    public CancelledBuckList(boolean val){
	debug = val;
    }
    public CancelledBuckList(boolean val,
			     String val2
			     ){
	debug = val;
	setId(val2);
    }	
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }

    public void setWhich_date(String val){
	if(val != null)
	    which_date = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public void setNoLimit(){
	limit = "";
    }
    //
    public String getId(){
	return id;
    }
    public String getUser_id(){
	return user_id;
    }
    public String getWhich_date(){
	return which_date;
    }
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getSortBy(){
	return sortBy ;
    }	
    public List<CancelledBuck> getBucks(){
	return bucks;
    }
    //
    public String find(){

	String qq = "select b.id, b.user_id,date_format(b.date_time,'%m/%d/%Y %H:%i')  ";
	String qf = "from cancelled_bucks b ";
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " b.id = ? ";
	}
	else {
	    if(!user_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.user_id = ? ";
	    }
	    if(!which_date.equals("")){
		if(!date_from.equals("")){
		    qw += which_date+" >= ? ";					
		}
		if(!date_to.equals("")){
		    qw += which_date+" <= ? ";					
		}
	    }
	}
	qq += qf;
	if(!qw.equals(""))
	    qq += " where "+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	qq += " "+limit;
	logger.debug(qq);
	//
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }
	    else{
		if(!user_id.equals("")){
		    pstmt.setString(jj++, user_id);				
		}
		if(!which_date.equals("")){
		    if(!date_from.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    }
		    if(!date_to.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    bucks = new ArrayList<CancelledBuck>();
	    while(rs.next()){
		CancelledBuck one =
		    new CancelledBuck(debug,
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3)
				      );
		bucks.add(one);
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




































