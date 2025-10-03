<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<table border="1"><caption><s:property value="#disputesTitle" /></caption>
    <tr>
	<td align="center"><b>ID</b></td>
	<td align="center"><b>Redeem</b></td>
	<td align="center"><b>MB/GC</b></td>
	<td align="center"><b>Status</b></td>
	<td align="center"><b>Reason</b></td>  
	<td align="center"><b>User</b></td>
	<td align="center"><b>Date & Time</b></td>
	<td align="center"><b>Invoice Notes</b></td>
	<td align="center"><b>Resolution</b></td>
    </tr>
    <s:iterator value="#disputes">
	<tr>
	    <td><a href="<s:property value='#application.url' />disputeEdit.action?id=<s:property value='id' />">View <s:property value="id" /></a></td>
	    
	    <td><a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='redeem_id' />">View <s:property value="redeem_id" /></a></td>
	    <td><s:property value="buck_id" /></td>
	    <td><s:property value="status" /></td>
	    <td><s:property value="reason" /></td>		
	    <td><s:property value="user" /></td>
	    <td><s:property value="date_time" /></td>
	    <td><s:property value="notes" /></td>	
	    <td>
		<s:if test="hasResolution()" >
		    <a href="<s:property value='#application.url' />resolutionView.action?id=<s:property value='resolution.id' />">View <s:property value="resolution.id" /></a>	  
		</s:if>
		<s:else>
		    &nbsp;
		</s:else>
	    </td>
	</tr>
    </s:iterator>
</table>






































