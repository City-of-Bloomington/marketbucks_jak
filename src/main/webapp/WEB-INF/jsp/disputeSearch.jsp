<%@  include file="header.jsp" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="disputeSearch" method="post">    
  <h3>Dispute Search</h3>
  <s:if test="hasActionErrors()">
      <s:actionerror/>
  </s:if>
  <s:elseif test="hasActionMessages()">
      <s:actionmessage/>
  </s:elseif>
  <table width="90%"><caption>Search Options</caption>
      <tr>
	  <th width="20%"><label for="status">Status:</label></th>
	  <td align="left"><s:radio name="disputeList.status" value="%{disputeList.status}" list="#{'-1':'All','Waiting':'Waiting','Rejected':'Rejected','Resolved':'Resolved'}" id="status" /></td>
      </tr>
      <tr>
	  <th><label for="reason">Reason:</label></th>
	  <td align="left"><s:radio name="disputeList.reason" value="%{disputeList.reason}" list="#{'-1':'All','Expired':'Expired','Not Exist':'Do not Exist','Not Issued':'Not Issued','Redeemed':'Already Redeemed'}" id="reason" /></td>
      </tr>		
      <tr>
	  <th><label for="redeem_id">Redemption ID:</label></th>
	  <td align="left"><s:textfield name="disputeList.redeem_id" maxlength="10" size="10" value="%{disputeList.redeem_id}" id="redeem_id" /></td>
      </tr>	  
      <tr>
	  <th valign="top"><label for="date_from">Date from:</label></th>
	  <td align="left"><s:textfield name="disputeList.date_from" maxlength="10" size="10" value="%{disputeList.date_from}" cssClass="date"  id="date_from" /><label for="date_to"> to</label>
	      <s:textfield name="disputeList.date_to" maxlength="10" size="10" value="%{disputeList.date_to}" cssClass="date" id="date_to" /></td>
      </tr>
      <tr>
	  <td>&nbsp;</td>	  
	  <td valign="top">
	      <s:submit name="action" type="button" value="Find" />
	  </td>

      </tr>
  </table>
</s:form>

<s:if test="disputes != null && disputes.size() > 0">
    <s:set var="disputes" value="disputes" />
    <s:set var="disputesTitle" value="disputesTitle" />
    <%@  include file="disputes.jsp" %>	
</s:if>
<s:elseif test="action !='' ">
  <p><s:property value="disputesTitle" /></p>
</s:elseif>

<%@  include file="footer.jsp" %>	






































