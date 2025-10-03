<table border="1" width="100%">
    <caption><s:property value="#usersTitle" /></caption>
    <tr>
	<td align="center"><b>ID</b></td>
	<td align="center"><b>Username</b></td>		
	<td align="center"><b>Full Name</b></td>
	<td align="center"><b>Role</b></td>
	<td align="center"><b>Active? </b></td>
    </tr>
    <s:iterator var="one" value="#users">
	<tr>
	    <td>
		<a href="<s:property value='#application.url' />user.action?id=<s:property value='id' />"> Edit <s:property value="id" /></a></td>
	    <td><s:property value="username" /></td>
	    <td><s:property value="fullName" /></td>
	    <td><s:property value="roleText" /></td>
	    <td><s:if test="active">Active</s:if><s:else>Inactive</s:else></td>
	</tr>
    </s:iterator>
</table>






































