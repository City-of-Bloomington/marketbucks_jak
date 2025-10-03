<%@  include file="header.jsp" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<s:form action="disputeEdit" method="post">
    <s:if test="dispute.canEdit()">
	<h3>Edit Dispute</h3>
	</s:if>
	<s:else>
	    <h3>View Dispute</h3>
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
	<s:hidden name="dispute.id" value="%{dispute.id}" />
	<s:if test="dispute.status == 'Waiting'">
	    <ul>
		<li>if you think this case can not be resolved, change the status to "Rejected' and then click on 'Update'.</li>
		<li>if you think this was a mistake, click on 'Delete'</li>
		<li>Otherwise click on 'Resolution' to resolve this case.</li>
	    </ul>
	</s:if>
	<table width="90%"><caption>Dispute Update</caption>
	    <tr>
		<th><b>View Redemption:</b></th>
		<td><a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='dispute.redeem_id'/>"> <s:property value="dispute.redeem_id" /></a></td>		  
	    </tr>
	    <tr>
		<th><b>Buck ID:</b></th>
		<td align="left"><s:property value="dispute.buck_id" /></td>
	    </tr>
	    <s:if test="dispute.canEdit()">
		<tr><td colspan="2"><label for="notes"> Invoice Notes:</label> (the text entered here will show on vendor&#39;s invoice)</td></tr>
		<tr>
		    <th valign="top"><label></label></th>
		    <td align="left"><s:textarea name="dispute.notes" value="%{dispute.notes}" rows="5" cols="70" id="notes" /></td>
		</tr>
	    </s:if>
	    <s:elseif test="dispute.hasNotes()">
		<tr><td colspan="2"><b> Invoice Notes: </b>(the text entered here will show on vendor&#39;s invoice)</td></tr>					
		<tr>
		    <th valign="top">&nbsp;</th>
		    <td align="left"><s:property value="%{dispute.notes}" /></td>
		</tr>
	    </s:elseif>
	    <tr>
		<th width="35%"><b>Status:</b></th>
		<td align="left">
		    <s:if test="dispute.isResolved()">
			Resolved
		    </s:if>
		    <s:else>
			<s:if test="dispute.isWaiting()">
			    <input type="radio" name="dispute.status" value="Waiting" checked="true" id="waiting" /><label for="waiting">Waiting</label>
			    <input type="radio" name="dispute.status" value="Rejected" id="rejected" /><label for="rejected">Rejected</label>
			</s:if>
			<s:else>
			    <input type="radio" name="dispute.status" value="Waiting" id="waiting" /><label for="waiting">Waiting</label>
			    <input type="radio" name="dispute.status" value="Rejected" id="rejected" checked="true" /><label for="rejected">Rejected</label>
			</s:else>
		    </s:else>
		</td>
	    </tr>
                    	    
	    <tr>
		<th valign="top"><b>Reason:</b></th>
		<td align="left"><s:property value="dispute.reason" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>User:</b></th>
		<td align="left"><s:property value="dispute.user" /></td>
	    </tr>
	    <tr>
		<th valign="top"><b>Date & Time:</b></th>
		<td align="left"><s:property value="dispute.date_time" /></td>
	    </tr>		
	    <s:if test="dispute.isWaiting()">
		<tr>
		    <td align="left">
			<s:submit name="action" type="button" value="Update" />
		    </td>
		    <td align="center">
			<s:submit name="action" type="button" value="Delete" />
		    </td>
		</tr>
	    </s:if>
	    <s:else>
		<tr>
		    <td>&nbsp;</td>
		    <td>
			<s:if test="dispute.hasResolution()">
			    <button onclick="document.location='<s:property value='#application.url' />resolutionView.action?id=<s:property value='dispute.resolution.id' />';return false;">View Resolution</button>
			</s:if>
			<s:else>
			<button onclick="document.location='<s:property value='#application.url' />resolutionEdit.action?dispute_id=<s:property value='dispute.id' />';return false;">Start Resolution</button>
			</s:else>
		    </td>
		</tr>
	    </s:else>
	</table>
</s:form>

<s:if test="disputes != null && disputes.size() > 0">
  <s:set var="disputes" value="disputes" />
  <s:set var="disputesTitle" value="disputesTitle" />  
  <%@  include file="disputes.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































