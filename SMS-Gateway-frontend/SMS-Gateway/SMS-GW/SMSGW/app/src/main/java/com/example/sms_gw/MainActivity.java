package com.example.sms_gw;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.view.Gravity;
import  android.view.ViewGroup.LayoutParams.*;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.*;

import android.widget.*;

import org.json.*;

public class MainActivity extends AppCompatActivity {
    private String id ;
    private String phoneNumber ;
    private String MessageBody ;
    TextView tv;
    final private String deviceIP = "192.168.1.10";
    final private String EmulatorIP = "10.0.2.2";
    final private  String portNo = "3000";
    private int counter =0;
    final private  Boolean startPressed = true;
    private  APIHandler apihandle = new APIHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout text = new LinearLayout(this);
        text.setOrientation(LinearLayout.VERTICAL);
        tv = new TextView(this);
        LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
        tv.setBackgroundColor(Color.WHITE);
        tv.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        tv.setLayoutParams(Textparams);
        tv.setText("Sent SMS Messages: ");
        tv.setTextColor(Color.rgb(0,0,0));
        text.setBackgroundColor(Color.rgb(255,255,255));
        text.addView(tv);


        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button startButton= new Button(this);
        Button stopButton = new Button(this);
        buttons.setBackgroundColor(Color.WHITE);
        startButton.setBackgroundColor(Color.rgb(235,235,235));
        stopButton.setBackgroundColor(Color.rgb(235,235,235));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);

        LinearLayout.LayoutParams Buttonsparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams Buttonsparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);

        Buttonsparams.setMargins(130,20,130,20);
        Buttonsparams2.setMargins(130,20,130,20);
        Buttonsparams.gravity = Gravity.BOTTOM;
        Buttonsparams2.gravity = Gravity.BOTTOM;



        // buttons customization
        startButton.setLayoutParams(Buttonsparams);
        startButton.setBackground(getDrawable(R.drawable.rounded));
        stopButton.setLayoutParams(Buttonsparams2);
        stopButton.setBackground(getDrawable(R.drawable.rounded));

        buttons.setLayoutParams(params);
        startButton.setText("Start");
        startButton.setGravity(Gravity.CENTER);
        stopButton.setText("Stop");
        stopButton.setGravity(Gravity.CENTER);

        startButton.setTextColor(Color.BLACK);
        stopButton.setTextColor(Color.BLACK);
        buttons.addView(startButton);
        buttons.addView(stopButton);

        text.addView(buttons);
        setContentView(text);

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                apihandle = new APIHandler();
                apihandle.execute("http://"+EmulatorIP+":"+portNo+"/getSMS");

            }});

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                apihandle.cancel(true);
            }
        });

        if(checkPermission(Manifest.permission.SEND_SMS)){
            Log.e("ok", "okay");
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }

    }
//########################################################
    private  class APIHandler extends AsyncTask<String,String,String> {
        private JSONObject SMSdata = new JSONObject();
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            String ID;

            while(!isCancelled()) {


                // consume the get SMS API
                 getSMS_APICaller(params[0]);
                // send the actual sms


                // consume the smsSent API
                // extract the id from the JSON returned from the getSMS
                smsSent_APICaller();

                //update the counter and send the actual sms to the android emulator
                try {
                    publishProgress(String.valueOf(counter), SMSdata.getString("phone").toString(), SMSdata.getString("body").toString());
                }catch (org.json.JSONException e) {}



                // delay for 5 seconds
                SystemClock.sleep(5000);
            }
            return null;
        }
//#####################################
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
//#####################


        @Override
        protected void onProgressUpdate(String... values) {
            tv.setText("Sent SMS Messages: "+ counter);
            Log.d("Test", values[1]);
            Log.d("Test", values[2]);
            smsPackageManager_Handler(values[1],values[2]);

        }

//###################################################
        void getSMS_APICaller(String api_url){
            //  """ This method is responsible for retrieving data from the getSMS API """
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            Boolean NonEmptySMS = false;

         //   while(!NonEmptySMS) {
                try {
                    URL url = new URL(api_url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        Log.d("Full JSON file", line);
                    }
                    JSONObject JReader = new JSONObject(buffer.toString());
                    JSONArray JArray = JReader.getJSONArray("result");
                    SMSdata = JArray.getJSONObject(0);
                    Log.d("Status", SMSdata.toString());
                    counter++;
                    Log.i("ID", SMSdata.getString("id"));
                    Log.i("Phone", SMSdata.getString("phone"));
                    Log.i("body", SMSdata.getString("body"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("HERE", "doInBackground: Inside Exception ");
                    e.printStackTrace();
                } catch (JSONException e) {

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           // }
        }

//####################################
        void smsSent_APICaller(){
            //"""This method is responsible for setting the sms sent status to 1 marking it as sent"""
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String ID;

            try {
                ID = SMSdata.getString("id").toString();
                URL url = new URL("http://"+EmulatorIP+":"+portNo+"/smsSent");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.connect();

                // JSONObject to send
                JSONObject dataTosend = new JSONObject();
                dataTosend.put("smsID",Integer.parseInt(ID));
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(dataTosend.toString());
                os.flush();
                os.close();
                Log.d("Debug data to send", dataTosend.toString());
                Log.d("Status", String.valueOf(connection.getResponseCode()));
                Log.d("ResponseBody", connection.getResponseMessage());


                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("HERE", "doInBackground: Inside Exception ");
                e.printStackTrace();
            }catch (org.json.JSONException e){

            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

//###################################
private  void smsPackageManager_Handler(String phoneNumber, String message){
    //""" This method is used to send actual SMS messages retrieved from the database"""
    Log.d("SMS pm test","HERE");
    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    Toast.makeText(MainActivity.this, "A New Message Sent", Toast.LENGTH_SHORT).show();


}
//#############################

public boolean checkPermission(String permission){
    int check = ContextCompat.checkSelfPermission(this, permission);
    return (check == PackageManager.PERMISSION_GRANTED);
}



}
//######################################

