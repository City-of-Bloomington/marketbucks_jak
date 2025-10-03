/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.model;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Vendor implements java.io.Serializable{

    String lname="", fname="", vendor_num="",
	id="", business_name="",
	fullName="", active="y",
	payType="MB:GC";//MB:GC, GC // Farmer Market:MB, GC, A Fair of Arts: GC
    boolean debug = false;
    static final long serialVersionUID = 132L;		
    static Logger logger = LogManager.getLogger(Vendor.class);
    String errors = "";
    public Vendor(){
    }		
    public Vendor(boolean deb){
	debug = deb;
    }
    public Vendor(boolean deb, String val){
	debug = deb;
	setId(val);
    }
    // for new vendor
    public Vendor(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4){
	debug = deb;
	setVendorNum(val);				
	setLname(val2);
	setFname(val3);
	setPayType(val4);

    }			
    public Vendor(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5){
	debug = deb;
	setId(val);
	setVendorNum(val2);				
	setLname(val3);
	setFname(val4);
	setPayType(val5);

    }	
    public Vendor(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
		  String val6,
		  boolean val7){
	debug = deb;
	setId(val);
	setVendorNum(val2);
	setLname(val3);
	setFname(val4);
	setBusinessName(val5);
	setPayType(val6);				
	setActive(val7);
		
    }

    //
    public void setDebug(){
	debug = true;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getVendorNum(){
	return vendor_num;
    }		
    public String getLname(){
	return lname;
    }
    public String getFname(){
	return fname;
    }
    public String getBusinessName(){
	return business_name;
    }		
    public String getPayType(){
	if(payType.equals("")){
	    return "-1";
	}
	return payType;
    }
    public String getPayTypeStr(){
	String ret = "";
	if(payType.indexOf("MB") > -1){
	    ret = "Market Bucks";
	}
	if(payType.indexOf("GC") > -1){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Gift Certificates";
	}				
	return ret;
    }
    public String getFullName(){
	if(fullName.equals("")){
	    fullName = lname;
	    if(!fname.equals("")){
		fullName += ", ";
		fullName += fname;
	    }
	}
	return fullName;
    }
    public String getCleanName(){
	String str = getFullName();
	if(str.length() > 20){
	    str = str.substring(0,20);
	}
	str = str.replace(",","");
	str = str.replace("(","");
	str = str.replace(")","");		
	str = str.replace("'","_");
	str = str.replace(" ","_");		
	return str;
    }
    /*
    // causes conflict with getActive()
    public boolean isActive(){
    return !active.equals("");
    }
    */
    public boolean isInActive(){
	return active.isEmpty();
    }		
    public boolean getActive(){
	return !active.isEmpty();
    }
    public String getActiveStr(){
	return active.isEmpty() ? "No":"Yes";
    }
		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setVendorNum(String val){
	if(val != null)
	    vendor_num = val;
    }		
    public void setLname (String val){
	if(val != null)
	    lname = val;
    }
    public void setFname (String val){
	if(val != null)
	    fname = val;
    }
    public void setBusinessName (String val){
	if(val != null)
	    business_name = val;
    }		
    public void setPayType (String val){
	if(val != null && !val.equals("-1")){
	    payType = val;
	}
	if(val == null || val.equals("-1")){
	    payType = "";
	}
    }	
    public void setActive (boolean val){
	if(val)
	    active = "y";
	else
	    active = "";
    }
    
    public String toString(){
	return getFullName();
    }
    public String getInfo(){
	String str = getFullName();
	if(!business_name.isEmpty()){
	    if(!str.isEmpty()){
		str +=", ";
	    }
	    str += business_name;
	}
	
	return str+", #: "+vendor_num;
    }    
    @Override
    public int hashCode() {
	int hash = 3, id_int = 0; 
	if(!vendor_num.equals("")){
	    try{
		id_int = Integer.parseInt(vendor_num);
	    }catch(Exception ex){}
	}
	hash = 53 * hash + id_int;
	return hash;
    }
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Vendor other = (Vendor) obj;
	return this.vendor_num.equals(other.vendor_num);
    }
    /**
     * check if this vendor is allowed to receive BK or GC
     */
    public boolean canReceive(String val){ // MB, GC
	return payType.indexOf(val) > -1;
    }
	
    public String doSelect(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select id,vendor_num,lname,fname,business_name,payType,active from vendors where id = ?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVendorNum(rs.getString(2));
		setLname(rs.getString(3));
		setFname(rs.getString(4));
		setBusinessName(rs.getString(5));
		setPayType(rs.getString(6)); // till we receive the update
		setActive(rs.getString(7) != null);

	    }
	    else{
		msg = "Not found";
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String doSave(){
	String msg="";
	PreparedStatement pstmt = null, pstmt2=null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "insert into vendors values(0,?,?,?,?,?,'y')";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, vendor_num);
	    pstmt.setString(2, lname);
	    if(business_name.isEmpty())
		business_name = lname;
	    if(!fname.equals("")){
		pstmt.setString(3, fname);
	    }
	    else
		pstmt.setNull(3, Types.VARCHAR);
	    pstmt.setString(4, business_name);
	    pstmt.setString(5, payType);
	    pstmt.executeUpdate();
	    Helper.databaseDisconnect(pstmt, rs);
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
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return msg;

    }
    public String doUpdate(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "update vendors set vendor_num=?,lname=?,fname=?,business_name=?, payType=?,active=? where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, vendor_num);
	    pstmt.setString(2, lname);
	    if(!fname.equals("")){
		pstmt.setString(3, fname);
	    }
	    else
		pstmt.setNull(3, Types.VARCHAR);
	    if(business_name.equals("")){
		business_name = lname;
	    }
	    pstmt.setString(4, business_name);
	    if(payType.equals(""))
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, payType);

	    if(!active.isEmpty())
		pstmt.setString(6, "y");
	    else
		pstmt.setNull(6, Types.CHAR);
	    pstmt.setString(7, id);
						
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;

    }		
    public String setAsInactive(){
		
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "update vendors set active = null where id=?";

	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    System.err.println(msg);
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;

    }	
    /*	
	find the vendors with the same vendor_num

	select count(*) cnt,business_name, vendor_num from vendors group by vendor_num having cnt > 1 order by lname;

	select * from redeems where vendor_id= the dup num
	change to old vendor_id
	delete the dup

    */
}
