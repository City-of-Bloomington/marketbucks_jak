<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="rxAdd" method="post">    
  <h4>Issue MarketRx Bucks</h4>
  <s:hidden name="rx.id" value="%{rx.id}" />
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
  <p>* Indicate a required field </p>
  <table border="0" width="90%">
      <caption>Transaction ID:<s:property value="%{rx.id}" /></caption>
      <tr>
	  <th><b>Amount:</b></th>
	  <td>$<s:property value="%{rx.amount}" />.00</td>
      </tr>
      <tr>
	  <th><b>Date & Time:</b></td>
	  <td align="left"><s:property value="%{rx.date_time}" /></td>		  
      </tr>
      <tr>
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="%{rx.user}" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td>$<s:property value="%{rx.total}" />.00</td>
      </tr>
      <s:if test="rx.isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td>Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="rx.isDispute_resolution()">
	  <tr>
	      <th><b>Type:</b></th>
	      <td>Dispute resolution Rx</td>
	  </tr>
      </s:if>				
      <s:elseif test="rx.hasBalance()">
	  <tr bgcolor="red">
	      <th><b>Balance:</b></th>
	      <td>$<s:property value="%{rx.balance}" />.00</td>
	  </tr>
      </s:elseif>
      <s:if test="!rx.isCancelled() && !rx.isDispute_resolution()">
	  <s:if test="rx.hasBalance()">
	      <tr>
		  <th><label for="bar_code_id">*Scan/Enter new Market Buck:</label></th>
		  <td><s:textfield name="rx.buck_id" value="" size="20" maxlength="20" required="true" id="bar_code_id"  autofocus="true" /></td>
	      </tr>
	      <tr>
		  <td>&nbsp;</td>
		  <td>
		      <s:submit name="action" type="button" value="Add" />
		  </td>
	      </tr>	  
	  </s:if>
	  <s:else>
	      <tr>
		  <td>&nbsp;</td>
		  <td> All Market Bucks are issued for this customer</td>
	      </tr>
	  </s:else>
	  <tr>
	      <th><a href="<s:property value='#application.url' />rxAdd.action?id=<s:property value='rx.id' />">Edit/Cancel This Transaction </a>
	      </td>
	  </tr>		  
      </s:if>
      <s:if test="rx.hasBucks()">
	  <tr><td align="center">	  
	      <s:set var="bucks" value="rx.bucks" />
	      <s:set var="bucksTitle" value="bucksTitle" />
	      <s:set var="total" value="rx.bucksTotal" />
	      <%@  include file="bucks.jsp" %>
	  </td></tr>
      </s:if>
  </table>
</s:form>


<%@  include file="footer.jsp" %>	






































