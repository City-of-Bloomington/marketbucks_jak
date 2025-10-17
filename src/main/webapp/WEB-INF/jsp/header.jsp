<?xml version="1.0" encoding="UTF-8" ?>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
    <head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<s:head />
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
	<link rel="SHORTCUT ICON" href="<s:property value='#application.url'/>images/favicon.ico" />
	<link rel="stylesheet" href="<s:property value='#application.url'/>css/marketb.css" type="text/css" media="all" />	
	<link rel="stylesheet" href="<s:property value='#application.url'/>css/top_menu.css" type="text/css" media="all" />
	<title>Market Bucks</title>
	<script type="text/javascript">
	 var APPLICATION_URL = '<s:property value='#application.url' />';
	</script>	
    </head>
    <body>
	<header>
	    <h1><a href="<s:property value='#application.url'/>">Market Bucks</a></h1>
	    <h3><a href="<s:property value='#application.url'/>">City of Bloomington, IN</a></h3>
	    <s:if test="user != null">
		<div id="div_top">
		    <ul id="ul_top">
			<li><a href="<s:property value='#application.url'/>snapStart.action">Online Purchase</a></li>
			<li><a href="<s:property value='#application.url'/>ebtAdd.action">Ebt MB</a></li>
			<li><a href="<s:property value='#application.url'/>wicAdd.action">FMNP WIC</a></li>
			<li><a href="<s:property value='#application.url'/>seniorAdd.action">FMNP Senior</a></li>
			<li><a href="<s:property value='#application.url'/>redeemStart.action">Redemptions</a></li>				
			<li><a href="<s:property value='#application.url'/>otherMenu.action">More Options</a></li>
			<s:if test="isAdmin()">
			    <li><a href="<s:property value='#application.url'/>settings">Settings</a></li>
			</s:if>
			<li><a href="<s:property value='#application.url'/>logout">log Out</a></li>
			<li><s:property value='fullName' /></li>
		    </ul>
		</div>
	    </s:if>
	</header>
	<br /><br />
		
