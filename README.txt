**********************
Mohamed Ashraf Taha
900172754
Mobile Applications Development
Assignment 3
SMS Gateway
*********************


** Functionality
The SMS gateway is fully functional as described in the assignment handout


** How to use

** Start three Android Emulators
	1.  Client Application """Used to send SMS by providing phone number and message body"""
	2. SMS Gateway """Used to receive the SMS messages from the database and sending it to the CN (Cellular Network)
	3. Third Android Emulator will receive the SMS
	

** Functions in the Assignment

1. Client Application
	- Class MainActivity
		-- Methods:
			A. onCreate """Starts the app and calls APIHandler execute to do the logic"""

	- APIHandler """Thread 2"""
		-- Methods:	
			A. onPreExecute 
			B. doInBackground """ Responsible for the logic of inserting to the database"""
			C.onPostExecute
2. SMS Gateway
	- Class MainActivity
		-- Methods:
			A. onCreate """Starts the app and calls APIHandler execute to do the logic"""

	- APIHandler """Thread 2"""
		-- Methods:	
			A. onPreExecute 
			B. doInBackground """ Responsible for the logic of inserting and retrieveing from/to the database"""
			C.onPostExecute
			D. getSMS_APICaller """Responsible for consuming the API getSMS that retrieves the SMS from the DB"""
			E.smsSent_APICaller """Responsible for consuming the API smsSent that updates the SMS message status to SENT""""
	-Method smsPackageManager """Responsible for sending message to the other emulator"""
	- Method checkPermission """Handles permissions of smspackagemanager"""


** APIs

1. getSMS	""" Retrieve the SMS info from the DB"""
2. sendSMS """ Inserting SMS data record to DB"""
3. smsSent	""" Set the status of the sms to SENT"""

APIs are implemented using NodeJS and server Express
