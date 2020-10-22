package com.example.socketstream;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SendTextStream extends AppCompatActivity {
    //send the input text to other permission transfer activity
    Button sendButton;

    //which take input from the users
    EditText inputText;

    String inputMessage=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_text_stream_activity);

        //initiliaze the EditText instance
        inputText=(EditText)findViewById(R.id.text_stream_input);



        sendButton=(Button)findViewById(R.id.send_button_text_stream);
        /*
        To handle click events on send button
         */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //get message from the input text
                inputMessage=inputText.getText().toString().trim();
                /*
                redirect to next activity if input text is not empty
                 */
                if(!inputMessage.equals("".trim())) {
                    Intent intent=new Intent(SendTextStream.this,PermissionRequiredTransfer.class);
                    intent.setData(Uri.parse("3"));
                    intent.putExtra("message",inputMessage);
                    startActivity(intent);
                }else{
                    Toast.makeText(SendTextStream.this, "Please enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
