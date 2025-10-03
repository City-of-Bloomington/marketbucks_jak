package bucks.web;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import java.util.*;
import java.util.Locale;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import java.text.*;

import java.net.URL;
 
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;//
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
import bucks.utils.*;

@WebServlet(urlPatterns = {"/AuditSheet.do"}, loadOnStartup = 1)
public class AuditSheet extends TopServlet{

    //
    String imageUrl="";
    static final long serialVersionUID = 64L;
    static final String fontName2 ="Times-Roman";
    static final String fontName ="Verdana";
    static final Locale locale = new Locale("en", "US");      
    static NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(locale);	
    static Logger logger = LogManager.getLogger(AuditSheet.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String message="";
	imageUrl = url+"images/prlogo.jpg";
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();		
	String name= "", action="Generate", id="";
	String value = "";
	BatchList bl = new BatchList(debug);
	bl.setNoLimit();
	List<Batch> batches = null;
	while (values.hasMoreElements()){
	    name =  values.nextElement().trim();
	    value = req.getParameter(name).trim();
	    if(name.equals("action")){
		action = value;
	    }
	    else if(name.equals("id")){
		bl.setId(value);
	    }
	    else if(name.equals("seq_id")){
		bl.setSeq_id(value);
	    }			
	    else if(name.equals("date_from")){
		bl.setDate_from(value);
	    }
	    else if(name.equals("date_to")){
		bl.setDate_to(value);
	    }			
	    else if(name.equals("type_id")){
		bl.setType_id(value);
	    }			
	    else if(name.equals("sortBy")){
		bl.setSortBy(value);
	    }
	    else if(name.equals("status")){
		bl.setStatus(value);
	    }			
	    else {
		System.err.println(" name "+name+" "+value);
	    }
	}
	message = bl.find();
	batches = bl.getBatches();
	if(batches.size() == 0){
	    message = "No records found ";
	}
	if(message.equals("")){
	    generateSheet(res, batches);
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
    void generateSheet(HttpServletResponse res, List<Batch> batches){
		

	//
	// paper size legal (A4) 8.5 x 11
	// page 1-inch = 72 points
	//
	String spacer = "   \n";
	ServletOutputStream out = null;
	String filename="marketbucks_audit_sheet.pdf";		
	try{
	    // space
	    //			
	    Rectangle pageSize = new Rectangle(612, 792); // 8.5" X 11"
	    Document document = new Document(pageSize, 36, 36, 18, 18);// 18,18,54,35			
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter writer = PdfWriter.getInstance(document, baos);
	    document.open();
	    URL imgUrl = new URL(imageUrl);
	    Image image = Image.getInstance(imgUrl);
	    image.scalePercent(20f);
	    // image.scaleAbsolute(100f, 100f);// width, height
	    // image.setRotation(3.1f); // in radians
	    // image.setRotationDegrees(90f); // degrees
	    // image.setSpacingBefore(10f);
	    // image.setSpacingAfter(10f);
	    // image.setWidthPercentage(25.0f);
	    Font fnt = new Font(Font.TIMES_ROMAN, 12, Font.NORMAL);
	    Font fntb = new Font(Font.TIMES_ROMAN,12, Font.BOLD);
	    Font fntb2 = new Font(Font.TIMES_ROMAN,14, Font.BOLD);						
	    Phrase spacePhrase = new Phrase();
	    Chunk ch = new Chunk(spacer, fnt);
	    spacePhrase.add(ch);
			
	    float[] widths = {25f, 75f}; // percentages
	    PdfPTable headTable = new PdfPTable(widths);
	    headTable.setWidthPercentage(100);
	    headTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    headTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

	    PdfPCell cell = new PdfPCell(image);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    headTable.addCell(cell);			
			
	    Phrase phrase = new Phrase();
	    ch = new Chunk("Printed Market Bucks Audit Sheet\n ",fntb2);
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
	    document.add(spacePhrase);
	    document.add(spacePhrase);				
	    //
	    float[] widths3 = {50f, 50f}; // percentages
	    PdfPTable table = new PdfPTable(widths3);
	    table.setWidthPercentage(100);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    phrase = new Phrase();
	    ch = new Chunk(" Date: ",fntb);
	    phrase.add(ch);
	    ch = new Chunk(Helper.getToday()+"\n\n\n", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);
	    // 
	    phrase = new Phrase();
	    ch = new Chunk(" ",fntb); // empty cell
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setBorder(Rectangle.NO_BORDER);
	    table.addCell(cell);			
	    //			
	    document.add(table);
	    //
	    phrase = new Phrase(new Chunk("\n", fnt));
	    document.add(phrase);
	    //
	    float[] widths2 = {15f, 10f, 15f, 15f, 15f, 15f, 15f}; // percentages
	    table = new PdfPTable(widths2);
	    table.setWidthPercentage(90);
	    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	    // table.getDefaultCell().setPadding(5);
	    phrase = new Phrase();
	    ch = new Chunk("Count of MB/GC",fntb);
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
	    ch = new Chunk("Value",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);			
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Total Dollars",fntb);
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
	    ch = new Chunk("Printed By",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);			
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Start Number",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);			
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("End Number",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);			
	    table.addCell(cell);
			
	    phrase = new Phrase();
	    ch = new Chunk("Printed Date",fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorderWidth(2f);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(4);
	    cell.setLeading(2f,1.2f);	
	    table.addCell(cell);
	    //
	    // next rows
	    //
	    for(Batch one:batches){
		phrase = new Phrase();
		ch = new Chunk(one.getBatch_size(), fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);

		phrase = new Phrase();
		ch = new Chunk("$"+one.getValue()+".00",fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);
		//
		phrase = new Phrase();
		ch = new Chunk(moneyFormat.format(one.getTotal()),fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);
		//
		phrase = new Phrase();				
		ch = new Chunk(one.getUser().toString(),fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);
				
		phrase = new Phrase();
		ch = new Chunk(one.getStart_seq(),fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);
				
		phrase = new Phrase();
		ch = new Chunk(one.getEnd_seq(),fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);
				
		phrase = new Phrase();
		ch = new Chunk(one.getDate(),fnt);
		phrase.add(ch);
		cell = new PdfPCell(phrase);
		cell.setBorderWidth(1f);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(4);
		cell.setLeading(2f,1.2f);	
		table.addCell(cell);	
	    }				
	    document.add(table);
	    //
	    document.add(spacePhrase);
	    document.add(spacePhrase);					
	    //
	    ch = new Chunk("Audit By:____________________________________________________________________\n\n", fnt);
	    phrase = new Phrase();
	    phrase.add(ch);			
	    document.add(phrase);
	    ch = new Chunk("           :_______________________________________)_____________________________\n\n", fnt);
	    phrase = new Phrase();
	    phrase.add(ch);			
	    document.add(phrase);
	    ch = new Chunk("           :_____________________________________________________________________\n\n", fnt);
	    phrase = new Phrase();
	    phrase.add(ch);			
	    document.add(phrase);			
	    //
	    document.add(spacePhrase);
	    document.add(spacePhrase);			
	    //
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






















































