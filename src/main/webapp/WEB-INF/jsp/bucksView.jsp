<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:if test="bucks != null">
    <table border="1"><caption><s:property value="bucksTitle" /></caption>
	<tr>
	    <td align="center"><b>ID</b></td>
	    <td align="center"><b>Expire Date</b></td>
	    <td align="center"><b>Fund Type</b></td>
	    <td align="center"><b>Voided?</b></td>	
	    <td align="center"><b>Face Value</b></td>
	</tr>
	<s:iterator var="one" value="#bucks">
	    <tr>
		<td><a href="<s:property value='#application.url' />buckInfo.action?id=<s:property value='id' />">Show Details <s:property value="id" /></a></td>			
		<td><s:property value="expire_date" /></td>
		<td><s:property value="fund_typeStr" /></td>
		<td><s:if test="#one.isVoided()">Voided</s:if></td>	
		<td align="right">$<s:property value="value" />.00</td>
	    </tr>
	</s:iterator>
    </table>
</s:if>
<s:else>
    <p>
	<s:property value="bucksTitle" />
    </p>
</s:else>






































