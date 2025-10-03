package bucks.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.naming.*;
import javax.sql.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.utils.*;

public class Buck implements java.io.Serializable{

    static final long serialVersionUID = 10L;	
    boolean debug = false, found = false;
    static Logger logger = LogManager.getLogger(Buck.class);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String value="", id="", fund_type="", buck_type_id="", voided="";
    int days_to_expire = 0; // number of days before expire, - is expired 
    String expire_date ="";
    Type buck_type = null;
    Batch batch = null;
    BuckConf conf = null;
    User user = null;
    Ebt ebt = null;
    Gift gift = null;
    // we get these from other buck info
    String ebt_id="", rx_id="", gift_id="", wic_id="", senior_id="", redeem_id="";
    Redeem redeem = null;
    public Buck(){
    }	
    public Buck(boolean val){
	debug = val;
    }
    public Buck(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Buck(boolean deb,
		String val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7
		){
	setValues( val,
		   val2,
		   val3,
		   val4,
		   val5,
		   val6,
		   val7
		   );
	debug = deb;
		
    }
    void setValues(
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7
		   ){
	setId(val);
	setValue(val2);
	setFund_type(val3);
	setBuck_type_id(val4);
	setExpire_date(val5);
	setDays_to_expire(val6);
	setVoided(val7);
	found = true;
    }

    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setValue(String val){
	if(val != null)
	    value = val;
    }
    public void setFund_type(String val){
	if(val != null)
	    fund_type = val;
    }
    public void setDays_to_expire(String val){
	if(val != null){
	    try{
		days_to_expire = Integer.parseInt(val);
	    }catch(Exception ex){}
	}
    }
    public void setBuck_type_id(String val){
	if(val != null)
	    buck_type_id = val;
    }
    public void setExpire_date(String val){
	if(val != null)
	    expire_date = val;
    }
    public void setVoided(String val){
	if(val != null)
	    voided = val;
    }	
    //
    public String getId(){
	return id;
    }
    public String getValue(){
	return value;
    }
    public int getValue_int(){
	int ret = 0;
	if(!value.equals("")){
	    try{
		ret = Integer.parseInt(value);
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
	return ret;
    }
    public String getExpire_date(){
	return expire_date;
    }	
    public String getFund_type(){
	return fund_type;
    }
    public boolean isEbtType(){
	return fund_type.equals("ebt");
    }
    public boolean isDmbType(){
	return fund_type.equals("dmb");
    }
    public boolean isRxType(){
	return fund_type.equals("rx");
    }
    public boolean isWicType(){
	return fund_type.equals("wic");
    }
    public boolean isSeniorType(){
	return fund_type.equals("senior");
    }
    public String getFund_typeStr(){
	String str = fund_type.toUpperCase();
	return str;
    }	
    public String getBuck_type_id(){
	if(buck_type_id.equals("") && buck_type != null){
	    buck_type_id = buck_type.getId();
	}
	return buck_type_id;
    }	
    public String getDays_to_expire(){
	return ""+days_to_expire;
    }
    public String getVoided(){
	return voided;
    }
    public boolean isVoided(){
	return !voided.equals("");
    }
    public boolean isNotVoided(){
	return voided.equals("");
    }		
    public boolean isAlreadyIssued(){
	return !fund_type.equals("") || !expire_date.equals("");
    }
    /**
     * needed when we do select
     */
    public boolean isFound(){
	return found;
    }
    public Type getBuck_type(){
	if(!buck_type_id.equals("") && buck_type == null){
	    Type one = new Type(debug, buck_type_id,null, "buck_types");
	    String back = one.doSelect();
	    if(back.equals("")){
		buck_type = one;
	    }
	}
	return buck_type;
    }		
    @Override
    public int hashCode() {
	int hash = 3, id_int = 0;
	if(!id.equals("")){
	    try{
		id_int = Integer.parseInt(id);
	    }catch(Exception ex){}
	}
	hash = 53 * hash + id_int;
	return hash;
    }
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Buck other = (Buck) obj;
	return this.id.equals(other.id);
    }
    public boolean isExpired(){
	return days_to_expire < 0;
    }

    public String toString(){
	return id;
    }
    public Batch getBatch(){

	if(batch == null && !id.equals("")){
	    BatchList bl = new BatchList(debug);
	    bl.setSeq_id(id);
	    String back = bl.find();
	    if(back.equals("")){
		List<Batch> batches = bl.getBatches();
		if(batches.size() > 0){
		    batch = batches.get(0);
		    conf = batch.getConf();
		}
	    }
	}
	return batch;
    }
    public BuckConf getConf(){

	if(conf == null && !id.equals("")){
	    getBatch();
	}
	return conf;
    }

    public String getConf_id(){

	String ret = "";
	if(conf == null && !id.equals("")){
	    getBatch();
	}
	if(conf != null){
	    ret = conf.getId();
	}
	return ret;
    }
    /*
      public String findOtherBuckInfo(){
      String back = "";
      if(id.equals("")) return "buck id not set";
      EbtList ebl = new EbtList(debug);
      ebl.setBuck_id(id);
      back = ebl.find();
      if(back.equals("")){
      List<Ebt> ones = ebl.getEbts();
      if(ones != null && ones.size() > 0){
      ebt = ones.get(0);
      }
      }
      if(ebt == null){
      GiftList gl = new GiftList(debug);
      gl.setBuck_id(id);
      back += gl.find();
      if(back.equals("")){
      List<Gift> ones = gl.getGifts();
      if(ones != null && ones.size() > 0){
      gift = ones.get(0);
      }
      }
      }
      RedeemList rl = new RedeemList(debug);
      rl.setBuck_id(id);
      back += rl.find();
      if(back.equals("")){
      List<Redeem> ones = rl.getRedeems();
      if(ones != null && ones.size() > 0){
      redeem = ones.get(0);
      }
      }
      return back;
      }
    */
    public boolean hasEbt(){
	return !ebt_id.isEmpty();
    }
    public boolean hasGift(){
	return !gift_id.isEmpty();				

    }
    public boolean hasRx(){
	return !rx_id.isEmpty();
    }
    public boolean hasWic(){
	return !wic_id.isEmpty();
    }
    public boolean hasSenior(){
	return !senior_id.isEmpty();
    }		
    public boolean hasRedeem(){
	return !redeem_id.isEmpty();
    }
    public boolean isRedeemed(){
	return !redeem_id.isEmpty();
    }		
    public Ebt getEbt(){
	if(ebt == null && hasEbt()){
	    Ebt one = new Ebt(debug, ebt_id);
	    String back = one.doSelect();
	    if(back.isEmpty())
		ebt = one;
	}
	return ebt;
    }
    public Gift getGift(){
	if(gift == null && hasGift()){
	    Gift one = new Gift(debug, gift_id);
	    String back = one.doSelect();
	    if(back.isEmpty())
		gift = one;
	}				
	return gift;
    }
    public Redeem getRedeem(){
	if(redeem == null && hasRedeem()){
	    Redeem one = new Redeem(debug, gift_id);
	    String back = one.doSelect();
	    if(back.isEmpty())
		redeem = one;
	}			
	return redeem;
    }
    public boolean isIssued(){
	return hasEbt() || hasGift() || hasRx() || hasWic() || hasSenior();
    }
    public String findOtherBuckInfo(){
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;				
	if(id.equals("")) {
	    msg = "buck id not set";
	    return msg;
	}
	String qq1 = "select eb.ebt_id as ID,'Ebt' as Type from ebt_bucks eb,ebts e where eb.ebt_id=e.id and eb.buck_id=? and e.cancelled is null ";
	String qq2 = "select xb.rx_id as ID,'Rx' as Type from rx_bucks xb,market_rx x where xb.rx_id=x.id and xb.buck_id=? and x.cancelled is null ";
	String qq3 = "select wb.wic_id as ID,'Wic' as Type from wic_bucks wb,fmnp_wics w where wb.wic_id=w.id and wb.buck_id=? and w.cancelled is null ";
	String qq4 = "select sb.senior_id as ID,'Senior' as Type from senior_bucks sb,fmnp_seniors s where sb.senior_id=s.id and sb.buck_id=? and s.cancelled is null ";						
	String qq5 = "select gb.gift_id as ID,'Gift' as Type from gift_bucks gb,gifts g where gb.gift_id=g.id and gb.buck_id=? and g.cancelled is null ";
	String qq6 = "select rb.redeem_id as ID,'Redeem' as Type from redeem_bucks rb,redeems r where rb.redeem_id=r.id and rb.buck_id=? ";
				
	String qq = qq1+" union "+qq2+" union "+qq3+" union "+qq4+" union "+qq5+" union "+qq6;
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, id);
	    pstmt.setString(3, id);
	    pstmt.setString(4, id);
	    pstmt.setString(5, id);
	    pstmt.setString(6, id);						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String p_id = rs.getString(1);
		String type = rs.getString(2);
		if(type != null && type.equals("Ebt"))
		    ebt_id=p_id;
		else if(type.equals("Gift"))
		    gift_id = p_id;
		else if(type.equals("Rx"))
		    rx_id = p_id;
		else if (type.equals("Wic"))
		    wic_id = p_id;
		else if(type.equals("Senior"))
		    senior_id = p_id;
		else if(type.equals("Redeem"))
		    redeem_id = p_id;								
		else {
		    System.err.println("Unknown type "+type);
		}
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    /**
     * bucks are saved in the batch class in a group
     */
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = "insert into bucks values(?,?,null,null,null)";
	//if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    fillData(pstmt, 1);
	    pstmt.executeUpdate();
	    found = true;
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.equals("")){
	    msg = doSelect();
	}
	return msg;
    }
	
