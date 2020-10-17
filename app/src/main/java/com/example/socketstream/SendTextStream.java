package com.example.socketstream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SendTextStream extends AppCompatActivity {
    Button sendButton;
    EditText inputText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_text_stream_activity);
        inputText=(EditText)findViewById(R.id.text_stream_input);
        sendButton=(Button)findViewById(R.id.send_button_text_stream);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SendTextStream.this,PermissionRequiredTransfer.class);
                startActivity(intent);
            }
        });
    }
}
