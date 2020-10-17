package com.example.socketstream;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionRequiredTransfer extends AppCompatActivity {
    Button wifiButton,locationButton,hotspotButton,bluetoothButton,sendOrReceiveButton;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel mChannel;
    WifiManager wifiManager;
    SocketStreamBroadcastReceiver socketStreamBroadcastReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_required_transfer);
        Intent intent=getIntent();

        getSupportActionBar().hide();
        initializeAllButtons();
        if(intent.getData()!=null){
            sendOrReceiveButton.setText("Receive");
        }
        socketStreamBroadcastReceiver=new SocketStreamBroadcastReceiver(  PermissionRequiredTransfer.this,mChannel,wifiP2pManager);
        intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        onClickButtonEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(socketStreamBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(socketStreamBroadcastReceiver);
    }

    public void initializeAllButtons(){
        wifiButton=(Button)findViewById(R.id.wifi_on_off_permission_button);
        locationButton=(Button)findViewById(R.id.location_on_off_permission);
        bluetoothButton=(Button)findViewById(R.id.bluetooth_on_off_permission);
        hotspotButton=(Button)findViewById(R.id.hotspot_on_off_permission);
        sendOrReceiveButton=(Button)findViewById(R.id.discover_peers_button);
        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiP2pManager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=wifiP2pManager.initialize(this,getMainLooper(),null);
    }
    public void runTimePermission(){
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_WIFI_STATE
        };
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                0);
    }

    public void runTimePermission1(){
        String[] PERMISSIONS = {
                Manifest.permission.CHANGE_WIFI_STATE
        };
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                0);
    }

    public void onClickButtonEventListener(){
        wifiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runTimePermission();
                runTimePermission1();

                if(!wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                    wifiButton.setText("OFF");
                }
                else{
                    wifiManager.setWifiEnabled(false);
                    wifiButton.setText("ON");
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
                LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(GpsStatus == true) {
                    locationButton.setText("OFF");
                } else {
                    locationButton.setText("ON");
                }
            }
        });

        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        hotspotButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        sendOrReceiveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PermissionRequiredTransfer.this,DiscoverPeers.class);
                startActivity(intent);
            }
        });
    }
}
