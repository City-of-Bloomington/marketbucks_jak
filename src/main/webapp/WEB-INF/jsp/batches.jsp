<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1"><caption><s:property value="#batchesTitle" /></caption>
	<tr>
	    <td align="center"><b>ID</b></td>
	    <td align="center"><b>Face Value</b></td>
	    <td align="center"><b>Type</b></td>
	    <td align="center"><b>Pages</b></td>  
	    <td align="center"><b>Status</b></td>
	    <td align="center"><b>Start #</b></td>
	    <td align="center"><b>End #</b></td>  
	    <td align="center"><b>Date</b></td>
	    <td align="center"><b>User</b></td>    
	</tr>
	<s:iterator value="#batches">
	    <tr>
		<td><a href="<s:property value='#application.url' />batchEdit.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
		<td align="right">$<s:property value="value" />.00</td>	
		<td><s:property value="type" /></td>
		<td align="right"><s:property value="pages" /></td>
		<td><s:property value="status" /></td>
		<td align="right"><s:property value="start_seq" /></td>
		<td align="right"><s:property value="end_seq" /></td>
		<td><s:property value="date" /></td>
		<td><s:property value="user" /></td>
	    </tr>
	</s:iterator>
</table>






































