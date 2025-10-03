/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE
 */
package bucks.action;

import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.opensymphony.xwork2.ModelDriven;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.action.SessionAware;
import org.apache.struts2.action.ParametersAware;
import org.apache.struts2.action.ServletContextAware;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import bucks.model.*;
import bucks.list.*;
import bucks.utils.*;

public abstract class TopAction extends ActionSupport implements ServletContextAware, ParametersAware, SessionAware{
    static final long serialVersionUID = 80L;
    boolean debug = false;
    String action="", url="", id="";
    static Logger logger = LogManager.getLogger(TopAction.class);
    static boolean enableVendorsAutoUpdate = false;
    static String vendorsCheckUrl = null;
    static String vendorsDatabase = null;
    static String vendorsUser = null;
    static String vendorsPassword = null;
    static int ebt_donor_max = 27, ebt_buck_value=3, rx_max_amount=30,
    wic_max_amount=45, senior_max_amount=45; // 21, 24
    User user = null;
    ServletContext ctx;
    Map<String, Object> sessionMap;
    HttpParameters paramMap = null;
    // Map<String, String[]> paramMap = null;
    @StrutsParameter(depth=1)
    public void setAction(String val) {
        if (val != null) { action = val; }
    }
    @StrutsParameter(depth=1)
    public void setAction2(String val) {
        if (val != null && !val.equals("")) { action = val; }
    }

    public String getAction() {
        return action;
    }
    @StrutsParameter(depth=1)
    public void setId(String val) {
        if (val != null) { id = val; }
    }
    @StrutsParameter(depth=1)
    public String getId() {
        return id;
    }
    @StrutsParameter(depth=1)
    public User getUser() {
        return user;
    }

    String doPrepare() {
        String back = "";
        try {
            user = (User)sessionMap.get("user");
            if (user == null) {
                back = LOGIN;
            }

            if (url.equals("")) {
                String val = ctx.getInitParameter("debug");
                if (val != null && val.equals("true")) {
                    debug = true;
                }

                val = ctx.getInitParameter("url");
                if (val != null) {
                    url = val;
                }

                val = ctx.getInitParameter("enableVendorsAutoUpdate");
                if (val != null && val.equals("true")) {
                    enableVendorsAutoUpdate = true;
                }

                val = ctx.getInitParameter("vendorsCheckUrl");
                if (val != null) {
                    vendorsCheckUrl = val;
                }

                val = ctx.getInitParameter("vendorsDatabase");
                if (val != null) {
                    vendorsDatabase = val;
                }

                val = ctx.getInitParameter("vendorsUser");
                if (val != null) {
                    vendorsUser = val;
                }

                val = ctx.getInitParameter("vendorsPassword");
                if (val != null) {
                    vendorsPassword = val;
                }

                val = ctx.getInitParameter("ebt_donor_max");
                if (val != null) {
                    ebt_donor_max = Integer.parseInt(val);
                }

                val = ctx.getInitParameter("ebt_buck_value");
                if (val != null) {
                    ebt_buck_value = Integer.parseInt(val);
                }

                val = ctx.getInitParameter("rx_max_amount");
                if (val != null) {
                    rx_max_amount = Integer.parseInt(val);
                }

                val = ctx.getInitParameter("wic_max_amount");
                if (val != null) {
                    wic_max_amount = Integer.parseInt(val);
                }

                val = ctx.getInitParameter("senior_max_amount");
                if (val != null) {
                    senior_max_amount = Integer.parseInt(val);
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return back;
    }


    public void withSession(Map<String, Object> map) {
        sessionMap=map;
    }

}
