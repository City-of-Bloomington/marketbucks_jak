<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="user" method="post" id="form_id">
    <s:if test="person.id == ''">
	<h4>New User</h4>
    </s:if>
    <s:else>
	<h3>Edit User</h3>
	<s:hidden name="person.id" value="%{person.id}" />
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
    <table width="90%">
	<caption>User </caption>
	<s:if test="person.id != ''">
	    <tr>
		<th><label>User ID:</label></th>
		<td align="left">
		    <s:property value="%{person.id}" />
		</td>
	    </tr>							 
	</s:if>
	<tr>
	    <th width="25%"><label for="username">Username:</label></th>
	    <td align="left">
		<s:textfield name="person.username" maxlength="30" size="30" value="%{person.username}" required="true" id="username" /> * (for login)
	    </td>
	</tr>
	<tr>
	    <th><label for="fname">Full Name:</label></th>
	    <td align="left"><s:textfield name="person.fullName" maxlength="30" size="50" required="true" value="%{person.fullName}" id="fname" /> ** </td>
	</tr>
	<tr>
	    <th><label for="role">Role:</label></th>
	    <td align="left"><s:radio name="person.role" value="%{person.role}" list="#{'-1':'View only','Edit':'Edit Only','Edit:Delete':'Edit and Delete','Admin:Edit:Delete':'All (admin)'}" id="role" /></td>
	</tr>
	<tr>
	    <th><label for="inactive">Inactive:</label></th>
	    <td align="left"><s:checkbox name="person.inactive" value="%{person.inactive}" id="inactive" /> Yes (check this to inactivate)				
	    </td>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <s:if test="person.id == ''">
		<td>
		    <s:submit name="action" type="button" value="Save" id="save_button" />
		</td>
	    </s:if>
	    <s:else>
		<td>
		    <s:submit name="action" type="button" id="update_button" value="Update" />
		</td>
	    </s:else>
	</tr>
    </table>
</s:form>
<br />
<s:if test="hasUsers()">
  <s:set var="users" value="users" />
  <s:set var="usersTitle" value="'Current Users'" />
  <%@  include file="users.jsp" %>	
</s:if>

<%@  include file="footer.jsp" %>	






































