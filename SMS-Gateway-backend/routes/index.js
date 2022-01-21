// We now import the connection object we exported in db.js.

const db = require("../controllers/db");

// More libraries…
const express = require("express");
const bodyParser = require("body-parser");
const {
    query
} = require("express");
const {
    restart
} = require("nodemon");
const {
    send
} = require("process");
const {
    STATUS_CODES
} = require("http");
const connection = require("../controllers/db");

const router = express.Router();

router.use(bodyParser.json()); // Automatically parse all POSTs as JSON.
router.use(bodyParser.urlencoded({
    extended: true
})); // Automatically parse URL parameters


//  API sendSMS used to add the sms data to the DB
// SMS Data {
//     "phone": "01xxxxxxx",
//     "message_body": "Hey",
// }


//######################################################


/* ********************** */
// Assignment APIs
/* ********************** */



router.post("/sendSMS", function (req, res) {
    //      """ This API add the sms info as a record to the DB  """

    // get the data in the body 


    // POST /1.1 sendSMS
    // BODY : phone number , message


    let {phone,MsgBody} = req.body;

    // insert quert to add the SMS data to the database
    let sqlQuery = "INSERT INTO smsData(phone,body,smsStatus) VALUES (";
    sqlQuery = sqlQuery + "\"" + phone + "\", \"" + MsgBody + "\", 0" + ");";
   
    db.query(sqlQuery, function (err, result) {
        if (err) {
            res.send(err)
            throw err;
        } else {
            // for debugging
            console.log("Result: ", JSON.stringify(result));

            try {
                let data = {
                    "Status": "Success",
                    result
                };
                res.json(data);
            } catch (exception) {
                res.status(500).json(JSON.stringify(exception));
            }
        }
    });
});

//####################################################

router.get("/getSMS", function (req, res) {

    //      """ This API gets all sms that have not been sent and the latest based on the time stamp"""

    let notSent = 0;
    let sqlQuery = "SELECT * FROM smsData WHERE smsStatus = ? ORDER BY _timestamp LIMIT 1;";

    db.query(sqlQuery, [notSent], function (err, result) {

            if (err) {
                res.send({
                    "Status": "Failed",
                    err
                });
                throw err;
            } else {
                // for debugging
                console.log("Result: ", JSON.stringify(result));
                try {
                    let data = {
                        "Status": "Success",
                        result
                    };
                    res.json(data);
                } catch (exception) {
                    res.status(500).json(JSON.stringify(exception));
                }
            }
        }

    );
});

//####################################################

router.post("/smsSent", function (req, res) {
    let body = req.body;


    let smsID = body.smsID;

    let sqlQuery = "UPDATE smsData set smsStatus = 1 WHERE id = ?";

    db.query(sqlQuery, [smsID], function (err, result) {
        if (err) {

            let errMgs = err;
            res.json(errMgs);
            throw err;
        } else {
            // debugging
            console.log("Result", JSON.stringify(result));
            
            try{
            let data = {
                "Status": "Success",
                result
            }
            res.json(data);
            }
            catch(exception){
                res.status(500).json(JSON.stringify(exception));
            }
        }
    });

});


/* ********************** */
// My APIs to test
/* ********************** */

router.get("/getAllsms", function (req, res) {

    // """ This API gets all the SMS from the database  """

    sqlQuery = "SELECT * FROM smsData";

    db.query(sqlQuery, function (err, result) {

        if (err) {
            res.send(err);
            throw err;
        } else {
            console.log("Result", JSON.stringify(result));
       
            try {
                let data = {
                    "Status": "Success",
                    result
                };
                res.json(data);
            } catch (exception) {
                res.status(500).json(JSON.stringify(exception));
            }
        }
    })


});
router.get("/deleteAllSMS", function (req, res) {

    // """ This API deletes all the SMS from the database  """

    sqlQuery = "DELETE FROM smsData";

    db.query(sqlQuery, function (err, result) {

        if (err) {
            res.send(err);
            throw err;
        } else {

            res.json({
                "Status": "Success",
                result
            });
        }
    })


});


// // Skeleton for POST request
// router.post("/mypostapi", function (req, res) {
//     let sql = `
//         Your SQL Query Here
//     `;
//     db.query(sql, function (err, result) {
//         console.log("Result: " + JSON.stringify(result));
//         if (err) {
//             return res.send(err);
//         } else {
//             let returnedObject = {};
//             // Your code here
//             return res.json(returnedObject);
//         }
//     });
// });

// // Skeleton for GET Request
// router.get("/mygetapi", function (req, res) {
//     let sql = `
//         Your SQL Query Here
//     `;
//     db.query(sql, function (err, result) {
//         console.log("Result: " + JSON.stringify(result));
//         if (err) {
//             return res.send(err);
//         } else {
//             let returnedObject = {};
//             // Your code here 
//             // You can use res.json(result); to send all data as a response 
//             return res.json(returnedObject);
//         }
//     });
// });

// ---




// Export the created router
module.exports = router;