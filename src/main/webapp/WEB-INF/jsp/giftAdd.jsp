<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="giftAdd" method="post" onsubmit="return confirmForCancel()">
  <s:if test="gift.id == ''">
      <h3>Add New Gift Transaction</h3>
  </s:if>
  <s:else>
      <h3>Edit Gift Transaction</h3>
      <s:hidden name="gift.id" value="%{gift.id}" />
      <s:if test="!gift.isCancelled()">
	  <ul>
	      <li>If the amount is changed, this may cause the removal of all of the GC's that have been added to this transaction.</li>
	      <li>if you want to "Cancel" this transaction, first you need to collect all the issued GC's from the customer. Mark them as voided. These GC's can not be issued or redeemed any more. Then click on Cancel.</li>
	      <li>If you want to Cancel/Void certain GC's only. Check the corresponding checkbox(es) and then click on "Void Selected GC's".</li>
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
  <table width="90%"><caption> Gift Transaction</caption>
      <tr>
	  <th width="35%"><label for="div5">* Requested Amount:</label></th>
	  <td align="left">$<s:textfield name="gift.amount" maxlength="4" size="4" required="true" value="%{gift.amount}" id="div5" cssClass="need_focus" onchange="checkDivBy5(this)" />.00 (Must be divisible by $5)</td>
      </tr>
      <tr>
	  <th><label for="type">* Payment Type:</label></th>
	  <td align="left"><s:radio name="gift.pay_type" required="true" value="%{gift.pay_type}" list="{'Cash','Check','Money Order','Credit Card','Honorary'}" id="type" /></td>
      </tr>	  
      <tr>
	  <th><label for="check_no">Check #/RecTrac:</label></th>
	  <td align="left"><s:textfield name="gift.check_no" maxlength="20" size="20" value="%{gift.check_no}" id="check_no" /></td>
      </tr>
      <s:if test="gift.isCancelled()">
	  <tr>
	      <th><b>Status:</b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="ebt.isDispute_resolution()">
	  <tr>
	      <th>Type:</th>
	      <td align="left">Dispute Resolution</td>
	  </tr>
      </s:if>						
      <tr>
	  <s:if test="gift.id == ''">
	      <td>&nbsp;</td>	   	      

	      <td valign="top">
		  <s:submit name="action" type="button" id="next_button" value="Next" />
	      </td>
	  </s:if>
	  <s:elseif test="!gift.isCancelled() && !gift.isDispute_resolution()">
	      <th>
		  <s:submit name="action" type="button" id="update_button" value="Update" />
	      </th>
	      <td align="left">
		  <s:submit name="action" type="button" id="cancel_button" value="Cancel" />
	      </td>
	  </s:elseif>
      </tr>
  </table>
  <s:if test="gift.bucks != null && gift.bucks.size() > 0">
     <table border="1" width="80%"><caption><s:property value="bucksTitle" /></caption>
	  <tr>
	      <td align="center"><b>**</b></td>
	      <td align="center"><b>ID</b></td>
	      <td align="center"><b>Expire Date</b></td>
	      <td align="center"><b>Voided?</b></td>	
	      <td align="center"><b>Face Value</b></td>
	  </tr>
	  <tr>
	      <td colspan="4" align="right">Total</td>
	      <td align="right">$<s:property value="gift.bucksTotal" />.00</td>
	  </tr>
	  <s:iterator var="one" value="gift.bucks">
	      <tr>
		  <td>&nbsp;
		      <s:if test="!isVoided()">
			  <input type="checkbox" name="gift.cancel_buck_id" value="<s:property value='id' />" />
		      </s:if>
		  </td>
		  <td><s:property value="id" /></td>
		  <td><s:property value="expire_date" /></td>
		  <td><s:if test="isVoided()">Voided</s:if></td>	
		  <td align="right">$<s:property value="value" />.00</td>
	      </tr>
	  </s:iterator>
	  <tr><td colspan="5">** check to cancel and void the corresponding GC</td></tr>
	  <tr>
	      <td align="center" colspan="5">
		  <s:submit name="action" type="button" id="cancel_button_2" value="Void Selected GC's" />
	      </td>
	  </tr>	  
     </table>
  </s:if>	
</s:form>


<a href="<s:property value='#application.url'/>giftSearch.action"> Gift certificates advance search</a>

<s:if test="hasGifts()">
  <s:set var="gifts" value="gifts" />
  <s:set var="giftsTitle" value="giftsTitle" />  
  <%@  include file="gifts.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































