<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="seniorAdd" method="post">    
  <h4>Issue FMNP Senior Bucks</h4>
  <s:hidden name="senior.id" value="%{senior.id}" />
	<s:hidden name="senior.ticketNum" value="%{senior.ticketNum}" />
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
  <p>*indicate a required field </p>
  <table border="0" width="90%">
      <caption> Issue FMNP</caption>
      <tr>
	  <th>
	      <b>Transaction ID: </b></th>
	  <td align="left"> <s:property value="%{senior.id}" />
	  </td>
      </tr>
      <tr>
	  <th><b>Amount:</b></th>
	  <td align="left">$<s:property value="%{senior.amount}" />.00</td>
      </tr>
      <tr>		  
	  <th><b>Ticket #:</b></th>
	  <td align="left"><s:property value="%{senior.ticketNum}" /></td>
      </tr>
      <tr>		  
	  <th><b>Date & Time:</b></th>
	  <td align="left"><s:property value="%{senior.date_time}" /></td>
      </tr>
      <tr>		      
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="%{senior.user}" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td align="left">$<s:property value="%{senior.total}" />.00</td>
      </tr>
      <s:if test="senior.isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left" align="left" colspan="3">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="senior.isDispute_resolution()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left">In Dispute</td>
	  </tr>
      </s:if>				
      <s:elseif test="senior.hasBalance()">
	  <tr>
	      <th><b>Balance:</b></th>
	      <td align="left">$<s:property value="%{senior.balance}" />.00</td>
	  </tr>
      </s:elseif>
      <s:if test="!senior.isCancelled() && !senior.isDispute_resolution()">
	  <s:if test="senior.hasBalance()">
	      <tr>
		  <th><label for="bar_code_id">* Scan/Enter new Market Buck:</label></th>
		  <td align="left">
		      <s:textfield name="senior.buck_id" value="" size="20" maxlength="20" required="true" id="bar_code_id" autofocus="true" /></td>
	      </tr>
	      <tr>
		  <td>&nbsp;</td>
		  <td>
		      <s:submit name="action" type="button" value="Add" />
		  </td>
	      </tr>	  
	  </s:if>
	  <s:else>
	      <tr><td  colspan="2">All Market Bucks are issued for this customer</td></tr>
	  </s:else>
	  <tr>
	      <td>&nbsp;</td>
	      <td><a href="<s:property value='#application.url' />seniorAdd.action?id=<s:property value='senior.id' />">Edit/Cancel Transaction <s:property value="id" /></a>.
	      </td>
	  </tr>		  
      </s:if>
  </table>
  <s:if test="senior.hasBucks()">
      <s:set var="bucks" value="senior.bucks" />
      <s:set var="bucksTitle" value="bucksTitle" />
      <s:set var="total" value="senior.bucksTotal" />
      <%@  include file="bucks.jsp" %>
  </s:if>
</s:form>


<%@  include file="footer.jsp" %>	






































