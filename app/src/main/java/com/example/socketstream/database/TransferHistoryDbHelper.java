package com.example.socketstream.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.socketstream.database.TransferHistoryContract.ReceiveTextMessage;
import com.example.socketstream.database.TransferHistoryContract.ReceiveImages1;

public class TransferHistoryDbHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME="TransferHistory.db";
    final static int DATABASE_VERSION=1;
    public TransferHistoryDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the receive text table
        String SQL_CREATE_RECEIVE_TEXT_TABLE =  "CREATE TABLE " + ReceiveTextMessage.RECEIVE_TEXT_TABLE_NAME + " ("
                + ReceiveTextMessage.RECEIVE_TEXT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReceiveTextMessage.RECEIVE_TEXT_MESSAGE_COLUMN + " TEXT NOT NULL);"
              ;

        // Create a String that contains the SQL statement to create the received images table
        String SQL_CREATE_RECEIVE_IMAGES_TABLE =  "CREATE TABLE " + ReceiveImages1.RECEIVE_IMAGES_TABLE_NAME + " ("
                + ReceiveImages1.RECEIVE_IMAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReceiveImages1.RECEIVE_IMAGES_COLUMN_URI + " TEXT NOT NULL);"
                ;

        //create tables
        sqLiteDatabase.execSQL(SQL_CREATE_RECEIVE_TEXT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RECEIVE_IMAGES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
