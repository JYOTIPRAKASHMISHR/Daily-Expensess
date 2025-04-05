package com.example.dailyexpenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String message = intent.getStringExtra("sms_message");
            String phoneNumber = intent.getStringExtra("sms_phone");

            if (message != null && phoneNumber != null) {
                sendSms(phoneNumber, message);
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d(TAG, "SMS Sent: " + message);
        } catch (Exception e) {
            Log.e(TAG, "SMS Sending Failed: " + e.getMessage());
        }
    }
}
