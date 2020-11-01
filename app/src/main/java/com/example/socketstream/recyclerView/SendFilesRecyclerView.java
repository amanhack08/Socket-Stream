package com.example.socketstream.recyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;

public class SendFilesRecyclerView extends RecyclerView.Adapter<SendFilesRecyclerView.ViewHolder> {
    Cursor mMediaStoreCursor=null;
    ArrayList<File> filesUri;

    public SendFilesRecyclerView(ArrayList<File> uri){
        filesUri=new ArrayList<>(uri);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file=filesUri.get(position);
        holder.getFileNameTextView().setText(file.getName());
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
}
