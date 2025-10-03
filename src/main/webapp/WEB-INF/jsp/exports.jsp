<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1"><caption><s:property value="#exportsTitle" /></caption>
	<tr>
		<td align="center"><b>ID</b></td>
		<td align="center"><b>Date & Time</b></td>  
		<td align="center"><b>User</b></td>
		<td align="center"><b>NW Batch Number</b></td>
		<td align="center"><b>Status</b></td>
	</tr>
	<s:iterator value="#exports">
		<tr>
			<td>
				<a href="<s:property value='#application.url' />exportAdd.action?id=<s:property value='id' />">View <s:property value="id" /></a>
			</td>
			<td><s:property value="date_time" /></td>
			<td><s:property value="user" /></td>	
			<td><s:property value="nw_batch_name" /></td>
			<td><s:property value="status" /></td>
		</tr>
	</s:iterator>
</table>






































