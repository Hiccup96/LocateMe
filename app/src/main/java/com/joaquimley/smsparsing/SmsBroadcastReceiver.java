package com.joaquimley.smsparsing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";
    LocationTrack locationTrack;
    Context mContext;
    String lat,lang;

    @Override
    public void onReceive(Context context, Intent intent) {
         mContext=context;
         if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody += smsMessage.getMessageBody();
                smsSender+=smsMessage.getDisplayOriginatingAddress();
            }

            if ((smsSender.contains("9986515800") || smsSender.contains("9483568220") || smsSender.contains("8073980373") || smsSender.contains("9481678718") || smsSender.contains("8618584139"))&& smsBody.contains("Locate")) {
                Log.d(TAG, "SMS detected: From " + smsSender + " With text " + smsBody);
                locationTrack = new LocationTrack(context);
                if (locationTrack.canGetLocation()) {
                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    lat = Double.toString(latitude);
                    lang = Double.toString(longitude);
                    Toast.makeText(context, "Longitude:" + lat + "\nLatitude:" + lang, Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Longitude:" + lat+ "\nLatitude:" + lang);
                    //https://www.google.com/maps/@15.3673682,75.142887,19z
                    String link = "https://www.google.com/maps/@"+lat+","+lang+","+"18z";
                    sendSMS(smsSender,link);

                }
            }

        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(mContext, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(mContext,ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
