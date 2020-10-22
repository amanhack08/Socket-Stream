package com.example.socketstream;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DiscoverPeers extends AppCompatActivity {
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel mChannel;
    WifiManager wifiManager;
    ListView listView;
    List<WifiP2pDevice> peers;
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    DiscoversPeersBroadcastReciever discoversPeersBroadcastReciever;
    IntentFilter intentFilter;
    static final int MESSAGE_READ=1;
    ServerClass serverClass;
    SendReceive sendReceive;
    ClientClass clientClass;
    String getDataFromPreviousActivity=null;
    Button sendTextMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_peers_activity);
        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=wifiP2pManager.initialize(this,getMainLooper(),null);
        peers=new ArrayList<>();
        discoverPeers();
        sendTextMessage=(Button)findViewById(R.id.send_button_discover_peers);
        sendTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDataFromPreviousActivity!=null) {
                    final String mesg =getDataFromPreviousActivity;
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            sendReceive.write(mesg.getBytes());
                        }
                    };

                    Thread thread=new Thread(runnable);
                    thread.start();

                }
            }
        });

        Intent intent=getIntent();
        if(intent.getData()!=null&&intent.getData().toString().length()!=0)
        getDataFromPreviousActivity=intent.getStringExtra("message");

        Toast.makeText(this, getDataFromPreviousActivity, Toast.LENGTH_SHORT).show();

        listView=(ListView)findViewById(R.id.item_list_view);

        onCLickListViewItem();
        
        discoversPeersBroadcastReciever=new DiscoversPeersBroadcastReciever(this,mChannel,wifiP2pManager);
        intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(discoversPeersBroadcastReciever,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(discoversPeersBroadcastReciever);
    }

    WifiP2pManager.PeerListListener  peerListListener=new WifiP2pManager.PeerListListener(){

        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if(!wifiP2pDeviceList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());
                deviceArray=new WifiP2pDevice[peers.size()];
                deviceNameArray=new String[peers.size()];
                int i=0;
                for(WifiP2pDevice device:peers){
                    deviceArray[i]=device;
                    deviceNameArray[i]=device.deviceName;
                    i++;
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);

            }
            if(peers.size()==0){
                Toast.makeText(DiscoverPeers.this, "No device found", Toast.LENGTH_SHORT).show();
            }
        }


    };

    /*
    *TO establish a connection when we click on list view item
     */
    public void onCLickListViewItem(){
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
                        Toast.makeText(DiscoverPeers.this,"Connected to "+deviceNameArray[positon],Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(DiscoverPeers.this, "Connect failed. Retry.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed&&wifiP2pInfo.isGroupOwner){
                Toast.makeText(DiscoverPeers.this,"Host",Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                sendTextMessage.setVisibility(View.VISIBLE);
                serverClass=new ServerClass();
                serverClass.start();

            }
            else{
                Toast.makeText(DiscoverPeers.this,"Client",Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                clientClass=new ClientClass(groupOwnerAddress);
                clientClass.start();
            }


        }
    };




    public void discoverPeers(){
        wifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(DiscoverPeers.this, "Discovery started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {

                switch(reasonCode){

                    case WifiP2pManager.ERROR:

                        Toast.makeText(DiscoverPeers.this, "Getting error while peers discover", Toast.LENGTH_SHORT).show();
                        break;

                    case WifiP2pManager.P2P_UNSUPPORTED:
                        Toast.makeText(DiscoverPeers.this, "Device is not supported", Toast.LENGTH_SHORT).show();
                        break;


                    case WifiP2pManager.BUSY:
                        Toast.makeText(DiscoverPeers.this, "Device is busy", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }

    /*
    Creates a server socket class which response back to client
     */
    public class ServerClass extends Thread{
        ServerSocket serverSocket;
        Socket socket;

        @Override
        public void run() {
            try {
                serverSocket=new ServerSocket(3333);
                socket=serverSocket.accept();
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    client side class which send request to server
     */
    public class ClientClass extends Thread{
        String hostAddress;
        Socket socket;
        public ClientClass(InetAddress hostAddres){
            this.hostAddress=hostAddres.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress,3333),500);
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case MESSAGE_READ:
                    byte[] readByte=(byte[])message.obj;
                    String tempMsg=new String(readByte,0,message.arg1);
                    Toast.makeText(DiscoverPeers.this, tempMsg, Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;


        public SendReceive(Socket sock){
            this.socket=sock;
            try{
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer=new byte[1024];
            int bytes;
            while(socket!=null){
                try{
                    bytes=inputStream.read(buffer);
                    if(bytes>0){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
