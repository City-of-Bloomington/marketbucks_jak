<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="redeemStart" method="post">    
  <h3>Add New Redemption</h3>
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
  <p>*indicate a required field</p>
  <table border="0" width="90%">
  	 <caption>Pick Vendor</caption>
      <tr>
	  <td align="right" width="35%"><label for="vend_id">* Vendor:</label></td>
	  <td align="left">$<s:select name="redeem.vendor_id" required="true" value="%{redeem.vendor_id}" list="vendors" listKey="id" listValue="fullName" headerKey="-1" headerValue="Pick Vendor" id="vend_id" /></td>
      </tr>
      <tr>
	  <td>&nbsp;</td>
	  <td valign="top" align="center" colspan="2">
	      <s:submit name="action" type="button" value="Save" />
	  </td>
      </tr>
  </table>
</s:form>

<s:if test="redeems != null && redeems.size() > 0">
  <s:set var="redeems" value="redeems" />
  <%@  include file="redeems.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































