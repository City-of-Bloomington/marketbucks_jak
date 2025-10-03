/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.utils;

import java.util.*;
import java.io.*;
import java.text.*;
import jakarta.mail.*;
import jakarta.mail.Address;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;

public class InventoryJob implements Job{

    boolean debug = true;
    Logger logger = LogManager.getLogger(InventoryJob.class);
    long serialVersionUID = 2227L;
    String emailStr = "@bloomington.in.gov";
    JobDataMap dataMap = null;
    boolean activeMail = false;
    String from = "", to="", msg="", cc="";
    //
    // will send emails
    public InventoryJob(){

    }
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
	try{
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	    logger.debug("map = " + dataMap.entrySet());
	    logger.debug("dataMap = " + context.getJobDetail().getJobDataMap());
	    /*
	      to = (String)dataMap.get("to");
	      from = (String)dataMap.get("from");
	      String str = (String)dataMap.get("cc");
	      if(str != null){
	      cc = str;
	      }
	    */
	    String str = (String)dataMap.get("activeMail");
	    if(str != null && str.equals("true")){
		activeMail = true;
	    }
	    doInit();
	    doWork();
	    doDestroy();
	}
	catch(Exception ex){
	    logger.error(ex);
	    System.err.println(ex);
	}
    }
    public void doInit(){

	//
	// good idea to clear old data from quartz tables
	//

    }
    public void doDestroy() {
	// inventory = null;
    }	    
    /**
     * 
     */
    public void doWork(){

	String msg = "";
	String from = "";
	String to = "";
	String cc = "";
	String bcc = "";
		
	List<MailUser> users = null;
	MailUserList ml = new MailUserList();
		
	String back = ml.find();
	if(back.equals("")){
	    users = ml.getMailUsers();
	}
	if(users != null){
	    for(MailUser one:users){
		if(one.isSuper_user()){
		    from = one.getUsername()+emailStr;
		    to = one.getUsername()+emailStr;
		}
		else{
		    if(!cc.equals("")){
			cc +=",";
		    }
		    cc += one.getUsername()+emailStr;
		    // if no super user is set
		    if(from.equals("")){
			from = one.getUsername()+emailStr;
			to = one.getUsername()+emailStr;
		    }
		}
	    }
	}
	// back = ml.doClean(); // delete old record from quartz tables;
	if(!back.equals("")){
	    logger.error(back);
	}		
	Inventory inventory = new Inventory();
	back = inventory.checkInventory();
	if(!back.equals("")){
	    logger.error(back);
	    return;
	}
	else{
	    if(inventory.isInventoryCritical()){
		msg = inventory.getMessage();
		System.err.println(" from "+from);
		System.err.println(" to "+to);
		System.err.println(" cc "+cc);
		System.err.println(" msg "+msg);
		logger.error(" msg "+msg);
		if(!from.equals("")){
		    back = sendEmail(from, to, cc, bcc, msg);
		    if(!back.equals("")){
			logger.error(back);
		    }
		}
		else{
		    back = "No users to be notified";				
		    logger.error(back);
		}
	    }
	}
    }
    String sendEmail(String from, String to, String cc, String bcc,
		     String msgText){
	String message= "";
	String host = "localhost"; // whoville.city. ...
	String subject = "Marketbucks inventory notification";
	try {
	    //
	    // create some properties and get the default Session
	    //
	    Properties props = new Properties();
	    props.put("mail.smtp.host", host);
	    if (debug) props.put("mail.debug", "true");
			
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(debug);
	    //
	    // create a message
	    //
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    if(to != null && !to.equals("")){
		InternetAddress[] address = {new InternetAddress(to)};
		msg.setRecipients(Message.RecipientType.TO, address);
	    }
	    if(cc != null && !cc.equals("")){
		InternetAddress[] address2 = jakarta.mail.internet.InternetAddress.parse(cc);
		msg.addRecipients(Message.RecipientType.CC, address2);
	    }
	    if(bcc != null && !bcc.equals("")){
		InternetAddress[] address3 = jakarta.mail.internet.InternetAddress.parse(bcc);
		msg.setRecipients(Message.RecipientType.BCC, address3);
	    }
	    msg.setSubject(subject);
	    msg.setSentDate(new Date());

	    // If the desired charset is known, you can use
	    // setText(text, charset)
	    msg.setText(msgText);
	    //
	    Transport.send(msg);
	} catch (MessagingException mex){

	    logger.debug("\n--Exception handling in MailHandle.java");
	    //   mex.printStackTrace();
	    message += " Exception in MailHandle "+mex;
	    Exception ex = mex;
	    do {
		if (ex instanceof SendFailedException) {
		    SendFailedException sfex = (SendFailedException)ex;
		    Address[] invalid = sfex.getInvalidAddresses();
		    if (invalid != null) {
			System.out.println("    ** Invalid Addresses");
			if (invalid != null) {
			    for (int i = 0; i < invalid.length; i++) 
				message += "         " + invalid[i];
			}
		    }
		    Address[] validUnsent = sfex.getValidUnsentAddresses();
		    if (validUnsent != null) {
			System.out.println("    ** ValidUnsent Addresses");
			if (validUnsent != null) {
			    for (int i = 0; i < validUnsent.length; i++) 
				message += "         "+validUnsent[i];
			}
		    }
		    Address[] validSent = sfex.getValidSentAddresses();
		    if (validSent != null) {
			System.out.println("    ** ValidSent Addresses");
			if (validSent != null) {
			    for (int i = 0; i < validSent.length; i++) 
				message += "         "+validSent[i];
			}
		    }
		}
		System.out.println();
		if (ex instanceof MessagingException){
		    ex = ((MessagingException)ex).getNextException();
		}
		else{
		    ex = null;
		}
	    } while (ex != null);
	    logger.error(message);
	}
	return message;
    }
}






















































