package com.example.purav.busyreply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import static com.example.purav.busyreply.MySQLiteHelper.COLUMN_ID;
import static com.example.purav.busyreply.MySQLiteHelper.COLUMN_REPLY;
import static com.example.purav.busyreply.MySQLiteHelper.TABLE_REPLY;

/**
 * Created by Purav on 2/9/2015.
 */
public class ReplyDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper helper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_REPLY};

    public ReplyDataSource(Context context) {
        helper = new MySQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Reply creatReply(String reply) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_REPLY, reply);
        long insertId = database.insert(TABLE_REPLY, null, values);
        Cursor cursor = database.query(TABLE_REPLY, allColumns, COLUMN_ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Reply newReply = new Reply(insertId, reply);
        cursor.close();
        return newReply;
    }

    public void deleteReply(Reply reply) {
        long id = reply.getId();
        database.delete(TABLE_REPLY, COLUMN_ID + " = " + id, null);
    }

    public Reply updateReply(Reply replyToEdit, String newText) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPLY, newText);

        database.update(TABLE_REPLY, values, COLUMN_ID + " = ?", new String[]{replyToEdit.getId() + ""});
        return new Reply(replyToEdit.getId(), newText);
    }

    public List<Reply> getAllReplies() {
        List<Reply> replies = new ArrayList<Reply>();
        Cursor cursor = database.query(TABLE_REPLY, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Reply reply = cursorToReply(cursor);
            replies.add(reply);
            cursor.moveToNext();
        }

        cursor.close();
        return replies;
    }

    private Reply cursorToReply(Cursor cursor) {
        Reply reply = new Reply();
        reply.setId(cursor.getLong(0));
        reply.setReply(cursor.getString(1));
        return reply;
    }
}
