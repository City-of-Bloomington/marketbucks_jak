/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.list;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.utils.*;

public class VendorList{

    boolean debug = false;
    static final long serialVersionUID = 123L;		
    static Logger logger = LogManager.getLogger(VendorList.class);
    static String vendorsCheckUrl=null,
	vendorsDatabase=null,
	vendorsUser=null,
	vendorsPassword=null;
    List<Vendor> vendors = null;
    List<String> vendorNumbers = null;
    String id="", name = "", active="", vendor_num="", activeStatus="";
    public VendorList(){
    }		
    public VendorList(boolean deb){
	debug = deb;
    }
    public VendorList(boolean deb, String val){
	debug = deb;
	setVendorNum(val);
    }		
    public VendorList(boolean deb,
		      String val,
		      String val2,
		      String val3,
		      String val4
		      ){
	debug = deb;
	if(val != null){
	    vendorsCheckUrl = val;
	}
	if(val2 != null){
	    vendorsDatabase = val2;
	}
	if(val3 != null){
	    vendorsUser = val3;
	}
	if(val4 != null){
	    vendorsPassword = val4;
	}
    }		
    //
    // setters
    //
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }		
    public void setVendorNum(String val){
	if(val != null)
	    vendor_num = val;
    }		
    public void setActiveOnly(){
	active = "y";
    }
    public void setInactiveOnly(){
	active = "n";
    }
    public void setActiveStatus(String val){
	if(val != null && !val.equals("-1")){
	    if(val.equals("y"))
		setActiveOnly();
	    else
		setInactiveOnly();
	    activeStatus = val;
	}
    }
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getVendorNum(){
	return vendor_num;
    }
    public String getActiveStatus(){
	if(activeStatus.isEmpty())
	    return "-1";
	return activeStatus;
    }
    public List<Vendor> getVendors(){
	return vendors;
    }
    public List<String> getNwVendorNumbers(){
	return vendorNumbers;
    }
    public String find(){
	String msg = "";
	String qq = " select id,vendor_num,lname,fname,business_name,payType,active from vendors ";
	String qw = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
				
	if(!id.isEmpty()){
	    qw = " vendor_num = ? ";
	}
	else if(!vendor_num.isEmpty()){
	    qw = " vendor_num = ? ";
	}				
	else if(!name.isEmpty()){
	    qw = " lname like ? or fname like ? or business_name like ? ";
	}
	if(active.equals("y")){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " active is not null ";
	}
	else if(active.equals("n")){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " active is null ";
	}
	String qo = " order by lname ";
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += qo;
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect ";
	    return msg;
	}
	try{
	    vendors = new ArrayList<Vendor>();
	    vendorNumbers = new ArrayList<>();
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.isEmpty()){
		pstmt.setString(jj++, id);
	    }
	    else if(!vendor_num.isEmpty()){
		pstmt.setString(jj++, vendor_num);
	    }
	    else if(!name.isEmpty()){
		pstmt.setString(jj++, "%"+name+"%");
		pstmt.setString(jj++, "%"+name+"%");
		pstmt.setString(jj++, "%"+name+"%");								
	    }
	    rs = pstmt.executeQuery();	
	    while(rs.next()){
		Vendor one = new Vendor(debug,
					rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5),
					rs.getString(6),
					rs.getString(7) != null);
		vendors.add(one);
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
     * vsVendorType 1156:approved, tbd, unapproved
     * vsVendorCategory 1263:farmers market, ?:Affair of Arts
     */
    public String findNewVendors(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select VendorNumber, LastName, FirstName,vsVendorCategory from dbo.Vendor V JOIN dbo.CentralName N ON V.CentralNameID = N.CentralNameID where (V.vsVendorCategory=1263 or V.vsVendorCategory=1295) and V.ActiveFlag=1 and V.vsVendorType=1156 "; // approve ones, active only
	if(vendorsCheckUrl == null ||
	   vendorsDatabase == null ||
	   vendorsUser == null){
	    msg = "Could not connect to Database, url not available";
	    return msg;
	}
	logger.debug(qq);
	con = Helper.getNwConnection(vendorsCheckUrl,
				     vendorsDatabase,
				     vendorsUser,
				     vendorsPassword);
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    vendors = new ArrayList<>();
	    vendorNumbers = new ArrayList<>();
	    while(rs.next()){
		int vtype = rs.getInt(4);
		String pay="MB:GC";
		if(vtype == 1295){
		    pay = "GC";
		}
		String vendNum = rs.getString(1);
		Vendor one = new Vendor(debug, 
					vendNum,
					rs.getString(2),
					rs.getString(3),
					pay);
		if(!vendors.contains(one)){
		    vendors.add(one);
		}
		if(!vendorNumbers.contains(vendNum)){
		    vendorNumbers.add(vendNum);
		}
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
	
}
