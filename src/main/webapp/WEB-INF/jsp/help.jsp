<%@  include file="header.jsp" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h2>Marketbucks User Guide</h2>

<h3>Content Table</h3>
<ul>
  <li><a href="#intor"> Introduction </a></li>
  <li><a href="#howto"> How To Use </a></li>
  <ul>
		<li><a href="#config">Configuration  </a></li>
		<li><a href="#generate">Generate & Print </a></li>
		<li><a href="#issue_mb">Issue Market Bucks (MB) </a></li>
		<li><a href="#issue_gc">Issue Gift Certificates (GC) </a></li>
		<li><a href="#redeem">Redemptions </a></li>
		<li><a href="#dispute">Disputes & Resolutions </a></li>
		<li><a href="#export"> Exports </a></li>
		<li><a href="#searchBatch"> Search Generated Batches</a></li>
		<li><a href="#audit"> Printing Audit Sheet</a></li>	
		<li><a href="#searchMB"> Search Issued MB</a></li>
		<li><a href="#searchGC"> Search Issued GC</a></li>	
		<li><a href="#search"> Search Redemptions</a></li>
		<li><a href="#report"> Reports </a></li>
  </ul>
  <li><a href="training.action"> 3-Training </a></li>
</ul>
<h3 id="intro">1-Introduction</h3>

<p>
Marketbucks is an applications designed for Parks & Recs Dept to handle farmers market and A Fair of Arts market bucks (MB) and gift certificates (GC). The process includes generating and printing, issuing, redemption, exporting, reports, ..etc.
</p>
<p>
Access to the system is controlled by IT department and a set of users are allowed to handle different activities based in roles in this system.
</p>
<p>
When the user is granted access to the system, he/she is allowed to do certain activities limited by the role assigned to the user with the designation of Parks & Recs department.
</p>
<p>
To login to this system, use the link below for testing and training:<br />

http://outlaw/marketbucks
<br />

and for production use the following link:
<br />

http://marketbucks
<br />

<p>
  We will be using a hand held scanner to scan the market bucks and gift certificates instead of typing the market bucks numbers. The scanner has three beep levels. You can use this <a href="volume_setting_codes.pdf">document</a> to change the current level if it is too loud or too quiet.
  
</p>

<h3 id="howto">2-How to Use </h3>
<p>
  Going to the link mentioned above you will be required to login in a similar way that you would login to your email account. 
The successful login user will be introduced to the marketbucks interface, depending on your privileges, you may see some or all of the following options in the top menu (starting from the left):
</p>


<h3 id="config">2-1 Configuration </h3>
<p>

Configuration is only needed once a year, normally when preparing for next season. For 2015 this is done for you. We have introduced three different configurations, each related to different type of buck certificate or gift certificate. So we have one for MB and two for GC's since we need two kind of GC's. One for $5 and one for $20. 
</p>
<p>
Each configuration has the following components:
-Name: give the configuration a meaning name that represent the type of certificate that it represents such as "MB 2015 $3 Bucks" for MB or "MB 2015 GC $5" and so on.
</p>
<ul>
  <li>Type: pick a type from the three types we currently have (MB $3, GC $5, GC $20).</li>
  <li>Face value, could be $3, $5, or $20 (this may change in the future).</li>

  <li>Max Donations: This is relevant to MB only and depending on funds available, in our example we set this to $18 per customer for this year.</li>

  <li>GL Account: The GL string used in New World and set by Controller Department for each certificate type. Fro MB it is: 201-18-186503-47240 and for GC: 201-18-186503-47230.</li>

  <li>Expire Date: this is the expiration date that every MB or GC will be linked to when issued. This can be changed later if the date need to be extended depending on decision made by  Parks & Recs Dept and Controller Dept.</li>
</ul>

