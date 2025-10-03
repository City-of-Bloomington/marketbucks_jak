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
//
// for barbacue barcode 
import javax.swing.*;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;
import jakarta.servlet.annotation.WebServlet;//

import java.io.FileOutputStream;
import java.io.IOException;
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
//import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bucks.list.*;
import bucks.model.*;
import bucks.utils.*;
@WebServlet(urlPatterns = {"/GenerateChecks.do"}, loadOnStartup = 1)

public class GenerateChecks extends HttpServlet{
    //
    String url="", imageUrl="";
    boolean debug = false;
    static Font fnts=null,fnt10=null,fnt=null,fntb=null, fnt2=null, fntb2=null;
    static final long serialVersionUID = 63L;
    static final String fontName ="Verdana";	
    static Logger logger = LogManager.getLogger(GenerateChecks.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String message="";
	if(url.equals("")){
	    url  = getServletContext().getInitParameter("url");
	    String str = getServletContext().getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	    imageUrl = url +"images/city_logo3.jpg";
	    try{
		FontFactory.register("c:/windows/fonts/verdana.ttf","Verdana");
		FontFactory.register("c:/windows/fonts/verdana.ttf","VerdanaB");
	    }catch(Exception e){
		// linux 
		// need to be installed
		try{
		    FontFactory.register("/srv/webapps/marketbucks/fonts/verdana.ttf","Verdana");								
		    FontFactory.register("/srv/webapps/marketbucks/fonts/verdanab.ttf","VerdanaB");								
		}catch(Exception ee){
		    FontFactory.register("/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf","Verdana");
		    FontFactory.register("/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf","VerdanaB");								
		}
	    }
	    fnts = FontFactory.getFont(fontName, 8); // small
	    fnt10 = FontFactory.getFont(fontName, 10);								
	    fnt = FontFactory.getFont(fontName, 12);				
	    fntb = FontFactory.getFont(fontName, 12, Font.BOLD);
	    fnt2 = FontFactory.getFont(fontName, 14);	
	    fntb2 = FontFactory.getFont(fontName, 14, Font.BOLD);								
	}
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();		
	String name= "", action="Generate";
	String value = "", id="";
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
	if(!id.equals("")){
	    Batch batch = new Batch(debug, id);
	    String back = batch.doSelect();
	    if(back.equals("")){
		generate(res, batch);
	    }
	    else{
		message += back;
	    }
	}
	else{
	    message = "Batch id not set ";
	}
	if(!message.equals("")){
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
    void generate(HttpServletResponse res, Batch batch){
	ServletOutputStream out = null;
	BuckConf conf = batch.getConf();
	int start_seq = batch.getStart_seq_int();
	int size = batch.getBatch_size_int();
	String type = conf.getType_id();
	String value = conf.getValue();
	String filename = "market_bucks_"+Helper.getCurrentYear()+"_"+batch.getId()+".pdf";
	// we need to register these fonts
	// windows we use verdana

	//
	// for test purpose
	//
	/*
	  start_seq = 612001;
	  type="2";
	  value="5";
	  size = 6;
	*/
	try{
	    Image image = Image.getInstance(imageUrl);
	    // image.setWidthPercentage(20.0f);
	    image.scalePercent(10f);
	    Rectangle pageSize = new Rectangle(612, 792); // 8.5" X 11"
	    // left right top bottom
	    Document document = new Document(pageSize, 15, 15, 18, 12);// 1/4 inch
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter writer = PdfWriter.getInstance(document, baos);
	    document.open();
	    //
	    int row = 1, page_no = 0;
	    int page = 1;
	    if(type.equals("1")){ // MB
		for(int jj=0;jj<size;jj++){
		    generate_mb(document, start_seq+jj, value, image, writer);
		    row++;
		    if(row == 4 && (jj+1) < size){ // to avoid empty page
			row = 1;
			page++;
			page_no = writer.getPageNumber();
			if(page_no < page){
			    document.newPage();
			}
		    }
		}
	    }
	    else{  // GC
		for(int jj=0;jj<size;jj++){
		    generate_gc(document, start_seq+jj, value, image, writer);
		    row++;
		    if(row == 4 && (jj+1) < size){ // to avoid empty page
			row = 1;
			page++;
			page_no = writer.getPageNumber();
			// System.err.println(page+" "+page_no);
			if(page_no < page){
			    document.newPage();
			}						
		    }
		}
	    }			
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
    void generate_mb(Document document, int seq,
		     String value,
		     Image image,
		     PdfWriter writer){
	try{
	    float[] widths = {15f, 40f, 45f}; // percentages			
	    PdfPTable table = new PdfPTable(widths);
	    table.setWidthPercentage(100);
	    table.setSpacingAfter(0f);
	    table.setSpacingBefore(0f);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    PdfPCell cell = new PdfPCell(image);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setFixedHeight(46f);
	    table.addCell(cell);
	    Phrase phrase = new Phrase();
	    Chunk ch = new Chunk("FARMERS' MARKET BUCKS\n",fntb2);
	    phrase.add(ch);			
	    ch = new Chunk("Parks & Recreation\nBloomington, IN", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Issue Date___________ ", fnt10);
	    phrase.add(ch);
	    ch = new Chunk("No. "+seq, fnt10);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);
	    //
	    // document.add(table);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("\nThis certificate is good for ", fnt);
	    phrase.add(ch);
	    ch = new Chunk("$"+value+".00 ", fntb2);
	    phrase.add(ch);
	    ch = new Chunk("towards the purchase of ", fnt);
	    phrase.add(ch);			
	    ch = new Chunk("eligible food items ", fntb);
	    phrase.add(ch);
	    ch = new Chunk("at the Bloomington Community Farmers' Market. ", fnt);
	    phrase.add(ch);
	    ch = new Chunk("No change allowed. \n", fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    cell.setFixedHeight(48f);
	    cell.setColspan(3);
	    table.addCell(cell);
			
	    document.add(table);
	    //
	    // float[] widths2 = {60f, 25f, 8f, 7f };
	    float[] widths2 = {65f, 35f }; // 60, 40		    
	    table = new PdfPTable(widths2);
	    table.setWidthPercentage(100);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    //table.getDefaultCell().setPadding(0);
	    table.setSpacingAfter(0f);
	    table.setSpacingBefore(0f);
	    //
	    // empty cell
	    phrase = new Phrase();
	    ch = new Chunk(" ", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    // cell.setColspan(2);
	    cell.setFixedHeight(30f); //81
	    table.addCell(cell);
	    ///
	    // now we add barcode
	    // move the followin after barcode
	    //
	    /**
	    phrase = new Phrase();
	    ch = new Chunk("Customers shall redeem this certificate at Farmers' Market\nby December 1st of the year issued.", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

	    cell.setColspan(2);
	    cell.setFixedHeight(81f);
	    table.addCell(cell);
	    //
	    */
	    Barcode barcode = BarcodeFactory.createCode39(""+seq, false);
	    // barcode text has problem, so we turned off
	    // and decided to add it ourselves.
	    barcode.setDrawingText(false);
	    // barcode.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
	    // barcode.setLabel(""+seq);
	    BufferedImage bi = BarcodeImageHandler.getImage(barcode);
	    /*
	      int width = bi.getWidth();
	      int height = bi.getHeight();
	      Graphics2D g2 = bi.createGraphics();
	      g2.setColor(Color.BLACK);
	      g2.drawString(""+seq, 60, 70);
	      g2.dispose();
	    */
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    //
	    // added this hack to get rid of underline in the barcode
	    // this worked on windows, but the text did not show up on linux
	    // that is why we decided to abandon the text all the way
	    // and add it ourselves
	    /*
	      BarcodeImageHandler.writePNG(barcode, baos);
	      File bcImg = File.createTempFile("bc-", ".png");
	      bcImg.deleteOnExit();
	      byte[] imageBytes = baos.toByteArray();
	      FileOutputStream fos = new FileOutputStream(bcImg);
	      BarcodeImageHandler.writePNG(barcode, fos);
	    */
	    //
	    // old
	    ImageIO.write(bi, "png", baos);
	    byte[] imageBytes = baos.toByteArray();
	    //
	    Image barCodeImage = Image.getInstance(imageBytes);
	    // to make it vertical rotate 90 deg
	    // barCodeImage.setRotationDegrees(90f);
	    // barCodeImage.scalePercent(35f);
	    barCodeImage.scalePercent(35f, 50f);
	    // float widthf = barCodeImage.getWidth();
	    // float heightf = barCodeImage.getHeight();
						
	    cell = new PdfPCell(barCodeImage);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER); // RIGHT
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(0);
	    cell.setLeading(0f,0f);
	    // cell.setFixedHeight(81f);
	    table.addCell(cell);
	    //
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Customers shall redeem this certificate at Farmers' Market\nby December 1st of the year issued.", fnt10);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

	    // cell.setColspan(2);
	    // cell.setFixedHeight(31f); // 81
	    table.addCell(cell);
	    //
	    //
	    // the barcode text that we added underneath the barcode
	    //
	    int textWidth=53, textHeight=50;
	    BufferedImage bi2 = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = bi2.createGraphics();
	    g.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 10));
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, textWidth, textHeight);
	    g.setColor(Color.BLACK);						
	    g.drawString(""+seq, 5, 10);
						
	    g.dispose();
	    baos = new ByteArrayOutputStream();						
	    ImageIO.write(bi2, "png", baos);
	    imageBytes = baos.toByteArray();
	    Image barCodeTextImage = Image.getInstance(imageBytes);
						
	    // barCodeTextImage.setRotationDegrees(90f);
	    cell = new PdfPCell(barCodeTextImage);
	    cell.setBorder(Rectangle.NO_BORDER);
	    // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER); // RIGHT
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);	    
	    cell.setPadding(0);
	    cell.setLeading(0f,0f);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("\nThis certificate is not refundable and cannot be replaced if lost or stolen.\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setFixedHeight(34f);
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("\nMust have official stamp to be valid.\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setColspan(2); // 3
	    table.addCell(cell);
	    document.add(table);
	    //
	    float[] widths4 = {40f, 60f}; // percentages			
	    table = new PdfPTable(widths4);
	    table.setWidthPercentage(100);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    table.setSpacingBefore(0f);			
	    table.setSpacingAfter(0f);
	    phrase = new Phrase();
	    ch = new Chunk("APPROVED BY THE STATE BOARD OF ACCOUNTS\nFOR THE CITY OF BLOOMINGTON, IN 2007\n\n\n",fnts);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
	    cell.setFixedHeight(50f); // 40
	    table.addCell(cell);
	    phrase = new Phrase();
	    ch = new Chunk("VENDERS SHALL REDEEM THIS CERTIFICATE THROUGH BLOOMINGTON'S\nPARKS & REC DEPT. BY DECEMBER 15TH OF THE YEAR ISSUED\n\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);		    
	    table.addCell(cell);
	    document.add(table);
	}catch(Exception ex){
	    System.err.println(ex);
	}	
			
    }
		
    void generate_gc(Document document, int seq, String value,
		     Image image,
		     PdfWriter writer
		     ){
	try{
	    float[] widths = {13f, 54f, 33f}; // percents
	    PdfPTable table = new PdfPTable(widths);
	    table.setWidthPercentage(100);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.setSpacingAfter(0f);
	    table.setSpacingBefore(0f);
	    PdfPCell cell = new PdfPCell(image);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    cell.setFixedHeight(46f);
	    table.addCell(cell);
	    Phrase phrase = new Phrase();
	    Chunk ch = new Chunk("FARMERS' MARKET GIFT CERTIFICATE\n",fntb2);
	    phrase.add(ch);			
	    ch = new Chunk("Parks & Recreation\nBloomington, IN", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("Issue Date__________ ", fnt10);
	    phrase.add(ch);
	    ch = new Chunk("No. "+seq, fnt10);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    table.addCell(cell);
	    //
	    phrase = new Phrase();
	    ch = new Chunk("\nThis certificate is good for ", fnt);
	    phrase.add(ch);
	    ch = new Chunk("$"+value+".00 ", fntb2);
	    phrase.add(ch);
	    ch = new Chunk("towards the purchase of ", fnt);
	    phrase.add(ch);			
	    ch = new Chunk("products from ", fnt);
	    phrase.add(ch);
	    ch = new Chunk("the Bloomington Community Farmers' Market and A Fair of The Arts Vendors. ", fnt);
	    phrase.add(ch);
	    ch = new Chunk("Change may be given. \n", fntb);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    cell.setColspan(3);
	    cell.setFixedHeight(48f);			
	    table.addCell(cell);			
	    document.add(table);
	    //
	    float[] widths2 = {65f, 35f}; 
	    table = new PdfPTable(widths2);
	    table.setWidthPercentage(100);
	    table.setSpacingBefore(0f);
	    table.setSpacingAfter(0f);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    //
	    // empty cell
	    phrase = new Phrase();
	    ch = new Chunk(" ", fnt);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    cell.setFixedHeight(30f); //81
	    table.addCell(cell);
	    ///
	    Barcode barcode = BarcodeFactory.createCode39(""+seq,false);
	    barcode.setDrawingText(false);
	    BufferedImage bi = BarcodeImageHandler.getImage(barcode);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    //
	    ImageIO.write(bi, "png", baos);
	    byte[] imageBytes = baos.toByteArray();
	    //
	    Image barCodeImage = Image.getInstance(imageBytes);						
	    // rotate to be vertical
	    // barCodeImage.setRotationDegrees(90);
	    // barCodeImage.scalePercent(35f);
	    barCodeImage.scalePercent(35f);
	    cell = new PdfPCell(barCodeImage);
	    //
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setPadding(0);
	    cell.setLeading(0f,0f);
	    table.addCell(cell);
	    //
	    
	    phrase = new Phrase();
	    ch = new Chunk("Customers shall redeem this certificate at Market\nwithin one year of date issued. ", fnt10);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	    // cell.setColspan(2);
	    // cell.setFixedHeight(81f);
	    table.addCell(cell);
	    //
	    // the barcode text
	    int textWidth=53, textHeight=50;
	    BufferedImage bi2 = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D g = bi2.createGraphics();
	    g.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 10));
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, textWidth, textHeight);
	    g.setColor(Color.BLACK);						
	    g.drawString(""+seq, 5, 10);
						
	    g.dispose();
	    baos = new ByteArrayOutputStream();						
	    ImageIO.write(bi2, "png", baos);
	    imageBytes = baos.toByteArray();
	    Image barCodeTextImage = Image.getInstance(imageBytes);
						
	    // barCodeTextImage.setRotationDegrees(90f);
	    cell = new PdfPCell(barCodeTextImage);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);	    
	    
	    cell.setPadding(0);
	    cell.setLeading(0f,0f);
	    table.addCell(cell);
	    //
	    //
	    phrase = new Phrase();
	    ch = new Chunk("\nThis certificate is not refundable and cannot be replaced if lost or stolen.\n\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setFixedHeight(34f);
	    table.addCell(cell);

	    phrase = new Phrase();
	    ch = new Chunk("\nMust have official stamp to be valid.\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_TOP);
	    // cell.setColspan(3);
	    table.addCell(cell);
	    document.add(table);
	    //
	    float[] widths3 = {40f, 60f}; // percentages			
	    table = new PdfPTable(widths3);
	    table.setWidthPercentage(100);
	    table.setSpacingBefore(0f);
	    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
	    phrase = new Phrase();
	    ch = new Chunk("APPROVED BY THE STATE BOARD OF ACCOUNTS\nFOR THE CITY OF BLOOMINGTON, IN 2007\n\n\n",fnts);
	    phrase.add(ch);			
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.setFixedHeight(40f);
	    table.addCell(cell);
	    phrase = new Phrase();
	    ch = new Chunk("VENDERS SHALL REDEEM THIS CERTIFICATE THROUGH BLOOMINGTON'S\nPARKS & REC DEPT. BY DECEMBER 15TH OF THE YEAR RECEIVED\n\n\n",fnts);
	    phrase.add(ch);
	    cell = new PdfPCell(phrase);
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    // cell.setFixedHeight(50f); 
	    table.addCell(cell);
			
	    document.add(table);
	    // document.add( Chunk.NEWLINE );
	}catch(Exception ex){
	    System.err.println(ex);
	}	
    }
	
}






















































