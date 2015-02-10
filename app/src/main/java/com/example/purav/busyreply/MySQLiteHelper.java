package com.example.purav.busyreply;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Purav on 2/9/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_REPLY = "replies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REPLY = "reply";

    private static final String DATABASE_NAME = "replies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_REPLY + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_REPLY
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_REPLY, "Sorry, I'm busy right now. I will call you back later");
        db.insert(TABLE_REPLY, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_REPLY);
        onCreate(db);
    }
}
