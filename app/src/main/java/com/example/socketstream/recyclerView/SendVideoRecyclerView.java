package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
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

import com.example.socketstream.SendFilesViewPager;
import com.example.socketstream.connect.Helper;
import com.example.socketstream.recyclerView.AboutVideo;

import com.example.socketstream.R;

import java.util.ArrayList;

public class SendVideoRecyclerView extends RecyclerView.Adapter<SendVideoRecyclerView.ViewHolder> {


    Cursor mMediaStoreCursor=null;
    Activity mActivity;
    ArrayList<Uri> videoUri,checkedVideoUri;
    SharedPreferences sharedPreferences;
    public SendVideoRecyclerView(Activity mActivity){
        this.mActivity=mActivity;
        videoUri=new ArrayList<>();
        sharedPreferences=mActivity.getSharedPreferences(SendFilesViewPager.SELECT_ITEMS,Context.MODE_PRIVATE);
        videoUri=SendFilesViewPager.getArrayList(SendFilesViewPager.VIDEOS,sharedPreferences);
        if(videoUri==null)
            videoUri=new ArrayList<>();
        checkedVideoUri=SendFilesViewPager.getArrayList(SendFilesViewPager.VIDEOS,sharedPreferences);
        if(checkedVideoUri==null)
            checkedVideoUri=new ArrayList<>();
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
        int colSize=mMediaStoreCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        String size=mMediaStoreCursor.getString(colSize);
        final CheckBox checkBox=holder.getCheckBox();
        mMediaStoreCursor.moveToPosition(position);
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Video.Media._ID);
        int id=mMediaStoreCursor.getInt(idIndex);
        final Uri uri1= ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id);
        //final Uri uri=getUri(uri1);
        final Uri uri=uri1;
        for(int i=0;i<checkedVideoUri.size();i++){
            if(checkedVideoUri.get(i).toString().equals(uri.toString())){
                checkBox.setChecked(true);
                break;
            }
        }
        if(bitmap!=null){
            holder.getImageView().setImageBitmap(bitmap);
            holder.getTextView().setText(String.valueOf(size));
            holder.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if(!checkBox.isChecked()) {
                            checkBox.setChecked(false);
                            for (int i = 0; i < videoUri.size(); i++) {
                                if (videoUri.get(i).toString().trim().equals(uri.toString().trim())) {
                                    videoUri.remove(i);
                                    break;
                                }
                            }
                            Toast.makeText(mActivity, "" + videoUri.size(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            checkBox.setChecked(true);
                            videoUri.add(uri);
                            Toast.makeText(mActivity, ""+videoUri.size(), Toast.LENGTH_SHORT).show();
                        }
                    ArrayList<String> arrayList=new ArrayList<>();
                    for(int i=0;i<videoUri.size();i++)
                        arrayList.add(videoUri.get(i).toString());
                    SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.VIDEOS,sharedPreferences);
                    }

            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                    if(!checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        for (int i = 0; i < videoUri.size(); i++) {
                            if (videoUri.get(i).toString().trim().equals(uri.toString().trim())) {
                                videoUri.remove(i);
                                break;
                            }
                        }
                        Toast.makeText(mActivity, "" + videoUri.size(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        checkBox.setChecked(true);
                        videoUri.add(uri);

                        Toast.makeText(mActivity, ""+new Helper(mActivity).getNameFromURI(uri), Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<String> arrayList=new ArrayList<>();
                    for(int i=0;i<videoUri.size();i++)
                        arrayList.add(videoUri.get(i).toString());
                    SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.VIDEOS,sharedPreferences);
                }
            });
        }

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
        public CheckBox getCheckBox(){
            return checkBox;
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
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Video.Media._ID);
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        mMediaStoreCursor.moveToPosition(position);
        //switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
//            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
//                return MediaStore.Images.Thumbnails.getThumbnail(
//                        mActivity.getContentResolver(),
//                        mMediaStoreCursor.getLong(idIndex),
//                        MediaStore.Images.Thumbnails.MICRO_KIND,
//                        null
//                );
           // case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                return MediaStore.Video.Thumbnails.getThumbnail(
                        mActivity.getContentResolver(),
                        mMediaStoreCursor.getLong(idIndex),
                        MediaStore.Video.Thumbnails.MICRO_KIND,
                        null);
              //  );
            //default:
              //  return null;

    }
    public ArrayList<Uri> getArrayListUri(){
        return videoUri;
    }
}
