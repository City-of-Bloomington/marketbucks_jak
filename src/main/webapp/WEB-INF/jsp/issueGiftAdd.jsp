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
  <s:hidden name="gift.id" value="%{id}" />
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
      <caption>GC Payment ID:<s:property value="id" /></caption>
      <tr>
	  <th><b>Payment Type:</b></th>
	  <td align="left"><s:property value="pay_type" /></td>
      </tr>
      <s:if test="hasCheck_no()">
	  <tr>
	      <th><b>Check #:</b></th>
	      <td align="left"><s:property value="check_no" /></td>
	  </tr>
      </s:if>
      <tr>
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="gift_user" /></td>
      </tr>
      <tr>
	  <th><b>Requested:</b></th>
	  <td align="left">$<s:property value="amount" />.00</td>
      </tr>
      <tr>
	  <th><b>Date & Time:</b></th>
	  <td align="left"><s:property value="date_time" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td align="left">$<s:property value="bucksTotal" />.00</td>
      </tr>
      <s:if test="isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>				
      <s:if test="hasBalance()">
	  <tr bgcolor="red">
	      <th><b>Balance:</b></th>
	      <td align="left">$<s:property value="balance" />.00</td>
	  </tr>
      </s:if>
      <s:if test="!isCancelled()">
	  <s:if test="needMoreIssue()">
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
	  <s:if test="needMoreIssue()">
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
		 <a href="<s:property value='#application.url' />giftAdd.action?id=<s:property value='id' />&action=Cancel">Cancel This Transaction </a>.
	      </td>
	  </tr>		  
      </s:if>
  </table>      
  <s:if test="hasBucks()">
      <s:set var="bucks" value="bucks" />
      <s:set var="bucksTitle" value="bucksTitle" />
      <s:set var="total" value="bucksTotal" />
      <%@  include file="giftBucks.jsp" %>
  </s:if>
  
</s:form>

<%@  include file="footer.jsp" %>	






































