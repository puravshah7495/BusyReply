package com.example.purav.busyreply;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
    private Button startButton;
    private Button stopButton;
    private EditText replyField;
    private String reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        replyField = (EditText) findViewById(R.id.reply_text);

        final SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean serviceRunning = sharedPref.getBoolean("RUNNING", false);

        if (serviceRunning) {
            stopButton.setEnabled(true);
            replyField.setEnabled(false);
            startButton.setEnabled(false);
            reply = sharedPref.getString("REPLY", "");
            replyField.setText(reply);
        }

        final Intent serviceIntent = new Intent(this, SmsService.class);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply = replyField.getText().toString();

                replyField.setEnabled(false);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);

                sharedPref.edit().putString("REPLY", reply).apply();
                v.getContext().startService(serviceIntent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                //return to original view
                stopButton.setEnabled(false);
                replyField.setEnabled(true);
                startButton.setEnabled(true);
            }
        });
    }
}


