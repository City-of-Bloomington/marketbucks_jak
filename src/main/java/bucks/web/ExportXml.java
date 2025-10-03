/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package bucks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
//
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;
import bucks.utils.*;

@WebServlet(urlPatterns = {"/ExportXml.do"}, loadOnStartup = 1)
public class ExportXml extends TopServlet{

    //
    static final long serialVersionUID = 64L;	
    static Logger logger = LogManager.getLogger(ExportXml.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String msg="";
		
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();		
	String name= "", action="Generate", id="";
	String value = "";
	while (values.hasMoreElements()){
	    name =  values.nextElement().trim();
	    value = req.getParameter(name).trim();
	    if(name.equals("action")){
		action = value;
	    }
	    if(name.equals("id")){
		id = value;
	    }			
	}

	Export export = new Export(debug, id);
	msg = export.doSelect();
	if(msg.equals("")){
	    List<Vendor> vendors = new ArrayList<>();
	    List<Redeem> redeems = export.getRedeems();
	    for(Redeem one:redeems){
		Vendor vndr = one.getVendor();
		if(!vendors.contains(vndr)){
		    vendors.add(vndr);
		}
	    }
	    if(vendors.size() > 0){
		List<String> nwVendorNumbers = null;
		VendorList vl = new VendorList(debug,
					       vendorsCheckUrl,
					       vendorsDatabase,
					       vendorsUser,
					       vendorsPassword);
		String back = vl.findNewVendors();
		if(back.equals("")){
		    nwVendorNumbers = vl.getNwVendorNumbers();
		    if(nwVendorNumbers != null && nwVendorNumbers.size() > 0){
			for(Vendor one:vendors){
			    String vn = one.getVendorNum();
			    if(!nwVendorNumbers.contains(vn)){
				msg += "<li>"+one.getInfo()+"</li>";
			    }
			}
		    }
		    if(!msg.isEmpty()){
			msg = "The following vendors were not found in New World<br /> They need to be added or their vendor number be fixed <br />"+msg;
		    }
		}
		else{
		    msg += back;
		}		
	    }
	    if(msg.isEmpty()){
		generateXml(res, export);
	    }
	}
	if(!msg.isEmpty()){
	    res.setContentType("text/html");
	    PrintWriter out = res.getWriter();
	    out.println("<head><title></title><body>");
	    out.println("<p><font color=\"red\">");
	    out.println(msg);
	    out.println("</p>");
	    out.println("</body>");
	    out.println("</html>");
	    out.flush();
	}
			
    }
    void generateXml(HttpServletResponse res, Export export){
	String filename="export_"+Helper.getCurrentYear()+"_"+export.getId()+".xml";
	String all = "";
	PrintWriter out = null;		
	try{
	    List<Redeem> redeems = export.getRedeems();
	    if(redeems != null && redeems.size() > 0){
		all += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
		all += "<ROOT>\n";
		for(Redeem one:redeems){
		    all += writeRedeem(one);
		}
		all += "</ROOT>\n";
	    }
			
	    res.setHeader("Expires", "0");
	    res.setHeader("Cache-Control", 
			  "must-revalidate, post-check=0, pre-check=0");
	    res.setHeader("Pragma", "public");
	    //
	    // setting the content type
	    res.setHeader("Content-Disposition"," attachment; filename="+filename);			
	    res.setContentType("application/xml");
	    out = res.getWriter();
	    out.print(all);
	    out.close();
	}catch(Exception ex){
	    System.err.println(ex);
	}
    }
    String writeRedeem(Redeem redeem){
	Vendor vendor = redeem.getVendor();
	List<Buck> mb_bucks = redeem.getBk_bucks(); // ebt and rx
	List<Buck> gc5_bucks = redeem.getGc5_bucks();
	List<Buck> gc20_bucks = redeem.getGc20_bucks();
	List<Buck> gc_bucks = redeem.getGc_bucks();
	int mb_total = 0, gc5_total = 0, gc20_total = 0, total = 0;
	int gc_total = 0;
	String desc = "";
	if(mb_bucks.size() > 0){
	    mb_total = mb_bucks.size() * 3;
	    desc = "Market Bucks";
	}
	if(gc5_bucks.size() > 0){
	    gc5_total = gc5_bucks.size() * 5;
	    gc_total = gc5_bucks.size() * 5;
	}
	if(gc20_bucks.size() > 0){
	    gc20_total = gc20_bucks.size() * 20;
	    gc_total += gc20_bucks.size() * 20;
	}
	if(gc_total > 0){
	    if(!desc.equals("")) desc += " and ";
	    desc += "Gift Certificates";
	}
	total = mb_total + gc_total;
	String str = "";
	if(total > 0){
	    str = "<accountsPayableInvoice number=\""+redeem.getId()+"\" date=\"\" description=\""+desc+"\" amount=\""+total+"\" freightAmount=\"\" stateSalesTaxAmount=\"\" countySalesTaxAmount=\"\" citySalesTaxAmount=\"\" discountAmount=\"\" checkNumber=\"\" checkSortCode=\"\" confirmingEFT=\"false\" confirmingEFTGLDate=\"\">\n";
			
	    str += "<payee payeeName=\"\" payeeIDNumber=\""+vendor.getVendorNum()+"\" payeeIDNumberType=\"1\" />\n";
	    str += "<remittanceAddress addressLine1=\"\" addressLine2=\"\" addressLine3=\"\" city=\"\" state=\"\" zip=\"\" vendorContactID=\"0\" />\n";
	}
	str += "<items>\n";
	if(mb_total > 0){
	    str += "   <item description=\"Market Bucks\" quantity=\"1\" unitOfMeasure=\"EA\" pricePerUnit=\""+mb_total+"\" applyFreight=\"false\" applyTaxes=\"false\" applyDiscounts=\"false\" type1099=\"\" box1099=\"\" purchaseOrderNumber=\"\">\n";
	    str += "    <glDistributions>\n";
	    str += "		<glDistribution glAccount=\"22111818650347240\" projectCode1=\"\" projectCode2=\"\" projectCode3=\"\" amount=\""+mb_total+"\" />\n";	    
	    str += "</glDistributions>\n";
	    str += "  </item>\n";
	}
	if(gc_total > 0){
	    str += "   <item description=\"Gift Cerificates\" quantity=\"1\" unitOfMeasure=\"EA\" pricePerUnit=\""+gc_total+"\" applyFreight=\"false\" applyTaxes=\"false\" applyDiscounts=\"false\" type1099=\"\" box1099=\"\" purchaseOrderNumber=\"\">\n";
	    str += "    <glDistributions>\n";
	    str += "		<glDistribution glAccount=\"22111818650347230\" projectCode1=\"\" projectCode2=\"\" projectCode3=\"\" amount=\""+gc_total+"\" />\n";
	    str += "</glDistributions>\n";
	    str += "  </item>\n";
	}		
	str += "</items>\n";
	str += " </accountsPayableInvoice>\n";
	return str;
    }

	
}






















































