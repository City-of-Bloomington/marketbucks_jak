<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page session="false" %>
<% 
response.setHeader("Expires", "0");
response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
response.setHeader("Pragma", "public");
response.setHeader("Content-Disposition","inline; filename=marketbucks_data.csv");
response.setContentType("application/csv");
%>
<s:iterator value="all" status="allStatus"><s:iterator status="rowStatus"><s:if test="#rowStatus.index == 0"><s:property value="second" /></s:if><s:elseif test="#rowStatus.index == 1"><s:property value="first" />,<s:property value="second" /><s:if test="size == 3">,<s:property value="third" /></s:if></s:elseif><s:else><s:property value="first" /><s:if test="size == 2">,<s:property value="second" /></s:if><s:else>,<s:property value="second" /></s:else><s:if test="size == 3">,<s:property value="third" /></s:if></s:else>
</s:iterator>
</s:iterator>
























































