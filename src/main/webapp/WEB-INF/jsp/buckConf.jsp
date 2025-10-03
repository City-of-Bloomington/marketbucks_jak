<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
-->
<s:if test="buckConf.id == ''">
    <h3>New Configuration</h3>
    Select MB/GC type from the list below, many of the fields will be set for you.<br />
</s:if>
<s:else>
    <s:hidden name="buckConf.id" value="%{buckConf.id}" />
    <h3>Edit Configuration</h3>
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
<p>*indicates a required field</p>
<s:form action="buckConf" method="post">
  <s:if test="buckConf.id != ''">  
      <s:hidden name="buckConf.id" value="%{buckConf.id}" />
  </s:if>
  <table width="90%" border="0"><caption> Configuration</caption>
      <s:if test="buckConf.id != ''">
	  <tr>
	      <th><label for="id">ID:</label></th>
	      <td align="left"><s:property value="id" /></td>
	  </tr>
      </s:if>
      <tr>
	  <th width="30%"><label for="name">* Name:</label></th>
	  <td align="left" colspan="3">$<s:textfield name="buckConf.name" maxlength="50" size="40" value="%{buckConf.name}" required="true" cssClass="need_focus" id="name" /></td>
      </tr>		
      <tr>
	  <td colspan="4" aling="left">
	      Face value is the $ value of the bucks that will be printed on the bucks and gift certificates in this season.
	  </td>
      </tr>
      <tr>
	  <th width="30%" valign="top"><label for="type_id">* Type:</label></th>
	  <td align="left"><s:select name="buckConf.type_id" value="%{buckConf.type_id}" required="true" list="buck_types" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Type" size="4" onchange="setGl_account()" id="type_id" /></td>
	  <td width="30%" valign="top"><label for="gl_account">* GL Account:</label></td>
	  <td align="left"><s:select name="buckConf.gl_account" value="%{buckConf.gl_account}" list="gl_accounts" listKey="name" listValue="name" required="true" headerKey="-1" headerValue="Pick GL account" size="4" id="gl_account" /></td>
      </tr>		
      <tr>
	  <th width="30%"><label for="face_value_id">*Face Value:</label></th>
	  <td align="left" colspan="3">$<s:textfield name="buckConf.value" maxlength="2" size="2" value="%{buckConf.value}" required="true" id="face_value_id" />.00</td>
      </tr>
      <tr>
	  <th width="30%"><label for="max_don_id">* Max Donation:</label></th>
	  <td align="left" colspan="3">$<s:textfield name="buckConf.donor_max_value" maxlength="2" size="2" value="%{buckConf.donor_max_value}" required="true" id="max_don_id" />.00 (Per customer)</td>
      </tr>
      <s:if test="buckConf.id != ''">
	  <tr>
	      <th><b>Last Update:</b></th>
	      <td align="left"><s:property value="buckConf.date" /></td>
	  </tr>
	  <tr>
	      <th><b>Updated By:</b></th>
	      <td align="left"><s:property value="buckConf.user" /></td>
	  </tr>		  
      </s:if>
      <s:if test="buckConf.id == ''">
	  <tr>
	      <td valign="top" align="right" colspan="2">
		  <s:submit name="action" type="button" value="Save" />
	      </td>
	  </tr>
      </s:if>
      <s:else>
	  <tr>
	      <th valign="top">
		  <s:submit name="action" type="button" value="Update" />
	      </th>
	      <s:if test="buckConf.isCurrent()">
		  <td valign="top">
		      <button onclick="document.location='<s:property value='#application.url' />batchEdit.action?conf_id=<s:property value='buckConf.id' />';return false;">Generate New Batch</button></td>
	      </s:if>
	  </tr>
      </s:else>
  </table>
  <s:if test="buckConf.hasBatches()">
      <s:set var="batches" value="buckConf.batches" />
      <s:set var="batchesTitle" value="batchesTitle" />
      <%@  include file="batches.jsp" %>	
  </s:if>
</s:form>
<s:if test="buckConfs != null && buckConfs.size() > 0">
    <s:set var="buckConfs" value="buckConfs" />
    <%@  include file="buckConfs.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































