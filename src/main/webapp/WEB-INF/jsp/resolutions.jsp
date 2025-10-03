<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1">
    <caption><s:property value="#resolutionsTitle" /></caption>
    <tr>
	<td align="center"><b>ID</b></td>
	<td align="center"><b>Dispute ID</b></td>
	<td align="center"><b>Configure ID</b></td>
	<td align="center"><b>Expire Date</b></td>  
	<td align="center"><b>Value</b></td>
	<td align="center"><b>Authorization #</b></td>
	<td align="center"><b>Card Last 4</b></td>
	<td align="center"><b>Pay Type</b></td>
	<td align="center"><b>Check #</b></td>
	<td align="center"><b>Date & Time</b></td>
	<td align="center"><b>Status</b></td>
	<td align="center"><b>User</b></td>
	
    </tr>
    <s:iterator value="#resolutions">
	<tr>
	    <td><a href="<s:property value='#application.url' />resolutionView.action?id=<s:property value='id' />">View <s:property value="id" /></a></td>
	    <td><s:property value="dispute_id" /></td>
	    <td><s:property value="conf_id" /></td>	
	    <td><s:property value="expire_date" /></td>
	    <td><s:property value="value" /></td>
	    <td><s:property value="approve" /></td>
	    <td><s:property value="card_last_4" /></td>
	    <td><s:property value="pay_type" /></td>
	    <td><s:property value="check_no" /></td>
	    <td><s:property value="date_time" /></td>
	    <td><s:property value="status" /></td>
	    <td><s:property value="user" /></td>	
	</tr>
    </s:iterator>
</table>






































