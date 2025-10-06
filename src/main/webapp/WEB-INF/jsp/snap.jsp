<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<s:form action="snap" method="post" id="form_id" onsubmit="return confirmForCancel()">
  <s:if test="snap.id == ''">
      <h4>New Online Purchase</h4>
  </s:if>
  <s:else>
      <h3>Edit Online Purchase</h3>
      <p>Note: if you make a mistake you can Update the record or Cancel.
	  If Cancelled the record will not be deleted but marked as
	  Cancelled (Inactive)</p>
      <s:hidden name="snap.id" value="%{id}" />
      <s:hidden name="snap.dblMax" value="%{dblMax}" />
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
  <table width="90%" border="0">
      <caption>
	  Online Purchase (SNAP)
	  <s:if test="hasId()"><s:property value="id" /></s:if>
      </caption>
      <tr>
	  <th><label for="amount">* Purchase Amount: </label></th>
	  <td align=left">$<s:textfield name="snap.snapAmount" maxlength="8" size="8" required="true" value="%{snapAmount}" id="amount" cssClass="need_focus" />(xx.xx format only)</td>
      </tr>
      <tr>
	  <th><label for="dbl">Include Double:</label></th>
	  <td align="left">
	      <s:radio name="snap.includeDouble"
		       value="%{includeDouble}"
		       list="#{'y':'Yes','n':'No'}" listKey="key"
		       listValue="value" />	
	  </td>
      </tr>
      <tr>	      
	  <th><label for="cardnum">* Customer Card #: </label></th>
	  <td align="left"><s:textfield name="snap.cardNumber" maxlength="4" size="4" required="true" value="%{cardNumber}" id="cardnum" /></td>
      </tr>
      <tr>
	  <th><label for="auth">* Authorization #: </label></th>
	  <td align="left"><s:textfield name="snap.authorization" maxlength="10" size="10" required="true" value="%{authorization}" id="auth" /></td>
      </tr>
      <tr>
	  <th><b>Ebt Amount: </b></th>
	  <td align="left">$<s:property value="ebtAmount" /></td>
      </tr>
      <s:if test="canDouble()">
	  <tr>
	      <th><b>Dbl Amount: </b></th>
	      <td align="left">$<s:property value="dblAmount" /></td>
	  </tr>
      </s:if>
      <s:else>
	  <tr>
	      <th><b>Dbl Amount: </b></th>
	      <td align="left">No Double</td>
	  </tr>
      </s:else>
      <s:if test="isCancelled()">
	  <tr>
	      <th><b>Status: </b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="hasSnapUser()">
	  <tr>
	      <th><b>User: </b></th>
	      <td align="left"><s:property value="snap_user" /></td>
	  </tr>
      </s:if>
      <s:if test="!hasId()">
	  <tr>
	      <td>&nbsp;</td>
	      <td>
		  <s:submit name="action" type="button" value="Save" />
	      </td>
	  </tr>
      </s:if>
      <s:elseif test="!isCancelled()">
	  <tr>
	      <th><label for="date">Date: </label></th> 
	      <td align=left"><s:textfield name="snap.date" maxlength="10" size="10" required="true" value="%{date}" cssClass="date" id="date" /> Time:<s:textfield name="snap.time" maxlength="5" size="5" required="true" value="%{time}" /></td>
			  
	  </tr>
	  <tr>
	      <th>
		  <s:submit name="action" type="button" id="update_button" value="Update" /> &nbsp;
	      </th>
	      <td align="center">
		  <s:submit name="action" type="button"  id="cancel_button" value="Cancel" />
	      </td>
	  </tr>
      </s:elseif>
  </table> 
</s:form>
<br />

<%@  include file="footer.jsp" %>	






































