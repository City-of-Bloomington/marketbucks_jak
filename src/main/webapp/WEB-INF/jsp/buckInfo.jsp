<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<h1>Detailed Buck/Gift Info</h1>
<s:if test="hasActionErrors()">
    <div class="errors">
	<s:actionerror/>
    </div>
</s:if>
<s:elseif test="hasActionMessages()">
    <div class="welcome">
	<s:actionmessage/>
    </div>
</s:elseif>

<table border="1">
    <caption>MB/GC Details</caption>
    <tr>
	<th>Id:</th><td><s:property value="id" /></td>
    </tr>
    <tr>
	<th>Value:</th><td align="right">$<s:property value="value" />.00</td>
    </tr>
    <tr>
	<th>Expire Date:</th><td><s:property value="expire_date" /></td>				
    </tr>
    <tr>
	<th>Fund Type:</th><td><s:property value="fund_typeStr" /></td>
    </tr>
    <tr>
	<th>Is Voided? </th><td><s:if test="isVoided()">Yes</s:if><s:else>No</s:else></td>	
    </tr>
</table>
<table border="1"><caption>Related Transactions</caption>
    <tr>
	<td>
	    <s:if test="hasEbt()">
		Related Ebt: <a href="<s:property value='#application.url' />issueAdd.action?id=<s:property value='ebt_id' />" id="view_ebt"> View Ebt </a>
	    </s:if>
	    <s:else>
		No Ebt found 
	    </s:else>
	</td>
    </tr>
    <tr>
	<td>
	    <s:if test="hasGift()">
		Related Gift: <a href="<s:property value='#application.url' />issueGiftAdd.action?id=<s:property value='gift_id' />">View Gift Info</a>
	    </s:if>
	    <s:else>
		No related gift found 
	    </s:else>
	</td>
    </tr>
    <tr>
	<td>
	    <s:if test="hasRedeem()">
		Related Redemption: <a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='redeem_id' />">View Redemption </a>
	    </s:if>
	    <s:else>
		No related Redemption found 
	    </s:else>
	</td>
    </tr>
</table>


































