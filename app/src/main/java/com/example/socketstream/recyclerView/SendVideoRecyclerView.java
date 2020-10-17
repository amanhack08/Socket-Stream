package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socketstream.recyclerView.AboutVideo;

import com.example.socketstream.R;

import java.util.ArrayList;

public class SendVideoRecyclerView extends RecyclerView.Adapter<SendVideoRecyclerView.ViewHolder> {


    Cursor mMediaStoreCursor=null;
    Activity mActivity;

    public SendVideoRecyclerView(Activity mActivity){
        this.mActivity=mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.send_video_recycler_list_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap=getBitmapFromMediaStore(position);
        int colSize=mMediaStoreCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);
        int size=mMediaStoreCursor.getInt(colSize);
        if(bitmap!=null){
            holder.getImageView().setImageBitmap(bitmap);
            holder.getTextView().setText(String.valueOf(size));
        }

    }

    @Override
    public int getItemCount() {
        return mMediaStoreCursor==null? 0 : mMediaStoreCursor.getCount();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.send_video_recycler_image_view);
            checkBox=(CheckBox)itemView.findViewById(R.id.send_video_recycler_check_box);
            textView=(TextView)itemView.findViewById(R.id.video_duration_recycler_view);
        }

        public ImageView getImageView(){
            return  imageView;
        }

        public  TextView getTextView(){
            return textView;
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
//            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
//                return MediaStore.Images.Thumbnails.getThumbnail(
//                        mActivity.getContentResolver(),
//                        mMediaStoreCursor.getLong(idIndex),
//                        MediaStore.Images.Thumbnails.MICRO_KIND,
//                        null
//                );
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                return MediaStore.Video.Thumbnails.getThumbnail(
                        mActivity.getContentResolver(),
                        mMediaStoreCursor.getLong(idIndex),
                        MediaStore.Video.Thumbnails.MICRO_KIND,
                        null
                );
            default:
                return null;
        }
    }
}
