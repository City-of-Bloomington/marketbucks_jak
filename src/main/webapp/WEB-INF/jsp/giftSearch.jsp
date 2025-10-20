<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Search Issued Gift Certificates</h3>
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
<s:form action="giftSearch" method="post">
    <table width="90%">
	<caption>Search Options</caption>
	<tr><th><label for="trans_id">Transaction ID:</label></th>
	  <td align="left"><s:textfield name="giftList.id" value="%{id}" size="8" id="trans_id" /></td>
      </tr>
      <tr><th><label for="gc_id">GC ID:</label></th>
	  <td align="left"><s:textfield name="giftList.buck_id" value="%{buck_id}" size="8" id="gc_id" /></td>
      </tr>
      <tr>
	  <th><label for="type">Payment Type:</label></th>
	  <td align="left"><s:radio name="giftList.pay_type" value="%{pay_type}" list="{'All','Cash','Check','Money Order','Credit Card','Honorary'}" id="type" /></td>
      </tr>	  		  
      <tr>
	  <th><label for="check_no">Check #/RecTrac #:</label></th>
	  <td align="left"><s:textfield name="giftList.check_no" value="%{check_no}" size="10" id="check_no" /> </td>
      </tr>
      <tr>
	  <th><label for="amount">Amount ($):</label></th>
	  <td align="left"><s:textfield name="giftList.amount" value="%{amount}" size="4" maxlength="4" id="amount" /> </td>
      </tr>
      <tr>
	  <th><label for="status">Status:</label></th>
	  <td align="left"><s:radio name="giftList.cancelled" value="%{cancelled}" list="#{'-1':'All','n':'Active','y':'Cancelled'}" id="status" /> </td>
      </tr>
      <tr>
	  <th><label for="dispute">Dispute Resolution?</label></th>
	  <td align="left"><s:radio name="giftList.dispute_resolution" value="%{dispute_resolution}" list="#{'-1':'All','y':'Yes','n':'No'}" id="dispute" /> </td>
      </tr>							
      <tr>
	  <th><b>Date:</b></th>
	  <td align="left"><label for="from"> From</label><s:textfield name="giftList.date_from" value="%{giftList.date_from}" size="10" maxlength="10" cssClass="date" id="from" /><label for="to"> To </label><s:textfield name="giftList.date_to" value="%{giftList.date_to}" size="10" maxlength="10" cssClass="date" id="to" />(mm/dd/yyyy)</td>
      </tr>  
      <tr>
	  <th><label for="sortby">Sort by:</label></th>
	  <td align="left">
	      <s:select name="giftList.sortBy" id="sortby"
			value="%{sortBy}"
			      list="#{'-1':'ID','g.date_time':'Date & Time'}" headerKey="-1" headerValue="ID" /></td>
      </tr>  
      <tr>
	  <td>&nbsp;</td>
	  <td>
	      <s:submit name="action" type="button" value="Submit" />
	  </td>
      </tr>
  </table>
</s:form>
<s:if test="action != ''">
  <s:set var="gifts" value="gifts" />
  <s:set var="giftsTitle" value="giftsTitle" />
  <%@  include file="gifts.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>























































