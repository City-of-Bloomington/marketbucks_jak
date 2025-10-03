<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="batchEdit" method="post">    
    <s:if test="batch.id == ''">
	<h3>Generate & Print Bucks</h3>
  </s:if>
  <s:else>
      <s:hidden name="batch.id" value="%{batch.id}" />
      <s:if test="batch.id != '' && batch.status == 'Waiting'">
	  <h3>Update Batch</h3>
      </s:if>
      <s:else>
	  <h3>Batch Info</h3>	  
      </s:else>
  </s:else>
  <s:hidden name="batch.conf_id" value="%{batch.conf_id}" />  
  <s:if test="hasActionErrors()">
      <div class="errors">
	  <s:actionerror/>
      </div>
  </s:if>
  <s:elseif test="hasActionMessages()">
      <div class="welcome">
	  <s:actionmessage/>
      </div>
  </s:elseif>
  <p>* Indicate a required field</p>
  <s:if test="batch.status == 'Waiting'">
      <s:if test="batch.id != ''">
	  <p>Note: Printing Setup<br />
	      When create a new batch, before you print please make sure
	      the following options are checked on the Printer page after you click on 'Printable Certificates':<br />
	      <ul>
		  <li>Actual size</li>
		  <li>Choose paper source by PDF page size</li>
		  <li>Portrait</li>
		  <li>any thing else should be unchecked</li>
	      </ul>
	      We recommend that you print the first page for test purpose, and see if
	      the printing is OK before you send the whole batch to printer.
	  </p>
      </s:if>
  </s:if>
  
  <table width="90%" border="0">
      <caption> Batch Details</caption>
      <s:if test="batch.id != ''">
	  <tr>
	      <th><b>ID:</b></th>
	      <td align="left"><s:property value="id" /></td>
	  </tr>
      </s:if>
      <tr>
	  <th width="30%"><label for="batch_pages">* Number of pages:</label></th>
	  <td><s:textfield name="batch.pages" maxlength="4" size="4" required="true" value="%{batch.pages}" id="batch_pages" cssClass="need_focus" /> (Each page has 3 MB's or GC's)</td>
      </tr>
      <tr>
	  <th width="30%"><label for="start_seq">*Start seq:</label></th>
	  <td align="left"><s:textfield name="batch.start_seq" maxlength="10" size="10" required="true" id="start_seq" value="%{batch.start_seq}" /></td>
      </tr>
      <tr>
	  <th><b>Type:</b></th>		
	  <td align="left"><s:property value="%{batch.type}" /></td>
      </tr>
      <tr>
	  <th><b>Face value:</b></th>		
	  <td align="left">$<s:property value="%{batch.value}" />.00</td>
      </tr>		
      <tr>
	  <th><b>Status:</b></th>
	  <td align="left"><s:property value="%{batch.status}" /></td>
      </tr>
      <s:if test="batch.id != ''">				
	  <tr>
	      <th><b>Date:</b></th>
	      <td align="left"><s:property value="%{batch.date}" /></td>
	  </tr>
	  <tr>
	      <th><lb>User:</b></th>
	      <td align="left"><s:property value="%{batch.user}" /></td>
	  </tr>		  
      </s:if>
      <s:if test="batch.id != '' && batch.status == 'Waiting'">
	  <tr>
	      <td>&nbsp;</td>
	      <td>
		  Please enter the last printed buck number if different than what is showing.
	      </td>
	  </tr>
	  <tr>
	      <th><label for="last_seq_id">* Last MB/GC number:</label></th>
	      <td align="left"><s:textfield name="batch.last_seq_printed" maxlength="20" size="20" required="true" id="last_seq_id" value="%{batch.end_seq}" /></td>
	  </tr>
      </s:if>
      <s:if test="batch.status == 'Waiting'">
	      <s:if test="batch.id == ''">
		  <s:if test="batch.conf_id != ''">
		      <tr>	      
			  <td>&nbsp;</td>
			  <td>
			      <s:submit name="action" type="button" value="Next" />
			  </td>
		      </tr>
		  </s:if>
	      </s:if>
	      <s:else>
		  <tr><td colspan="2">Note:Print the Certificate first and OK then Confirm </td>
		  </tr>
		  <tr>
		      <th valign="top">
		      	  <button onclick="document.location='<s:property value='#application.url' />GenerateChecks.do?id=<s:property value='batch.id' />';return false;">Printable Certificates</button>
		      </th>		      
		      <td valign="top"><label for="confirm"></label><s:submit name="action" type="button" value="Confirm" id="confirm" />
		      </td>		  

		  </tr>
	      </s:else>
      </s:if>
      <s:if test="batch.conf_id != '' && batch.status == 'Printed' ">
	  <tr>
	      <td>&nbsp;</td>
	      <td>
		  <button id="add_batch" onclick="document.location='<s:property value='#application.url' />batchEdit.action?conf_id=<s:property value='batch.conf_id' />';return false;"><label for="add_batch">Add New Batch</label></button>
	      </td>
	  </tr>      	      
      </s:if>
  </table>
  <s:if test="batch.id != '' && batch.seq_list != null">
      <table width="100%">
	  <caption>Certificate numbers in this batch </caption>
	  <tr><td>
	      <s:iterator value="batch.seq_list">
		  <s:property />
	      </s:iterator>
	  </td></tr>
      </table>
  </s:if>	
</s:form>

<s:if test="batches != null">
    <s:set var="batches" value="batches" />
    <s:set var="batchesTitle" value="batchesTitle" />  
    <%@  include file="batches.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