    String fillData(PreparedStatement pstmt, int c){
	String msg="";
	try{
	    // all these are required
	    pstmt.setString(c++, id);
	    pstmt.setString(c++, value);
			
	}
	catch(Exception ex){
	    msg += ex;
	    logger.error(msg);
	}
		
	return msg;					
    }
    public String insertBucks(List<String> seq_list){

	if(value.equals("")){
	    return "buck value not set ";
	}
	if(seq_list == null || seq_list.size() < 1){
	    return "No bucks list to add ";
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
			
	String qq = "insert into bucks values(?,?,null,null,null)";
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    for(String one:seq_list){
		pstmt.setString(1, one);
		pstmt.setString(2, value);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }	
    public String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = "update bucks set fund_type=?,expire_date=? where id=?";
	expire_date = "12/31/"+Helper.getCurrentYear();
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    if(fund_type.equals("")){
		pstmt.setNull(1, Types.VARCHAR);
	    }
	    else 
		pstmt.setString(1, fund_type);
	    if(!expire_date.equals(""))
		pstmt.setDate(2, new java.sql.Date(dateFormat.parse(expire_date).getTime()));
	    else
		pstmt.setNull(2, Types.DATE);						
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.equals("")){
	    msg = doSelect();
	}
	return msg;
    }
    public String setAsVoided(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	String qq = "update bucks set voided='y' where id=?";
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(msg.equals("")){
	    msg = doSelect();
	}
	return msg;
    }		
    //
    // 
    //
    /**
       select b.id, b.value,b.fund_type,c.type_id,date_format(b.expire_date,'%m/%d/%Y'),if(b.expire_date is not null, datediff(b.expire_date, now()), null),b.voided from bucks b join buck_seq s on s.id=b.id join batches bs on bs.id=s.batch_id join buck_confs c on bs.conf_id=c.id where b.id=143905;

			 
    */
    public String doSelect(){
	// 
	String qq = "select b.id, b.value,b.fund_type,c.type_id,date_format(b.expire_date,'%m/%d/%Y'),if(b.expire_date is not null, datediff(b.expire_date, now()), null),b.voided from bucks b join buck_seq s on s.id=b.id join batches bs on bs.id=s.batch_id join buck_confs c on bs.conf_id=c.id where b.id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	if(id.equals("")){
	    msg = "Buck id not set";
	    return msg;
	}
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();	
	    if(rs.next()){
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5),
			  rs.getString(6),
			  rs.getString(7)
			  );
	    }
	    else{
		msg = "Not found";
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;			
    }
    public boolean exists(){
	String qq = "select count(*) from buck_seq where id=? ";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String msg = "";
	boolean found = false;
	// if(debug)
	logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect ";
		return found;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();	
	    if(rs.next()){
		if(rs.getInt(1) > 0) found = true;
	    }
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return found;	

    }

}





































