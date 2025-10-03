<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="rxView" method="post" id="form_id" >
  <s:hidden name="rx.id" value="%{rx.id}" />
  <h3>View MarketRx </h3>
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
  <table border="0" width="90%"><caption>MB Rx</caption>
      <tr>
	  <th width="30%"><b>Voucher #:</b></th>
	  <td align="left"><s:property value="rx.voucherNum" /></td>
      </tr>
      <tr>
	  <th><b>Amount:</b></th>
	  <td align="left">$<s:property value="rx.amount" /></td>
      </tr>
      <tr>
	  <th><b>Issued Amount:</b></th>
	  <td align="left">$<s:property value="rx.bucksTotal" /></td>
      </tr>				
      <tr>
	  <th align="right"><b>Date & Time:</b></th>
	  <td align="left">$<s:property value="rx.date_time" /></td>
      </tr>								
      <tr>
	  <th align="right"><b>Issued by:</b></th>
	  <td align="left">$<s:property value="rx.user" /></td>
      </tr>
      
      <s:if test="rx.isCancelled()">
	  <tr><th align="right">Status:</th><td align="left">Cancelled</td></tr>
      </s:if>
      <s:if test="rx.isDispute_resolution()">
	  <tr><th align="right">Type:</th><td align="left">Dispute Resolution</td></tr>
      </s:if>				
  </table>
</s:form>
<s:if test="rx.hasBucks()">
  <s:set var="bucks" value="rx.bucks" />
  <%@  include file="bucks.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































