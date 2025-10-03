/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.model;

import java.util.*;
import java.util.Locale;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Report{
	
    static Logger logger = LogManager.getLogger(Report.class);
    static final long serialVersionUID = 70L;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat dfTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    static final Locale locale = new Locale("en", "US");     
    static final NumberFormat currencyFormatter = 
        NumberFormat.getCurrencyInstance(locale);	
    String year = "",date_from="",date_to="", date_to_2="",type="issueMB";
    String title = "", which_date="",by="", day="", prev_year="", next_year="";
    String vendor_id="";
    boolean debug = false;
    boolean
	distributeMB=true,
	distributeGC=false,
	distributeRX=false,
	distributeWic=false,
	distributeSenior=false,
	issued = false,
	unissued = false,
	redeem = false,
	redeemRX = false,
	redeemWic = false,
	redeemSenior = false,
	participate = false,
	inventory = false,
	redeemOld = false,
	participateSnap = false,
	issuedNotRedeemed = false;
    List<List<ReportRow>> all = new ArrayList<List<ReportRow>>();
    Hashtable<String, ReportRow> all2 = new Hashtable<String, ReportRow>(4);
    DecimalFormat decFormat = new DecimalFormat("###,###.##");
    List<ReportRow> rows = null; 
    ReportRow columnTitle = null;
    //
    int totalIndex = 2; // DB index for row with 2 items
    public Report(){

    }
    public Report(boolean deb){
	debug = deb;
    }
    public void setYear(String val){
	if(val != null && !val.equals("-1"))
	    year = val;
    }
    public void setPrev_year(String val){
	if(val != null && !val.equals("-1"))
	    prev_year = val;
    }
    public void setNext_year(String val){
	if(val != null && !val.equals("-1"))
	    next_year = val;
    }
    public void setVendor_id(String val){
	if(val != null && !val.equals("-1"))
	    vendor_id = val;
    }
    public String getVendor_id(){
	if(vendor_id.equals("")){
	    return "-1";
	}
	return vendor_id;
    }
    public void setDay(String val){
	if(val != null && !val.equals(""))
	    day = val;
    }	
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }	
    public void setDate_to(String val){
	if(val != null && !val.isEmpty()){
	    date_to = val;
	    date_to_2 = val+" 23:59";
	}
    }
    public void setBy(String val){
	if(val != null)
	    by = val;
    }
    public void setDistributeMB(Boolean val){
	distributeMB = val;
    }
    public void setDistributeGC(Boolean val){
	distributeGC = val;
    }
    public void setDistributeRX(Boolean val){
	distributeRX = val;
    }
    public void setDistributeWic(Boolean val){
	distributeWic = val;
    }
    public void setDistributeSenior(Boolean val){
	distributeSenior = val;
    }
    public void setRedeemRX(Boolean val){
	redeemRX = val;
    }
    public void setRedeemWic(Boolean val){
	redeemWic = val;
    }
    public void setRedeemSenior(Boolean val){
	redeemSenior = val;
    }
    public void setIssued(Boolean val){
	issued = val;
    }
    public void setUnissued(Boolean val){
	unissued = val;
    }
    public void setRedeem(Boolean val){
	redeem = val;
    }
    public void setParticipate(Boolean val){
	participate = val;
    }
    public void setParticipateSnap(Boolean val){
	participateSnap = val;
    }    
    public void setInventory(Boolean val){
	inventory = val;
    }
    public void setRedeemOld(Boolean val){
	redeemOld = val;
    }
    public void setIssuedNotRedeemed(Boolean val){
	issuedNotRedeemed = val;
    }		
    //
    // getters
    //
    public String getYear(){
	return year;
    }
    public String getPrev_year(){
	return prev_year;
    }
    public String getNext_year(){
	return next_year;
    }	
    public String getDay(){
	return day;
    }	
    public String getDate_from(){
	return date_from ;
    }	
    public String getDate_to(){
	return date_to ;
    }
    public String getBy(){
	return by ;
    }
    public String getType(){
	return type;
    }
    public boolean getDistributeMB(){
	return distributeMB;
    }
    public boolean getDistributeGC(){
	return distributeGC;
    }
    public boolean getDistributeRX(){
	return distributeRX;
    }
    public boolean getDistributeWic(){
	return distributeWic;
    }
    public boolean getDistributeSenior(){
	return distributeSenior;
    }
    public boolean getRedeemRX(){
	return redeemRX;
    }
    public boolean getRedeemWic(){
	return redeemWic;
    }
    public boolean getRedeemSenior(){
	return redeemSenior;
    }		
    public boolean getIssued(){
	return issued;
    }
    public boolean getUnissued(){
	return unissued;
    }
    public boolean getRedeem(){
	return redeem;
    }
    public boolean getParticipate(){
	return participate;
    }
    public boolean getInventory(){
	return inventory;
    }
    public boolean getRedeemOld(){
	return redeemOld;
    }
    public boolean getIssuedNotRedeemed(){
	return issuedNotRedeemed;
    }		
    public String getTitle(){
	return title;
    }	
    public List<ReportRow> getRows(){
	return rows;
    }
    public List<List<ReportRow>> getAll(){
	return all;
    }
    public List<ReportRow> getInventoryList(){
	List<ReportRow> list = new ArrayList<ReportRow>();
	if(all2 != null){
	    for(String key:all2.keySet()){
		ReportRow one = all2.get(key);
		list.add(one);
	    }
	}
	return list;
    }
    public ReportRow getColumnTitle(){
	return columnTitle;
    }
    /**
     // here are what we need to do
     //
     MB EBT amount distributed on Saturday and Tuesday separately and combined weekly, monthly, annually.

     MB Double amount distributed on Saturday and Tuesday separately and combined weekly, monthly, annually.
	   
     MB amount redeemed.

     GC amount distributed and amount redeemed.

     Number of different households that participated in MB annually.

     Average number of times a household participated MB annually.

     Total number of transactions per month and per year.

     Average amount of MB transaction annually.

     Ideally it would be great to be able to put this information from previous years into this application so the report can track the change over time.


    */
    public String find(){
	String msg = "";
	if(!day.equals("")){
	    date_from = day;
	    date_to = Helper.getNextDay(day);
	    date_to_2 = Helper.getNextDay(day) + " 00:00";
	}
	if(distributeMB){
	    msg += distributeMB();
	}
	if(distributeGC){
	    msg +=  distributeGC();
	}
	if(distributeRX){
	    msg +=  distributeRX();
	}
	if(distributeWic){
	    msg +=  distributeWic();
	}
	if(distributeSenior){
	    msg +=  distributeSenior();
	}								
	if(redeem){
	    msg +=  redeems();
	}
	if(redeemOld){
	    msg +=  redeemOld();
	}
	if(redeemRX){
	    msg +=  redeemRX();
	}
	if(redeemWic){
	    msg +=  redeemWic();
	}
	if(redeemSenior){
	    msg +=  redeemSenior();
	}
	if(participate){
	    msg += participateData();
	}
	if(participateSnap){
	    msg += participateSnap();
	}	
	if(inventory){
	    msg += inventoryStats();
	}
	if(issued){
	    msg +=  issued();
	}
	if(unissued){
	    msg +=  unissued();
	}
	if(issuedNotRedeemed){
	    msg +=  findIssuedNotRedeemed();
	}				
	return msg;
    }
    void setTitle(){
	if(!day.equals("")){
	    title +=" "+day;
	}
	else if(!year.equals("")){
	    title +=" "+year;
	}
	else {
	    if(!date_from.equals("")){
		title += " "+date_from;
	    }
	    if(!date_to.equals("")){
		if(!date_from.equals(date_to)){
		    title += " - "+date_to;
		}
	    }
	}
    }
    public String distributeMB(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null, pstmt6=null;				
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qq2="", qq3="", qq4="", qq5="",qq6="", qw="", qg="";
	which_date="e.date_time ";
	title = "EBT MB Distribution ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Fund Type","Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.fund_type type, sum(b.value) amount from bucks b left join ebt_bucks eb on b.id=eb.buck_id left join ebts e on e.id=eb.ebt_id where b.voided is null ";
	qg = " group by type having amount > 0 ";
	qq2 = " select count(*) from ebts e where e.cancelled is null ";
	qq5 = " select count(*) from ebts e where e.dispute_resolution is not null ";		
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq5 += qw;
	}
	qq3 = qq;
	qq4 = qq2;
	qq6 = qq5;
	qq3 += " and ";
	qq4 += " and ";
	qq6 += " and ";
	qq3 += " dayofweek("+which_date+") = 7 "; //(sunday=1) saturday=7
	qq4 += " dayofweek("+which_date+") = 7 ";
	qq6 += " dayofweek("+which_date+") = 7 ";
				
	qq += qg;
	qq3 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);
	logger.debug(qq4);
	logger.debug(qq5);
	logger.debug(qq6);				
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);
	    pstmt5 = con.prepareStatement(qq5);
	    pstmt6 = con.prepareStatement(qq6);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
		pstmt4.setString(jj, year);
		pstmt5.setString(jj, year);
		pstmt6.setString(jj, year);								
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt5.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt6.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
								
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt4.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt5.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt6.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0, totalAll=0, countAll=0;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    totalAll = total;
	    rows.add(one);
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    rows.add(one);
	    countAll = count; // trans
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    all.add(rows);
	    count = 0;
	    rs = pstmt5.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}
	    }
	    title = "Saturdays Ebt MB Distribution ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Fund Type","Amount");
	    rows.add(one);
	    total = 0; count=0;
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    totalAll = total;
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt4.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    countAll = count;
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    count = 0;
	    rs = pstmt6.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}								
	    }
	    all.add(rows);
			
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6);
	}		
	return msg;
    }
    public String distributeRX(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null, pstmt6=null;				
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qq2="", qq3="", qq4="", qq5="",qq6="", qw="", qg="";
	which_date="e.date_time ";
	title = "MB Rx Distribution ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Fund Type","Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.fund_type type, sum(b.value) amount from bucks b left join rx_bucks eb on b.id=eb.buck_id join market_rx e on e.id=eb.rx_id where b.voided is null ";
		
	qg = " group by type having amount > 0 ";
	qq2 = " select count(*) from market_rx e where e.cancelled is null ";
	qq5 = " select count(*) from market_rx e where e.dispute_resolution is not null ";		
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq5 += qw;
	}
	qq3 = qq;
	qq4 = qq2;
	qq6 = qq5;
	qq3 += " and ";
	qq4 += " and ";
	qq6 += " and ";
	qq3 += " dayofweek("+which_date+") = 7 "; //(sunday=1) saturday=7
	qq4 += " dayofweek("+which_date+") = 7 ";
	qq6 += " dayofweek("+which_date+") = 7 ";
				
	qq += qg;
	qq3 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);
	logger.debug(qq4);
	logger.debug(qq5);
	logger.debug(qq6);				
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);
	    pstmt5 = con.prepareStatement(qq5);
	    pstmt6 = con.prepareStatement(qq6);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
		pstmt4.setString(jj, year);
		pstmt5.setString(jj, year);
		pstmt6.setString(jj, year);								
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt5.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt6.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
								
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt4.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt5.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt6.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0, totalAll=0, countAll=0;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    totalAll = total;
	    rows.add(one);
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    rows.add(one);
	    countAll = count; // trans
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    all.add(rows);
	    count = 0;
	    rs = pstmt5.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}
	    }
	    title = "Saturdays MB Distribution ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Fund Type","Amount");
	    rows.add(one);
	    total = 0; count=0;
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    totalAll = total;
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt4.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    countAll = count;
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    count = 0;
	    rs = pstmt6.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}								
	    }
	    all.add(rows);
			
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6);
	}		
	return msg;
    }
    // FMNP WIC
    public String distributeWic(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null, pstmt6=null;				
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qq2="", qq3="", qq4="", qq5="",qq6="", qw="", qg="";
	which_date="e.date_time ";
	title = "FMNP WIC Distribution ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Fund Type","Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.fund_type type, sum(b.value) amount from bucks b left join wic_bucks eb on b.id=eb.buck_id join fmnp_wics e on e.id=eb.wic_id where b.voided is null ";
		
	qg = " group by type having amount > 0 ";
	qq2 = " select count(*) from fmnp_wics e where e.cancelled is null ";
	qq5 = " select count(*) from fmnp_wics e where e.dispute_resolution is not null ";		
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq5 += qw;
	}
	qq3 = qq;
	qq4 = qq2;
	qq6 = qq5;
	qq3 += " and ";
	qq4 += " and ";
	qq6 += " and ";
	qq3 += " dayofweek("+which_date+") = 7 "; //(sunday=1) saturday=7
	qq4 += " dayofweek("+which_date+") = 7 ";
	qq6 += " dayofweek("+which_date+") = 7 ";
				
	qq += qg;
	qq3 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);
	logger.debug(qq4);
	logger.debug(qq5);
	logger.debug(qq6);				
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);
	    pstmt5 = con.prepareStatement(qq5);
	    pstmt6 = con.prepareStatement(qq6);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
		pstmt4.setString(jj, year);
		pstmt5.setString(jj, year);
		pstmt6.setString(jj, year);								
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt5.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt6.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
								
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt4.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt5.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt6.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0, totalAll=0, countAll=0;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    totalAll = total;
	    rows.add(one);
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    rows.add(one);
	    countAll = count; // trans
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    all.add(rows);
	    count = 0;
	    rs = pstmt5.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}
	    }
	    title = "Saturdays FMNP WIC Distribution ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Fund Type","Amount");
	    rows.add(one);
	    total = 0; count=0;
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    totalAll = total;
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt4.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    countAll = count;
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    count = 0;
	    rs = pstmt6.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}								
	    }
	    all.add(rows);
			
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6);
	}		
	return msg;
    }
    public String distributeSenior(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null, pstmt6=null;				
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qq2="", qq3="", qq4="", qq5="",qq6="", qw="", qg="";
	which_date="e.date_time ";
	title = "FMNP Seniors Distribution ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Fund Type","Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.fund_type type, sum(b.value) amount from bucks b left join senior_bucks eb on b.id=eb.buck_id join fmnp_seniors e on e.id=eb.senior_id where b.voided is null ";
		
	qg = " group by type having amount > 0 ";
	qq2 = " select count(*) from fmnp_seniors e where e.cancelled is null ";
	qq5 = " select count(*) from fmnp_seniors e where e.dispute_resolution is not null ";		
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq5 += qw;
	}
	qq3 = qq;
	qq4 = qq2;
	qq6 = qq5;
	qq3 += " and ";
	qq4 += " and ";
	qq6 += " and ";
	qq3 += " dayofweek("+which_date+") = 7 "; //(sunday=1) saturday=7
	qq4 += " dayofweek("+which_date+") = 7 ";
	qq6 += " dayofweek("+which_date+") = 7 ";
				
	qq += qg;
	qq3 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);
	logger.debug(qq4);
	logger.debug(qq5);
	logger.debug(qq6);				
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);
	    pstmt5 = con.prepareStatement(qq5);
	    pstmt6 = con.prepareStatement(qq6);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
		pstmt4.setString(jj, year);
		pstmt5.setString(jj, year);
		pstmt6.setString(jj, year);								
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt5.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt6.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
								
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt4.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt5.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt6.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0, totalAll=0, countAll=0;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    totalAll = total;
	    rows.add(one);
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    rows.add(one);
	    countAll = count; // trans
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    all.add(rows);
	    count = 0;
	    rs = pstmt5.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}
	    }
	    title = "Saturdays FMNP Seniors Distribution ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Fund Type","Amount");
	    rows.add(one);
	    total = 0; count=0;
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str.toUpperCase(),
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    totalAll = total;
	    if(total > 0){
		count = total/3;
		one = new ReportRow(debug, 2);			
		one.setRow("Count",""+count);
		rows.add(one);				
	    }
	    rs = pstmt4.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Transactions",""+count);
	    countAll = count;
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    count = 0;
	    rs = pstmt6.executeQuery();						
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);			
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		total = count*3;
		totalAll =totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}								
	    }
	    all.add(rows);
			
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5, pstmt6);
	}		
	return msg;
    }		

		
    public String distributeGC(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	ResultSet rs = null;

	String msg = "";
	String qq = "", qw="", qw3="", qg="", qg4="", qq2="", qq3="", qq4="";
	String which_date = "g.date_time ";			  
	title = "GC Distribution ";
	setTitle();
	rows = new ArrayList<ReportRow>();
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Payment Type","Amount");
	rows.add(one);
	/*
	  select count(*) ttl, g.pay_type type,sum(g.amount) amount from gifts g where year(g.date_time) = 2017 group by type having amount > 0 

	*/
	qq = " select g.pay_type type,sum(g.amount) amount from gifts g ";
	qg = " group by type having amount > 0 ";
	qw = " where g.cancelled is null ";
	qq2 = " select count(*) from gifts g "; // trans
	qq3 = " select count(*) from gifts g, gift_bucks gb ";
	qw3 = " where g.id=gb.gift_id and g.cancelled is null ";
	qq4 = " select count(*),amount from gifts g where g.dispute_resolution is not null ";
	qg4 = " group by amount having amount > 0 ";
	//
	if(!year.equals("")){
	    qw += " and ";
	    qw3 += " and ";
	    qw += " year("+which_date+") = ? ";
	    qw3 += " year("+which_date+") = ? ";
	    qq4 += " and year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw3 += " and ";					
		qw += which_date+" >= ? ";
		qw3 += which_date+" >= ? ";
		qq4 += " and "+which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw3 += " and ";
		qw += which_date+" <= ? ";
		qw3 += which_date+" <= ? ";
		qq4 += " and "+which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq3 += qw3;
	}
	qq4 += qg4;
	qq += qg;
	logger.debug(qq);
	logger.debug(qq2);		
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    qq = qq3;
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
		pstmt4.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt4.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    int total = 0, count = 0;
	    int totalAll = 0, countAll = 0;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    totalAll = total;
	    rows.add(one);			
	    rs = pstmt3.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
		if(count > 0){
		    one = new ReportRow(debug, 2);
		    one.setRow("Count",""+count);
		    rows.add(one);			
		}
	    }			
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Transactions",""+count);
	    countAll = count;
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Transaction Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    count = 0; total = 0;
	    int val = 0;
	    rs = pstmt4.executeQuery();
	    while(rs.next()){
		count = rs.getInt(1);
		val = rs.getInt(2);
		total += val*count;
	    }
	    if(count > 0){
		one = new ReportRow(debug, 2);
		one.setRow("Dispute Resolution Trans",""+count);
		rows.add(one);
		totalAll = totalAll - total;
		one = new ReportRow(debug, 2);								
		one.setRow("Adjusted Total","$"+totalAll+".00");
		countAll = countAll - count;
		rows.add(one);
		if(countAll > 0){
		    double average = (totalAll +0.0)/countAll;
		    one = new ReportRow(debug, 2);
		    one.setRow("Adjusted Transaction Average",				
			       currencyFormatter.format(average));
		    rows.add(one);
		}		
								
	    }
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4);
	}		
	return msg;
    }

    
    public String redeems(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;		
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="";
	which_date="r.date_time ";
	//
	qq = " select concat_ws(' ',v.lname,v.fname) name, sum(b.value) amount from bucks b join redeem_bucks rb on b.id=rb.buck_id join redeems r on r.id=rb.redeem_id join vendors v on v.id=r.vendor_id ";
	qg = " group by name having amount > 0 ";
	qq2 = " select count(*) from redeems r ";

	qq3 = " select b.value name, count(*) amount from bucks b join redeem_bucks rb on b.id=rb.buck_id join redeems r on r.id=rb.redeem_id ";
		
	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" <= ? ";
	    }
	}
	if(!vendor_id.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += "r.vendor_id=? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	    qq2 += " where "+qw;
	    qq3 += " where "+qw;
	}
	qq += qg;
	qq3 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);		
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    qq = qq3;
	    pstmt3 = con.prepareStatement(qq3);			
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);				
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));					
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));					
		    jj++;
		}
	    }
	    if(!vendor_id.equals("")){
		pstmt.setString(jj, vendor_id);
		pstmt2.setString(jj, vendor_id);
		pstmt3.setString(jj, vendor_id);				
		jj++;
	    }
	    title = "Redemptions classified by type ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();
	    ReportRow one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);			
	    one = new ReportRow(debug, 3);
	    one.setRow("Certificate Type","Count","Amount");
	    rows.add(one);
	    int total = 0, count = 0;
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = "";
		one = new ReportRow(debug, 3);
		int val = rs.getInt(1);
		int cnt = rs.getInt(2);
		count += cnt;
		if(val == 3){
		    str = "MB $3";
		}
		else if(val == 5){
		    str = "GC $5";
		}
		else{
		    str = "GC $20";
		}
		val = cnt*val;				
		one.setRow(str,
			   ""+cnt,
			   "$"+val+".00"
			   );
		total += val;
		rows.add(one);
	    }
	    one = new ReportRow(debug, 3);			
	    one.setRow("Total",""+count, "$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	    //
	    title = "Redemptions ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Vendor","Amount");
	    rows.add(one);		

	    total = 0; count = 0;						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Redemptions Count",""+count);
	    rows.add(one);
	    if(total > 0 && count > 0){
		double average = (total +0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Redemption Average",				
			   currencyFormatter.format(average));
		rows.add(one);					
	    }
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}		
	return msg;
    }
    public String redeemOld(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;

	String msg = "";
	String which_date = "", which_date2="";
	String qq = "", qw="", qg="", qq2="";
	which_date="e.date_time ";
	which_date2= "r.date_time ";
	if(prev_year.equals("") || next_year.equals("")){
	    return "Need to set the issue year and redeem year";
	}
	//
	qq = " select b.value name, count(*) amount from bucks b join redeem_bucks rb on b.id=rb.buck_id join redeems r on r.id=rb.redeem_id "+
	    " join ebt_bucks eb on eb.buck_id=b.id "+
	    " join ebts e on e.id = eb.ebt_id ";
	qq2 = " select b.value name, count(*) amount from bucks b join redeem_bucks rb on b.id=rb.buck_id join redeems r on r.id=rb.redeem_id "+
	    " join gift_bucks gb on gb.buck_id=b.id "+
	    " join gifts e on e.id=gb.gift_id ";
	qg = " group by name having amount > 0 ";
	qw += " where year("+which_date+") = ? and year("+which_date2+") = ? ";
	qq += qw;
	qq2 += qw;
	qq += qg;
	qq2 += qg;
	logger.debug(qq);
	logger.debug(qq2);		
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    pstmt.setString(jj, prev_year);
	    pstmt2.setString(jj, prev_year);
	    jj++;
	    pstmt.setString(jj, next_year);
	    pstmt2.setString(jj, next_year);				

	    title = "MB/GC Issued "+prev_year+" redeemed "+next_year;
	    rows = new ArrayList<ReportRow>();
	    ReportRow one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);			
	    one = new ReportRow(debug, 3);
	    one.setRow("Certificate Type","Count","Amount");
	    rows.add(one);
	    int total = 0, count = 0;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = "";
		one = new ReportRow(debug, 3);
		int val = rs.getInt(1);
		int cnt = rs.getInt(2);
		count += cnt;
		if(val == 3){
		    str = "MB $3";
		}
		else if(val == 5){
		    str = "GC $5";
		}
		else{
		    str = "GC $20";
		}
		val = cnt*val;				
		one.setRow(str,
			   ""+cnt,
			   "$"+val+".00"
			   );
		total += val;
		rows.add(one);
	    }
	    //
	    rs = pstmt2.executeQuery();
	    while(rs.next()){
		String str = "";
		one = new ReportRow(debug, 3);
		int val = rs.getInt(1);
		int cnt = rs.getInt(2);
		count += cnt;
		if(val == 3){
		    str = "MB $3";
		}
		else if(val == 5){
		    str = "GC $5";
		}
		else{
		    str = "GC $20";
		}
		val = cnt*val;				
		one.setRow(str,
			   ""+cnt,
			   "$"+val+".00"
			   );
		total += val;
		rows.add(one);
	    }
	    one = new ReportRow(debug, 3);			
	    one.setRow("Total",""+count, "$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}		
	return msg;
    }
    /**
       select v.voucher_num voucher, sum(b.value) amount from bucks b                  join redeem_bucks rb on b.id=rb.buck_id                                         join redeems r on r.id=rb.redeem_id                                             join rx_bucks rx on rx.buck_id=b.id			                                       join market_rx v on v.id=rx.rx_id			                                         group by voucher having amount > 0 ;


			 
    */
    public String redeemRX(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="";
	which_date="v.date_time ";
	//
	qq = "select v.voucher_num voucher, sum(b.value) amount from bucks b                  join redeem_bucks rb on b.id=rb.buck_id                                         join redeems r on r.id=rb.redeem_id                                             join rx_bucks rx on rx.buck_id=b.id			                                       join market_rx v on v.id=rx.rx_id ";			   
				
	qg = " group by voucher having amount > 0 ";

	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += qg;
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }
	    title = "Redemptions Classified by Voucher ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();
	    ReportRow one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);			
	    one = new ReportRow(debug, 2);
	    one.setRow("Voucher","Amount");
	    rows.add(one);
	    int total = 0, count = 0;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt);
	}		
	return msg;
    }
    public String redeemWic(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="";
	which_date="fw.date_time ";
	//
	qq = "select fw.ticket_num ticket, sum(b.value) amount from bucks b                  join redeem_bucks rb on b.id=rb.buck_id                                         join redeems r on r.id=rb.redeem_id                                             join wic_bucks w on w.buck_id=b.id			                                       join fmnp_wics fw on fw.id=w.wic_id ";			   
				
	qg = " group by ticket having amount > 0 ";

	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += qg;
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }
	    title = "Redemptions Classified by Ticket # ";
	    setTitle();
	    rows = new ArrayList<ReportRow>();
	    ReportRow one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);			
	    one = new ReportRow(debug, 2);
	    one.setRow("Ticket #","Amount");
	    rows.add(one);
	    int total = 0, count = 0;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt);
	}		
	return msg;
    }		
    public String redeemSenior(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="";
	which_date="fs.date_time ";
	//
	qq = "select fs.ticket_num ticket, sum(b.value) amount from bucks b                  join redeem_bucks rb on b.id=rb.buck_id                                         join redeems r on r.id=rb.redeem_id                                             join senior_bucks s on s.buck_id=b.id			                                    join fmnp_seniors fs on fs.id=s.senior_id ";			   
				
	qg = " group by ticket having amount > 0 ";

	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += qg;
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }
	    title = "FMNP Senior Redemptions Classified by Ticket #";
	    setTitle();
	    rows = new ArrayList<ReportRow>();
	    ReportRow one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);			
	    one = new ReportRow(debug, 2);
	    one.setRow("Ticket #","Amount");
	    rows.add(one);
	    int total = 0, count = 0;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   "$"+rs.getString(2)+".00"
			   );
		total += rs.getInt(2);
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt);
	}		
	return msg;
    }
		
    //
    public String participateData(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null, pstmt3=null;		
	ResultSet rs = null;
	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="",qg3="";
	which_date="e.date_time ";
	title = "Households Participated in MB ";
	setTitle();
	rows = new ArrayList<ReportRow>();
	ReportRow one = new ReportRow(debug, 3);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 3);
	one.setRow("Household card #","Fund Type","Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select e.card_last_4 card, b.fund_type type, sum(b.value) amount from bucks b left join ebt_bucks eb on b.id=eb.buck_id left join ebts e on e.id=eb.ebt_id ";
	qg = " group by card, type having amount > 0 ";
	qq2 = " select count(*) from ebts e ";
	qw = " where e.dispute_resolution is null and e.cancelled is null ";
	//
	qq3 = " select e.card_last_4 card, count(*), sum(b.value) amount from bucks b left join ebt_bucks eb on b.id=eb.buck_id left join ebts e on e.id=eb.ebt_id ";
	qg3 = " group by card having amount > 0 ";
				
	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		else{
		    qw = " where ";
		}
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		else{
		    qw = " where ";
		}
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	    qq3 += qw;
	}
	qq += qg;
	qq3 += qg3;
	// System.err.println(qq);
	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);						
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		pstmt3.setString(jj, year);
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt3.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));										
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt3.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));										
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0 , count = 0, ebt_total=0; // families count
	    int dmb_total = 0;
	    String prev = "";
	    jj=1;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		if(!str.equals(prev)){
		    count++;
		    prev = str;
		}
		String str2 = rs.getString(2);
		if(str2 == null) str2 = "Unknown";
		else str2 = str2.toUpperCase();
		one = new ReportRow(debug, 3);
		one.setRow(str,
			   str2,
			   "$"+rs.getString(3)+".00"
			   );
		if(str2.equals("EBT")){
		    ebt_total += rs.getInt(3);
		}
		else{
		    dmb_total += rs.getInt(3);
		}
		rows.add(one);
	    }
	    title = "Households Participated Stats ";
	    setTitle();
	    List<ReportRow> rows2 = new ArrayList<ReportRow>();
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows2.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Item", "Value");
	    rows2.add(one);			
	    total = ebt_total+dmb_total;
	    one = new ReportRow(debug, 2);			
	    one.setRow("EBT Total","$"+ebt_total+".00");
	    rows2.add(one);
	    one = new ReportRow(debug, 2);			
	    one.setRow("DMB Total","$"+dmb_total+".00");
	    rows2.add(one);
	    one = new ReportRow(debug, 2);			
	    one.setRow("Total","$"+total+".00");
	    rows2.add(one);			
	    one = new ReportRow(debug, 2);						
	    one.setRow("Household count",""+count);			
	    rows2.add(one);
	    rs = pstmt2.executeQuery();
	    int count2 = 0; // trans count
	    if(rs.next()){
		count2 = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);						
	    one.setRow("Transaction count",""+count2);
	    rows2.add(one);			
	    if(total > 0 && count2 > 0){
		double average = (total+0.0)/count2;
		one = new ReportRow(debug, 2);
		one.setRow("Average Participation",				
			   currencyFormatter.format(average));
		rows2.add(one);				
	    }
	    if(count > 0 && count2 > 0){
		double average = (count2+0.0)/count;
		one = new ReportRow(debug, 2);
		one.setRow("Average particiaptions number per household",
			   decFormat.format(average));
		rows2.add(one);				
	    }
	    all.add(rows2);
	    all.add(rows);
	    //
	    total = 0;
	    title = "Households Transcations ";
	    setTitle();
	    List<ReportRow> rows3 = new ArrayList<ReportRow>();
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows3.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Household Card #", "Count");
	    rows3.add(one);
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		int cnt = rs.getInt(2);
		total += cnt;
		one = new ReportRow(debug, 2);
		one.setRow(str, ""+cnt);
		rows3.add(one);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Total",""+total);
	    rows3.add(one);
	    all.add(rows3);						
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}		
	return msg;
    }
    public String participateSnap(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null, pstmt3=null;		
	ResultSet rs = null;
	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qq3="",qg3="";
	which_date="b.date ";
	title = "Households Participated in Snap Purchases ";
	setTitle();
	rows = new ArrayList<ReportRow>();
	ReportRow one = new ReportRow(debug, 3);
	one.setRow("Title", title,"");
	rows.add(one);
	one = new ReportRow(debug, 3);
	one.setRow("Household card #","Amount","Ebt Amount");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.card_number card, sum(b.snap_amount) amount,sum(b.ebt_amount) ebt,sum(b.dbl_amount) dbl from snap_purchases b ";
	qg = " group by card having amount > 0 ";
	qq2 = " select count(*) from snap_purchases b ";
	qw = " b.cancelled is null ";
				
	if(!year.equals("")){
	    if(!qw.equals("")){
		qw += " and ";
	    }
	    qw += " year(b.date) = ? ";
	}
	else {
	    if(!date_from.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}
		qw += "date(b.date) >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		}

		qw += " date(b.date) <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	    qq2 += " where "+qw;
	}
	qq += qg;
	logger.debug(qq);
	logger.debug(qq2);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));

		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int count = 0 , count2 = 0, family_count=0; // families count
	    double amount_total = 0, ebt_total=0, dbl_total=0;
	    String prev = "";
	    jj=1;
	    while(rs.next()){
		String str = rs.getString(1);
		if(str == null) str = "Unknown";
		if(!str.equals(prev)){
		    family_count++;
		    prev = str;
		}
		one = new ReportRow(debug, 3);
		one.setRow(str,
			   currencyFormatter.format(rs.getDouble(2)),
			   currencyFormatter.format(rs.getDouble(3))
			   // rs.getString(4)
			   );
		amount_total += rs.getDouble(2);
		ebt_total += rs.getDouble(3);
		dbl_total += rs.getDouble(4);
		rows.add(one);
	    }
	    all.add(rows);
	    title = "Snap Households Participation ";
	    setTitle();
	    List<ReportRow> rows2 = new ArrayList<ReportRow>();
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows2.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Item", "Value");
	    rows2.add(one);			
	    one = new ReportRow(debug, 2);			
	    one.setRow("Amount Total",currencyFormatter.format(amount_total));
	    rows2.add(one);
	    one = new ReportRow(debug, 2);			
	    one.setRow("EBT Total",currencyFormatter.format(ebt_total));
	    rows2.add(one);
	    one = new ReportRow(debug, 2);			
	    one.setRow("DBL Total",currencyFormatter.format(dbl_total));
	    rows2.add(one);
	    one = new ReportRow(debug, 2);				    
	    one.setRow("Household count",""+family_count);			
	    rows2.add(one);
	    rs = pstmt2.executeQuery();
	    count = 0; // trans count
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Transactions #",""+count);
	    rows2.add(one);			
	    if(amount_total > 0 && count > 0){
		double average = amount_total/count;
		one = new ReportRow(debug, 2);
		one.setRow("Average Participation",				
			   currencyFormatter.format(average));
		rows2.add(one);				
	    }
	    if(count > 0 && family_count > 0){
		double average = (count+0.0)/family_count;
		one = new ReportRow(debug, 2);
		one.setRow("Average participations per household",
			   decFormat.format(average));
		rows2.add(one);				
	    }
	    all.add(rows2);
	    //
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}		
	return msg;
    }    

    
    public String inventoryStats(){
		
	Connection con = null;
	PreparedStatement pstmt  = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;		
	ResultSet rs = null;
	Hashtable<String, Integer> table = new Hashtable<String, Integer>(3);
	all2.put("GC 20",new ReportRow(debug, 4));
	all2.put("GC 5",new ReportRow(debug, 4));
	all2.put("MB 3",new ReportRow(debug, 4));
	all2.put("Total",new ReportRow(debug, 4));
	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="", qw2="", qq3="", qw3="";
	which_date="b.date ";
	String which_date2 = "e.date_time ";
	title = "MB & GC Inventory Stats ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 3);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("Certificate Type","Printed Amount");
	rows.add(one);		
	qq = " select t.name type,count(*) amount from buck_seq s, batches b, buck_confs c,buck_types t ";
	qw = " where b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' ";		
	qq2 = " select t.name type,count(*) amount from gift_bucks eb, buck_seq s, batches b, buck_confs c,buck_types t,gifts e ";
	qw2 = " where eb.buck_id=s.id and b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' and e.id=eb.gift_id ";		
	qq3 = " select t.name type,count(*) amount from ebt_bucks eb, buck_seq s, batches b, buck_confs c,buck_types t,ebts e ";
	qw3 = " where eb.buck_id=s.id and b.id=s.batch_id and b.conf_id=c.id and c.type_id=t.id and b.status='Printed' and e.id = eb.ebt_id ";
		
	qg = " group by type having amount > 0 order by type ";
	//		
	// issuing of the printed MB GC of the printed in 2015
	// we ignore all that was issued before
	//
	year = "2015-01-01"; 
	if(!qw.equals("")){
	    qw += " and ";
	    qw2 += " and ";
	    qw3 += " and ";				
	}
	// qw
	qw += which_date+" > '"+ year+"'";		
	qw2 += which_date+" > '"+ year+"'";
	qw3 += which_date+" > '"+ year+"'";			
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw2;
	    qq3 += qw3;			
	}
		
	qq += qg;
	qq2 += qg;
	qq3 += qg;		

	logger.debug(qq);
	logger.debug(qq2);
	logger.debug(qq3);			

	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
						
	    int jj=1;
	    rs = pstmt.executeQuery();
	    int total = 0, val=0, old_val=0;
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		total += val;
		table.put(str, new Integer(val));
		ReportRow old = all2.get(str);
		if(old != null){
		    old.setRow(0, str); // type
		    old.setRow(1, ""+val);
		    all2.put(str, old);
		}
		str = "Printed "+str;
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   ""+val
			   );
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Printed Total",""+total);
	    ReportRow old2 = all2.get("Total");
	    if(old2 != null){
		old2.setRow(0, "Total"); // type
		old2.setRow(1, ""+total);
		all2.put("Total", old2);
	    }			
	    rows.add(one);
	    rs = pstmt2.executeQuery();
	    total = 0;
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		total += val;
		if(table.containsKey(str)){
		    old_val = table.get(str).intValue();
		    old_val = old_val - val;
		    table.put(str, new Integer(old_val));
		}
		ReportRow old = all2.get(str);
		if(old != null){
		    old.setRow(2, ""+val);
		    all2.put(str, old);
		}				
		str = "Issued "+str;
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   ""+val
			   );
		rows.add(one);
	    }
	    //
	    rs = pstmt3.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		val = rs.getInt(2);
		total += val;
		if(table.containsKey(str)){
		    old_val = table.get(str).intValue();
		    old_val = old_val - val;
		    table.put(str, new Integer(old_val));
		}
		ReportRow old = all2.get(str);
		if(old != null){
		    old.setRow(2, ""+val);
		    all2.put(str, old);
		}								
		str = "Issued "+str;
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   ""+val
			   );
		rows.add(one);
	    }
	    one = new ReportRow(debug, 2);			
	    one.setRow("Issued Total",""+total);
	    rows.add(one);
	    old2 = all2.get("Total");
	    if(old2 != null){
		old2.setRow(2, ""+total);
		all2.put("Total", old2);
	    }									
	    //
	    // current inventory
	    //
	    total = 0;
	    List<String> list = new ArrayList<String>(table.keySet());
	    Collections.sort(list);
	    for(String key:list){
		one =  new ReportRow(debug, 2);
		val = table.get(key).intValue();
		total += val;
		one.setRow("Inventory "+key, ""+val);
		rows.add(one);
		ReportRow old = all2.get(key);
		if(old != null){
		    old.setRow(3, ""+val);
		    all2.put(key, old);
		}					
	    }
	    if(total > 0){
		one =  new ReportRow(debug, 2);
		one.setRow("Inventory Total", ""+total);
		rows.add(one);
		old2 = all2.get("Total");
		if(old2 != null){
		    old2.setRow(3, ""+total);
		    all2.put("Total", old2);
		}					
	    }
	    else{
		old2 = all2.get("Total");
		if(old2 != null){
		    old2.setRow(3, ""+total);
		    all2.put("Total", old2);
		}			
	    }
	    // all.add(rows);
			
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}		
	return msg;
    }
    public String issued(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;		
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="";
	which_date="e.date_time ";
	title = "Issued MB's ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("MB Number","Value");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.id,b.value from bucks b left join ebt_bucks eb on b.id=eb.buck_id left join ebts e on e.id=eb.ebt_id ";

	//
	// GC
	//
	qq2 = " select b.id,b.value from bucks b left join gift_bucks eb on b.id=eb.buck_id left join gifts e on e.id=eb.gift_id ";
	qw = " where b.voided is null ";
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	}
	qq += qg;
	logger.debug(qq);
	logger.debug(qq2);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0;
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   rs.getString(2)
			   );
		total += rs.getInt(2);
		rows.add(one);
		count++;
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Count",""+count);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	    //
	    title = "Issued GC's ";
	    setTitle();			
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("GC Number","Value");
	    rows.add(one);
	    rs = pstmt2.executeQuery();
	    total = 0;count=0;
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   rs.getString(2)
			   );
		total += rs.getInt(2);
		rows.add(one);
		count++;
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Count",""+count);
	    rows.add(one);			
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);

	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}		
	return msg;
    }
    public String unissued(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;		
	ResultSet rs = null;
	//
	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="";
	which_date="bs.date ";
	title = "Unissued MB's ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 2);
	one.setRow("MB Number","Value");
	rows.add(one);
	//
	// MB
	//
	qq = " select b.id,b.value from bucks b join buck_seq s on s.id=b.id join batches bs on bs.id=s.batch_id where b.voided is null and b.value = 3 and b.id not in  (select e.buck_id from ebt_bucks e) ";
	//
	// GC
	//
	qq2 = " select b.id,b.value from bucks b join buck_seq s on s.id=b.id join batches bs on bs.id=s.batch_id where b.voided is null and b.value > 3 and b.id not in(select g.buck_id from gift_bucks g)";
	if(!year.equals("")){
	    qw += " and year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += "and "+which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += "and "+which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	}			
	logger.debug(qq);
	logger.debug(qq2);
	try{
	    con = Helper.getConnection();
			
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }

	    rs = pstmt.executeQuery();
	    int total = 0, count=0;
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   rs.getString(2)
			   );
		total += rs.getInt(2);
		rows.add(one);
		count++;
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Count",""+count);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);
	    //
	    title = "Unissued GC's ";
	    setTitle();			
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 2);
	    one.setRow("GC Number","Value");
	    rows.add(one);
	    rs = pstmt2.executeQuery();
	    total = 0;count=0;
	    while(rs.next()){
		String str = rs.getString(1);
		one = new ReportRow(debug, 2);
		one.setRow(str,
			   rs.getString(2)
			   );
		total += rs.getInt(2);
		rows.add(one);
		count++;
	    }
	    one = new ReportRow(debug, 2);
	    one.setRow("Count",""+count);
	    rows.add(one);			
	    one = new ReportRow(debug, 2);
	    one.setRow("Total","$"+total+".00");
	    rows.add(one);
	    all.add(rows);

	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}		
	return msg;
    }
    /**
     * we want all the MB and GC that were issued but not redeemed
     *
     * the scripts below will change the expire date to 12/31/next year
     *
     select count(*),b.value val from bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2015 group by val ";

     select b.id,b.value val,date_format(b.expire_date,'%m/%d/%Y') from bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2015 

     update bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id set b.expire_date='2016-12-31' where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2015

     select count(*),b.value val from bucks b join gift_bucks eb on b.id=eb.buck_id join gifts e on e.id=eb.gift_id  where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2015 group by val;

     update bucks b join gift_bucks eb on b.id=eb.buck_id join gifts e on e.id=eb.gift_id  set b.expire_date='2016-12-31' where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2015

     select count(*),b.value val from bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2016 group by val ;
		 
     update bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id set b.expire_date='2017-12-31' where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2016

     update bucks b join gift_bucks eb on b.id=eb.buck_id join gifts e on e.id=eb.gift_id  set b.expire_date='2017-12-31' where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb) and year(e.date_time) = 2016

		 
    */
    public String findIssuedNotRedeemed(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;		
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qw="", qg="", qq2="";
	which_date="e.date_time ";
	title = "Issued MB's and GC's Issued but not Redeemed ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 3);
	one.setRow("MB","Count","Total($)");
	rows.add(one);
	//
	// MB
	//
	qq = " select count(*),b.value val from bucks b join ebt_bucks eb on b.id=eb.buck_id join ebts e on e.id=eb.ebt_id ";

	//
	// GC
	//
	qq2 = " select count(*),b.value val from bucks b join gift_bucks eb on b.id=eb.buck_id join gifts e on e.id=eb.gift_id ";
	qw = " where b.voided is null and not b.id in (select rb.buck_id from redeem_bucks rb)  ";
	qg = " group by val ";
	if(!year.equals("")){
	    qw += " and ";
	    qw += " year("+which_date+") = ? ";
	}
	else {
	    if(!date_from.equals("")){
		qw += " and ";
		qw += which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		qw += " and ";
		qw += which_date+" <= ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += qw;
	    qq2 += qw;
	}
	qq += qg;
	qq2 += qg;
	logger.debug(qq);
	logger.debug(qq2);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    qq = qq2;
						
	    pstmt2 = con.prepareStatement(qq2);
	    int jj=1;
	    if(!year.equals("")){
		pstmt.setString(jj, year);
		pstmt2.setString(jj, year);
		jj++;
	    }
	    else {
		if(!date_from.equals("")){
		    pstmt.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    jj++;
		}
		if(!date_to.equals("")){
		    pstmt.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    pstmt2.setTimestamp(jj, new java.sql.Timestamp(dfTime.parse(date_to_2).getTime()));
		    jj++;
		}
	    }
	    rs = pstmt.executeQuery();
	    int total = 0, count=0, sum=0, totalSum=0;
	    while(rs.next()){
		int val = rs.getInt(2);
		count = rs.getInt(1);
		sum = count*val;
		total += count;								
		totalSum += sum;
		one = new ReportRow(debug, 3);
		one.setRow(""+val,
			   ""+count,
			   "$"+sum+".00"
			   );
		rows.add(one);
	    }
	    rs = pstmt2.executeQuery();
	    count = 0;sum=0;
	    while(rs.next()){
		int val = rs.getInt(2);
		count = rs.getInt(1);
		total += count;
		sum = val*count;
		totalSum += sum;
		one = new ReportRow(debug, 3);
		one.setRow(""+val,
			   ""+count,
			   "$"+sum+".00"
			   );
		rows.add(one);
	    }
	    one = new ReportRow(debug, 3);
	    one.setRow("Total",""+total,"$"+totalSum+".00");
	    rows.add(one);
	    all.add(rows);

	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}		
	return msg;
    }		
}






















































