<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Resolution <s:property value="resolution.id" /></h3>
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
<table border="0" width="90%">
    <caption>Resolution Info</caption>
    <tr>
	<th width="35%"><b>Dispute ID:</b></th>
	<td align="left">
	    <a href="<s:property value='#application.url' />disputeEdit.action?id=<s:property value='resolution.dispute_id' />"> <s:property value="resolution.dispute_id" /></a></td>					
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
	    <th valign="top" width="35%"><b>New Expire Date:</b></th>
	    <td align="left"><s:property value="%{resolution.expire_date}" /></td>
	</tr>
    </s:if>
    <s:elseif test="%{resolution.dispute.reason == 'Not Exist'}">
	<tr>
	    <th width="35%"><b>New MB/GC ID:</b></th>
	    <td align="left"><s:property value="%{resolution.new_buck_id}" /></td>
	</tr>
	<tr>
	    <th valign="top" width="35%"><b>Value/Expire Date:</b></th>
	    <td align="left"><s:property value="%{resolution.conf.info}" /></td>
	</tr>
	<s:if test="%{resolution.buck.buck_type_id == 1}">
	    <tr>
		<th valign="top" width="35%"><b>Authorization #:</b></th>
		<td align="left"><s:property value="%{resolution.approve}" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>Customer Card #:</b></th>
		<td align="left"><s:property value="%{resolution.card_last_4}" /></td>
	    </tr>
	</s:if>
	<s:else>
	    <tr>
		<th valign="top" width="35%"><b>Payment Type:</b></th>
		<td align="left"><s:property value="%{resolution.pay_type}" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>Check #:</b></td>
		<td align="left"><s:property value="%{resolution.check_no}" /></td>
	    </tr>
	</s:else>
    </s:elseif>
    <s:elseif test="%{resolution.dispute.reason == 'Not Issued'}">
	<s:if test="%{resolution.buck.buck_type_id == 1}">
	    <tr>
		<th valign="top" width="35%"><b>Authorization #:</b></th>
		<td align="left"><s:property value="%{resolution.approve}" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>Customer Card #:</b></th>
		<td align="left"><s:property value="%{resolution.card_last_4}" /></td>
	    </tr>
	</s:if>
	<s:else>
	    <tr>
		<th valign="top" width="35%"><b>Payment Type:</b></th>
		<td align="left"><s:property value="%{resolution.pay_type}" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>Check #:</b></th>
		<td align="left"><s:property value="%{resolution.check_no}" /></td>
	    </tr>
	</s:else>
    </s:elseif>
</table>

<s:if test="resolutions != null && resolutions.size() > 0">
  <s:set var="resolutions" value="resolutions" />
  <s:set var="resolutionsTitle" value="resolutionsTitle" />  
  <%@  include file="resolutions.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































