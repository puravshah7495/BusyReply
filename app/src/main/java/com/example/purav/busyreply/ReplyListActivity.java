package com.example.purav.busyreply;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReplyListActivity extends ActionBarActivity {
    private ArrayAdapter<Reply> listAdapter;
    private ArrayList<Reply> replyList;
    private String newReplyText;
    private SharedPreferences sharedPreferences;
    private Toolbar mToolbar;
    private ListView listView;

    private ReplyDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.reply_list);

        replyList = new ArrayList<Reply>();
        final SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        //readFile();
        dataSource = new ReplyDataSource(this);
        dataSource.open();
        replyList = (ArrayList<Reply>) dataSource.getAllReplies();
        dataSource.close();

        listAdapter = new ArrayAdapter<Reply>(this, android.R.layout.simple_list_item_single_choice,
                replyList);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        String reply = sharedPreferences.getString("REPLY", "");
        if (replyList.size() == 1 || reply.equals("")) {
            listView.setItemChecked(0, true);
        } else {
            int replyIndex = sharedPreferences.getInt("INDEX", 0);
            listView.setItemChecked(replyIndex, true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                sharedPreferences.edit().putString("REPLY", replyList.get(position).toString()).apply();
                sharedPreferences.edit().putInt("INDEX", position).apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        menu.getItem(0).setIcon(android.R.drawable.ic_menu_add);
        menu.getItem(1).setIcon(android.R.drawable.ic_menu_delete);

        if (replyList.size() == 1) {
            menu.getItem(1).setEnabled(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            promptInput();
        }

        if (id == R.id.menu_delete) {
            removeReply();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (replyList.size() == 1) {
            menu.getItem(1).setEnabled(false);
        } else {
            menu.getItem(1).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void promptInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Reply");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newReplyText = input.getText().toString();
                if (!newReplyText.isEmpty()) {
                    dataSource.open();
                    replyList.add(dataSource.creatReply(newReplyText));
                    listAdapter.notifyDataSetChanged();
                    dataSource.close();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void removeReply() {
        if (replyList.size() == 1) {
            Toast.makeText(this, "Must have at least 1 reply", Toast.LENGTH_SHORT).show();
            return;
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int removeIndex = sharedPreferences.getInt("INDEX", 0);
        Reply replyToDelete = replyList.get(removeIndex);
        replyList.remove(removeIndex);
        listAdapter.notifyDataSetChanged();
        listView.setItemChecked(removeIndex - 1, true);

        sharedPreferences.edit().putString("REPLY", replyList.get(removeIndex - 1).toString()).apply();
        sharedPreferences.edit().putInt("INDEX", removeIndex - 1).apply();

        dataSource.open();
        dataSource.deleteReply(replyToDelete);
        dataSource.close();
    }
}
