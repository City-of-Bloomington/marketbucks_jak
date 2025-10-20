<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Search Batches</h3>
<s:if test="hasActionErrors()">
    <s:actionerror/>
</s:if>
<s:elseif test="hasActionMessages()">
    <s:actionmessage/>
</s:elseif>
<s:form action="batchSearch" method="post">
    <table width="90%">
	<caption>Batch Search</caption>
	<tr>
	    <th><label for="batch_id">Batch ID:</label></th>
	    <td align="left"><s:textfield name="batchList.id" value="%{id}" size="8" id="batch_id" /></td>
	</tr>
	<tr><th><label for="mb_id">MB/GC ID:</label></th>
	    <td align="left"><s:textfield name="batchList.seq_id" value="%{seq_id}" size="8" id="mb_id" /></td>
	</tr>
	<tr>
	    <th><label for="status">Status:</label></th>
	    <td align="left"><s:radio name="batchList.status" value="%{status}" list="#{'-1':'All','Printed':'Printed','Waiting':'Waiting'}" headerKey="-1" headerValue="All" id="status" /> </td>
	</tr>		  
	<tr>
	    <th><label for="type">Type:</label></th>
	    <td align="left"><s:radio name="batchList.type_id" value="%{type_id}" list="buck_types" listKey="id" listValue="name" headerKey="-1" headerValue="All" id="type" /> </td>
	</tr>
	<tr>
	    <th><b>Date:</b></th>
	    <td align="left"><label for="date_from"> From</label><s:textfield name="batchList.date_from" value="%{date_from}" size="10" maxlength="10" cssClass="date" id="date_from" /><label for="date_to"> To </label><s:textfield name="batchList.date_to" value="%{date_to}" size="10" maxlength="10" cssClass="date" id="date_to" /></td>
	</tr>  
	<tr>
	    <th><label for="sort_by">Sort by:</label></th>
	    <td align="left">
		<s:select name="batchList.sortBy" id="sort_by"
			  value="%{sortBy}"
			  list="#{'-1':'ID','b.date':'Date','b.start_seq':'Start Seq'}" headerKey="-1" headerValue="ID" /></td>
	</tr>
	<tr>
	    
	    <th>
		<s:submit name="action" type="button" value="Printable Audit Sheet" />
	    </th>			
	    <td>
		<s:submit name="action" type="button" value="Search" />
	    </td>
	</tr>
    </table>		  
</s:form>
<s:if test="action != ''">
  <s:set var="batches" value="batches" />
  <s:set var="batchesTitle" value="batchesTitle" />
  <%@  include file="batches.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>























































