<%@  include file="header.jsp" %>
<%@ page session="false" %>
<!--  
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 *
	-->
<h2>Marketbucks Training Guide</h2>

<h3>Content Table</h3>
<ul>
  <li><a href="#config"> 1 Configuration </a></li>
  <li><a href="#generate"> 2 Generate & Print </a></li>
  <li><a href="#issue_mb"> 3 Issue MB </a></li>
  <li><a href="#issue_gc"> 4 Issue GC </a></li>
  <li><a href="#redeem"> 5 Redemptions </a></li>
</ul>

<p>
Before we start a new training, please contact the Auther (Walid Sibo, Tel:3575) at IT department to prepare the system for new training.
</p>

<h3 id="config"> 1-Configuration (You can skip) </h3>
<p>
It is recommended to skip this section as we added the needed configurations.
Therefore we are showing this for reference purpose only.
</p>
<p>
Here will assume the system is empty and no configurations were entered. 
In this training we need three users to create three records, one for MB, the second for GC 5 and the third GC 20. Dates and years can be changed. We use them here for illustration purpose.
</p>
<p>
For the first user 1:
<ul>
  <li> Name: MB 2015 $3 (for MB) </li>
  <li> Type: MB 3 </li>
  <li> Face Value: 3 </li>
  <li> Max Donation: 18 </li>
  <li> GL Account: 201-18-186503-47240 </li>
  <li> Expire Date: 12/15/2016 </li>
</ul>
For the first user 2:
<ul>
  <li> Name: GC 2015 $5 (for GC) </li>
  <li> Type: GC 5 </li>
  <li> Face Value: 5 </li>
  <li> Max Donation: 0 </li>
  <li> GL Account: 201-18-186503-47230 </li>
  <li> Expire Date: 12/15/2016 </li>
</ul>

For the first user 3:
<ul>
  <li> Name: GC 2015 $20 (for GC) </li>
  <li> Type: GC 20 </li>
  <li> Face Value: 20 </li>
  <li> Max Donation: 0 </li>
  <li> GL Account: 201-18-186503-47230 </li>
  <li> Expire Date: 12/15/2016 </li>
</ul>
We may add more but these three are good enough.
</p>

<h3 id="generate"> 2-Generate & Print (You can skip) </h3>
<p>
If this section is skipped, we will assume that we have printed a bunch of MB and GC to be used in the next sections.
</p>
<p>
Assuming we successfully added at least the three configurations we needed in the previous exercise. Now we can generate and print MB and GC.
</p>
<p>
Depending on the number of users, we assume we have three groups of users:
The first group will generate MB. We will assign numbers that each user will use.
</p>
<p>
The sequences use below are for illustration purpose only, you may need to use different sequences if you got in conflict.
</p>
<p>
Group 1, user 1: MB $3 seq: 3001-3020 <br />
Group 1, user 2: MB $3 seq: 3101-3120 <br />
Group 1, user 3: MB $3 seq: 3201-3220 <br />
..
</p>
<p>
This group will click on "Generate & Print" menu option, and pick the corresponding configuration that is designed for MB $3 value and click on "Generate New Batch". Now enter the batch size=20, and change the start seq to the sequence assigned to you and then hit "Next". Next you would click on "Printable Certificates". A new dialogue window will appear to ask you to "Save" or Open. You can click on Open. This is a pdf file that you can print. There will be 3 bucks per page.
</p>
<p>
After printing, now you can confirm the last number you got. <br />


Group 2 will print GC with face value of $5 <br />
Group 2, user 1: GC $5 seq: 5001-5020 <br />
Group 2, user 2: GC $5 seq: 5101-5120 <br />
..
</p>
<p>
Similar to the previous group, except we are print $20 GC<br />

Group 3 will print GC with face value of $20 <br />
Group 3, user 1: GC $20 seq: 2001-2010 <br />
Group 3, user 2: GC $20 seq: 2101-2120 <br />
..

</p>
<p>
Similar to the previous group, except we are printing GC
</p>

<h3 id="issue_mb"> 3-Issue MB's </h3>
<p>
When a customer asks for MB's he/she will provide an EBT card. The user will ask for amount in $, the amount must be multiple of $3 as was set in the configuration page. We will assume that the max DMB amount was set to $18 only per customer.
</p>
<p>
In this exercise we assume having three customers, <br />
Customer 1, request amount: $15 (the total bucks should be $30) <br />
Customer 2, request amount: $18 (the total bucks should be $36) <br />
Customer 3, request amount: $21 (the total bucks should be $39) <br />

</p>
<p>
So each user will pick one of the customers and try to fulfill his/her request using the bucks that he/she received.
</p>
<p>
Each user will enter the amount requested, either 15, 18, or 21. <br />
Then enter any values for "Approve #", and "Customer Card #" and click on "Next". <br />
Now each user will scan a number of bucks till each request is completed.
</p>

<h3 id="issue_gc"> 4-Issue GC's </h3> 
<p>
To issue GC, the customer will ask for certain amount of GC. It has to be a multiple of 5, such as 5, 10, 15, 20, 25, ... The user will pick the method of payment (Cash, check, etc), and enter the check number or RecTrac number.
</p>
<p>
So we will assume having two customers: <br />

Customer 1: request amount:$45 <br />
Customer 2: request amount:$25 <br />
<br />

One user will handle one customer, <br />
User 1 will enter 45 for the amount, and  pick cash for example, then click on "Next" <br />
Now he/she will scan 2x$20 of GC and one $5 GC <br />
</p>

<p>
Another user: will handle customer two. <br />
User 2 will enter $25 for amount, and pick "check" as method of payment and enter 123133 for check no. and click on "Next". The user then scans one $20 GC and one $5 GC.
</p>

<h3 id="redeem"> 5-Redemptions</h3>
<p>
So we have $30, $36 and $39 of MB issued. And we have $45 and $25 GC issued. Total of $105 MB and $70 GC can be redeemed.
</p>
<p>
We will assume we have two vendors that they want to redeem their MB or GC.<br />

Vendor 1: Hunter's Honey Farm, vendor number:52276 <br />
Vendor 2: Freedom Valley Farm: vendor number:3898 <br />
<br />
One user will handle vendor 1, and another will handle vendor 2. <br />
<br />
The first user will click on "Redemption" menu item.<br />

The user will enter either "52276" in the vendor number field, or start typing vendor name in the vendor name field to pick from the list. <br />
Now the user will start scanning the MB or/and GC that are handed to him. <br />
Depending on the GC values, each time a MB or GC is scanned, the total value will change, till no more bucks are available.  <br />
Then, the user will click on "Finalize" to complete the transaction. <br />
Now the user can print the invoice by clicking on "Generate Invoice". <br />
The user will print two of these, one will be handed to the vendor and the other will be kept with bucks.
</p>


</div>
</main>
</body>
</html>
