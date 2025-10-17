<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="exportStart" id="form_id" method="post">
  <h3>Redemptions Export</h3>
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
      <caption>Export</caption>
      <s:if test="hasRedeems() ">
	  <tr>
	      <td align="center">
		  <s:set var="redeems" value="redeems" />
		  <s:set var="redeemsTitle" value="redeemsTitle" />
		  <%@  include file="redeems.jsp" %>	
	      </td>
	  </tr>
	  <tr>
	      <s:if test="hasDisputes()">
		  <td valign="top" align="left">
		      One or more of the redemptions have dispute(s), please resolve the disputes first before you do the export
		  </td>
	      </s:if>
	      <s:else>
		  <td valign="top" align="right">
		      <s:submit name="action" type="button" value="Start Export" />
		  </td>
	      </s:else>
	  </tr>
      </s:if>
      <s:else>
	  No redemptions available to export.
      </s:else>
  </table>
</s:form>

<s:if test="hasExports()">
  <s:set var="exports" value="exports" />
  <s:set var="exportsTitle" value="exportsTitle" />
  <%@  include file="exports.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































