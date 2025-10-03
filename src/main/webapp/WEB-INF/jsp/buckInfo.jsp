<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<h3>Detailed Buck/Gift Info</h3>
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
<s:if test="hasEbt()">
    
    <table border="1"><caption>Related Ebt</caption>
	<tr>
	    <td align="center"><b>ID</b></td>
	    <td align="center"><b>Amount</b></td>
	    <td align="center"><b>DMB Amount</b></td>  
	    <td align="center"><b>Authorization #</b></td>
	    <td align="center"><b>Card #</b></td>  
	    <td align="center"><b>User</b></td>
	    <td align="center"><b>Date & Time</b></td>
	    <td align="center"><b>Cancelled?</b></td>
	</tr>
	<tr>
	    <td><a href="<s:property value='#application.url' />issueAdd.action?id=<s:property value='buck.ebt.id' />" id="view_ebt"><label for="view_ebt" View Ebt </label></a></td>
	    <td align="right">$<s:property value="buck.ebt.amount" />.00</td>
	    <td align="right">$<s:property value="buck.ebt.dmb_amount" />.00</td>	
	    <td><s:property value="buck.ebt.approve" /></td>
	    <td><s:property value="buck.ebt.card_last_4" /></td>
	    <td><s:property value="buck.ebt.user" /></td>
	    <td><s:property value="buck.ebt.date_time" /></td>
	    <td>&nbsp;<s:if test="buck.ebt.isCancelled()">Yes</s:if><s:else>No</s:else></td>
	</tr>
    </table>
</s:if>
<s:else>
    <p>No ebt info found </p>
</s:else>
<s:if test="hasGift()">
    <table border="1"><caption>Related Gift</caption>
	<tr>
	    <td align="center"><b>Gift ID</b></td>
	    <td align="center"><b>Amount</b></td>
	    <td align="center"><b>Payment Type</b></td>
	    <td align="center"><b>Check #/RecTrac</b></td>  
	    <td align="center"><b>User</b></td>
	    <td align="center"><b>Date & Time</b></td>
	    <td align="center"><b>Cancelled?</b></td>
	</tr>
	<tr>
	    <td><a href="<s:property value='#application.url' />issueGiftAdd.action?id=<s:property value='buck.gift.id' />">View Gift Info</a></td>
	    <td align="right"><s:property value="buck.gift.amount" /></td>	
	    <td><s:property value="buck.gift.pay_type" /></td>
	    <td><s:property value="buck.gift.check_no" /></td>
	    <td><s:property value="buck.gift.user" /></td>
	    <td><s:property value="buck.gift.date_time" /></td>
	    <td><s:if test="buck.gift.isCancelled()">Yes</s:if><s:else>No</s:else></td>
	</tr>
    </table>
</s:if>
<s:else>
    <p>No gift info found </p>
</s:else>
<s:if test="hasRedeem()">
    <table border="1"><caption>Related Redemption</caption>
	<tr>
	    <td align="center"><b>Redemption ID</b></td>
	    <td align="center"><b>Vendor</b></td>
	    <td align="center"><b>User</b></td>
	    <td align="center"><b>Date & Time</b></td>
	    <td align="center"><b>Total Value</b></td>
	    <td align="center"><b>Status</b></td>
	    <td align="center"><b>Disputes?</b></td>
	</tr>
	<tr>
	    <td><a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='buck.redeem.id' />">View Redemption </a></td>
	    <td><s:property value="buck.redeem.vendor" /></td>
	    <td><s:property value="buck.redeem.user" /></td>
	    <td><s:property value="buck.redeem.date_time" /></td>
	    <td align="right">$<s:property value="buck.redeem.total" />.00</td>
	    <td><s:property value="buck.redeem.status" /></td>
	    <td>&nbsp;<s:if test="buck.redeem.hasDisputes()">Yes</s:if><s:else>No</s:else></td>
	</tr>
    </table>
</s:if>
<s:else>
    <p>No redemption info found </p>
</s:else>

































