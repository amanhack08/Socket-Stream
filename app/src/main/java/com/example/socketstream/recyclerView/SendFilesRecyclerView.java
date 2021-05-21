package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import com.example.socketstream.SendFilesViewPager;
import com.example.socketstream.connect.Helper;

import java.io.File;
import java.util.ArrayList;

public class SendFilesRecyclerView extends RecyclerView.Adapter<SendFilesRecyclerView.ViewHolder> {
    Cursor mMediaStoreCursor=null;
    ArrayList<File> filesUri;
    ArrayList<Uri> seleFileUri,checkedFileUri;
    Activity mActivity;
    SharedPreferences sharedPreferences;
    public SendFilesRecyclerView(ArrayList<File> uri, Activity mActivity){
        filesUri=new ArrayList<>(uri);
        seleFileUri=new ArrayList<>();
        this.mActivity=mActivity;
        sharedPreferences=mActivity.getSharedPreferences(SendFilesViewPager.SELECT_ITEMS,Context.MODE_PRIVATE);
        seleFileUri=SendFilesViewPager.getArrayList(SendFilesViewPager.FILES,sharedPreferences);
        if(seleFileUri==null)
            seleFileUri=new ArrayList<>();
        checkedFileUri=SendFilesViewPager.getArrayList(SendFilesViewPager.FILES,sharedPreferences);
        if(checkedFileUri==null)
            checkedFileUri=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.send_files_recycler_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new SendFilesRecyclerView.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file=filesUri.get(position);
        holder.getFileNameTextView().setText(file.getName());
        final Uri uri=Uri.fromFile(file);
        //final Uri uri=Uri.fromFile(file);
        if(file.getName().endsWith(".docx")){
            holder.getImageView().setImageResource(R.drawable.docx);
        }else if(file.getName().endsWith(".pdf")){
            holder.getImageView().setImageResource(R.drawable.pdf);
        }else if(file.getName().endsWith(".odt")){
            holder.getImageView().setImageResource(R.drawable.odt);
        }else if(file.getName().endsWith(".pptx")){
            holder.getImageView().setImageResource(R.drawable.ppt);
        }else if(file.getName().endsWith(".zip")){
            holder.getImageView().setImageResource(R.drawable.zip);
        }
        final CheckBox checkBox1=holder.getCheckBox();
        for(int i=0;i<checkedFileUri.size();i++){
            if(checkedFileUri.get(i).toString().equals(uri.toString())){
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
                    for(int i=0;i<seleFileUri.size();i++){
                        if(seleFileUri.get(i).toString().equals(uri.toString())){
                            seleFileUri.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(mActivity, ""+seleFileUri.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBox1.setChecked(true);
                    seleFileUri.add(uri);
                    Toast.makeText(mActivity, ""+seleFileUri.size(), Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> arrayList=new ArrayList<>();
                for(int i=0;i<seleFileUri.size();i++)
                    arrayList.add(seleFileUri.get(i).toString());
                SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.FILES,sharedPreferences);
            }
        });
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                if(!checkBox1.isChecked()) {
                    checkBox1.setChecked(false);
                    for (int i = 0; i < seleFileUri.size(); i++) {
                        if (seleFileUri.get(i).toString().equals(uri.toString())) {
                            seleFileUri.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(mActivity, "" + seleFileUri.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBox1.setChecked(true);
                    seleFileUri.add(uri);
                    Toast.makeText(mActivity, uri+" "+"g "+new Helper(mActivity).getNameFromURI(uri), Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> arrayList=new ArrayList<>();
                for(int i=0;i<seleFileUri.size();i++)
                    arrayList.add(seleFileUri.get(i).toString());
                SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.FILES,sharedPreferences);
            }
        });

       // holder.getImageView().setImageURI(Uri.parse(file.getAbsolutePath()));
    }

    @Override
    public int getItemCount() {
        return filesUri.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView fileNameTextView;


        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=(ImageView)v.findViewById(R.id.file_image_recycler_view);
            checkBox=(CheckBox)v.findViewById(R.id.file_checked_recycler_view);
            fileNameTextView=(TextView)v.findViewById(R.id.file_name_recycler_view);

        }

        public ImageView getImageView(){
            return imageView;
        }

        public CheckBox getCheckBox(){
            return checkBox;
        }

        public TextView getFileNameTextView(){
            return  fileNameTextView;
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
        return seleFileUri;
    }
}
