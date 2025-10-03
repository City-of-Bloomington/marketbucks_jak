<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<table border="1"><caption>Most Recent Configurations</caption>
    <tr>
	<td align="center"><b>ID</b></td>
	<td align="center"><b>Name</b></td>  
	<td align="center"><b>Type</b></td>
	<td align="center"><b>Value</b></td>    
	<td align="center"><b>Max Donation Per Customer</b></td>
	<td align="center"><b>GL Account</b></td>
	<td align="center"><b>Last Update</b></td>
	<td align="center"><b>Update By</b></td>
	<td align="center"><b>Action</b></td>
    </tr>
	<s:iterator value="#buckConfs">
	    <tr>
		<td><a href="<s:property value='#application.url' />buckConf.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
		<td><s:property value="name" /></td>
		<td><s:property value="type" /></td>	
		<td align="right">$<s:property value="value" />.00</td>
		<td align="right">$<s:property value="donor_max_value" />.00</td>	
		<td><s:property value="gl_account" /></td>
		<td><s:property value="date" /></td>
		<td><s:property value="user" /></td>
		<td>&nbsp;
		    <s:if test="isCurrent()">
			<button onclick="document.location='<s:property value='#application.url' />batchEdit.action?conf_id=<s:property value='id' />';return false;">Generate New Batch</button>
		    </s:if>
		</td>		
	    </tr>
	</s:iterator>
</table>






































