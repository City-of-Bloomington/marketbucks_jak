<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="issueGiftAdd" method="post">    
    <h3>Issue Gift Certificates</h3>
    <hr />
  <s:hidden name="gift.id" value="%{gift.id}" />
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
  <p>*indicates required field</p>
  <table width="90%">
      <caption>GC Payment ID:<s:property value="gift.id" /></caption>
      <tr>
	  <th><b>Payment Type:</b></th>
	  <td align="left"><s:property value="gift.pay_type" /></td>
      </tr>
      <tr>
	  <th><b>Check #:</b></th>
	  <td align="left"><s:property value="gift.check_no" /></td>
      </tr>
      <tr>
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="gift.user" /></td>
      </tr>
      <tr>
	  <th><b>Requested:</b></th>
	  <td align="left">$<s:property value="gift.amount" />.00</td>
      </tr>
      <tr>
	  <th><b>Date & Time:</b></th>
	  <td align="left"><s:property value="gift.date_time" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td align="left">$<s:property value="gift.bucksTotal" />.00</td>
      </tr>
      <s:if test="gift.isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>				
      <s:if test="gift.hasBalance()">
	  <tr bgcolor="red">
	      <th><b>Balance:</b></th>
	      <td align="left">$<s:property value="gift.balance" />.00</td>
	  </tr>
      </s:if>
      <s:if test="!gift.isCancelled()">
	  <s:if test="gift.needMoreIssue()">
	      <tr>
		  <th><label for="bar_code_id">* Scan/Enter new gift certificate:</label>
		  </th>
		  <td align="left">
		      <s:textfield name="gift.buck_id" value="" size="20" maxlength="20" required="true" id="bar_code_id" autofocus="true" /></td>
	      </tr>
	  </s:if>
	  <s:else>
	      <tr>
		  <th>All GC are issued for this customer</th>
		  <td>&nbsp;</td>
	      </tr>
	  </s:else>
	  <s:if test="gift.needMoreIssue()">
	      <tr>
		  <td>&nbsp;</td>			  
		  <td>
		      <s:submit name="action" type="button" value="Add" />
		  </td>
	      </tr>
	  </s:if>
	  <tr>
	      <th>&nbsp;</th>
	      <td>
		 <a href="<s:property value='#application.url' />giftAdd.action?id=<s:property value='gift.id' />">Edit This Transaction </a>.
	      </td>
	  </tr>		  
      </s:if>
  </table>      
  <s:if test="gift.bucks != null && gift.bucks.size() > 0">
      <s:set var="bucks" value="gift.bucks" />
      <s:set var="bucksTitle" value="bucksTitle" />
      <s:set var="total" value="gift.bucksTotal" />
      <%@  include file="giftBucks.jsp" %>
  </s:if>
  
</s:form>

<%@  include file="footer.jsp" %>	






































