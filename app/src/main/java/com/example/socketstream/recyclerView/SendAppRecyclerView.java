package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;

import java.util.ArrayList;
import java.util.List;

public class SendAppRecyclerView extends RecyclerView.Adapter<SendAppRecyclerView.ViewHolder> {

    Cursor mMediaStoreCursor=null;
    Activity mActivity;
    List<PackageInfo> installedApps;

    public SendAppRecyclerView(Activity mActivity, List<PackageInfo> list){
        this.mActivity=mActivity;
        installedApps=new ArrayList<>(list);
    }

    @NonNull
    @Override
    public SendAppRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.send_app_recycler_list_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new SendAppRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendAppRecyclerView.ViewHolder holder, int position) {
        PackageInfo object=installedApps.get(position);
        String appName=object.applicationInfo.loadLabel(mActivity.getPackageManager()).toString();
        String version=object.versionName;
        holder.getAppNameTextView().setText(appName);
        holder.getAppSizeTextView().setText(version);
        holder.getImageView().setImageDrawable(object.applicationInfo.loadIcon(mActivity.getPackageManager()));


    }

    @Override
    public int getItemCount() {
        return installedApps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView appNameTextView;
        TextView appSizeTextView;
        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=(ImageView)v.findViewById(R.id.app_image_recycler_view);
            checkBox=(CheckBox)v.findViewById(R.id.app_checked_recycler_view);
            appNameTextView=(TextView)v.findViewById(R.id.app_name_recycler_view);
            appSizeTextView=(TextView)v.findViewById(R.id.app_size_recycler_view);
        }

        public ImageView getImageView(){
            return imageView;
        }

        public CheckBox getCheckBox(){
            return checkBox;
        }

        public TextView getAppNameTextView(){
            return  appNameTextView;
        }

        public TextView getAppSizeTextView(){
            return appSizeTextView;
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
        int t=cursor.getCount();
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

}
