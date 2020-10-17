package com.example.socketstream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class SocketStreamBroadcastReceiver extends BroadcastReceiver {

    WifiP2pManager p2pManager;
    WifiP2pManager.Channel channel;
    PermissionRequiredTransfer mainActivity;

    public SocketStreamBroadcastReceiver(PermissionRequiredTransfer mainActivity, WifiP2pManager.Channel channel,WifiP2pManager p2pManager){
        this.mainActivity=mainActivity;
        this.channel=channel;
        this.p2pManager=p2pManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context,"Wifi is on",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Wifi is off",Toast.LENGTH_SHORT).show();
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
//            if(p2pManager!=null){
//                p2pManager.requestPeers(channel,mainActivity.peerListListener);
//            }

        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){

        }
    }
}
