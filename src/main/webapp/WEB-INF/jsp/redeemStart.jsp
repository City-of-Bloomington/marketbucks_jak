<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="redeemStart" id="form_id" method="post">
  <s:hidden name="action2" id="action_id" value=""/>
  
  <h3>New Redemption</h3>
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
  <table border="0" width="90%"><caption>Redemption</caption>
      <tr><th>Select a vendor from the list or enter vendor number </th>
	  <td>&nbsp;</td>
      </tr>
      <tr>
	  <th><label for="ven_id">Vendor:</label></th>
	  <td align="left">
	      <s:if test="hasVendors()">
		  <s:select name="redeem.vendorNum2" list="vendors" listKey="vendorNum" listValue="fullName" headerKey="-1" headerValue="Pick Vendor" id="ven_id">
		  </s:select>
	      </s:if>
	      <s:else>
		  &nbsp;
	      </s:else>
	  </td>
      </tr>	      
      <tr>
	  <th><label for="vendor_id"> Vendor number:</label></th>
	  <td align="left">
	      <s:textfield name="redeem.vendorNum" id="vendor_id" value="%{redeem.vendorNum}" />
	  </td>
      </tr>
      <tr>
	  <td>&nbsp;</td>
	  <th>
	      <s:submit name="action" type="button" value="Next" />
	  </th>
      </tr>
  </table>
</s:form>
<br />
<a href="<s:property value='#application.url'/>redeemSearch.action">Advance Search </a>
<br />
<s:if test="redeems != null && redeems.size() > 0">
  <s:set var="redeems" value="redeems" />
  <s:set var="redeemsTitle" value="redeemsTitle" />
  <%@  include file="redeems.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































