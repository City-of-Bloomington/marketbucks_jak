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
      <s:hidden name="snap.id" value="%{snap.id}" />
      <s:hidden name="snap.dblMax" value="%{snap.dblMax}" />
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
  <table width="90%" border="0"><caption>Online Purchase (SNAP)
      <tr>
	  <th><label for="amount">* Purchase Amount: </label></th>
	  <td align=left">$<s:textfield name="snap.snapAmount" maxlength="8" size="8" required="true" value="%{snap.snapAmount}" id="amount" cssClass="need_focus" />(xx.xx format only)</td>
      </tr>
      <tr>
	  <th><label for="dbl">Include Double:</label></th>
	  <td align="left">
		  <s:checkbox name="snap.includeDouble" value="%{snap.includeDouble}" id="dbl" />Yes (uncheck if not included)
	  </td>
      </tr>
      <tr>	      
	  <th><label for="cardnum">* Customer Card #: </label></th>
	  <td align="left"><s:textfield name="snap.cardNumber" maxlength="4" size="4" required="true" value="%{snap.cardNumber}" id="cardnum" /></td>
      </tr>
      <tr>
	  <th><label for="auth">* Authorization #: </label></th>
	  <td align="left"><s:textfield name="snap.authorization" maxlength="10" size="10" required="true" value="%{snap.authorization}" id="auth" /></td>
      </tr>
      <tr>
	  <th><b>Ebt Amount: </b></th>
	  <td align="left">$<s:property value="snap.ebtAmount" /></td>
      </tr>
      <s:if test="snap.canDouble()">
	  <tr>
	      <th><b>Dbl Amount: </b></th>
	      <td align="left">$<s:property value="snap.dblAmount" /></td>
	  </tr>
      </s:if>
      <s:else>
	  <tr>
	      <th><b>Dbl Amount: </b></th>
	      <td align="left">No Double</td>
	  </tr>
      </s:else>
      <s:if test="snap.isCancelled()">
	  <tr>
	      <th><b>Status: </b></th>
	      <td align="left">Cancelled</td>
	  </tr>
      </s:if>
      <s:if test="snap.hasUser()">
	  <tr>
	      <th><b>User: </b></th>
	      <td align="left"><s:property value="snap.user" /></td>
	  </tr>
      </s:if>
      <s:if test="snap.id == ''">
	  <tr>
	      <td>&nbsp;</td>
	      <td>
		  <s:submit name="action" type="button" value="Save" />
	      </td>
	  </tr>
      </s:if>
      <s:elseif test="!snap.isCancelled()">
	  <tr>
	      <th><label for="date">Date: </label></th> 
	      <td align=left"><s:textfield name="snap.date" maxlength="10" size="10" required="true" value="%{snap.date}" cssClass="date" id="date" /> Time:<s:textfield name="snap.time" maxlength="5" size="5" required="true" value="%{snap.time}" /></td>
			  
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






































