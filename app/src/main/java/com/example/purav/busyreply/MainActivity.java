package com.example.purav.busyreply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private TextView replyView;
    private Button changeButton;
    private Switch toggleReply;
    private Toolbar mToolbar;
    private String reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        replyView = (TextView) findViewById(R.id.reply_text);
        toggleReply = (Switch) findViewById(R.id.reply_toggle);
        changeButton = (Button) findViewById(R.id.change_button);

        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.darker_navy_blue));
        }

        final SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean serviceRunning = sharedPref.getBoolean("RUNNING", false);

        reply = sharedPref.getString("REPLY", "");
        if (!reply.equals(""))
            replyView.setText(reply);

        if (serviceRunning) {
            replyView.setEnabled(false);
            toggleReply.setChecked(true);
            replyView.setText(reply);
        }

        final Intent serviceIntent = new Intent(this, SmsService.class);

        toggleReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(serviceIntent);
                } else {
                    stopService(serviceIntent);
                }
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(v.getContext(), ReplyListActivity.class);
                startActivity(startIntent);
            }
        });
    }
}


