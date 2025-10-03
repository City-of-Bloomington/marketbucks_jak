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
import java.text.*;

import java.net.URL;
 
import java.io.FileOutputStream;
import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Font;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
// import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;

@WebServlet(urlPatterns = {"/RedeemInvoice.do"}, loadOnStartup = 1)
public class RedeemInvoice extends TopServlet{

    //
    String imageUrl="";
    static final long serialVersionUID = 63L;
    static final String fontName ="Times-Roman";
    static final String fontName2 ="Verdana";		
    static Logger logger = LogManager.getLogger(RedeemInvoice.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String message="";
	// imageUrl = url+"images/PnRlogo.jpg";			
	imageUrl = url+"images/prlogo.jpg";			
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

	Redeem redeem = new Redeem(debug, id);
	message = redeem.doSelect();
	if(message.equals("")){
	    generateInvoice(res, redeem);
	}
	else{
	    res.setContentType("text/html");
	    PrintWriter out = res.getWriter();
	    out.println("<head><title></title><body>");
	    out.println("<p><font color=red>");
	    out.println(message);
	    out.println("</p>");
	    out.println("</body>");
	    out.println("</html>");
	    out.flush();
	}
			
    }
    void generateInvoice(HttpServletResponse res, Redeem redeem){
		

	//
	// paper size legal (A4) 8.5 x 11
	// page 1-inch = 72 points
	//
	String spacer = "   \n";
	User user = redeem.getUser();
	Vendor vendor = redeem.getVendor();
	List<Buck> bk_bucks = redeem.getBk_bucks();
	List<Buck> gc5_bucks = redeem.getGc5_bucks();
	List<Buck> gc20_bucks = redeem.getGc20_bucks();
	List<Buck> gc_bucks = redeem.getGc_bucks();
	List<Dispute> disputes = redeem.getDisputes();
	int bk_total = 0, gc5_total = 0, gc20_total = 0, total = 0;
	if(bk_bucks.size() > 0){
	    bk_total = bk_bucks.size() * 3;
	}
	if(gc5_bucks.size() > 0){
	    gc5_total = gc5_bucks.size() * 5;
	}
	if(gc20_bucks.size() > 0){
	    gc20_total = gc20_bucks.size() * 20;
	}
	total = bk_total + gc5_total + gc20_total;
	String vendor_name = "";
	if(vendor != null){
	    vendor_name = vendor.getCleanName();
	}
	ServletOutputStream out = null;
	String filename="invoice_"+vendor_name+"_"+redeem.getId()+".pdf";		
	try{
	    // space
	    //			
	    Rectangle pageSize = new Rectangle(612, 792); // 8.5" X 11"
	    Document document = new Document(pageSize, 36, 36, 18, 18);// 18,18,54,35			
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter writer = PdfWriter.getInstance(document, baos);
	    document.open();			
	    Image image = Image.getInstance(imageUrl);
	    // image = Image.getInstance(byte bytes[]);// generated image
	    image.scalePercent(20f);						
	    Font fnt = new Font(Font.TIMES_ROMAN, 12, Font.NORMAL);
	    Font fntb = new Font(Font.TIMES_ROMAN,12, Font.BOLD);
	    Font fntb2 = new Font(Font.TIMES_ROMAN,14, Font.BOLD);			
						
	    //
	    Phrase spacePhrase = new Phrase();
	    Chunk ch = new Chunk(spacer, fnt);
	    spacePhrase.add(ch);
			
	    float[] widths = {15f, 85f}; // percentages
	    PdfPTable headTable = new PdfPTable(widths);
	    headTable.setWidthPercentage(100);
	    headTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    headTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    // image.setWidthPercentage(15.0f);
	    PdfPCell cell = new PdfPCell(image);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    // cell.setFixedHeight(46f);
	    headTable.addCell(cell);			
			
	    Phrase phrase = new Phrase();
	    ch = new Chunk("City of Bloomington Community Farmers Market\n Gift Certificates/Market Bucks Invoice\n ",fntb2);
	    phrase.add(ch);
	    Paragraph pp = new Paragraph();
	    pp.setIndentationLeft(20);
	    pp.setAlignment(Element.ALIGN_LEFT);
	    pp.add(phrase);
	    cell = new PdfPCell(pp);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
	    headTable.addCell(cell);
	    document.add(headTable);
	    //
	    float[] widths3 = {50f, 50f}; // percentages
	    PdfPTable table = new PdfPTable(widths3);
	    table.setWidthPercentage(100);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    phrase = new Phrase();
	    ch = new Chunk(" Vendor Name: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(vendor.getFullName(), fnt);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);
	    //			
	    phrase = new Phrase();
	    ch = new Chunk("Date: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(redeem.getDate(), fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);
	    phrase = new Phrase();
	    ch = new Chunk(" Vendor Number: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(vendor.getVendorNum(), fnt);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);
	    phrase = new Phrase(); // empty cell
	    ch = new Chunk(" ",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);
	    table.addCell(cell); // extra space
	    table.addCell(cell);			
	    document.add(table);
	    //
	    phrase = new Phrase(new Chunk("\n", fnt));
	    document.add(phrase);
	    //
	    float[] widths2 = {30f, 25f, 15f, 15f, 15f}; // percentages
	    table = new PdfPTable(widths2);
	    table.setWidthPercentage(90);
	    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	    // table.getDefaultCell().setPadding(5);
	    phrase = new Phrase();
	    ch = new Chunk("Type of Voucher",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);			
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("City Account Number",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);	
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Quantity",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);	
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("Multiply",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);	
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("Value",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);	
	    table.addCell(cell);
	    //
	    // second row
	    phrase = new Phrase();
	    ch = new Chunk("Market Bucks",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("201-18-1865003-47240",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);			
	    //
	    phrase = new Phrase();
	    String str = " ";
	    if(bk_bucks.size() > 0){
		str = ""+bk_bucks.size();
	    }
	    ch = new Chunk(str, fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("x $3.00 = ",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);

	    phrase = new Phrase();
	    str = " ";
	    if(bk_total > 0){
		str = "$"+bk_total+".00";
	    }
	    ch = new Chunk(str,fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);
	    //
	    // 3rd row
	    phrase = new Phrase();
	    ch = new Chunk("$5 Gift Certificates",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("201-18-1865003-47230",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);			
	    //
	    phrase = new Phrase();
	    str = " ";
	    if(gc5_bucks.size() > 0){
		str = ""+gc5_bucks.size();
	    }
	    ch = new Chunk(str,fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("x $5.00 = ",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);

	    phrase = new Phrase();
	    str = " ";
	    if(gc5_total > 0){
		str = "$"+gc5_total+".00";
	    }
	    ch = new Chunk(str, fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);
	    //
	    // 4th row
	    phrase = new Phrase();
	    ch = new Chunk("$20 Gift Certificates",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("201-18-1865003-47230",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);			
	    //
	    phrase = new Phrase();
	    str = " ";
	    if(gc20_bucks.size() > 0){
		str = ""+gc20_bucks.size();
	    }
	    ch = new Chunk(str, fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("x $20.00 = ",fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    table.addCell(cell);
	    str = " ";
	    if(gc20_total > 0){
		str = "$"+gc20_total+".00";
	    }
	    phrase = new Phrase();
	    ch = new Chunk(str,fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(4);
	    table.addCell(cell);			
	    //
	    // 5th row total
	    phrase = new Phrase();
	    ch = new Chunk(" Total Value: ",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setColspan(4);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setBorderWidth(2f);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);
	    table.addCell(cell);					
	    //
	    phrase = new Phrase();
	    ch = new Chunk("$"+total+".00",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell.setVerticalAlignment(Element.ALIGN_CENTER);
	    cell.setBorderWidth(2f);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);
	    table.addCell(cell);				
	    document.add(table);
	    //
	    //
	    float[] withs5 = {100f};
	    table = new PdfPTable(withs5);
	    table.setWidthPercentage(100);			
	    cell = new PdfPCell(new Phrase(" "));
	    cell.setBorder(Rectangle.BOTTOM);
	    // cell.setBorderColorBottom(Color.BLACK);
	    cell.setBorderWidthBottom(2f);
	    table.addCell(cell);			
	    document.add(table);
	    //
	    ch = new Chunk("\n", fnt);
	    phrase = new Phrase();
	    phrase.add(ch);			
	    document.add(phrase);
	    //
	    // float[] widths3 = {50f, 50f}; // percentages
	    table = new PdfPTable(widths3);
	    table.setWidthPercentage(100);
	    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    phrase = new Phrase();
	    ch = new Chunk(" Data Entry Completed by: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(user.getFullName() ,fnt);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
	    table.addCell(cell);			
	    //
	    phrase = new Phrase();
	    ch = new Chunk(" Invoice Number: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(redeem.getId() ,fnt);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
	    table.addCell(cell);
	    document.add(table);
	    document.add(spacePhrase);
	    document.add(spacePhrase);			
	    //
	    // adding bucks and GC
	    //
	    float[] widths4 = {100f}; // percentages
	    table = new PdfPTable(widths4);
	    table.setWidthPercentage(100);
	    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    int row = 1, col=1;
	    if(bk_bucks.size() > 0){
		phrase = new Phrase();
		ch = new Chunk(" Market Bucks ",fntb);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(2f);
		table.addCell(cell);
		phrase = new Phrase();				
		for(Buck one:bk_bucks){
		    ch = new Chunk(""+one.getId()+" ", fntb);
		    phrase.add(ch);
		    col++;
		    if(col > 15){
			row++;
			col = 1;
			cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
			table.addCell(cell);
			phrase = new Phrase();	
		    }
		}
		if(col > 1){
		    cell = new PdfPCell(phrase);
		    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
		    table.addCell(cell);
		}
		if(row < 10){
		    for(int j=0;j<2;j++){
			phrase = new Phrase();
			ch = new Chunk(" ",fntb);
			phrase.add(ch);
			cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
			table.addCell(cell);
			row++;
		    }
		}
	    }
	    col = 1;
	    if(gc_bucks.size() > 0){
		phrase = new Phrase();
		ch = new Chunk(" Gift Certificates ",fntb);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(2f);
		table.addCell(cell);
		phrase = new Phrase();				
		for(Buck one:gc_bucks){
		    ch = new Chunk(""+one.getId()+" ", fntb);
		    phrase.add(ch);
		    col++;
		    if(col > 15){
			row++;
			col = 1;
			cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
			table.addCell(cell);
			phrase = new Phrase();	
		    }
		}
		if(col > 1){
		    cell = new PdfPCell(phrase);
		    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
		    table.addCell(cell);
		}
		if(row < 15){
		    for(int j=0;j<2;j++){
			phrase = new Phrase();
			ch = new Chunk(" ",fntb);
			phrase.add(ch);
			cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
			table.addCell(cell);
			row++;
		    }
		}
	    }
	    if(disputes != null && disputes.size() > 0){
		phrase = new Phrase();
		ch = new Chunk(" Disputed Market Bucks and/or Gift Certificates (number: reason)",fntb);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(2f);
		table.addCell(cell);
		row++;
		for(Dispute one:disputes){
		    phrase = new Phrase();
		    ch = new Chunk(one.getBuck_id()+": "+one.getReason(), fnt);
		    phrase.add(ch);
		    if(one.hasNotes()){
			ch = new Chunk("\nNotice: "+one.getNotes(), fnt);
			phrase.add(ch);
			row++;
		    }
		    cell = new PdfPCell(phrase);
		    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
		    table.addCell(cell);
		    row++;
		}
	    }
	    if(redeem.hasNotes()){
		phrase = new Phrase();
		ch = new Chunk("\nNotice: "+redeem.getNotes(), fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
		table.addCell(cell);
		row++;
	    }
	    if(row < 18){
		for(int j=row;j<18;j++){
		    phrase = new Phrase();
		    ch = new Chunk(" ",fntb);
		    phrase.add(ch);
		    cell = new PdfPCell(phrase);
		    cell.setHorizontalAlignment(Element.ALIGN_LEFT);	
		    table.addCell(cell);
		}
	    }
			
	    document.add(table);
	    document.close();
	    writer.close();
	    res.setHeader("Expires", "0");
	    res.setHeader("Cache-Control", 
			  "must-revalidate, post-check=0, pre-check=0");
	    res.setHeader("Pragma", "public");
	    //
	    // setting the content type
	    res.setHeader("Content-Disposition"," attachment; filename="+filename);			
	    res.setContentType("application/pdf");
	    //
	    // the contentlength is needed for MSIE!!!
	    res.setContentLength(baos.size());
	    //
	    out = res.getOutputStream();
	    if(out != null){
		baos.writeTo(out);
	    }			
	}catch(Exception ex){
	    System.err.println(ex);
	}
    }

	
}






















































