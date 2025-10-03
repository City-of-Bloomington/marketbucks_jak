<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1" width="60%">
  <caption><s:property value="bucksTitle" /></caption>
	<s:if test="#total != null">
		<tr>
		    <td align="center"><b>ID</b></td>
		    <td align="center"><b>Expire Date</b></td>
		    <td align="center"><b>Voided?</b></td>	
		    <td align="center"><b>Face Value</b></td>
		</tr>
		<tr>
		    <td colspan="3" align="right">Total</td>
		    <td align="right">$<s:property value="#total" />.00</td>
		</tr>
	</s:if>
	<s:iterator var="one" value="#bucks">
		<tr>
		    <td><s:property value="id" /></td>
		    <td><s:property value="expire_date" /></td>
		    <td><s:if test="#one.isVoided()">Voided</s:if></td>	
		    <td align="right">$<s:property value="value" />.00</td>
		</tr>
	</s:iterator>
</table>






































