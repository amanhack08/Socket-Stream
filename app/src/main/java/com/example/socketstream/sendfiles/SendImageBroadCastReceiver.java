package com.example.socketstream.sendfiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;
import com.example.socketstream.sendfiles.SendImages;

import com.example.socketstream.DiscoverPeers;

public class SendImageBroadCastReceiver extends BroadcastReceiver {
    WifiP2pManager p2pManager;
    WifiP2pManager.Channel channel;
    SendImages mainActivity;

    public SendImageBroadCastReceiver(SendImages mainActivity, WifiP2pManager.Channel channel,WifiP2pManager p2pManager){
        this.mainActivity=mainActivity;
        this.channel=channel;
        this.p2pManager=p2pManager;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if(p2pManager!=null){
                p2pManager.requestPeers(channel,mainActivity.peerListListener);
            }

        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if(p2pManager==null)
                return;
            NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()){
                p2pManager.requestConnectionInfo(channel,mainActivity.connectionInfoListener);
            }
            else{
                Toast.makeText(mainActivity, "Is is disconnected", Toast.LENGTH_SHORT).show();
            }

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

        }
    }
}
