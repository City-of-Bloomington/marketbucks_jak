<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
     * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
     * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
     * @author W. Sibo <sibow@bloomington.in.gov>
     *
     *
-->
<h3>Other Menu Options</h3>
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
    <li>
	<a href="<s:property value='#application.url'/>giftAdd.action">Issue GC</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>rxAdd.action">Issue Rx</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>disputeSearch.action">Disputes</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>ebtSearch.action">Ebt Search</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>snapSearch.action">Online Purchase Search (SNAP)</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>fmnpSearch.action">FMNP Search</a>
    </li>	
    <li>
	<a href="<s:property value='#application.url'/>buckSearch.action">Buck Search</a>
    </li>
    <li>
	<a href="<s:property value='#application.url'/>settings.action">Settings (Admin only)</a>
    </li>    
    <li>
	<a href="<s:property value='#application.url'/>help.action">User Guide</a>
    </li>
</ul>

<%@  include file="footer.jsp" %>























































