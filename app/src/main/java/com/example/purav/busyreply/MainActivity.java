package com.example.purav.busyreply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {
    private TextView mReplyView;
    private Button mChangeButton;
    private ToggleButton mToggleReply;
    private Toolbar mToolbar;
    private String reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mReplyView = (TextView) findViewById(R.id.reply_text);
        mToggleReply = (ToggleButton) findViewById(R.id.reply_toggle);
        mChangeButton = (Button) findViewById(R.id.change_button);

        setSupportActionBar(mToolbar);

        final SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean serviceRunning = sharedPref.getBoolean("RUNNING", false);

        reply = sharedPref.getString("REPLY", "");
        if (!reply.equals(""))
            mReplyView.setText(reply);

        if (serviceRunning) {
            mReplyView.setEnabled(false);
            mToggleReply.setChecked(true);
            mReplyView.setText(reply);
        }

        final Intent serviceIntent = new Intent(this, SmsService.class);

        mToggleReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(serviceIntent);
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
                        sharedPref.edit().putBoolean("VIBRATE", true).commit();
                    else
                        sharedPref.edit().putBoolean("VIBRATE", false).commit();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                } else {
                    stopService(serviceIntent);
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (sharedPref.getBoolean("VIBRATE", false) == true)
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    else
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        });

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(v.getContext(), ReplyListActivity.class);
                startActivity(startIntent);
            }
        });
    }
}


