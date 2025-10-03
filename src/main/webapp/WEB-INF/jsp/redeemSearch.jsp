<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Search Redemptions</h3>
<s:form action="redeemSearch" method="post">
    <table width="90%">
	<caption>Search Options</caption>
	<tr><th><label for="rem_id">Redemption ID:</label></th>
	    <td align="left"><s:textfield name="redeemList.id" value="%{redeemList.id}" size="8" id="rem_id" /></td>
	</tr>
	<tr><th><label for="buck_id">MB/GC ID:</label></th>
	    <td align="left"><s:textfield name="redeemList.buck_id" value="%{redeemList.buck_id}" size="8" id="buck_id" /></td>
	</tr>
	<tr>
	    <th><label for="ven_id">Vendor:</label></th>
	    <td align="left">
		<s:select name="redeemList.vendor_id" list="vendors" listKey="vendorNum" listValue="fullName" headerKey="-1" headerValue="Pick Vendor" id="ven_id">
		</s:select>
	    </td>
	</tr>		
	<tr>
	    <th><label for="vendor_num">Vendor Number:</label></th>
	    <td align="left"><s:textfield name="redeemList.vendor_num" value="%{redeemList.vendor_num}" size="8" id="vendor_num" /></td>
	</tr>  
	<tr>
	    <th><label for="vendorName">Vendor Name:</label></th>
	    <td align="left"><s:textfield name="vendorName" value="" size="30" maxlength="30" id="vendorName" /> </td>
	</tr>
	<tr>
	    <th><label for="status">Status:</label></th>
	    <td align="left"><s:radio name="redeemList.status" value="%{redeemList.status}" list="#{'-1':'All','Open':'Open','Completed':'Finalized'}" headerKey="-1" headerValue="All" id="status" /> </td>
	</tr>		  
	<tr>
	    <th><label for="type">Payment Type:</label></th>
	    <td align="left"><s:radio name="redeemList.payType" value="%{redeemList.payType}" list="#{'-1':'All','MB:GC':'MB & GC','GC':'GC Only'}" headerKey="-1" headerValue="All" id="type" /> </td>
	</tr>
	<tr>
	    <th><label for="exp">Redemption Export:</label></th>
	    <td align="left"><s:radio name="redeemList.export" value="%{redeemList.export}" list="#{'-1':'All','exported':'Exported Only','notExported':'Not Exported Yet'}" headerKey="-1" headerValue="All" id="exp" /> </td>
	</tr>		 
	<tr>
	    <th><b>Date:</b></th>
	    <td align="left"><label for="from"> From</label><s:textfield name="redeemList.date_from" value="%{redeemList.date_from}" size="10" maxlength="10" cssClass="date" id="from" /><label for="to"> To </label><s:textfield name="redeemList.date_to" value="%{redeemList.date_to}" size="10" maxlength="10" cssClass="date" id="to" /></td>
	</tr>  
	<tr>
	    <th><label for="sortBy">Sort by:</label></th>
	    <td align="left">
		<s:select name="redeemList.sortBy"
			  value="%{redeemList.sortBy}"
			  list="#{'-1':'ID','v.lname':'Vendor Name','r.date_time':'Date'}" headerKey="" headerValue="ID" id="sortBy" /></td>
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
  <s:set var="redeems" value="redeems" />
  <s:set var="redeemsTitle" value="redeemsTitle" />
  <%@  include file="redeems.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>























































