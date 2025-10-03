<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1"><caption><s:property value="#giftsTitle" /></caption>
	<tr>
		<td align="center"><b>ID</b></td>
		<td align="center"><b>Requested Amount</b></td>
		<td align="center"><b>Payment Type</b></td>
		<td align="center"><b>Check #/RecTrac</b></td>  
		<td align="center"><b>User</b></td>
		<td align="center"><b>Date & Time</b></td>
		<td align="center"><b>Notes</b></td>
	</tr>
	<s:iterator var="one" value="#gifts">
		<tr>
			<td><a href="<s:property value='#application.url' />issueGiftAdd.action?id=<s:property value='id' />">View <s:property value="id" /></a></td>
			<td align="right"><s:property value="amount" /></td>	
			<td><s:property value="pay_type" /></td>
			<td><s:property value="check_no" /></td>
			<td><s:property value="user" /></td>
			<td><s:property value="date_time" /></td>
			<td><s:if test="isCancelled()">Cancelled</s:if>
			&nbsp;<s:if test="isDispute_resolution()">In dispute</s:if></td>
		</tr>
	</s:iterator>
</table>






































