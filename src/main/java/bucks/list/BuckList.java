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

public class BuckList implements java.io.Serializable{

    static final long serialVersionUID = 30L;	
   
    boolean debug = false;
    static Logger logger = LogManager.getLogger(BuckList.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String id="", ebt_id="", redeem_id="", export_id="", sweep_id="",
	gift_id="", fund_type="", buck_id="", batch_id="", limit="",
	which_date="bs.date", // batch date
	rx_id="", wic_id="",senior_id="",
	type="";  // issued/unissued
    boolean ebtOrGiftFlag = false;
    String date_from="", date_to="", sortBy="b.id desc";
    List<Buck> bucks = null;
	
    public BuckList(){
    }	
    public BuckList(boolean val){
	debug = val;
    }
    public BuckList(boolean val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6
		    ){
	debug = val;
	setEbt_id(val2);
	setGift_id(val3);
	setRedeem_id(val4);
	setExport_id(val5);
	setSweep_id(val6);
    }	
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setEbt_id(String val){
	if(val != null)
	    ebt_id = val;
    }
    public void setRx_id(String val){
	if(val != null)
	    rx_id = val;
    }
    public void setWic_id(String val){
	if(val != null)
	    wic_id = val;
    }
    public void setSenior_id(String val){
	if(val != null)
	    senior_id = val;
    }		
    public void setGift_id(String val){
	if(val != null)
	    gift_id = val;
    }	
    public void setRedeem_id(String val){
	if(val != null)
	    redeem_id = val;
    }
    public void setExport_id(String val){
	if(val != null)
	    export_id = val;
    }
    public void setSweep_id(String val){
	if(val != null)
	    sweep_id = val;
    }	
    public void setFund_type(String val){
	if(val != null)
	    fund_type = val;
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
    public void setBatch_id(String val){
	if(val != null)
	    batch_id = val; 
    }				
    public void setType(String val){
	if(val != null && !val.isEmpty() && !val.equals("-1"))
	    type = val; // issued/unissued
    }		
    public void setEbtOrGiftFlag(){
	ebtOrGiftFlag = true;
    }
    public void setNoLimit(){
	limit = "";
    }
    //
    public String getId(){
	return id;
    }
    public String getEbt_id(){
	return ebt_id;
    }
    public String getRx_id(){
	return rx_id;
    }
    public String getWic_id(){
	return wic_id;
    }
    public String getSenior_id(){
	return senior_id;
    }
    public String getBatch_id(){
	return batch_id;
    }		
    public String getGift_id(){
	return gift_id;
    }	
    public String getRedeem_id(){
	return redeem_id;
    }	
    public String getFund_type(){
	return fund_type;
    }
    public String getExport_id(){
	return export_id;
    }
    public String getSweep_id(){
	return sweep_id;
    }	
    public String getWhich_date(){
	return which_date; // bs.date is batch date
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
    public String getType(){
	if(type.equals("")) return "-1";
	return type;
    }
    public List<Buck> getBucks(){
	return bucks;
    }
    //
    public String find(){

	String qq = "select b.id, b.value,b.fund_type,c.type_id,date_format(b.expire_date,'%m/%d/%Y'),if(b.expire_date is not null, datediff(b.expire_date, now()),null),b.voided ";
	String qf = "from bucks b "+
	    "join buck_seq s on s.id=b.id "+
	    "join batches bs on bs.id=s.batch_id "+
	    "join buck_confs c on bs.conf_id=c.id ";
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
	    if(!fund_type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.fund_type = ? ";
	    }
	    if(!batch_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " bs.id = ? ";
	    }						
	    if(!ebt_id.equals("")){
		qf += " join ebt_bucks e ";
		if(!qw.equals("")) qw += " and ";				
		qw += " e.buck_id = b.id and e.ebt_id=? ";
	    }
	    if(!rx_id.equals("")){
		qf += " join rx_bucks r ";
		if(!qw.equals("")) qw += " and ";				
		qw += " r.buck_id = b.id and r.rx_id=? ";
	    }
	    if(!wic_id.equals("")){
		qf += " join wic_bucks r ";
		if(!qw.equals("")) qw += " and ";				
		qw += " r.buck_id = b.id and r.wic_id=? ";
	    }
	    if(!senior_id.equals("")){
		qf += " join senior_bucks r ";
		if(!qw.equals("")) qw += " and ";				
		qw += " r.buck_id = b.id and r.senior_id=? ";
	    }						
	    if(!gift_id.equals("")){
		qf += " join gift_bucks g ";
		if(!qw.equals("")) qw += " and ";				
		qw += " g.buck_id = b.id and g.gift_id=? ";
	    }			
	    if(!redeem_id.equals("")){
		qf += " join redeem_bucks r ";
		if(!qw.equals("")) qw += " and ";				
		qw += " r.buck_id = b.id and r.redeem_id=? ";
	    }
	    if(!export_id.equals("")){
		qf += "join export_bucks x ";
		if(!qw.equals("")) qw += " and ";				
		qw += " x.buck_id = b.id and x.export_id=? ";
	    }
	    if(!sweep_id.equals("")){
		qf += " join sweep_bucks ss ";
		if(!qw.equals("")) qw += " and ";				
		qw += " ss.buck_id = b.id and ss.sweep_id=? ";
	    }
	    if(type.equals("unissued")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.id not in (select buck_id from ebt_bucks union select buck_id from gift_bucks) ";
	    }
	    else if(type.equals("issued")){
		if(!qw.equals("")) qw += " and ";
		qw += " b.id in (select buck_id from ebt_bucks union select buck_id from gift_bucks) ";
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
		if(!fund_type.equals("")){
		    pstmt.setString(jj++, fund_type);				
		}
		if(!batch_id.equals("")){
		    pstmt.setString(jj++, batch_id);				
		}								
		if(!ebt_id.equals("")){
		    pstmt.setString(jj++, ebt_id);				
		}
		if(!rx_id.equals("")){
		    pstmt.setString(jj++, rx_id);				
		}
		if(!wic_id.equals("")){
		    pstmt.setString(jj++, wic_id);				
		}
		if(!senior_id.equals("")){
		    pstmt.setString(jj++, senior_id);				
		}
		if(!gift_id.equals("")){
		    pstmt.setString(jj++, gift_id);				
		}				
		if(!redeem_id.equals("")){
		    pstmt.setString(jj++, redeem_id);				
		}
		if(!export_id.equals("")){
		    pstmt.setString(jj++, export_id);				
		}
		if(!sweep_id.equals("")){
		    pstmt.setString(jj++, sweep_id);				
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
	    bucks = new ArrayList<Buck>();
	    while(rs.next()){
		Buck one = new Buck(debug,
				    rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4),
				    rs.getString(5),
				    rs.getString(6),
				    rs.getString(7)
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




































