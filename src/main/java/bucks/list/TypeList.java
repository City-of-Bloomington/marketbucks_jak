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

public class TypeList{

    String whichList ="vendors";
    boolean debug = false;
    static final long serialVersionUID = 120L;		
    static Logger logger = LogManager.getLogger(TypeList.class);
    List<Type> types = null;
		
    String name = "", sortBy="name";
    public TypeList(boolean deb){
	debug = deb;
    }	
    public TypeList(boolean deb, String val){
	debug = deb;
	setWhichList(val);
    }
    //
    // setters
    //
    public void setWhichList(String val){
	if(val != null)
	    whichList = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }		
    public List<Type> getTypes(){
	return types;
    }
    public String find(){
	String msg = "";
	String qq = " select id,name from "+whichList;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(!name.equals("")){
	    qq += " where name like ? ";
	}
	String qo = " order by "+sortBy;
	qq += qo;
	logger.debug(qq);
	try{
	    types = new ArrayList<Type>();
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    if(!name.equals("")){
		pstmt.setString(1, "%"+name+"%");
	    }
	    rs = pstmt.executeQuery();	
	    while(rs.next()){
		Type one = new Type(debug, rs.getString(1), rs.getString(2));
		types.add(one);
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	    System.err.println(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
			
    }

	
}
