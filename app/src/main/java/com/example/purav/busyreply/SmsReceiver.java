package com.example.purav.busyreply;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        String reply = sharedPref.getString("REPLY", "");
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage newMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String senderNum = newMessage.getDisplayOriginatingAddress();

                    if (!reply.equals("")) {
                        smsManager.sendTextMessage(senderNum, null, reply, null, null);
                    }
                }
            }
        }
    }
}
