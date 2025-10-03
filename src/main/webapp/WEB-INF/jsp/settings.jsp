<%@  include file="header.jsp" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h3>Settings</h3>
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
    <li><a href="<s:property value='#application.url'/>buckConf.action">Configuration</a></li>
    <li><a href="<s:property value='#application.url'/>batchStart.action">Generate & Print</a></li>
    <li><a href="<s:property value='#application.url'/>cancelledSearch.action">Voided MB/GC</a></li>
    <li><a href="<s:property value='#application.url'/>mailNotification.action">Inventory Mail Notifications</a></li>				
    <li><a href="<s:property value='#application.url'/>exportStart.action">Exports</a></li>
    <li><a href="<s:property value='#application.url'/>vendor.action">Vendors</a></li>
    <li><a href="<s:property value='#application.url'/>report.action">Reports</a></li>
    <li><a href="<s:property value='#application.url'/>user.action">Users</a></li>
</ul>

<%@  include file="footer.jsp" %>























































