package com.example.purav.busyreply;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReplyListActivity extends ListActivity {
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> replyList;
    private FileInputStream fileInput;
    private FileOutputStream fileOutput;
    private final String FILENAME = "reply_list.txt";
    private String newReplyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView listView = getListView();
        final SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());

        replyList = new ArrayList<String>();
        replyList.add("Sorry, I'm busy right now. I will call you back later");
        readFile();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
                replyList);
        setListAdapter(listAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        String reply = sharedPreferences.getString("REPLY", "");
        if (replyList.size() == 1 || reply.equals("")) {
            listView.setItemChecked(0, true);
        } else {
            int replyIndex = replyList.indexOf(reply);
            listView.setItemChecked(replyIndex, true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                sharedPreferences.edit().putString("REPLY", replyList.get(position)).apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        menu.getItem(0).setIcon(android.R.drawable.ic_menu_add);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            promptInput();
        }

        return super.onOptionsItemSelected(item);
    }

    public void readFile() {
        String lineRead;
        try {
            fileInput = openFileInput(FILENAME);
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            while ((lineRead = bufferedReader.readLine()) != null) {
                replyList.add(lineRead);
            }
            fileInput.close();
            streamReader.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            File file = new File(getFilesDir(), FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String s) {
        Log.w("file path", getFilesDir().getAbsolutePath());
        try {
            fileOutput = openFileOutput(FILENAME, MODE_APPEND);
            fileOutput.write(s.getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                newReplyText = newReplyText + "\n";
                replyList.add(newReplyText);
                listAdapter.notifyDataSetChanged();
                writeToFile(newReplyText);
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
}
