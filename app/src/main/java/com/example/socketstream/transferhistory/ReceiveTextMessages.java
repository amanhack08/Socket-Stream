package com.example.socketstream.transferhistory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socketstream.R;
import com.example.socketstream.database.TransferHistoryDbHelper;

import java.util.ArrayList;
import com.example.socketstream.database.TransferHistoryContract.ReceiveTextMessage;

public class ReceiveTextMessages extends AppCompatActivity {
    ArrayList<String> allTextMessages;
    String insertMessage=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_text_msg_wifi_direct);
        allTextMessages=new ArrayList<>();
        Intent intent=getIntent();
        if(intent.getData()!=null&&intent.getData().toString().equals("0")){
            insertMessage=intent.getStringExtra("message");
            insertInDatabase();
        }
        displayAllDataBaseInfo();

    }

    public void displayAllDataBaseInfo(){
        TransferHistoryDbHelper transferHistoryDbHelper=new TransferHistoryDbHelper(this);
        SQLiteDatabase database=transferHistoryDbHelper.getReadableDatabase();
        Cursor cursor =database.rawQuery("SELECT * FROM "+ReceiveTextMessage.RECEIVE_TEXT_TABLE_NAME,null);
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                String textMessage=cursor.getString(cursor.getColumnIndexOrThrow(ReceiveTextMessage.RECEIVE_TEXT_MESSAGE_COLUMN));
                allTextMessages.add(textMessage);
            }while(cursor.moveToNext());
        }
        cursor.close();

        ListView listView=(ListView)findViewById(R.id.receive_text_list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,allTextMessages);
        listView.setAdapter(adapter);
    }

    public void insertInDatabase(){
        TransferHistoryDbHelper transferHistoryDbHelper=new TransferHistoryDbHelper(this);
        SQLiteDatabase database=transferHistoryDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReceiveTextMessage.RECEIVE_TEXT_MESSAGE_COLUMN,insertMessage);
        database.insert(ReceiveTextMessage.RECEIVE_TEXT_TABLE_NAME,null,contentValues);
    }
}
