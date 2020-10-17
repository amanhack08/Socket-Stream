package com.example.socketstream;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_peers_activity);
        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=wifiP2pManager.initialize(this,getMainLooper(),null);
        peers=new ArrayList<>();
        listView=(ListView)findViewById(R.id.item_list_view);
        discoversPeersBroadcastReciever=new DiscoversPeersBroadcastReciever(this,mChannel,wifiP2pManager);
        intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        discoverPeers();
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
}
