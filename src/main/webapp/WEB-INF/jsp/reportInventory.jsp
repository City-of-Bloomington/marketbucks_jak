<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<table border="1" width="80%">
  <caption><s:property value="report.title" /></caption>
  <tr>
      <td align="center">Certificate Type</td>
      <td align="center">Printed</td>
      <td align="center">Issued</td>
      <td align="center">Inventory</td>
  </tr>
  <s:iterator value="report.inventoryList" status="allStatus">
      <tr>
	  <td align="left"><label><s:property value="first" /></label></td>
	  <td align="right"><label><s:property value="second" /></label></td>
	  <td align="right"><label><s:property value="third" /></label></td>
	  <td align="right"><label><s:property value="forth" /></label></td>	  
      </tr>
  </s:iterator>
</table>























































