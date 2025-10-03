<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="redeemEdit" id="form_1" method="post">    
    <h3>Redeem MB & GC</h3>
    <input type="hidden" name="id" value="<s:property value='id' />" />
    <input type="hidden" name="redeem.id" value="<s:property value='id' />" />
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
  <p>*indicates a required field</p>
  <table width="90%">
      <caption>Redemption ID:<s:property value="id" /></caption>
      <tr>
	  <th><b>Vendor:</b></th>
	  <td align="left"><s:property value="vendor" /></td>
      </tr>
      <tr>
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="redeemUser" /></td>		  
      </tr>
      <tr>
	  <th><b>Date & Time:</b></th>
	  <td align="left"><s:property value="date_time" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td align="left">$<s:property value="total" />.00</td>
      </tr>
      <tr>
	  <th><b>Count:</b></th>
	  <td align="left"><s:property value="count" /></td>
      </tr>
      <tr>
	  <th><label for="notes">Invoice notes:</label> </th>
	  <td align="left"><textarea name="redeem.notes" rows="5" cols="80" id="notes" ><s:property value="notes" /></textarea>
	  </td>
      </tr>		
      <tr>
	  <s:if test="status == 'Open'">
	      <td>&nbsp;</td>	      
	      <td>
		  <s:submit name="action" type="button" value="Scan more MB/GC" />
	      </td>
	  </s:if>
	  <s:else>
	      <th>&nbsp;</th>
	      <td>
		  <s:submit name="action" type="button" value="Update" />
		  <s:if test="redeem.canCancel()">
		      <s:submit name="action" type="button" value="Cancel" />
		  </s:if>
		  <button onclick="document.location='<s:property value='#application.url' />RedeemInvoice.do?id=<s:property value='redeem.id' />';return false;">Generate Invoice</button>
	      </td>
	  </s:else>
      </tr>
  </table>
</s:form>
<s:if test="redeem.canFinalize()">
  <s:form action="redeemAdd" id="form_2" method="post">
      <s:hidden name="redeem.id" />
      <table border="1" width="90%">
	  <tr>
	      <td>If you are done, click on 'Finalize' to complete this transaction and produce the 'Invoice':
		  <s:submit name="action" type="button" value="Finalize" />		
	      </td>
	  </tr>
      </table>
  </s:form>
</s:if>
<s:if test="hasBucks()">
  <table border="1" width="90%">
      <tr><td align="center">
	  <s:set var="bucks" value="bucks" />
	  <s:set var="bucksTitle" value="bucksTitle" />
	  <s:set var="total" value="total" />
	  <%@  include file="bucks.jsp" %>
      </td></tr>
  </table>
</s:if>
<s:if test="hasDisputes()">
  <s:set var="disputes" value="disputes" />
  <s:set var="disputesTitle" value="disputesTitle" />
  <%@  include file="disputes.jsp" %>
</s:if>
<%@  include file="footer.jsp" %>	






































