package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;
import com.example.socketstream.SendFilesViewPager;
import com.example.socketstream.connect.Helper;

import java.util.ArrayList;
import java.util.BitSet;

public class SendImageRecyclerView extends RecyclerView.Adapter<SendImageRecyclerView.ViewHolder>{



    Cursor mMediaStoreCursor=null;

    static ArrayList<Uri> imageUri,checkedImageUri;
    SharedPreferences sharedPreferences;


    Activity mActivity;

    //public constructor
    public SendImageRecyclerView(Activity mActivity){
        this.mActivity=mActivity;
        imageUri=new ArrayList<>();
        sharedPreferences=mActivity.getSharedPreferences(SendFilesViewPager.SELECT_ITEMS,Context.MODE_PRIVATE);
        imageUri=SendFilesViewPager.getArrayList(SendFilesViewPager.IMAGES,sharedPreferences);
        if(imageUri==null)
            imageUri=new ArrayList<>();
        checkedImageUri=SendFilesViewPager.getArrayList(SendFilesViewPager.IMAGES,sharedPreferences);
        if(checkedImageUri==null)
            checkedImageUri=new ArrayList<>();
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

    /*
    give value to layout widgets
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = getBitmapFromMediaStore(position);
        final CheckBox checkBox1=holder.getCheckBox();
        //in some cases, it will prevent unwanted situations
        //checkBox1.setOnCheckedChangeListener(null);
        mMediaStoreCursor.moveToPosition(position);
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int id=mMediaStoreCursor.getInt(idIndex);
        final Uri uri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id);
        for(int i=0;i<checkedImageUri.size();i++){
            if(checkedImageUri.get(i).toString().equals(uri.toString())){
                checkBox1.setChecked(true);
                break;
            }
        }
        if (bitmap != null) {
            holder.getImageView().setImageBitmap(bitmap);
            holder.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // CheckBox checkBox=(CheckBox)view.findViewById(R.id.send_image_recycler_check_box);
                    //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                    if(checkBox1.isChecked()){
                        checkBox1.setChecked(false);
                        for(int i=0;i<imageUri.size();i++){
                            if(imageUri.get(i).toString().equals(uri.toString())){
                                imageUri.remove(i);
                                break;
                            }
                        }
                        Toast.makeText(mActivity, ""+imageUri.size(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        checkBox1.setChecked(true);
                        imageUri.add(uri);
                        Toast.makeText(mActivity, ""+imageUri.size(), Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<String> arrayList=new ArrayList<>();
                    for(int i=0;i<imageUri.size();i++)
                        arrayList.add(imageUri.get(i).toString());
                    SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.IMAGES,sharedPreferences);

                }
            });

            checkBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                    if(!checkBox1.isChecked()) {
                        checkBox1.setChecked(false);
                        for (int i = 0; i < imageUri.size(); i++) {
                            if (imageUri.get(i).toString().equals(uri.toString())) {
                                imageUri.remove(i);
                                break;
                            }
                        }
                        Toast.makeText(mActivity, "" + imageUri.size(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        checkBox1.setChecked(true);
                        imageUri.add(uri);
                        Toast.makeText(mActivity, ""+new Helper(mActivity).getNameFromURI(uri), Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<String> arrayList=new ArrayList<>();
                    for(int i=0;i<imageUri.size();i++)
                        arrayList.add(imageUri.get(i).toString());
                    SendFilesViewPager.saveArrayList(arrayList,SendFilesViewPager.IMAGES,sharedPreferences);
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

    //return the number of images
    @Override
    public int getItemCount() {
        return mMediaStoreCursor==null? 0 : mMediaStoreCursor.getCount();
    }



    /*
    Creates a view holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=(ImageView)v.findViewById(R.id.send_image_recycler_image_view);
            checkBox=(CheckBox)v.findViewById(R.id.send_image_recycler_check_box);
        }
        /*
        function which return imageview
         */
        public ImageView getImageView(){
            return  imageView;
        }
        public CheckBox getCheckBox(){
            return checkBox;
        }

    }

    /*
    if cursor changes new value assign to mMediaStoreCursor
     */
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

    /*
    if cursor change then we close the oldcursor
    and assign new value to mMediaStoreCursor after calling swapCursor
     */
    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    /*
    return bitmap of an image which the cursor is pointed
     */
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

    /*
    function to return an ArrayList of uri
     */
    public static ArrayList<Uri> getArrayListUri(){
        return imageUri;
    }
}
