<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:if test="canEdit()">
    <s:form action="vendor" method="post" id="form_id">
	<s:if test="id == ''">
	    <h4>New Vendor</h4>
	</s:if>
	<s:else>
	    <h3>Edit Vendor</h3>
	    <s:hidden name="vendor.id" value="%{id}" />
	    <s:hidden name="id" value="%{id}" />	    
	</s:else>
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
	<p>
	    * The Vendor number is the same ID number on the vendor issued card. <br />
	    This is needed when scaning vendor card to redeem MB or GC.<br />
	    ** indicates a required field <br />
	</p>
	<table border="0" width="90%">
	    <s:if test="id != ''">
		<tr>
		    <th><b>Vendor ID:</b></th>
		    <td align="left"><s:property value="%{id}" />
			<a href="<s:property value='#application.url' />vendor.action?id=<s:property value='id' />&action=setInactive"> Set as inactive </a></td>
		</tr>
	    </s:if>
	    <tr>
		<th><label for="num">* Vendor Number:</label></th>
		<td align="left">
		    <s:textfield name="vendor.vendorNum" maxlength="10" size="10" value="%{vendorNum}" required="true" id="num" />
		</td>
	    </tr>
	    <tr>
		<th><label for="lname">* Last Name:</label></th>
		<td align="left"><s:textfield name="vendor.lname" maxlength="50" size="40" required="true" value="%{lname}" id="lname" /> </td>
	    </tr>
	    <tr>
		<th><label for="fname">First Name:</label></th>
		<td align="left"><s:textfield name="vendor.fname" maxlength="30" size="30" value="%{fname}" id="fname" /></td>
	    </tr>
	    <tr>
		<th><label for="bus">Related Business:</label></th>
		<td align="left"><s:textfield name="vendor.businessName" maxlength="50" size="30" value="%{businessName}" id="bus" />(business name if different from Last Name)</td>
	    </tr>
	    <tr>
		<th><label for="act">Active? </label></th>
		<td align="left"><s:checkbox name="vendor.active" value="%{active}" id="act" />Yes (uncheck to inactivate)
		</td>
	    </tr>
	    <tr>
		<th><label for="ptype">Allowed Pay Type:</label></th>
		<td align="left" align="left"><s:radio name="vendor.payType" value="%{payType}" list="#{'-1':'None','GC':'GC only','MB:GC':'MB and GC'}" i="ptype" /></td>
	    </tr>
	    <tr>
		<td>&nbsp;</td>
		<s:if test="id == ''">
		    <td>
			<s:submit name="action" type="button" value="Save" id="save_button" />
		    </td>
		</s:if>
		<s:else>
		    <td>
			<s:submit name="action" type="button" id="update_button" value="Update" /> &nbsp;&nbsp;
			
		    </td>
		</s:else>
	    </tr>
	</table>
    </s:form>		
    <ul>
	<li> <a href="<s:property value='#application.url' />vendor.action"> New Vendor</a></li>
	<li>
	    <a href="<s:property value='#application.url' />vendorSearch.action">Vendors Search </a></li>
    </ul>
</s:if>
<br />
<s:if test="hasVendors()">
  <s:set var="vendors" value="vendors" />
  <s:set var="vendorsTitle" value="vendorsTitle" />
  <%@  include file="vendors.jsp" %>	
</s:if>

<%@  include file="footer.jsp" %>	






































