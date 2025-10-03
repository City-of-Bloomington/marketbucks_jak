/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;

@WebServlet(urlPatterns = {"/VendorService"}, loadOnStartup = 1)
public class VendorService extends TopServlet{

    static final long serialVersionUID = 180L;	
    static Logger logger = LogManager.getLogger(VendorService.class);
    /**
     * Generates the Group form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     */
    
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGetost
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	String id = "";

	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String fullName="", term ="", type="";
	boolean success = true;
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	VendorList vl = null;
	List<Vendor> vendors = null;
	if(term.length() > 1){
	    vl = new VendorList(debug);
	    vl.setName(term);
	    vl.setActiveOnly();
	    String back = vl.find();
	    vendors = vl.getVendors();
	}
	if(vendors != null && vendors.size() > 0){
	    String json = writeJson(vendors);
	    out.println(json);
	}
	out.flush();
	out.close();
    }
    /**
     * *************************
     *
     * json format as an array
     [
     {"value":"John Doe",
     "id":1,
     "dept":"ITS"
     },
     {"value":"John Test",
     "id":2,
     "dept":"ITS"
     }
     ]
     ***************************
     */
    String writeJson(List<Vendor> ones){
	String json="";
	if(ones.size() > 0){
	    for(Vendor one:ones){
		if(!json.equals("")) json += ",";
		json += "{\"id\":\""+one.getVendorNum()+"\",\"value\":\""+one.getFullName()+"\"}";
	    }
	}
	json = "["+json+"]";
	return json;
    }

}






















































