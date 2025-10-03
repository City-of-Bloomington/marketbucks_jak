/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.action;
import java.util.*; 
import jakarta.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public class WelcomeAction extends TopAction {
    private static final long serialVersionUID = 190L;
    static Logger logger = LogManager.getLogger(WelcomeAction.class);
    //
    @Override
    public String execute(){
	String ret = SUCCESS;
	doPrepare();
	if(user == null){
	    ret = LOGIN;
	}
	return ret;
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


