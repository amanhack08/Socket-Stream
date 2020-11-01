package com.example.socketstream.transferhistory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.socketstream.database.ReceiveImagesRecyclerView;
import com.example.socketstream.database.TransferHistoryContract;
import com.example.socketstream.database.TransferHistoryContract.ReceiveImages1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;
import com.example.socketstream.database.TransferHistoryDbHelper;
import com.example.socketstream.recyclerView.SendImageRecyclerView;

import java.util.ArrayList;

public class ReceiveImages extends AppCompatActivity {

    ArrayList<Uri> imagesUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_images_history);
        Intent intent=getIntent();
        if(intent.getData()!=null){
            ArrayList<Uri> getAllFilesUri=(ArrayList<Uri>)intent.getSerializableExtra("mylist");
            for(int i=0;i<getAllFilesUri.size();i++)
                  insertInDatabase(getAllFilesUri.get(i));
        }
        imagesUri=new ArrayList<>();
        displayDatabaseInfo();
        ReceiveImagesRecyclerView receiveImagesRecyclerView=new ReceiveImagesRecyclerView(imagesUri,getContentResolver());
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.receive_images_tranfer_history);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        recyclerView.setAdapter(receiveImagesRecyclerView);
    }

    private void insertInDatabase(Uri data){
        TransferHistoryDbHelper transferHistoryDbHelper=new TransferHistoryDbHelper(this);
        SQLiteDatabase database=transferHistoryDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReceiveImages1.RECEIVE_IMAGES_COLUMN_URI,data.toString());
        database.insert(ReceiveImages1.RECEIVE_IMAGES_TABLE_NAME,null,contentValues);
    }

    private void displayDatabaseInfo(){
        TransferHistoryDbHelper transferHistoryDbHelper=new TransferHistoryDbHelper(this);
        SQLiteDatabase database=transferHistoryDbHelper.getReadableDatabase();
        Cursor cursor =database.rawQuery("SELECT * FROM "+ ReceiveImages1.RECEIVE_IMAGES_TABLE_NAME,null);
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                String imageUri=cursor.getString(cursor.getColumnIndexOrThrow(ReceiveImages1.RECEIVE_IMAGES_COLUMN_URI));
                imagesUri.add(Uri.parse(imageUri));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }



}
