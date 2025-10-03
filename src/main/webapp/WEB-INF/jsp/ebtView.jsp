<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<s:form action="ebtView" method="post" id="form_id" >
  <s:hidden name="ebt.id" value="%{ebt.id}" />
  <h3>View Ebt </h3>
  <s:if test="hasActionErrors()">
	  <s:actionerror/>
  </s:if>
  <s:elseif test="hasActionMessages()">
	  <s:actionmessage/>
  </s:elseif>
  <table border="1" width="80%">
      <caption>Ebt Details</caption>
      <tr><td> 
	  <table width="90%" border="0">
	      <tr>
		  <th width="30%"><label>EBT Amount:</label></th>
		  <td align="left"><s:property value="ebt.amount" /></td>
	      </tr>
	      <tr>
		  <th width="30%"><label>DMB Amount:</label></th>
		  <td align="left"><s:property value="ebt.dmb_amount" /></td>
	      </tr>		
	      <tr>
		  <th><label>Approve Text:</label></th>
		  <td align="left"><s:property value="ebt.approve" /></td>
	      </tr>	  
	      <tr>
		  <th><label>Last 4 Digits Card #:</label></th>
		  <td align="left"><s:property value="ebt.card_last_4" /></td>
	      </tr>
	      <tr>
		  <th><label>Ebt Donor Max:</label></th>
		  <td align="left">$<s:property value="ebt.ebt_donor_max" /></td>
	      </tr>
	      <tr>
		  <th><label>Buck Value:</label></th>
		  <td align="left">$<s:property value="ebt.ebt_buck_value" /></td>
	      </tr>
	      <tr>
		  <th><label>Include Double:</label></th>
		  <td align="left"><s:if test="ebt.includeDouble">Yes</s:if><s:else>No</s:else></td>
	      </tr>				
	      <s:if test="ebt.isCancelled()"><tr><th>Status:</th><td align="left">Cancelled</td></tr></s:if>
	      <s:if test="ebt.isDispute_resolution()"><tr><th>Notes:</th><td align="left"><s:property value="ebt.notes" /></td></tr></s:if>				
	  </table></td>
      </tr>
  </table>
</s:form>

<s:if test="ebt.hasBucks()">
  <s:set var="bucks" value="ebt.bucks" />
  <%@  include file="bucks.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































