/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.action;

import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;  
import jakarta.servlet.http.HttpServletResponse;  
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;


public class UserAction extends TopAction{

    static final long serialVersionUID = 150L;	
    static boolean debug = false;
    static Logger logger = LogManager.getLogger(UserAction.class);
    User person = null;
    List<User> users = null;
    String usersTitle = "Current Users";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.equals("")){
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
	    }				
	}
	if(action.equals("Save")){
	    back = person.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		id = person.getId();
		addActionMessage("Saved Successfully");
	    }
	}
	else if(action.equals("Update")){
	    back = person.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	if(!id.equals("")){
	    person = new User(debug);
	    person.setId(id);
	    back = person.doSelect();
	    if(!back.equals("")){
		addActionError(back);
	    }
	}
	return ret;
    }
    @StrutsParameter
    public boolean hasUsers(){
	getUsers();
	return users != null && users.size() > 0;
    }
    @StrutsParameter
    public List<User> getUsers(){
	if(users == null){
	    UserList tl = new UserList(debug);
	    String back = tl.find();
	    if(back.equals("")){
		users = tl.getUsers();
	    }
	}
	return users;
    }
    @StrutsParameter
    public void setPerson(User val){
	if(val != null)
	    person = val;
    }
    @StrutsParameter
    public User getPerson(){
	if(person == null){
	    person = new User();
	}
	return person;
    }
    @StrutsParameter
    @Override
    public String getId(){
	if(id.equals("") && person != null){
	    id = person.getId();
	}
	return id;
    }		
    public String populate(){
	String ret = SUCCESS;
	return ret;
    }
    @StrutsParameter
    public String getUsersTitle(){
	return usersTitle;
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





































