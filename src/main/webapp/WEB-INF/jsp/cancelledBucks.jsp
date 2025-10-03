<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1"><caption><s:property value="bucksTitle" /></caption>
  <tr>
      <td align="center"><b>ID</b></td>
      <td align="center"><b>Date, Time</b></td>
      <td align="center"><b>User</b></td>	
      <td align="center"><b>Value</b></td>
  </tr>
  <s:iterator var="one" value="#bucks">
      <tr>
	  <td><s:property value="id" /></td>
	  <td><s:property value="date_time" /></td>
	  <td><s:property value="user" /></td>
	  <td align="right">$<s:property value="buck.value" />.00</td>
      </tr>
  </s:iterator>
</table>






































