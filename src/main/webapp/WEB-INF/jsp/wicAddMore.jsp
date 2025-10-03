<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="wicAdd" method="post">    
  <h4>Issue FMNP WIC Bucks</h4>
  <s:hidden name="wic.id" value="%{wic.id}" />
  <s:hidden name="wic.ticketNum" value="%{wic.ticketNum}" />
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
      <caption>Issue FMNP WIC </caption>
      <tr>
	  <th>Transaction ID: </td><td align="left"> <s:property value="%{wic.id}" /></td>
      </tr>
      <tr>
	  <th><b>Amount:</b></td>
	  <td align="left">$<s:property value="%{wic.amount}" />.00</td>
      </tr>
      <tr>
	  <th><b>Ticket #:</b></td>
	  <td align="left"><s:property value="%{wic.ticketNum}" /></td>		  
      </tr>
      <tr>
	  <th><b>Date & Time:</b></td>
	  <td align="left"><s:property value="%{wic.date_time}" /></td>		  
      </tr>
      <tr>
	  <th><b>User:</b></td>
	  <td align="left"><s:property value="%{wic.user}" /></td>
	  
      </tr>
      <tr>
	  <th><b>Total:</b></td>
	  <td align="left">$<s:property value="%{wic.total}" />.00</td>
      </tr>
      
      <s:if test="wic.isCancelled()">
	  <tr>			  
	      <th><b>Status:</b></td>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="wic.isDispute_resolution()">
	  <tr>
	      <th><b>Status:</b></td>
	      <td align="left">Dispute resolution</td>
	  </tr>
      </s:if>				
      <s:elseif test="wic.hasBalance()">
	  <tr>
	      <th bgcolor="red">
		  <b>Balance:</b></th> 
	      <td>$<s:property value="%{wic.balance}" />.00</td>
	  </tr>
      </s:elseif>
      <s:if test="!wic.isCancelled() && !wic.isDispute_resolution()">
	  <s:if test="wic.hasBalance()">
	      <tr>
		  <th><label for="bar_code_id"> * Scan/Enter new Market Buck:</label></th>
		  <td>
		      <s:textfield name="wic.buck_id" value="" size="20" maxlength="20" required="true" id="bar_code_id" autofocus="true" />
		  </td>
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
		  <td>All Market Bucks are issued for this customer</td>
	      </tr>
	  </s:else>
	  <tr>
	      <th>&nbsp;</th>
	      <td>
		  <a href="<s:property value='#application.url' />wicAdd.action?id=<s:property value='wic.id' />">To Edit/Cancel this transaction</a>.
	      </td>
	  </tr>		  
      </s:if>
  </table>
  
  <s:if test="wic.hasBucks()">
      <s:set var="bucks" value="wic.bucks" />
      <s:set var="bucksTitle" value="bucksTitle" />
      <s:set var="total" value="wic.bucksTotal" />
      <%@  include file="bucks.jsp" %>
  </s:if>
</s:form>

<%@  include file="footer.jsp" %>	






































