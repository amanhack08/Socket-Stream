package com.example.socketstream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socketstream.transferhistory.ReceiveImages;
import com.example.socketstream.transferhistory.ReceiveTextMessages;

public class TransferFileHistoryActivity extends AppCompatActivity {
    TextView receiveTextMessageHistory,receiveImagesHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_history_activity);
        receiveTextMessageHistory=(TextView)findViewById(R.id.receive_text_message_history);
        receiveImagesHistory=(TextView)findViewById(R.id.receive_images_history);
        setClickListner();
    }

    public void setClickListner(){
        receiveTextMessageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TransferFileHistoryActivity.this, ReceiveTextMessages.class);
                startActivity(intent);
            }
        });

        receiveImagesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TransferFileHistoryActivity.this, ReceiveImages.class);
                startActivity(intent);
            }
        });
    }
}
