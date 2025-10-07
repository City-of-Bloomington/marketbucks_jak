<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Generate & Print Bucks</h3>
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
<p>To start a new batch of MB or GC, click on the 'Generate New Batch' that corresponds to the related configuration in the table below:</p>
<s:if test="hasBuckConfs()">
    <s:set var="buckConfs" value="buckConfs" />
    <%@  include file="buckConfs.jsp" %>	
</s:if>	


<table border="1" width="80%">
  <s:if test="hasRecentDate()">
      <tr>
	  <th><label for="printable"> The audit sheet for the most recent prints of <s:property value="recentBatchDate" /></label>
	  </th>
	  <td>
	  <button id="printable" onclick="document.location='<s:property value='#application.url'/>AuditSheet.do?date_from=<s:property value='recentBatchDate' />';return false;">Printable Audit Sheet</button></th><td>&nbsp;</td>
      </tr>
  </s:if>
</table>
<br />
<a href="<s:property value='#application.url'/>batchSearch.action"> For MB batch Search </a> <br />
<a href="<s:property value='#application.url'/>buckSearch.action"> For any (Issued/not issued) MB/GC search  </a> <br />
<s:if test="batches != null && batches.size() > 0 ">
    <s:set var="batches" value="batches" />
    <s:set var="batchesTitle" value="batchesTitle" />  
    <%@  include file="batches.jsp" %>	
</s:if>
<%@  include file="footer.jsp" %>	






































