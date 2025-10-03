/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.action;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;  
import org.apache.struts2.ServletActionContext;  
import jakarta.servlet.http.HttpServletResponse;
import org.apache.struts2.action.ServletContextAware;
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class LogoutAction extends TopAction{

    private static boolean debug = false;
    private static final long serialVersionUID = 60L;
    @Override
    public String execute(){
	try{
	    String cas_url = ctx.getInitParameter("cas_url");
	    HttpServletRequest request = ServletActionContext.getRequest();  
	    HttpSession session=request.getSession();
	    if(session != null)
		session.invalidate();
	    HttpServletResponse res = ServletActionContext.getResponse();
	    res.sendRedirect(cas_url);
	    return super.execute();				
	}catch(Exception ex){
	    System.out.println(ex);
	}
	return SUCCESS;
    }     
    @Override
    public void withServletContext(ServletContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public void withParameters(HttpParameters pars) {
        paramMap = pars;
    }
}



