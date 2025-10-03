<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1"><caption><s:property value="#redeemsTitle" /></caption>
	<tr>
		<td align="center"><b>ID</b></td>
		<td align="center"><b>Vendor</b></td>
		<td align="center"><b>User</b></td>
		<td align="center"><b>Date & Time</b></td>
		<td align="center"><b>Total Value</b></td>
		<td align="center"><b>Status</b></td>
		<td align="center"><b>Disputes?</b></td>
		<td align="center"><b>Invoice</b></td>
	</tr>
	<s:iterator var="one" value="#redeems">
		<tr>
			<td><a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='id' />">View <s:property value="id" /></a></td>
			<td><s:property value="vendor" /></td>
			<td><s:property value="user" /></td>
			<td><s:property value="date_time" /></td>
			<td align="right">$<s:property value="total" />.00</td>
			<td><s:property value="status" /></td>
			<td>&nbsp;<s:if test="#one.hasDisputes()">Yes</s:if></td>
			<td>
				<s:if test="status == 'Open' || total == 0">&nbsp;
				</s:if>
				<s:else>
					<button onclick="document.location='<s:property value='#application.url' />RedeemInvoice.do?id=<s:property value='id' />';return false;">View Invoice</button>
				</s:else>
			</td>
		</tr>
	</s:iterator>
</table>






































