package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;
import com.example.socketstream.SendAppsStorage;
import com.example.socketstream.SendFilesViewPager;
import com.example.socketstream.connect.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendAppRecyclerView extends RecyclerView.Adapter<SendAppRecyclerView.ViewHolder> {

    Cursor mMediaStoreCursor=null;
    Activity mActivity;
    List<ApplicationInfo> installedApps;
    ArrayList<Uri> appUri,checkedAppUri;
    SharedPreferences sharedPreferences;
    public SendAppRecyclerView(Activity mActivity, List<ApplicationInfo> list){
        this.mActivity=mActivity;
        installedApps=new ArrayList<>(list);
        appUri=new ArrayList<>();
        sharedPreferences=mActivity.getSharedPreferences(SendFilesViewPager.SELECT_ITEMS,Context.MODE_PRIVATE);
        appUri=SendFilesViewPager.getArrayList(SendFilesViewPager.APPS,sharedPreferences);
        if(appUri==null)
            appUri=new ArrayList<>();
        checkedAppUri=SendFilesViewPager.getArrayList(SendFilesViewPager.APPS,sharedPreferences);
        if(checkedAppUri==null)
            checkedAppUri=new ArrayList<>();
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
        //PackageInfo object=installedApps.get(position);
        ApplicationInfo pkg= installedApps.get(position);

        holder.getImageView().setImageDrawable(pkg.loadIcon(SendAppsStorage.packageManager));
        holder.getAppNameTextView().setText(pkg.loadLabel(SendAppsStorage.packageManager));
       // holder.getAppSizeTextView().setText(pkg.);


        final Uri uri;
        //Toast.makeText(this, app.sourceDir, Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            uri = Uri.parse(pkg.sourceDir);
            //uriList.add(uri.toString());
           // Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        } else{
           // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            uri = Uri.fromFile(new File(pkg.sourceDir));
            //uriList.add(uri.toString());
           // Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        }
        final CheckBox checkBox1=holder.getCheckBox();
        for(int i=0;i<checkedAppUri.size();i++){
            if(checkedAppUri.get(i).toString().equals(uri.toString())){
                checkBox1.setChecked(true);
                break;
            }
        }
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CheckBox checkBox=(CheckBox)view.findViewById(R.id.send_image_recycler_check_box);
                //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                if(checkBox1.isChecked()){
                    checkBox1.setChecked(false);
                    for(int i=0;i<appUri.size();i++){
                        if(appUri.get(i).toString().equals(uri.toString())){
                            appUri.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(mActivity, ""+appUri.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBox1.setChecked(true);
                    appUri.add(uri);
                    Toast.makeText(mActivity, ""+uri, Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> arrayList=new ArrayList<>();
                for(int i=0;i<appUri.size();i++)
                    arrayList.add(appUri.get(i).toString());
                SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.APPS,sharedPreferences);
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                if(!checkBox1.isChecked()) {
                    checkBox1.setChecked(false);
                    for (int i = 0; i < appUri.size(); i++) {
                        if (appUri.get(i).toString().equals(uri.toString())) {
                            appUri.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(mActivity, "" + appUri.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBox1.setChecked(true);
                    appUri.add(uri);
                    Toast.makeText(mActivity, uri+" and "+new Helper(mActivity).getNameFromURI(uri), Toast.LENGTH_SHORT).show();
                   // Toast.makeText(mActivity, ""+appUri.size(), Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> arrayList=new ArrayList<>();
                for(int i=0;i<appUri.size();i++)
                    arrayList.add(appUri.get(i).toString());
                SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.APPS,sharedPreferences);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
    public ArrayList<Uri> getArrayListUri(){
        return appUri;
    }

}
