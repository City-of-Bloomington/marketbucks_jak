<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Search MarketRx Bucks</h3>
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
<s:form action="rxSearch" method="post">
    <table width="90%">
	<caption>Search Options</caption>
	<tr><th><label for="rx_id">MarketRx ID:</label></th>
	    <td align="left"><s:textfield name="rxList.id" value="%{rxList.id}" size="8" id="rx_id" /></td>
	</tr>
	<tr><th><label for="mb_id">MB ID:</label></th>
	    <td align="left"><s:textfield name="rxList.buck_id" value="%{rxList.buck_id}" size="8" id="mb_id" /></td>
	</tr>
	<tr>
	    <th><label for="vouch">Voucher #:</label></th>
	    <td align="left"><s:textfield name="rxList.voucherNum" value="%{rxList.voucherNum}" size="10" maxlength="10" id="vouch" /> </td>
	</tr>		  
	<tr>
	    <th><label for="amount">Rx Amount ($):</label></th>
	    <td align="left"><s:textfield name="rxList.amount" value="%{rxList.amount}" size="4" maxlength="4" id="amount" /> </td>
	</tr>
	<tr>
	    <th><label for="status">Status:</label></th>
	    <td align="left"><s:radio name="rxList.cancelled" value="%{rxList.cancelled}" list="#{'-1':'All','n':'Active','y':'Cancelled'}" id="status" /> </td>
	</tr>
	<tr>
	    <th><label for="disp">Dispute Resolution?</label></th>
	    <td align="left"><s:radio name="rxList.dispute_resolution" value="%{rxList.dispute_resolution}" list="#{'-1':'All','n':'No','y':'Yes'}" id="disp" /> </td>
	</tr>					
	<tr>
	    <th><b>Date:</b></th>
	    <td align="left"><label for="from"> From</label><s:textfield name="rxList.date_from" value="%{rxList.date_from}" size="10" maxlength="10" cssClass="date" id="from" /><label for="to"> To </label><s:textfield name="rxList.date_to" value="%{rxList.date_to}" size="10" maxlength="10" cssClass="date" id="to" /></td>
	</tr>  
	<tr>
	    <th><label for="sortby">Sort by:</label></th>
	    <td align="left">
		<s:select name="rxList.sortBy" id="sortby"
				value="%{rxList.sortBy}"
			  list="#{'-1':'ID','r.date_time':'Date & Time','r.voucher_num':'Voucher Number'}" headerKey="-1" headerValue="ID" /></td>
	</tr>  
	<tr>
	    <td>&nbsp;</td>
	    <td>
	      <s:submit name="action" type="button" value="Search" />
	    </td>
	</tr>
  </table>
</s:form>
<s:if test="action != ''">
    <s:if test="hasRxes()">
	<s:set var="rxes" value="rxes" />
	<s:set var="rxesTitle" value="rxesTitle" />
	<s:set var="rxTotal" value="rxTotal" />
	<s:set var="showTotal" value="'true'" />
	<%@  include file="rxes.jsp" %>	  
    </s:if>
</s:if>	
<%@  include file="footer.jsp" %>























































