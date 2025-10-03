/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.utils;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.security.MessageDigest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.model.*;
import bucks.list.*;

public class Helper{

    static int c_con = 0;
    //
    // Non static variables
    //
    static Logger logger = LogManager.getLogger(Helper.class);

    //
    // basic constructor
    public Helper(boolean deb){
	//
	// initialize
	//
    }
    public final static String getHashCodeOf(String buffer){

	String key = "Apps Secret Key "+getToday();
	byte[] out = performDigest(buffer.getBytes(),buffer.getBytes());
	String ret = bytesToHex(out);
	return ret;
	// System.err.println(ret);

    }
    public final static byte[] performDigest(byte[] buffer, byte[] key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest(key);
        } catch (Exception e) {
	    System.err.println(e);
        }
        return null;
    }

    public final static String bytesToHex(byte in[]) {
	byte ch = 0x00;
	int i = 0; 
	if (in == null || in.length <= 0)
	    return null;
	String pseudo[] = {"0", "1", "2",
	    "3", "4", "5", "6", "7", "8",
	    "9", "A", "B", "C", "D", "E",
	    "F"};
	StringBuffer out = new StringBuffer(in.length * 2);
	while (i < in.length) {
	    ch = (byte) (in[i] & 0xF0); // Strip off high nibble
		
	    ch = (byte) (ch >>> 4);
	    // shift the bits down
	    
	    ch = (byte) (ch & 0x0F);    
	    // must do this is high order bit is on!

	    out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	    ch = (byte) (in[i] & 0x0F); // Strip off low nibble 
	    out.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	    i++;
	}
	String rslt = new String(out);
	return rslt;
    }    
    //
    /**
     * Adds escape character before certain characters
     *
     */
    public final static String escapeIt(String s) {
		
	StringBuffer safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	boolean noEscapeBefore = true;
	while (c < len) {                           
	    if ((safe.charAt(c) == '\'' ||
		 safe.charAt(c) == '"') && noEscapeBefore){
		safe.insert(c, '\\');
		c += 2;
		len = safe.length();
		noEscapeBefore = true;
	    }
	    else if(safe.charAt(c) == '\\'){ // to avoid double \\ before '
		noEscapeBefore = false;
		c++;
	    }
	    else {
		noEscapeBefore = true;
		c++;
	    }
	}
	return safe.toString();
    }
    //
    // users are used to enter comma in numbers such as xx,xxx.xx
    // as we can not save this in the DB as a valid number
    // so we remove it 
    public final static String cleanNumber(String s) {

	if(s == null) return null;
	String ret = "";
	int len = s.length();
	int c = 0;
	int ind = s.indexOf(",");
	if(ind > -1){
	    ret = s.substring(0,ind);
	    if(ind < len)
		ret += s.substring(ind+1);
	}
	else
	    ret = s;
	return ret;
    }
    /**
     * replaces the special chars that has certain meaning in html
     *
     * @param s the passing string
     * @returns string the modified string
     */
    public final static String replaceSpecialChars(String s) {
	char ch[] ={'\'','\"','>','<'};
	String entity[] = {"&#39;","&#34;","&gt;","&lt;"};
	//
	// &#34; = &quot;

	String ret ="";
	int len = s.length();
	int c = 0;
	boolean in = false;
	while (c < len) {             
	    for(int i=0;i< entity.length;i++){
		if (s.charAt(c) == ch[i]) {
		    ret+= entity[i];
		    in = true;
		}
	    }
	    if(!in) ret += s.charAt(c);
	    in = false;
	    c ++;
	}
	return ret;
    }
    public final static String replaceQuote(String s) {
	char ch[] ={'\''};
	String entity[] = {"_"};
	//
	// &#34; = &quot;

	String ret ="";
	int len = s.length();
	int c = 0;
	boolean in = false;
	while (c < len) {             
	    for(int i=0;i< entity.length;i++){
		if (s.charAt(c) == ch[i]) {
		    ret+= entity[i];
		    in = true;
		}
	    }
	    if(!in) ret += s.charAt(c);
	    in = false;
	    c ++;
	}
	return ret;
    }
    /**
     * adds another apostrify to the string if there is any next to it
     *
     * @param s the passing string
     * @returns string the modified string
     */
    public final String doubleApostrify(String s) {
	StringBuffer apostrophe_safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	while (c < len) {                           
	    if (apostrophe_safe.charAt(c) == '\'') {
		apostrophe_safe.insert(c, '\'');
		c += 2;
		len = apostrophe_safe.length();
	    }
	    else {
		c++;
	    }
	}
	return apostrophe_safe.toString();
    }
    public final static Connection getConnection(){

	Connection con = null;
	int trials = 0;
	boolean pass = false;
	while(trials < 3 && !pass){
	    try{
		trials++;
		logger.debug("Connection try "+trials);
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_bucks");
		con = ds.getConnection();
		if(con == null){
		    String str = " Could not connect to DB ";
		    logger.error(str);
		}
		else{
		    pass = testCon(con);
		    if(pass){
			c_con++;
			logger.debug("Got connection: "+c_con);
			logger.debug("Got connection at try "+trials);
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex);
	    }
	}
	return con;
    }
    /**
     * vendors database connection
     * assuming you are using MS sql server database to check the
     * current active vendors with the existing once.
     *
     * if you change the database, change the connection url accordingly
     */
    public final static Connection getNwConnection(String dbUrl,
					    String dbDb,
					    String dbUser,
					    String dbPass){
	Connection con = null;
	String dbSql = "jdbc:sqlserver://"+dbUrl+";database="+dbDb+";user="+dbUser+";password="+dbPass+";";
	try{
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();					
	    con = DriverManager.getConnection(dbSql);
	}
	catch (Exception sqle) {
	    System.err.println(sqle);
	}
	return con;
    }
	
    final static boolean testCon(Connection con){
		
	boolean pass = false;
	Statement stmt  = null;
	ResultSet rs = null;
	String qq = "select 1+1";		
	try{
	    if(con != null){
		stmt = con.createStatement();
		logger.debug(qq);
		rs = stmt.executeQuery(qq);
		if(rs.next()){
		    pass = true;
		}
	    }
	    rs.close();
	    stmt.close();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	return pass;
    }
    /**
     * Connect to Oracle database
     *
     * @param dbStr database connect string
     * @param dbUser database user string
     * @param dbPass database password string
     */
    public final static Connection databaseConnect(String dbStr, 
						   String dbUser, 
						   String dbPass) {
	Connection con=null;
	try {
	    Class.forName("oracle.jdbc.OracleDriver");
	    con = DriverManager.getConnection(dbStr,
					      dbUser,dbPass);
	    c_con++;
	    logger.debug("Got connection: "+c_con);

	}
	catch (Exception sqle) {
	    logger.error(sqle);
	}
	return con;
    }
    /**
     * Disconnect the database and related statements and result sets
     * 
     * @param stmt
     * @param rs
     */
    public final static void databaseDisconnect(
						Statement stmt,
						ResultSet rs) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } catch (SQLException e) { ; }
		stmt = null;
	    }
	}
    }
    public final static void databaseDisconnect(Connection con,
						Statement stmt,
						ResultSet rs) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	    if(con != null) con.close();
	    con = null;
			
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } catch (SQLException e) { ; }
		stmt = null;
	    }
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { ; }
		con = null;
	    }
	}
    }		
    public final static void databaseDisconnect(Connection con,
						Statement stmt,
						Statement stmt2,
						ResultSet rs) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	    if(stmt2 != null) stmt2.close();
	    stmt2 = null;			
	    if(con != null) con.close();
	    con = null;
			
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } catch (SQLException e) { ; }
		stmt = null;
	    }
	    if (stmt2 != null) {
		try { stmt2.close(); } catch (SQLException e) { ; }
		stmt2 = null;
	    }			
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { ; }
		con = null;
	    }
	}
    }
    public final static void databaseDisconnect(Connection con,
						ResultSet rs,
						Statement ... stmts
						) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    for(Statement stmt: stmts){
		if(stmt != null) stmt.close();
		stmt = null;
	    }
	    if(con != null) con.close();
	    con = null;
			
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { ; }
		rs = null;
	    }
	    for(Statement stmt: stmts){			
		if (stmt != null) {
		    try { stmt.close(); } catch (SQLException e) { ; }
		    stmt = null;
		}
	    }
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { ; }
		con = null;
	    }
	}
    }		
    /**
     * Write the number in bbbb.bb format needed for currency.
     * = toFixed(2)
     * @param dd the input double number
     * @returns the formated number as string
     */
    public final static String formatNumber(double dd){
	//
	String str = ""+dd;
	String ret="";
	int l = str.length();
	int i = str.indexOf('.');
	int r = i+3;  // required length to keep only two decimal
	// System.err.println(str+" "+l+" "+r);
	if(i > -1 && r<l){
	    ret = str.substring(0,r);
	}
	else{
	    ret = str;
	}
	return ret;
    }

    //
    // format a number with only 2 decimal
    // usefull for currency numbers
    //
    final static String formatNumber(String that){

	int ind = that.indexOf(".");
	int len = that.length();
	String str = "";
	if(ind == -1){  // whole integer
	    str = that + ".00";
	}
	else if(len-ind == 2){  // one decimal
	    str = that + "0";
	}
	else if(len - ind > 3){ // more than two
	    str = that.substring(0,ind+3);
	}
	else str = that;

	return str;
    }
    public final static String findAge(String date){
	String age = "";
	int yy = 0;
	int mm = 0, day = 0;
	try{
	    String[] dd = date.split("/");
	    if(dd.length > 0){
		mm = Integer.parseInt(dd[0]);
		day = Integer.parseInt(dd[1]);
		yy = Integer.parseInt(dd[2]);
	    }
	}catch(Exception ex){
	    System.err.println(ex);
	}
	Calendar cal1 = new GregorianCalendar();
	Calendar cal2 = new GregorianCalendar();
	if(yy > 0){
	    cal2.set(yy, (mm-1), day, 0, 0, 0);
	    long t1 = cal1.getTime().getTime();
	    long t2  = cal2.getTime().getTime();
	    long diff = t1-t2;
	    // double days = ((double)(cal1.getTime()) - (double)(cal2.getTime()))/(1000*60*60*24);
	    long diff2 = diff/(3600000*24);
	    long years = (diff2/365);
	    long months = ((diff2%365)/30);
	    double age2 = years + (months/12.);
	    // System.err.println(" days "+diff2);
	    // System.err.println(" years "+diff2/365);
	    age = ""+age2;			
	}
	return age;
    }

    //
    public final static int getCurrentYear(){

	int year = 2015;
	Calendar current_cal = Calendar.getInstance();
	year = current_cal.get(Calendar.YEAR);
	return year;
    }
    public final static int getNextYear(){
	return getCurrentYear()+1;
    }		
    //
    public final static String getToday(){

	String day="",month="",year="";
	Calendar current_cal = Calendar.getInstance();
	int mm =  (current_cal.get(Calendar.MONTH)+1);
	int dd =   current_cal.get(Calendar.DATE);
	year = ""+ current_cal.get(Calendar.YEAR);
	if(mm < 10) month = "0";
	month += mm;
	if(dd < 10) day = "0";
	day += dd;
	return month+"/"+day+"/"+year;
    }
    public final static String getDateOneYearFromNow(){

	String day="",month="",year="";
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.YEAR, 1);
	int mm =  (cal.get(Calendar.MONTH)+1);
	int dd =   cal.get(Calendar.DATE);
	year = ""+ cal.get(Calendar.YEAR);
	if(mm < 10) month = "0";
	month += mm;
	if(dd < 10) day = "0";
	day += dd;
	return month+"/"+day+"/"+year;
    }		
    public final static String getNextDay(String date){
	String ret = "";
	int mm = 0, dd = 0, yy = 0;
	if(date != null){
	    String[] arr = date.split("/");
	    if(arr.length == 3){
		try{
		    mm = Integer.parseInt(arr[0]);
		    dd = Integer.parseInt(arr[1]);
		    yy = Integer.parseInt(arr[2]);
		    Calendar cal = Calendar.getInstance();
		    cal.set(Calendar.MONTH, (mm -1));
		    cal.set(Calendar.DATE, dd+1);
		    cal.set(Calendar.YEAR, yy);
					
		    mm = cal.get(Calendar.MONTH)+1;
		    dd = cal.get(Calendar.DATE);
		    yy = cal.get(Calendar.YEAR);
		    ret = mm+"/"+dd+"/"+yy;
		}catch(Exception ex){
		    System.err.println(ex);
		}
	    }
	}
	return ret;
    }
    public final static String todaySubtract(String years, String months){
	Calendar cal = Calendar.getInstance();
	int yy = 0, mm = 0, dd = 0;
	try{
	    if(years != null && !years.equals("0")){
		yy = Integer.parseInt(years);
	    }
	    if(months != null && !months.equals("0")){
		mm = Integer.parseInt(months);
	    }
	    mm = mm + yy*12; // total months
	    cal.add(Calendar.MONTH, -mm);
			
	}catch(Exception ex){
	    System.err.println(ex);
	}
	mm =  cal.get(Calendar.MONTH)+1;
	dd =  cal.get(Calendar.DATE);
	yy =  cal.get(Calendar.YEAR);
		
	return ""+mm+"/"+dd+"/"+yy;		
		
    }
    //
    // initial cap a word
    //
    final static String initCapWord(String str_in){
	String ret = "";
	if(str_in !=  null){
	    if(str_in.length() == 0) return ret;
	    else if(str_in.length() > 1){
		ret = str_in.substring(0,1).toUpperCase()+
		    str_in.substring(1).toLowerCase();
	    }
	    else{
		ret = str_in.toUpperCase();   
	    }
	}
	// System.err.println("initcap "+str_in+" "+ret);
	return ret;
    }
    //
    // init cap a phrase
    //
    final static String initCap(String str_in){
	String ret = "";
	if(str_in != null){
	    if(str_in.indexOf(" ") > -1){
		String[] str = str_in.split("\\s"); // any space character
		for(int i=0;i<str.length;i++){
		    if(i > 0) ret += " ";
		    ret += initCapWord(str[i]);
		}
	    }
	    else
		ret = initCapWord(str_in);// it is only one word
	}
	return ret;
    }
    final static String formatTime(String time){
	String ret = time;
	if(time.length() > 0 && time.indexOf(":") == -1){
	    if(time.length() == 4){
		ret = time.substring(0,2)+":"+time.substring(2);
	    }
	    else if(time.length() == 3){
		ret = "0"+time.substring(0,1)+":"+time.substring(1);
	    }
	}
	return ret;

    }
	
}






















































