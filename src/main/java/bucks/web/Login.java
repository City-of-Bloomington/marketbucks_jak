/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.jasig.cas.client.authentication.AttributePrincipal;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;

// change the following to /Login,/login if you want to use CAS
@WebServlet(urlPatterns = {"/CasLogin","/caslogin"}, loadOnStartup = 1)
public class Login extends TopServlet{
    static int count = 0;
    //
    static final long serialVersionUID = 60L;
    static Logger logger = LogManager.getLogger(Login.class);
    /**
     * Generates the login form for all users.
     *
     * @param req the request
     * @param res the response
     */
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	String message="", id="";
	boolean found = false;

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	HttpSession session = null;
	String userid = null;
	AttributePrincipal principal = null;
	if (req.getUserPrincipal() != null) {
	    principal = (AttributePrincipal) req.getUserPrincipal();
	    userid = principal.getName();
	}
	if(userid == null || userid.isEmpty()){
	    userid = req.getRemoteUser();
	}
	System.err.println(" usrid "+userid);
	if(userid != null){
	    session = req.getSession(true);
	    User user = getUser(userid);
	    if(user != null && user.userExists() && session != null){
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
	    count++;
	    if(count < 3){
		String str = url+"Login";
		res.sendRedirect(str);
	    }
	    message += " You can not access this system, check with IT or try again later";
	}
	// if we got here, the user is not authorized
	out.println("<head><title></title><body>");
	out.println("<p><font color=red>");
	out.println(message);
	out.println("</p>");
	out.println("</body>");
	out.println("</html>");
	out.flush();
    }
    //
    void setCookie(HttpServletRequest req,
		   HttpServletResponse res){
	Cookie cookie = null;
	boolean found = false;
	Cookie[] cookies = req.getCookies();
	if(cookies != null){
	    for(int i=0;i<cookies.length;i++){
		String name = cookies[i].getName();
		if(name.equals(cookieName)){
		    found = true;
		}
	    }
	}
	//
	// if not found create one with 0 time to live;
	//
	if(!found){
	    cookie = new Cookie(cookieName,cookieValue);
	    res.addCookie(cookie);
	}
    }
    /**
     * Procesesses the login and check for authontication.
     *
     * @param req
     * @param res
     */
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
