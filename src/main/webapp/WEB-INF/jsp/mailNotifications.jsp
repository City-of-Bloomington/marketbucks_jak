<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="mailNotification" method="post">    
  <h4>Inventory Mail Notifications</h4>
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
  <ul>
      <li>You can add/remove users to the email notified list about current inventory levels </li>
      <li>The email will be scheduled to run every Wednesday at 7AM.</li>
      <li>Next scheduled inventory notification date & time: </b><s:property value="next_fire_time" /></li>
  </ul>
  <table border="0" width="90%">
      <caption>Current user email list</caption>
      <tr>
	  <td style="text-align:right"><label>*check to remove</label></td>
	  <td style="text-align:left">User Name</td>
      </tr>
      <s:iterator var="one" value="mailUsers" status="status">
	  <tr>
	      <td style="text-align:right"><s:property value="(#status.index)+1" /> - <input type="checkbox" name="mailUserList.del_user_id" value="<s:property value='id' />" /></td>
	      <td style="text-align:left"><s:property value="fullName" /></td>
	  </tr>
      </s:iterator>
      <tr>
	  <td align="center" colspan="2">
	      <s:submit name="action" type="button" value="Remove Selected Users" />
	  </td>
      </tr>
  </table>
  <table width="90%" border="0">
      <caption>Users that can be added to email list</caption>
      <tr>
	  <td style="text-align:right"><label>*check to add</label></td>
	  <td style="text-align:left">User Name</td>
      </tr>
      <s:iterator var="one" value="nonMailUsers" status="status">		  
	  <tr>
	      <td style="text-align:right"><s:property value="(#status.index)+1" /> - <input type="checkbox" name="mailUserList.add_user_id" value="<s:property value='id' />" /></td>
	      <td style="text-align:left"><s:property value="fullName" /></td>
	  </tr>
      </s:iterator>
      <tr>
	  <td align="center" colspan="2">
	      <s:submit name="action" type="button" value="Add Selected Users" />
	  </td>
      </tr>
  </table>
</s:form>

<%@  include file="footer.jsp" %>	






































