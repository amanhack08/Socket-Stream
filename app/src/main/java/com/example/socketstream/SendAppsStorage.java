package com.example.socketstream;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.recyclerView.SendAppRecyclerView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SendAppsStorage extends Fragment  {

    View v;
    //List<PackageInfo> installedApps;
    //List<PackageInfo> getApps;
    List<ApplicationInfo> getApps;
    SendAppRecyclerView sendAppRecyclerView;
    public static PackageManager packageManager = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.send_apps_storage,container,false);

        //installedApps();
        packageManager=getActivity().getPackageManager();
        getApps = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        sendAppRecyclerView=new SendAppRecyclerView(getActivity(),getApps);



        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.app_recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),5));

        recyclerView.setAdapter(sendAppRecyclerView);

        return v;
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo info : list) {

            try {

                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    applist.add(info);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applist;
    }


//    public void installedApps(){
//        getApps=new ArrayList<>();
//        installedApps=getActivity().getPackageManager().getInstalledPackages(0);
//        for(int i=0;i<installedApps.size();i++){
//            PackageInfo packageInfo=installedApps.get(i);
//            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
//                getApps.add(packageInfo);
//            }
//        }
//
//    }
}
