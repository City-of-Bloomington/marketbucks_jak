
<p>Note: that the totals are requested totals only.</p>
<table border="1"><caption><s:property value="#fmnpWicsTitle" /></caption>
	<tr>
		<td align="center"><b>ID</b></td>
		<td align="center"><b>FMNP WIC Amount</b></td>
		<td align="center"><b>Ticket #</b></td>
		<td align="center"><b>User</b></td>
		<td align="center"><b>Date & Time</b></td>
		<td align="center"><b>Notes</b></td>
	</tr>
	<s:iterator var="one" value="#fmnpWics">
		<tr>
		    <td><a href="<s:property value='#application.url' />wicAdd.action?id=<s:property value='id' />">View/Edit <s:property value="id" /></a></td>
		    <td align="right">$<s:property value="amount" />.00</td>
		    <td><s:property value="ticketNum" /></td>
		    <td><s:property value="user" /></td>
		    <td><s:property value="date_time" /></td>
		    <td><s:if test="isCancelled()">Cancelled</s:if>
			&nbsp;<s:if test="isDispute_resolution()">In dispute</s:if></td>			
		</tr>
	</s:iterator>
	<s:if test="#showTotal">
		<tr>
		    <td>Total Requests</td>
		    <td align="right">$<s:property value="#wicTotal" />.00</td>
		    <td colspan="4">&nbsp;</td>
		</tr>
	</s:if>
</table>






