<h3 id="generate"> 2-3 Generate & Print </h3>
<p>
To generate a new batch of MB or GC you click on "Generate & Print" in the menu item on top. You will be presented with most recent configurations available. To generate MB pick the configuration line that correspond to MB and click on "Generate New Batch".
</p>
<p>
You need to enter the values "Batch Size" which determines how many MB's will be created, such as 1, 12, 21, 99, etc depending on your need, note that we recommend making the number is a multiple of 3 since the check stock page consist of 3 checks per page.
</p>
<p>
You also need to set the "Start Seq" if you do not want to use the current default value.
</p>
<p>
It is recommended that only one person create these batches so that we do not get conflict of generating similar batches that may contain similar sequences.
</p>
<p>
When these two fields are set click on "Next". <br />
A new form will be presented that shows the details of this newly created batch.<br /> 
Next action will be to print these certificates, so you would click on "Printable Certificates". <br />
A new pop window will appear to ask you to "Open" or "Save" the newly created pdf file. Click on "Open" then click on the printer icon to print the bucks. 
If all bucks were printed with no problem, now you can click on "Confirm" button on the main form. <br />
If for some reason, (such as not enough paper), enter the last sequence/number of the MB or GC that was printed. and then click on "Confirm".
If printing was not possible, do not confirm. As you can reprint the batch later when the printer is ready and have enough paper checks to use then you would confirm.
</p>
<p>
Also, in the page you could find the old batches that were generated before and what sequences were included.  By clicking on one of the most recent batches in the table below, you will be able to see more details, especially the sequences.
This is the place where you can come back to the batches that were generated and not printed yet. 
</p>

<h3 id="issue_mb"> 2-3 Issue Market Bucks (MB) </h3>
<p>
To issue new market bucks for customers, you click on "Issue MB" in top menu.
You enter the total amount in $, authorization number and customer Card number used in RecTrac. For illustration purpose, I will assume that the customer wanted $15 in MB.
</p>
<p>
I would enter 15, in the first field, 123123 in the second field and 1122 in the last field and then I click on "Next".
</p>
<p>
It is important to note that the amount must be multiple of 3, such as 6, 9, 12, 15, 18, 21 and son on. If the number is not divisible by 3, you will get an error message that will ask you to change the amount.
</p>
<p>
Now the system will  move to another screen, where it will show you the requested amount, DMB, the total with other details as new bucks are added to the request by scanning(or entering) one buck at a time. You keep adding bucks as long as a positive balance is shown.
</p>
<p>
In this example, the final total will be $15+$15 (DMB) = $30 total. The sequences of the MB and funding type will be shown in the table below. When the requested amount is reached, no more buck will be allowed to be added.
</p>
<p>
Note: scanning the same buck again will be ignored. Scanning a previously scanned buck will show an error so that the same buck can not be issued twice and that bug will be added to the "disputes" list.
</p>
<p>
Under this menu you would be able to see the most recent issues in the table below the main form.
</p>

<h3 id="issue_gc"> 2-4 Issue Gift Certificates (GC) </h3>
<p>
This option is similar to the previous one except here we are issuing GC for $5 or $20 and we do not have DMB. The payment option is either "Cash", "Check", "Credit Card" or "Honorary".
</p>
<p>
In case of using checks, money orders or cashier checks, enter the check number in the "Check #/RecTrac"  field.
</p>
<p>
If using credit card enter RecTrac number in the "Check #/RecTrac" field.
</p>
<p>
After that you can scan or enter GC one at a time till the total amount is reached.
</p>
<p>
It is important to note that the amount requested must be a multiple of 5, such as 5, 10, 15, 20, 25, 30, .. etc.
</p>

<h3 id="redeem"> 2-5 Redemptions </h3>
<p>
To redeem MB or GC, first you scan Vendor card in the "Vendor Number" field to find the vendor. If the vendor has no card, you can type his name/business name in the "Vendor Name" field to pick from the list. If the vendor is not in the list, he/she must be added to "New World" system first by "Controller" office before you continue.
</p>
<p>
Assuming the vendor is available in our system. The system will take you to a new screen where you scan MB or GC one at a time, the total will be adjusted as you scan.
</p>
<p>
When you are done with this vendor click on "Finalize" to complete the process.
</p>
<p>
When that is done you can click on "Generate Invoice" that you can print in two copies, one will be handed to the vendor and the other to be kept with MB or GC received.
</p>
<p>
In this page you can find the most recent redemptions. They will be listed in the table below the main form.
</p>

