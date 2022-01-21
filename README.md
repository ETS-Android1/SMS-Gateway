# SMS Gateway


# How to use

  * Start three Android Emulators
	  *  Client Application """Used to send SMS by providing phone number and message body"""
	  * SMS Gateway """Used to receive the SMS messages from the database and sending it to the CN (Cellular Network)
	  * Third Android Emulator will receive the SMS
	

 # Functions
 ## Frontend Using Java for Android development
  ### Client Application
	  Class MainActivity
		  - Methods:
			  - onCreate "Starts the app and calls APIHandler execute to do the logic"

	  Class APIHandler 
		  - Methods:	
			  A. onPreExecute 
			  B. doInBackground "Responsible for the logic of inserting to the database"
			  C. onPostExecute
  ### SMS Gateway
	   Class MainActivity "Responsible for the UI Thread"
		  - Methods:
			  - onCreate "Starts the app and calls APIHandler execute to do the logic"

	  Class APIHandler "Responsible for the background thread"
		 - Methods:	
			A. onPreExecute 
			B. doInBackground "Responsible for the logic of inserting and retrieveing from/to the database "
			C. onPostExecute
			D. getSMS_APICaller "Responsible for consuming the API getSMS that retrieves the SMS from the DB"
			E. smsSent_APICaller "Responsible for consuming the API smsSent that updates the SMS message status to SENT"
	    F. Method smsPackageManager "Responsible for sending message to the other emulator"
	    E. checkPermission "Handles permissions of smspackagemanager"

  ## Backend using Nodejs/MySQL
  ### APIs
    A. getSMS	" Retrieve the SMS info from the DB"
    B. sendSMS " Inserting SMS data record to DB"
    C. smsSent	" Set the status of the sms to SENT"
