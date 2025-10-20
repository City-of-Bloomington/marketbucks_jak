package bucks.list;
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
import javax.naming.*;
import javax.sql.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.utils.*;

public class BatchList implements java.io.Serializable{

    static final long serialVersionUID = 33L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(BatchList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String id="", status="", which_date="b.date", type_id="", seq_id="", conf_id="";

    String date_from="", date_to="", sortBy="b.id DESC";
    String limit = " 30 ";
	
    List<Batch> batches = null;
	
    public BatchList(){
    }	
    public BatchList(boolean val){
	debug = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setSeq_id(String val){
	if(val != null)
	    seq_id = val;
    }
    public void setConf_id(String val){
	if(val != null)
	    conf_id = val;
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
	if(val != null && !val.equals("-1"))
	    sortBy = val;
    }
    public void setStatus(String val){
	if(val != null && !val.equals("-1"))
	    status = val;
    }
    public void setNoLimit(){
	limit = "";
    }
    public void setLimit(String val){
	if(val != null)
	    limit = val;

    }
    public void setType_id(String val){
	if(val != null && !val.equals("-1"))
	    type_id = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getDate_from(){
	return date_from ;
    }
    public String getDate_to(){
	return date_to ;
    }
    public String getSeq_id(){
	return seq_id ;
    }    
    public String getSortBy(){
	if(sortBy.equals("")){
	    return "-1";
	}
	return sortBy ;
    }
    public String getStatus(){
	if(status.equals("")){
	    return "-1";
	}
	return status;
    }	
    public String getType_id(){
	if(type_id.equals("")){
	    return "-1"; // all
	}
	return type_id;
    }		
    public List<Batch> getBatches(){
	return batches;
    }
    //
    public String find(){

	String qq = "select b.id, b.conf_id,b.batch_size,b.status,b.start_seq,date_format(b.date,'%m/%d/%Y'),b.user_id ";		
	String qf = " from batches b ";
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
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.status = ? ";
	    }
	    if(!conf_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.conf_id = ? ";
	    }			
	    if(!type_id.equals("")){
		qf += ", buck_confs c ";
		if(!qw.equals("")) qw += " and ";
		qw += " c.type_id = ? and c.id=b.conf_id ";
	    }
	    if(!seq_id.equals("")){
		qf += ", buck_seq s ";
		if(!qw.equals("")) qw += " and ";
		qw += " s.id = ? and s.batch_id=b.id ";
	    }			
	    if(!which_date.equals("")){
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";					
		    qw += which_date+" >= ? ";					
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
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
	if(!limit.equals("")){
	    qq += " limit "+limit;
	}				
	logger.debug(qq);
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
		if(!status.equals("")){
		    pstmt.setString(jj++, status);				
		}
		if(!conf_id.equals("")){
		    pstmt.setString(jj++, conf_id);				
		}				
		if(!type_id.equals("")){
		    pstmt.setString(jj++, type_id);				
		}
		if(!seq_id.equals("")){
		    pstmt.setString(jj++, seq_id);				
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
	    batches = new ArrayList<Batch>();
	    while(rs.next()){
		Batch one = new Batch(debug,
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4),
				      rs.getString(5),
				      rs.getString(6),
				      rs.getString(7)
				      );
		batches.add(one);
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





































