<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Search Online Purchases (SNAP)</h3>
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
<s:form action="snapSearch" method="post">
    <table width="90%">
	<caption>Search Options</caption>
	<tr>
	    <th><label for="pid">Purchase ID:</label></th>
	    <td align="left"><s:textfield name="snapList.id" value="%{snapList.id}" size="8" id="pid" /></td>
	</tr>
	<tr>
 	    <th><label for="cardn">Card #:</label></th>
	    <td align="left"><s:textfield name="snapList.cardNumber" value="%{snapList.cardNumber}" size="4" maxlength="4" id="cardn" /> </td>
	</tr>		  
	<tr>
	    <th><label for="auth">Authorization #:</label></th>
	    <td align="left"><s:textfield name="snapList.authorization" value="%{snapList.authorization}" size="10" id="auth" /> </td>
	</tr>
	<tr>
	    <th><label for="amnt">Purchase Amount ($):</label></th>
	    <td align="left"><s:textfield name="snapList.amount" value="%{snapList.amount}" size="4" maxlength="4" id="amnt" /> </td>
	</tr>
	<tr>
	    <th><label for="dbl">Double Request:</label></th>
	    <td align="left"><s:radio name="snapList.doubleRequest" value="%{snapList.doubleRequest}" list="#{'-1':'All','Included':'Included','Not Included':'Not Included'}" id="dbl" /> </td>
	</tr>
	<tr>
	    <th><label for="status">Status:</label></th>
	    <td align="left"><s:radio name="snapList.status" value="%{snapList.status}" list="#{'-1':'All','Active':'Active','Cancelled':'Cancelled'}" id="status" /> </td>
	</tr>		
	<tr>
	    <th><b>Date:</b></th>
	    <td align="left"><label for="from"> From</label><s:textfield name="snapList.date_from" value="%{snapList.date_from}" size="10" maxlength="10" cssClass="date" id="from" /><label for="to"> To </label><s:textfield name="snapList.date_to" value="%{snapList.date_to}" size="10" maxlength="10" id="to" cssClass="date" /></td>
	</tr>  
	<tr>
	    <th><label for="sortby">Sort by:</label></th> 
	    <td align="left">
		<s:select name="snapList.sortBy" id="sortby"
			      value="%{snapList.sortBy}"
			  list="#{'-1':'ID','b.date':'Date & Time'}" headerKey="-1" headerValue="ID" /></td>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <td>
		<s:submit name="action" type="button" value="Search" />
	    </td>
	</tr>
    </table>
</s:form>
<s:if test="action != '' && hasSnaps()">
    <s:set var="snaps" value="snaps" />
    <s:set var="snapsTitle" value="snapsTitle" />
    <s:set var="snapTotal" value="snapTotal" />
    <s:set var="ebtTotal" value="ebtTotal" />
    <s:set var="dblTotal" value="dblTotal" />	
    <%@  include file="snaps.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>























































