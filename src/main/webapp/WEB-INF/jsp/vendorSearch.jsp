<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="vendorSearch" method="post" id="form_id">
    <h4>Search Vendors</h4>
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
    <table border="0" width="90%">
	<caption>Search Options</caption>
	<tr>
	    <th><label for="vid">MB Vendor ID #:</label></th>
	    <td align="left">
		<s:textfield name="vendLst.id" maxlength="10" size="10" value="%{id}" id="vid" />
	    </td>
	</tr>
	<tr>
	    <th><label for="vnum">Vendor Number:</label></th>
	    <td align="left">
		<s:textfield name="vendLst.vendorNum" maxlength="10" size="10" value="%{vendorNum}" id="vnum" />
	    </td>
	</tr>
	<tr>
	    <th><label for="name">Name:</label></th>
	    <td align="left"><s:textfield name="vendLst.name" maxlength="50" size="40" value="%{name}" id="name" /> </td>
	</tr>
	<tr>
	    <th><label for="status">Active Status:</label></th> 
	    <td align="left" align="left"><s:radio name="vendLst.activeStatus" value="%{activeStatus}" list="#{'-1':'All','y':'Active','n':'Inactive'}"  id="status" /></td>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <td>
		<s:submit name="action" type="button" value="Submint" id="save_button" />
	    </td>
	</tr>
    </table>
</s:form>
<br />
<s:if test="hasVendors()">
    <s:set var="vendors" value="vendors" />
    <s:set var="vendorsTitle" value="vendorsTitle" />
    <%@  include file="vendors.jsp" %>	
</s:if>

<%@  include file="footer.jsp" %>	






































