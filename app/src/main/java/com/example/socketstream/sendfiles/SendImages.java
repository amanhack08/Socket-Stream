package com.example.socketstream.sendfiles;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socketstream.R;
import com.example.socketstream.transferhistory.ReceiveImages;

import java.io.DataInputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

import java.util.List;


public class SendImages extends AppCompatActivity {

    protected static final int CHOOSE_FILE_RESULT_CODE = 20;
    SendImageBroadCastReceiver sendImageBroadCastReceiver;
    IntentFilter intentFilter;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel mChannel;
    WifiManager wifiManager;
    List<WifiP2pDevice> peers;
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    ListView listView;
    Button sendButton;
    WifiP2pInfo info;
    ArrayList<Uri> uri2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_images);


        Intent intent=getIntent();
        Uri uri1=intent.getData();
        if(uri1!=null&&intent.getData().toString().trim().equals("0".trim())){
            uri2 = (ArrayList<Uri>)intent.getSerializableExtra("mylist");
            Toast.makeText(this, ""+uri2.size(), Toast.LENGTH_LONG).show();
        }


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = wifiP2pManager.initialize(this, getMainLooper(), null);

        sendImageBroadCastReceiver = new SendImageBroadCastReceiver(SendImages.this, mChannel, wifiP2pManager);
        listView = (ListView) findViewById(R.id.send_image_list_view);
        peers = new ArrayList<>();


        sendButton=(Button)findViewById(R.id.send_image_button);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Intent serviceIntent = new Intent(SendImages.this, ImageTransferService.class);
                    serviceIntent.setAction(ImageTransferService.ACTION_SEND_FILE);
                    serviceIntent.putExtra(ImageTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
                            info.groupOwnerAddress.getHostAddress());
                    serviceIntent.putExtra(ImageTransferService.EXTRAS_GROUP_OWNER_PORT, 3333);
                    serviceIntent.putExtra(ImageTransferService.MY_LIST, uri2);
                    getApplicationContext().startService(serviceIntent);
            }

        });
        discoverPeers();
        onCLickListViewItem();
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(sendImageBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sendImageBroadCastReceiver);
    }

    public void discoverPeers() {
        wifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(SendImages.this, "Discovery started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {

                switch (reasonCode) {

                    case WifiP2pManager.ERROR:
                        Toast.makeText(SendImages.this, "Getting error while peers discover", Toast.LENGTH_SHORT).show();
                        break;

                    case WifiP2pManager.P2P_UNSUPPORTED:
                        Toast.makeText(SendImages.this, "Device is not supported", Toast.LENGTH_SHORT).show();
                        break;

                    case WifiP2pManager.BUSY:
                        Toast.makeText(SendImages.this, "Device is busy", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if (!wifiP2pDeviceList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());
                deviceArray = new WifiP2pDevice[peers.size()];
                deviceNameArray = new String[peers.size()];
                int i = 0;
                for (WifiP2pDevice device : peers) {
                    deviceArray[i] = device;
                    deviceNameArray[i] = device.deviceName;
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                listView.setAdapter(adapter);

            }
            if (peers.size() == 0) {
                Toast.makeText(SendImages.this, "No device found", Toast.LENGTH_SHORT).show();
            }
        }


    };

    /*
     *TO establish a connection when we click on list view item
     */
    public void onCLickListViewItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int positon, long l) {
                // Picking the first device found on the network.
                WifiP2pDevice device = deviceArray[positon];

                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                wifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
                        Toast.makeText(SendImages.this, "Connected to " + deviceNameArray[positon], Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(SendImages.this, "Connect failed. Retry.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

            info=wifiP2pInfo;
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                Toast.makeText(SendImages.this, "Host", Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);

                new FileServerAsyncTask(getApplicationContext())
                        .execute();


            } else {
                Toast.makeText(SendImages.this, "Client", Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                sendButton.setVisibility(View.VISIBLE); 
            }

        }
    };

    /**
     * A simple server socket that accepts connection and writes some data on
     * the stream.
     */
    public static class FileServerAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private Context context;

        /**
         * @param context
         */
        public FileServerAsyncTask(Context context) {
            this.context = context;

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> containsUri=new ArrayList<>();

            try {
                ServerSocket serverSocket = new ServerSocket(3333);
                Socket client = serverSocket.accept();

                //Toast.makeText(context, "accepted", Toast.LENGTH_SHORT).show();

                InputStream inputstream = client.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputstream);
                int noOfImage = dataInputStream.readInt();

                    for(int i=0;i<noOfImage;i++){
                        final File f = new File(context.getExternalFilesDir("received"),
                                "wifip2pshared-" + System.currentTimeMillis()
                                        + ".jpg");
                        File dirs = new File(f.getParent());
                        if (!dirs.exists())
                            dirs.mkdirs();
                        f.createNewFile();
                        OutputStream out=new FileOutputStream(f);
                        copyFile(inputstream, out);
                        out.close();
                        containsUri.add(f.getAbsolutePath());
                    }
                    int t=containsUri.size();

                    inputstream.close();
                    serverSocket.close();
                    return containsUri;
                } catch(IOException e){
                    return null;
                }

        }



        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result.size()>0) {

                ArrayList<Uri> uriInsertedImage=new ArrayList<Uri>();
                   for(int i=0;i<result.size();i++) {
                       Uri uri = null;


                       uri = Uri.parse("file://" + result.get(i));

                       Bitmap bitmap = null;
                       try {
                           bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                       String uriInsertedImage1 = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "IMG" + System.currentTimeMillis() + ".jpg", "" + System.currentTimeMillis());

                       if (uriInsertedImage1 != null)
                           uriInsertedImage.add(Uri.parse(uriInsertedImage1));
                   }

                Toast.makeText(context, "Image transfer succeed", Toast.LENGTH_SHORT).show();
                int t=uriInsertedImage.size();
                Intent intent = new Intent(context, ReceiveImages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("4"));
                intent.putExtra("mylist", uriInsertedImage);
                context.startActivity(intent);




            }
        }



    }
    public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            //out.close();
            //inputStream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
