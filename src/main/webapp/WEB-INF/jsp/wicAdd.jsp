<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="wicAdd" method="post" onsubmit="return confirmForCancel()">
  <s:if test="!hasId()">
      <h3>Add New FMNP WIC Transaction</h3>
  </s:if>
  <s:else>
      <h3>Edit FMNP WIC Transaction</h3>
      <s:hidden name="wic.id" value="%{wic.id}" />
      <s:hidden name="wic.wic_max_amount" value="%{wic_max_amount}" />		
      <s:if test="!wic.isCancelled()">
	  <ul>
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
  <p>Note: FMNP WIC Amount is predetermined amount, no change is needed </p>
  <hr />
  <table border="0" width="90%">
      <caption> FMNP WIC</caption>
      <s:if test="hasId()">
	  <tr>
	      <th>
		  <b>Transaction ID:</b>
	      </th>
	      <td align="left">
		  <s:property value="%{id}" />
	      </td>
	  </tr>
      </s:if>
      <tr>
	  <th><label for="div5">Amount:</label></th>
	  <td align="left">$<s:textfield name="wic.amount" maxlength="4" size="4" value="%{amount}" id="div3" cssClass="need_focus" readonly="true" onchange="checkDivBy3(this)" />.00 (Must be multiple of $3)</td>
      </tr>
      <s:if test="!hasId()">
	  <tr>
	      <th><label for="tnum">Ticket #:</label></th>
	      <td align="left"><s:textfield name="wic.ticketNum" maxlength="10" size="10" value="%{ticketNum}" required="true" id="tnum" /> *</td>
	  </tr>
      </s:if>
      <s:else>
	  <tr>
	      <th><b>Ticket #:</b></th>
	      <td align="left"><s:property value="%{ticketNum}" /></td>
	  </tr>
	  <tr>
	      <th><b>Date & Time:</b></th>
	      <td align="left"><s:property value="%{date_time}" /></td>
	  </tr>
	  <tr>
	      <th><b>User:</b></th>
	      <td align="left"><s:property value="%{wic_user}" /></td>
	  </tr>
	  <tr>
	      <th><b>Total:</b></th>
	      <td align="left">$<s:property value="%{total}" />.00</td>
	  </tr>
	  <s:if test="isCancelled()">
	      <tr>		      
		  <th><b>Status:</b></th>
		  <td align="left">Cancelled</td>
	      </tr>
	  </s:if>
	  <s:if test="isDispute_resolution()">
	      <tr>
		  <th><b>Status:</b></th>
		  <td align="left">Dispute Resolution</td>
	      </tr>
	  </s:if>
      </s:else>
      <tr>
	  <s:if test="!hasId()">
	      <td>&nbsp;</td>	      
	      <td>
		  <s:submit name="action" type="button" id="next_button" value="Next" />
	      </td>

	  </s:if>
	  <s:elseif test="!isCancelled() && !isDispute_resolution()">
	      <s:if test="hasBalance()">
		  <th>					
		      <s:submit name="action" type="button" id="next_button" value="Add Bucks" />
		  </th>
		  <td>				
		      <s:submit name="action" type="button" id="cancel_button" value="Cancel" />
		  </td>		  
	      </s:if>
	      <s:else>
		  <td>&nbsp;</td>		  		  
		  <td>				
		      <s:submit name="action" type="button" id="cancel_button" value="Cancel" />
		  </td>

	      </s:else>
	  </s:elseif>
      </tr>
  </table>
  <br />
  <s:if test="wic.hasBucks()">
      <table border="1" width="80%">
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
	      <th>$<s:property value="wic.bucksTotal" />.00</td>
	  </tr>
	  <s:iterator var="one" value="wic.bucks">
	      <tr>
		  <td>&nbsp;
		      <s:if test="!isVoided()">
			  <input type="checkbox" name="wic.cancel_buck_id" value="<s:property value='id' />" />
		      </s:if>
		  </td>
		  <td><s:property value="id" /></td>
		  <td><s:property value="expire_date" /></td>
		  <td><s:if test="isVoided()">Voided</s:if></td>	
		  <td>$<s:property value="value" />.00</td>
	      </tr>
	  </s:iterator>
	  <tr><td colspan="5">** check to cancel and void the corresponding Fmnp Wic Bucks</td></tr>
      </table>
      <dl>
	  <dd>
	      <s:submit name="action" type="button" id="cancel_button_2" value="Cancel Selected" />
	  </dd>
      </dl>
  </s:if>	
</s:form>
For FMNP WIC/Senior search click <a href="<s:property value='#application.url'/>fmnpSearch.action"> here. </a>
<br />
<s:if test="!hasId()">
    <s:if test="hasFmnpWics()">
	<s:set var="fmnpWics" value="fmnpWics" />
	<s:set var="fmnpWicsTitle" value="fmnpWicsTitle" />
	<s:set var="showTotal" value="'false'" />
	<%@  include file="fmnpWics.jsp" %>	
    </s:if>
</s:if>
<%@  include file="footer.jsp" %>	






































