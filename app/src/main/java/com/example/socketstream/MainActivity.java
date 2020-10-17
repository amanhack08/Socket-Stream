package com.example.socketstream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView transferFileHistory;
    Button sendButton,receiveButton,sendTextInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        To initialize all the objects
         */
        initializeAllObjects();

        runTimePermission();

        /*
        to create an event listner
         */
        onClickEvents();
    }

    public void runTimePermission(){
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                0);
    }

    /*
    function to initialize all objects
     */
    private void initializeAllObjects(){
        transferFileHistory=(TextView)findViewById(R.id.transfer_history_textView);
        sendButton=(Button)findViewById(R.id.sendButton);
        receiveButton=(Button)findViewById(R.id.receive_button);
        sendTextInputStream=(Button)findViewById(R.id.text_stream);
    }

    /*
    function to listen click events
     */
    private void onClickEvents(){
        //transfer files stored here
        transferFileHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TransferFileHistoryActivity.class);
                startActivity(intent);
            }
        });

        //send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SendFilesViewPager.class);
                startActivity(intent);
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PermissionRequiredTransfer.class);
                intent.setData(Uri.parse("0"));
                startActivity(intent);
            }
        });

        sendTextInputStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SendTextStream.class);
                startActivity(intent);
            }
        });
    }
}