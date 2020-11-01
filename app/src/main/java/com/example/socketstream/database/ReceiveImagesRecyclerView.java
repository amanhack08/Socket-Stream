package com.example.socketstream.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;
import com.example.socketstream.recyclerView.SendAppRecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class ReceiveImagesRecyclerView extends RecyclerView.Adapter<ReceiveImagesRecyclerView.ViewHolder> {
    ArrayList<Uri> imageUri;
    ContentResolver cr;

    public ReceiveImagesRecyclerView(ArrayList<Uri> uri, ContentResolver cr){
        this.cr=cr;
        imageUri=new ArrayList<>(uri);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.receive_images_recycler_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri=imageUri.get(position);
        Cursor cursor = cr.query(uri, null, null, null, null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        Bitmap bitmap= MediaStore.Images.Thumbnails.getThumbnail(cr,id,MediaStore.Images.Thumbnails.MICRO_KIND,null);
        holder.getImageView().setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return imageUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.receive_image_recycler_image_view);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }



}
