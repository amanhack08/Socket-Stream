package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;

import java.util.ArrayList;
import java.util.BitSet;

public class SendImageRecyclerView extends RecyclerView.Adapter<SendImageRecyclerView.ViewHolder>{


    ArrayList<Bitmap> imageBitmap;
    Cursor mMediaStoreCursor=null;

    CheckBox checkBox;
    Activity mActivity;

    public SendImageRecyclerView(Activity mActivity){
        this.mActivity=mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.send_image_recycler_list_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = getBitmapFromMediaStore(position);
        if (bitmap != null) {
            holder.getImageView().setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return mMediaStoreCursor==null?0 : mMediaStoreCursor.getCount();
    }



    /*
    Creates a view holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=(ImageView)v.findViewById(R.id.send_image_recycler_image_view);
            checkBox=(CheckBox)v.findViewById(R.id.send_image_recycler_check_box);
        }
        public ImageView getImageView(){
            return  imageView;
        }

    }

    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
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

    private Bitmap getBitmapFromMediaStore(int position) {
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        mMediaStoreCursor.moveToPosition(position);
        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                return MediaStore.Images.Thumbnails.getThumbnail(
                        mActivity.getContentResolver(),
                        mMediaStoreCursor.getLong(idIndex),
                        MediaStore.Images.Thumbnails.MICRO_KIND,
                        null
                );
//            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
//                return MediaStore.Video.Thumbnails.getThumbnail(
//                        mActivity.getContentResolver(),
//                        mMediaStoreCursor.getLong(idIndex),
//                        MediaStore.Video.Thumbnails.MICRO_KIND,
//                        null
//                );
            default:
                return null;
        }
    }
}
