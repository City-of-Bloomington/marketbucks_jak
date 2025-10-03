<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<s:form action="ebtAdd" method="post" id="form_id" onsubmit="return confirmForCancel()">
    <s:if test="hasId()">
	<s:hidden name="id" value="%{id}" />
	<s:hidden name="ebt.id" value="%{id}" />
	<s:hidden name="ebt.ebt_donor_max" value="%{ebt_donor_max}" />
	<s:hidden name="ebt.ebt_buck_value" value="%{ebt_buck_value}" />
      <s:if test="!ebt.isCancelled()">
	  <ul>
	      <li>If the amount is changed, this may cause the removal of all of the bucks that have been added to this transaction.</li>
	      <li>if you want to "Cancel" this transaction, first you need to collect all the issued MB's from the customer. Mark them as voided. These MB's can not be issued or redeemed any more. Then click on Cancel.</li>
	  </ul>
      </s:if>
  </s:if>
  <s:if test="hasActionErrors()">
      <s:actionerror/>
  </s:if>
  <s:elseif test="hasActionMessages()">
      <s:actionmessage/>
  </s:elseif>
  <p>*indicates a required field</p>
  <hr />
  <table width="90%"><caption>Issue/Edit Ebt</caption>
      <s:if test="hasId()">
	  <tr>
	      <th><b>Transaction ID:</b></th>
	      <td><s:property value="id" /></td>
	  </tr>
      </s:if>
      <tr>
	  <th><label for="div3">* EBT Amount:</label></th>
	  <td align="left">$ <s:textfield name="ebt.amount" maxlength="4" size="4" required="true" value="%{amount}" id="div3" onchange="checkDivBy3(this)" cssClass="need_focus" />.00 (Must be multiple of 3 ($))</td>
      </tr>
      <tr>
	  <th><label for="auth_num">* Authorization #:</label></th>
	  
	  <td align="left"><s:textfield name="ebt.approve" maxlength="20" size="20" required="true" id="auth_num" value="%{approve}" /></td>
      </tr>
      <tr>
	  <th><label for="cust_num">* Customer Card #:</label></th>
	  <td align="left"><s:textfield name="ebt.card_last_4" maxlength="4" size="4" required="true" id="cust_num" value="%{card_last_4}" /></td>
      </tr>
      <tr>
	  <th><label for="inc_dbl">Include Double? </label></th>
	  <td align="left">
	      <s:radio name="ebt.includeDouble"
		       value="%{includeDouble}"
		       list="#{'y':'Yes','n':'No'}" listKey="key"
		       listValue="value" />
	  </td>
      </tr>
      <s:if test="ebt.isCancelled()">
	  <tr>
	      <th><b>Status: </b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:elseif test="ebt.isDispute_resolution()">
	  <tr>
	      <th><b>Status: </b></th>
	      <td align="left">In Dispute</td>
	  </tr>
      </s:elseif>
      <tr>
	  <s:if test="!hasId()">
	      <td>&nbsp;</td>	      
	      <td>
		  <s:submit name="action" type="button" value="Next" id="next_button" />
	      </td>
	  </s:if>
	  <s:elseif test="!isCancelled() && !isDispute_resolution()">
	      <th align="right">
		  <s:submit name="action" type="button" id="update_button" value="Update" />
	      </th>
	      <td align="left">
		  <s:submit name="action" type="button"  id="cancel_button" value="Cancel" />
	      </td>
	  </s:elseif>
      </tr>
  </table>
  <hr />
</s:form>
<br />
For issued market bucks search click<a href="<s:property value='#application.url'/>ebtSearch.action"> here. </a>
<br />
For any (issued /unissued) MB/GC search click<a href="<s:property value='#application.url'/>buckSearch.action"> here. </a> <br />

<s:if test="ebt.id == ''">
    <s:if test="hasEbts()">
	<s:set var="ebts" value="ebts" />
	<s:set var="ebtsTitle" value="ebtsTitle" />  
	<%@  include file="ebts.jsp" %>	
    </s:if>
</s:if>
<%@  include file="footer.jsp" %>	






































