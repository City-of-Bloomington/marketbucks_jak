<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<h3>Search Ebt Transactions</h3>
<s:if test="hasActionErrors()">
    <s:actionerror/>
</s:if>
<s:elseif test="hasActionMessages()">
    <s:actionmessage/>
</s:elseif>
<s:form action="ebtSearch" method="post">
    <table width="80%">
	<caption>Search Options</caption>
	<tr><th><label for="ebt_id">Ebt ID:</label></th>
	    <td align="left"><s:textfield name="ebtList.id" value="%{ebtList.id}" size="8" id="ebt_id" /></td>
	</tr>
	<tr><th><label for="mb_id">MB ID:</label></th>
	    <td align="left"><s:textfield name="ebtList.buck_id" value="%{ebtList.buck_id}" size="8" id="mb_id" /></td>
	</tr>
	<tr>
	    <th><label for="card_num">Card #:</label></th>
	    <td align="left"><s:textfield name="ebtList.card_last_4" value="%{ebtList.card_last_4}" size="4" maxlength="4" id="card_num" /> </td>
	</tr>		  
	<tr>
	    <th><label for="auth_num">Authorization #:</label></th>
	    <td align="left"><s:textfield name="ebtList.approve" value="%{ebtList.approve}" size="10" id="auth_num" /> </td>
	</tr>
	<tr>
	    <th><label for="ebt_amnt">EBT Amount ($):</label></th>
	    <td align="left"><s:textfield name="ebtList.amount" value="%{ebtList.amount}" size="4" maxlength="4" id="ebt_amnt" /> </td>
	</tr>
	<tr>
	    <th><label for="status">Status:</label></th>
	    <td align="left"><s:radio name="ebtList.cancelled" value="%{ebtList.cancelled}" list="#{'-1':'All','n':'Active','y':'Cancelled'}" id="status" /> </td>
	</tr>
	<tr>
	    <th><label for="dispute">Dispute Resolution?</label></th>
	    <td align="left"><s:radio name="ebtList.dispute_resolution" value="%{ebtList.dispute_resolution}" list="#{'-1':'All','n':'No','y':'Yes'}" id="dispute" /> </td>
	</tr>					
	<tr>
	    <th><b>Date:</b></th>
	    <td align="left"><label for="from"> From</label><s:textfield name="ebtList.date_from" value="%{ebtList.date_from}" size="10" maxlength="10" cssClass="date" id="from" /><label for="to"> To </label><s:textfield name="ebtList.date_to" value="%{ebtList.date_to}" size="10" maxlength="10" cssClass="date" id="to" /> (mm/dd/yyyy)</td>
	</tr>  
	<tr>
	    <th><label for="sort_by">Sort by:</label></th>
	    <td align="left">
		<s:select name="ebtList.sortBy"
			  value="%{ebtList.sortBy}"
			  list="#{'-1':'ID','e.date_time':'Date & Time'}" headerKey="-1" headerValue="ID" id="sort_by" /></td>
	</tr>  
	<tr>
	    <td>&nbsp;</td>	    
	  <td>
	      <s:submit name="action" type="button" value="Search" />
	  </td>
      </tr>
  </table>
</s:form>
<s:if test="action != '' && hasEbts()">
    <s:set var="ebts" value="ebts" />
  <s:set var="ebtsTitle" value="ebtsTitle" />
  <%@  include file="ebts.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>























































