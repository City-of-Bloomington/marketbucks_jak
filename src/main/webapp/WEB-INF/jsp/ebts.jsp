<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<p>Note: that the totals are requested totals only.</p>
<table border="1"><caption><s:property value="#ebtsTitle" /></caption>
    <tr>
	<td align="center"><b>ID</b></td>
	<td align="center"><b>Reguested EBT Amount</b></td>
	<td align="center"><b>Available DMB Amount</b></td>  
	<td align="center"><b>Authorization #</b></td>
	<td align="center"><b>Card #</b></td>  
	<td align="center"><b>User</b></td>
	<td align="center"><b>Date & Time</b></td>
	<td align="center"><b>Included Double</b></td>		
	<td align="center"><b>Buck Value</b></td>
	
	<td align="center"><b>Donor Max</b></td>		
	<td align="center"><b>Notes</b></td>
    </tr>
    <s:iterator var="one" value="#ebts">
	<tr>
	    <td><a href="<s:property value='#application.url' />issueAdd.action?id=<s:property value='id' />">View <s:property value="id" /></a></td>
	    <td align="right">$<s:property value="amount" />.00</td>
	    <td align="right">$<s:property value="dmb_amount" />.00</td>	
	    <td><s:property value="approve" /></td>
	    <td><s:property value="card_last_4" /></td>
	    <td><s:property value="user" /></td>
	    <td><s:property value="date_time" /></td>
	    <td><s:if test="canDouble()">Yes</s:if><s:else>No</s:else></td>			
	    <td><s:property value="ebt_buck_value" /></td>
	    <td><s:property value="ebt_donor_max" /></td>
	    <td><s:if test="isCancelled()">Cancelled</s:if>
		&nbsp;<s:if test="isDispute_resolution()"><s:property value="notes" /></s:if></td>			
	</tr>
	</s:iterator>
	<tr>
	    <td>Total </td>
	    <td align="right">$<s:property value="ebtTotal" />.00</td>
	    <td align="right">$<s:property value="dmbTotal" />.00</td>
	    <td colspan="8">&nbsp;</td>
	</tr>
</table>






































