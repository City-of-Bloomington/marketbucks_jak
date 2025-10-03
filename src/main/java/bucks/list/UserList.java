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

public class UserList{

    boolean debug = false;
    static final long serialVersionUID = 1120L;		
    static Logger logger = LogManager.getLogger(UserList.class);
    String fullName = "";
    List<User> users = null;
    String name = "";

    public UserList(){
    }	
    public UserList(boolean deb){
	debug = deb;
    }	
    //
    // setters
    //
    public List<User> getUsers(){
	return users;
    }
    public String find(){
	String msg = "";
	String qq = " select id,userid,fullname,role,inactive from users ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(!fullName.equals("")){
	    qq += " where fullname like ? ";
	}
	String qo = " order by fullname ";
	qq += qo;
	logger.debug(qq);
	try{
	    users = new ArrayList<User>();
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    if(!fullName.equals("")){
		pstmt.setString(1, "%"+fullName+"%");
	    }
	    rs = pstmt.executeQuery();	
	    while(rs.next()){
		User one = new User(debug,
				    rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4),
				    rs.getString(5) != null
				    );
		users.add(one);
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
