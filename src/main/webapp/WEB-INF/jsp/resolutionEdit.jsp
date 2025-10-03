<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="resolutionEdit" method="post">
  <s:if test="resolution.id == ''">
      <h3>New Resolution</h3>
  </s:if>
  <s:else>
      <h3>Edit Resolution <s:property value="resolution.id" /></h3>
      <s:hidden name="resolution.id" value="%{resolution.id}" />	
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
  <s:hidden name="resolution.dispute_id" value="%{resolution.dispute_id}" />
  <p>To resolve this case, you need to set the fields below based on the dispute reason as suggested below:</p>
  <s:if test="resolution.dispute.reason == 'Expired'">
      <p>Since the reason for this dispute is Expired Date, the new expire date wil be reset to 12/31/ of this year</p>
  </s:if>
  <s:elseif test="resolution.dispute.reason == 'Not Exist'">
      <ul>
	  <li>If the cause of dispute is a typo, just enter the new MB/GC id in the "New MB/GC id" and ignore all the other fields then click on "Update"</li> 
	  <li>Else, select one of the 'Configuration' options that match the certificate. </li>
	  <li>Set the value. </li>
      </ul>
  </s:elseif>
  <s:elseif test="resolution.dispute.reason == 'Not Issued'">
      <s:if test="resolution.buck.buck_type_id == 1">
	  <li>This is a MB, the 'Authorization #' and 'Customer Card' will be set to a default values.</li>
      </s:if>
      <s:else>
	  <li>This is a GC then 'Pay Type' will be set to 'Dispute Resolution'.</li>
      </s:else>
      </ul>
  </s:elseif>
  <table border="0" width="90%">
      <tr>
	  <th width="35%"><b>Dispute ID:</b></td>
	  <td align="left">
	      <a href="<s:property value='#application.url' />disputeEdit.action?id=<s:property value='resolution.dispute_id'/>"> <s:property value="resolution.dispute_id" /></a></td>					
      </tr>
      <tr>
	  <th><b>Disputed MB/GC ID:</b></th>
	  <td align="left"><s:property value="resolution.dispute.buck_id" /></td>
      </tr>
      <tr>
	  <th valign="top" width="35%"><b>Reason:</b></th>
	  <td align="left"><s:property value="resolution.dispute.reason" /></td>
      </tr>
      <s:if test="resolution.id != ''">
	  <tr>
	      <th valign="top"><b>Status:</b></th>
	      <td align="left"><s:property value="resolution.status" /></td>
	  </tr>					
      </s:if>		  		  
      <s:if test="resolution.dispute.reason == 'Expired'">
	  <tr>
	      <th valign="top"><label for="exp_date">New Expire Date:</label></th>
	      <td align="left"><s:textfield name="resolution.expire_date" value="%{resolution.expire_date}" size="10" cssClass="date" id="exp_date" /></td>
	  </tr>
      </s:if>
      <s:elseif test="%{resolution.dispute.reason == 'Not Exist'}">
	  <tr>
	      <td colspan="2">If the dispute is caused by a typo, enter the new MB/GC id number below, ignore the other fields and then click on 'Submit'.</td>
	  </tr>
	  <tr>
	      <th width="35%"><label for="buck_id">New MB/GC ID:</label></th>
	      <td align="left"><s:textfield name="resolution.new_buck_id" value="%{resolution.new_buck_id}" size="8" maxlength="10" id="buck_id" /></td>
	  </tr>
	  <tr>
	      <td colspan="2">1 - If you think this MB/GC is valid and we need to add it to the system, then pick the value (3, 5 or 20) and expire date from the list below, then go to point 2 (for MB) or 3 (for GC) below to be able to issue it</td>
	  </tr>
	  <tr>
	      <th valign="top" width="35%"><label for="conf_id">Value:</label></th>
	      <td align="left"><s:select name="resolution.conf_id" value="%{resolution.conf_id}" list="confs" listKey="id" listValue="info" headerKey="-1" headerValue="" id="conf_id" /></td>
	  </tr>
	  <tr>
	      <td colspan="2">2 - If this is a MB, the authorization # and customer card # will be set to default values </td>
	  </tr>
  	  <tr>
	      <td colspan="2">3 - If this is a GC, the payment type will default to 'Dispute Resolution'</td>
	  </tr>			
	  <tr>
	      <th valign="top" width="35%"><b>Payment Type:</b></th>
	      <td align="left">Dispute Resolution</td>
	  </tr>
      </s:elseif>
	<s:elseif test="%{resolution.dispute.reason == 'Not Issued'}">
	    <s:if test="%{resolution.buck.buck_type_id == 1}">
		<tr>
		    <td colspan="2">For MB, the authorization # and customer card # will be set to default values</td>
		</tr>
	    </s:if>
	    <s:else>
		<tr>
		    <td colspan="2">For GC, the payment type will be set to 'Dispute Resolution' and check number will be ignored.</td>
		</tr>			
	    </s:else>
	</s:elseif>
	<s:if test="resolution.status != 'Success'">
	    <tr>
		<td>&nbsp;</td>		

		<td>
		    <s:submit name="action" type="button" value="Submit" />
		</td>
	    </tr>
	</s:if>
  </table>
</s:form>

<s:if test="resolutions != null && resolutions.size() > 0">
  <s:set var="resolutions" value="resolutions" />
  <s:set var="resolutionsTitle" value="resolutionsTitle" />  
  <%@  include file="resolutions.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































