<table border="1" width="100%">
	<caption><s:property value="#vendorsTitle" /></caption>
	<tr>
		<td align="center"><b>ID</b></td>
		<td align="center"><b>Vendor #</b></td>		
		<td align="center"><b>Last Name</b></td>
		<td align="center"><b>First Name</b></td>
		<td align="center"><b>Related Business</b></td>
		<td align="center"><b>Pay Type</b></td>		
		<td align="center"><b>Active? </b></td>
	</tr>
	<s:iterator var="one" value="#vendors">
		<tr>
		    <td>
			<a href="<s:property value='#application.url' />vendor.action?id=<s:property value='id' />"> Edit <s:property value="id" /></a></td>
		    <td><s:property value="vendorNum" /></td>
		    <td><s:property value="lname" /></td>
		    <td><s:property value="fname" /></td>
		    <td><s:property value="businessName" /></td>
		    <td><s:property value="payTypeStr" /></td>			
		    <td><s:property value="activeStr" /></td>
		</tr>
	</s:iterator>
</table>






































