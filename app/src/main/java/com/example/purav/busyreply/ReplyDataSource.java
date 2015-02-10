package com.example.purav.busyreply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

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
        long insertId = database.insert(MySQLiteHelper.TABLE_REPLY, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPLY, allColumns, MySQLiteHelper.COLUMN_ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Reply newReply = new Reply(insertId, reply);
        cursor.close();
        return newReply;
    }

    public void deleteReply(Reply reply) {
        long id = reply.getId();
        database.delete(MySQLiteHelper.TABLE_REPLY, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Reply> getAllReplies() {
        List<Reply> replies = new ArrayList<Reply>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPLY, allColumns, null, null, null, null, null);
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
