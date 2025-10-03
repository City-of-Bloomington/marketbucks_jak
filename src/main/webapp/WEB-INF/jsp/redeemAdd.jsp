<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="redeemAdd" id="form_1" method="post">    
  <h3>Redeem MB & GC</h3>
  <s:hidden name="redeem.id" value="%{redeem.id}" />
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
  <table border="0" width="90%">
      <caption>Redemption ID:<s:property value="redeem.id" /></caption>
      <tr>
	  <th><b>Vendor:</b></th>
	  <td align="left"><s:property value="redeem.vendor" /></td>
      </tr>
      <tr>
	  <th><b>User:</b></th>
	  <td align="left"><s:property value="redeem.user" /></td>		  
      </tr>
      <tr>
	  <th><b>Date & time:</b></th>
	  <td align="left"><s:property value="redeem.date_time" /></td>
      </tr>
      <tr>
	  <th><b>Total:</b></th>
	  <td align="left">$<s:property value="redeem.total" />.00</td>
      </tr>
      <tr>
	  <th valign="top"><b>Invoice notes:</b></th>
	  <td align="left"><s:property value="redeem.notes" /></td>
      </tr>
      <tr>
	  <th><b>Count:</b></th>
	  <td align="left"><s:property value="redeem.count" /></td>
      </tr>		
      <s:if test="redeem.status == 'Open'">
	  <tr>
	      <th><label for="bar_code_id">* Scan/Enter new MB/GC:</label></th>
	      		<td>  <s:textfield name="redeem.buck_id" value="" size="20" maxlength="20" required="true" id="bar_code_id" autofocus="true" /></td>
	  </tr>
	  <tr>
	      <td>&nbsp;</td>	      
	      <td>
		  <s:submit name="action" type="button" value="Add" />
	      </td>
	  </tr>
	  <tr>
	      <th>
		  <a href="<s:property value='#application.url' />redeemEdit.action?id=<s:property value='%{redeem.id}' />">Cancel this transaction </a>
	      </th>
	  </tr>
      </s:if>
      <s:else>
	  <tr>
	      <th>
		  <button onclick="document.location='<s:property value='#application.url' />RedeemInvoice.do?id=<s:property value='redeem.id' />';return false;">Generate Invoice</button>		
	      </th>
	      <td>&nbsp;</td>
	  </tr>
      </s:else>
  </table>
</s:form>
<s:if test="redeem.canFinalize()">
  <s:form action="redeemAdd" id="form_2" method="post">
      <s:hidden name="redeem.id" value="%{redeem.id}" />
      <table border="1" width="90%">
	  <caption> Final Action</caption>
	  <tr>
	      <td align="center">If you are done, click on 'Finalize' to complete this transaction and produce the 'Invoice':
		  <s:submit name="action" type="button" value="Finalize" />		
	      </td>
	  </tr>
      </table>
  </s:form>
</s:if>
<s:if test="hasBucks">
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






































