<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="exportAdd" id="form_id" method="post">
  <s:hidden name="export.id" value="%{export.id}" />
  <h3>Update Export</h3>
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
  <s:if test="export.isOpen()">
      To do the export, please do the following:
      <ul>
	  <li> Run 'Generate Export File' first. </li>
	  <li> Save the generated file to your computer, it should have the extension .xml </li>
	  <li> Use New World system to import this file.</li>
	  <li> You will get the text number from New World system.</li>
	  <li> Insert this text number in the field named 'NW Batch Number'.</li>
	  <li> Click on the 'Update' button. </li>
	  <li> You are done. </li>
      </ul>
  </s:if>
  <table width="80%">
      <caption>Export</caption>
      <tr>
	  <th width="30%">ID:</th>
	  <td align="left"><s:property value="export.id" /></td>
      </tr>
      <tr>
	  <th>Date & Time:</th>
	  <td align="left"><s:property value="export.date_time" /></td>
      </tr>
      <tr>
	  <th>User:</th>
	  <td align="left"><s:property value="export.user" /></td>
      </tr>
      <tr>
	  <th>Total Value:</th>
	  <td align="left">$<s:property value="export.total" />.00</td>
      </tr>		  
      <tr>
	  <th>New World Batch Number:</th>
	  <td align="left">
	      <s:if test="export.isOpen()">
		  <s:textfield name="export.nw_batch_name" value="%{export.nw_batch_name}" size="20" maxlength="20" />
	      </s:if>
	      <s:else>
		  <s:property value="export.nw_batch_name" />
	      </s:else>
	  </td>
      </tr>
      <tr>
	  <th>Status:</th>
	  <td align="left"><s:property value="export.status" /></td>
      </tr>
      <tr>
	  <s:if test="export.isOpen()">
	      <th>
		  <button onclick="document.location='<s:property value='#application.url' />ExportXml.do?id=<s:property value='export.id' />';return false;">Generate Export File</button>						
	      </th>
	      <td>
		  <s:submit name="action" type="button" value="Update" />
	      </td>
	  </s:if>
      </tr>
  </table>
</s:form>      
<s:if test="export.redeems != null && export.redeems.size() > 0">
    <s:set var="redeems" value="export.redeems" />
    <s:set var="redeemsTitle" value="redeemsTitle" />
    <%@  include file="redeems.jsp" %>
</s:if>	  

<s:if test="exports != null && exports.size() > 0">
  <s:set var="exports" value="exports" />
  <s:set var="exportsTitle" value="exportsTitle" />
  <%@  include file="exports.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































