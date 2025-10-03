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
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;

public class Logout extends TopServlet{

    static final long serialVersionUID = 13L;	
    /**
     * Logs the user out from the system and destroys the session info.
     * @param req the request input stream
     * @param res the response output stream
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration<String> values = req.getParameterNames();
	String name= "";
	String value = "";
	while (values.hasMoreElements()){
	    name =  values.nextElement().trim();
	    value = req.getParameter(name).trim();
	}
	try {
	    HttpSession session = req.getSession();
	    if(session != null){
				req.logout();
	    }
	} catch (Exception ex){
	    out.println(ex.toString());
	}
	res.sendRedirect(endpoint_logout_uri);
	return;

    }
  
}

