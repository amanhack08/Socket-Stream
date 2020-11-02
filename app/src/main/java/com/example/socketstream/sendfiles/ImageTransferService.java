package com.example.socketstream.sendfiles;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ImageTransferService extends IntentService {

    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE="com.example.socketstream.sendfiles.SEND_FILE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public static final String MY_LIST="my_list";


    public ImageTransferService(){
        super("ImageTransferService");

    }

    public ImageTransferService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Context context = getApplicationContext();
        if (intent.getAction().equals(ACTION_SEND_FILE)) {

            ArrayList<Uri> fileUri = (ArrayList<Uri>) intent.getSerializableExtra(MY_LIST);
            String host = intent.getStringExtra(EXTRAS_GROUP_OWNER_ADDRESS);
            Socket socket = new Socket();
            int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
            try {
                socket.bind(null);
                socket.connect(new InetSocketAddress(host, 3333), SOCKET_TIMEOUT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            OutputStream stream = null;
            try {
                stream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataOutputStream dataOutputStream=new DataOutputStream(stream);
            try {
                dataOutputStream.writeInt(fileUri.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < fileUri.size(); i++) {


                    ContentResolver cr = context.getContentResolver();
                    InputStream is = null;
                    try {
                        is = cr.openInputStream(fileUri.get(i));
                    } catch (FileNotFoundException e) {

                    }
                    SendImages.copyFile(is, stream);
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                boolean h = socket.isConnected();
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // Give up
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
