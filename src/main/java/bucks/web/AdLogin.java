package bucks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
/**
 * This is used for testing only, in production this is disabled
 * and use Login combined with CAS
 */

public class AdLogin extends TopServlet{

    //
    static final long serialVersionUID = 13L;
    boolean debug = false;  
    static Logger logger = LogManager.getLogger(AdLogin.class);
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String name, value, id="";
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration<String> values = req.getParameterNames();
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    value = req.getParameter(name).trim();
	    if (name.equals("id")) {
		id = value;
	    }
	}

	out.println("<html><head><title>User Login</title>");
	out.println("<script>");
	//
	out.println(" function submitForm() {");
	out.println("	 if(document.myForm.userid.value == \"\")( ");
	out.println("	 alert(\"Please enter username\")");
        out.println("    return false;");
	out.println("  } ");
	out.println(" return true;");
	out.println(" }");
	out.println("</script>");
	out.println("</head><body onload=\"document.userid.focus();\">");
	out.println("<br /><br />");
	out.println("<center><h2>Welcome to Citation </h2>");
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return submitForm();\" >");
	out.println("<table border=\"0\">");
        out.println("<tr><td>Username</td><td><input name=\"userid\" value=\"\"  size=\"10\" type=\"text\"></td><tr>");
	out.println("<tr><td>&nbsp;</td><td><input type=\"submit\" value=\"Submit\" /></td></tr>");
	out.println("</table> ");
	out.println("</form> ");	
	out.println("</body></html>");
	out.close();
    }									
    public void doPost(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException{

	String userid = "", message="";
	PrintWriter out = null;
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	// 
	Enumeration<String> values = req.getParameterNames();
	String name, value, id="";
	out.println("<html>");
	HttpSession session = null;
	while (values.hasMoreElements()) {
	    name = values.nextElement().trim();
	    value = req.getParameter(name).trim();
	    if (name.equals("userid")) {
		userid = value.toLowerCase();
	    }
	}
	try {
	    if(userid != null){
		session = req.getSession(true);			
		User user = getUser(userid);
		if(user != null && session != null){
		    session.setAttribute("user",user);
		    if(user.canEdit()){
			out.println("<head><title></title><META HTTP-EQUIV=\""+
				    "refresh\" CONTENT=\"0; URL=" + url +
				    "welcome.action\"></head>");
		    }
		    else{
			out.println("<head><title></title><META HTTP-EQUIV=\""+
				    "refresh\" CONTENT=\"0; URL=" + url +
				    "welcome.action\"></head>");
		    }
		    out.println("<body>");
		    out.println("</body>");
		    out.println("</html>");
		    out.flush();
		    return;
		}
		else{
		    message = " Unauthorized access ";
		}
	    }
	    else{
		message += " You can not access this system, check with IT or try again later";
	    }
	    out.println("</body>");
	    out.println("</html>");
	}
	catch (Exception ex) {
	    System.err.println(""+ex);
	    out.println(ex);
	}
	out.flush();
	//
    }
    //
    User getUser(String username){

	User user = null;
	String message="";
	User user2 = new User(debug, username);
	String back = user2.doSelect();
	if(!back.equals("")){
	    message += back;
	    logger.error(message);
	}
	else{
	    user = user2;
	}
	return user;
    }		
}






















































