package com.example.purav.busyreply;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class SmsService extends Service {
    private IntentFilter filter;
    private SharedPreferences sharedPref;
    private SmsReceiver smsReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        filter = new IntentFilter();
        filter.addAction(SmsReceiver.SMS_RECEIVED);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        smsReceiver = new SmsReceiver();
        registerReceiver(smsReceiver, filter);
        setActive(true);
        return super.onStartCommand(intent, START_FLAG_REDELIVERY, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        setActive(false);
        super.onDestroy();
    }

    public void setActive(boolean active) {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("RUNNING", active);
        editor.apply();
    }
}
