<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="rxAdd" method="post" onsubmit="return confirmForCancel()">
  <s:if test="id == ''">
      <h3>Add New Rx Transaction</h3>
  </s:if>
  <s:else>
      <h3>Edit Rx Transaction</h3>
      <s:hidden name="rx.id" value="%{id}" />
      <s:hidden name="rx.rx_max_amount" value="%{rx_max_amount}" />		
      <s:if test="!isCancelled()">
	  <ul>
	      <li>If the amount is changed, this may cause the removal of all of the bucks that have been added to this transaction.</li>
	      <li>if you want to "Cancel" this transaction, first you need to collect all the issued MB's from the customer. These MB's will be retruned to the pool. Then click on Cancel.</li>
	      <li>If you want to Cancel certain MB's only. Check the corresponding checkbox(es) and then click on "Cancel Selected MB's".</li>
	  </ul>
      </s:if>	
  </s:else>
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
      <caption>Rx Bucks</caption>
      <tr><td colspan="2">Note: Rx Amount is predetermined amount, no change is needed </td></tr>
      <tr>
	  <th width="35%"><label for="div5">Rx Amount:</label></th>
	  <td align="left">$<s:textfield name="rx.amount" maxlength="4" size="4" required="true" value="%{amount}" id="div5" cssClass="need_focus" />.00 (Must be multiple of $3)</td>
      </tr>
      <tr>
	  <th><label for="vouch">Voucher #:</label></th>
	  <td align="left"><s:textfield name="rx.voucherNum" maxlength="10" size="10" value="%{voucherNum}" required="true" id="vouch" /> *</td>
      </tr>
      <s:if test="isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="isDispute_resolution()">
	  <tr>
	      <th><b>Type:</b></th>
	      <td align="left">Dispute Resolution</td>
	  </tr>
      </s:if>						
      <tr>
	  <s:if test="id == ''">
	      <td>&nbsp;</td>	      
	      <td>
		  <s:submit name="action" type="button" id="next_button" value="Next" />
	      </td>
	  </s:if>
	  <s:elseif test="!isCancelled() && !isDispute_resolution()">
	      <th>
		  <s:submit name="action" type="button" id="update_button" value="Update" />
	      </th>
	      <td>	      
	      <s:if test="hasBalance()">
		  <s:submit name="action" type="button" id="next_button" value="Add Bucks" />
	      </s:if>
	       <s:submit name="action" type="button" id="cancel_button" value="Cancel" />
	      </td>
	  </s:elseif>
      </tr>
  </table>
  <s:if test="hasBucks()">
      <table border="0" width="90%">
	  <caption><s:property value="bucksTitle" /></caption>
	  <tr>
	      <td align="center"><b>**</b></td>
	      <td align="center"><b>ID</b></td>
	      <td align="center"><b>Expire Date</b></td>
	      <td align="center"><b>Voided?</b></td>	
	      <td align="center"><b>Face Value</b></td>
	  </tr>
	  <tr>
	      <td colspan="4" align="right">Total</td>
	      <th>$<s:property value="bucksTotal" />.00</td>
	  </tr>
	  <s:iterator var="one" value="bucks">
	      <tr>
		  <td>&nbsp;
		      <s:if test="!isVoided()">
			  <input type="checkbox" name="cancel_buck_id" value="<s:property value='id' />" />
		      </s:if>
		  </td>
		  <td><s:property value="id" /></td>
		  <td><s:property value="expire_date" /></td>
		  <td><s:if test="isVoided()">Voided</s:if></td>	
		  <td align="right">$<s:property value="value" />.00</td>
	      </tr>
	  </s:iterator>
	  <tr>
	      <td colspan="5">** check to cancel and void the corresponding Rx Buck</td>
	  </tr>
	  <tr>
	      <td align="center" colspan="5">
		  <s:submit name="action" type="button" id="cancel_button_2" value="Cancel Selected" />
	      </td>
	  </tr>	  
      </table>
  </s:if>
</s:form>

<a href="<s:property value='#application.url'/>rxSearch.action">
Rx advance search</a>
<br />
<br />
<s:if test="id == ''">
    <s:if test="hasMarketRxes()">
	<s:set var="rxes" value="marketRxes" />
	<s:set var="rxesTitle" value="marketRxesTitle" />
	<s:set var="showTotal" value="'false'" />
	<%@  include file="rxes.jsp" %>	
    </s:if>
</s:if>
<%@  include file="footer.jsp" %>	






