<h3 id="dispute"> 2-6 Disputes & Resolutions </h3>
<p>
As mentioned in redemptions that some MB or GC may not pass and cause a dispute record. Each dispute will a have a reason for the problem and suggestions to resolve the problem (if any). The user who will be handling the disputes can do one of the options below. 1-Reject the dispute, 2-Delete the dispute (an error occurred during data entry), 3-Resolve the dispute by click on "Start Resolution".
</p>
<p>
  We list four reasons for the dispute.
  <ul>
	<li>The MB or GC is already redeemed, therefore can not be redeemed again.</li> 
	<li>The MB or GC has expired. The resolution will be to change the expiration date to some time in the future.</li>
	<li>The MB or GC is not in the system (not exist), the appropriate resolution will add the certificate to the system with the needed fields entered on the resolution page. This possible if some of the old MB or GC were issued but did not find their way to the spread sheet.</li>
	<li>The MB or GC is not issued. To resolve we need to issue this certificate by entering the related info.</li>
  </ul>
</p>

<h3 id="export"> 2-7 Exports </h3>
<p>
The export is the process of transferring the redeemed MB and GC to "New World" system so that the vendors can be paid.
</p>
<p>
The export will generate xml file that will be imported to "New World" system.
</p>
<p>
To start export, we must have some outstanding redemptions that are not exported yet.
</p>
<p>
These will be listed in top of the form before the "Start Export" button. If there are no redemptions there will be no export.
</p>
<p>
The only thing that the user need to do is "Click" on the "Start Export" button. The xml file will be created that the user will save to his/her computer and then perform the process of importing in "New World". If desired, the redemption invoices can be downloaded and attached to "New World" import page. Each invoice is listed in front of the redemption record.
</p>
<p>
If the import was successful, the New World Batch number need to be entered on the Export form and then clicking on "Update". When this is done, the export/import process is complete and no further action is needed.
</p>
<p>
In this page you will find most recent exports and their details in the table below the main export form.
</p>
<h3 id="searchBatch"> 2-8 Search Generated Batches</h3>
<p>
  To search for generated and printed MB's and GC's go to "Generate & Print" menu item.  In the middle of the page click on "For batch advance search click here". That link will take you to the batches search form. You can search type, date range, batch id or buck_id. After you enter the some of the parameter values, you would click on "Search". You will get the list of batches that match your search. In this form there is another button beside "Search" the "Printable Audit Sheet" button that will generate a pdf file that can be printed for Controller's employees to audit the printed MB and GC's.
</p>

<h3 id="audit"> 2-9 Printing Audit Sheet</h3>
<p>
refer to the previous section (2-8) for more details.
</p>

<h3 id="searchMB"> 2-10 Search Issued MB</h3>
<p>
To get to this page, first click on "Issue MB" menu item. In the middle of the page look for "For market bucks advance search click here". That link will take you to the search form. You can search by "Ebt ID" number, customer card number, amount, date range and so on.
</p>

<h3 id="searchGC"> 2-11 Search Issued GC</h3>
<p>
To get to this page, first click on "Issue GC" menu item. In the middle of the page look for "For gift certificates advance search click here". That link will take you to the search form. You can search by "Transaction ID" number, payment type, check number, amount, date range and so on.
</p>

<h3 id="search"> 2-12 Search Redemptions</h3>
<p>
<p>
To get to this page, first click on "Redemptions" menu item. In the middle of the page look for "For redemptions advance search click here". That link will take you to the search form. You can search by "Redemption ID" number, vendor name or number, status, payment type, date range and so on.
</p>


<h3 id="report"> 2-13 Reports </h3>
<p>
You can pick one or more of  the report types, such as "MB distributions", "GC distributions", "Redemptions", "Household participation stats" and "Inventory Stats". Other options may be added later as needed.
</p>
<p>
To generate daily report, you pick the date in the "Day" field and the click on "Submit".
</p>
<p>
To generate yearly report, you would pick the "Year" from the year list. Make sure the "Day" field is empty. Then click on Submit.
</p>
<p>
If you need to generate reports for certain period such week, month, quarter, .. etc, you need to specify the date range "Date From" and "Date to".  Make sure that the day and the year field are empty. Then click on the "Submit" button.
</p>

<h3><a href="training.action"> 3-Training </a></h3>

</div>
</main>
</body>
</html>
