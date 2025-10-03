<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="issueAdd" method="post">    
    <h4>Issue Market Bucks</h4>
    <s:hidden name="ebt.id" value="%{id}" />
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
    <p>*indicate a required field </p>
    <table width="90%"><caption>Issue Market Bucks</caption>
	<tr>
	    <th><b>Transaction ID: </b></th>
	    <td align="left"><s:property value="id" /></td>
	</tr>
	<tr>
	    <th><b>Authorization #: </b></th>
	    <td align="left"><s:property value="approve" /></td>
	</tr>
	<tr> <th><b>Card #: </b></th>
	    <td align="left"><s:property value="card_last_4" /></td>		    </tr>
	<tr>				
	    <th><b>Request Amount: </b></th>
	    <td align="left">$<s:property value="amount" />.00</td>
	</tr>
	<tr>		    
	    <th><b>DMB Amount: </b></th>
	    <td align="left">$<s:property value="dmb_amount" />.00</td>
	</tr>
	<tr>
	    <th><b>Issued EBT: </b></th>
	    <td align="left">$<s:property value="paid_amount" />.00</td>
	</tr>
	<tr>
	    <th><b>Date & Time: </b></th>
	    <td align="left"><s:property value="date_time" /></td>
	</tr>
	<tr>			
	    <th><b>Issued DMB: </b></th>
	    <td align="left">$<s:property value="donated_amount" />.00</td>
	</tr>
	<tr>
	    <th><b>User:</b></th>
	    <td align="left"><s:property value="ebt_user" /></td>
	</tr>
	<tr>
	    <th><b>Total: </b></th>
	    <td align="left">$<s:property value="total" />.00</td>
	</tr>
	<s:if test="isCancelled()">
	    <tr>
		<th><b>Status: </b></th>
		<td align="left">Cancelled</td>
	    </tr>
	</s:if>
	<s:if test="isDispute_resolution()">
	    <tr>
		<th><b>Notes: </b></th>
		<td align="left"><s:property value="notes" /></td>
	    </tr>
	</s:if>				
	<s:elseif test="hasBalance()">
	    <tr>
		<th bgcolor="red">
		    <b>Balance: </b></th>
		<td> $<s:property value="balance" />.00</td>
	    </tr>
	</s:elseif>
	<s:if test="!isCancelled() && !isDispute_resolution()">
	    <s:if test="needMoreIssue()">
		<tr>
		    <th><label for="bar_code_id">* Scan/Enter new Market Buck:</label></th>
		    <td align="left"><s:textfield name="ebt.buck_id" value="" size="10" maxlength="10" required="true" id="bar_code_id"  autofocus="true" /></td>
		</tr>
		<tr>
		    <td>&nbsp;</td>		    
		    <td>
			<s:submit name="action" type="button" value="Add" />
		    </td>
		</tr>	  
	    </s:if>
	    <s:else>
		<tr>
		    <th>All Market Bucks are issued for this customer</th>
		    <td>&nbsp;</td>
		</tr>
	    </s:else>
	    <tr>
		<th>
		    <a href="<s:property value='#application.url' />ebtAdd.action?id=<s:property value='ebt.id' />">Edit/Cancel this Transaction </a>
		</th>
		<td>&nbsp;</td>
	    </tr>		  
	</s:if>
    </table>	
    <s:if test="hasBucks()">
	<s:set var="bucks" value="ebt.bucks" />
	<s:set var="bucksTitle" value="bucksTitle" />
	<s:set var="total" value="ebt.bucksTotal" />
	<%@  include file="bucks.jsp" %>
    </s:if>

</s:form>


<%@  include file="footer.jsp" %>	






































