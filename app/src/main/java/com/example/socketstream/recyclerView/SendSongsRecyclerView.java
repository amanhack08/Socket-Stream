package com.example.socketstream.recyclerView;

import android.app.Activity;
import android.content.ContentUris;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.R;

import java.util.ArrayList;

public class SendSongsRecyclerView extends RecyclerView.Adapter<SendSongsRecyclerView.ViewHolder> {

    Cursor mMediaStore=null;
    Activity mActivity;
    static ArrayList<Uri> audioUri;

    //create constructor
    public SendSongsRecyclerView(Activity mActivity){
        audioUri=new ArrayList<>();
        this.mActivity=mActivity;
    }


    /*
    inflate layout
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutInflate=R.layout.send_media_recycler_list_view;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=(View)layoutInflater.inflate(layoutInflate,parent,false);
        return new SendSongsRecyclerView.ViewHolder(view);
    }


    /*
    assign song name to text view , songlength
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mMediaStore.moveToPosition(position);
        final CheckBox checkBox1=holder.getCheckBox();
        final Uri uri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mMediaStore.getInt(mMediaStore.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
        int songNameIndex= mMediaStore.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        int songLengthIndex=mMediaStore.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        String songName=mMediaStore.getString(songNameIndex);
        holder.getSongNameTextView().setText(mMediaStore.getString(songNameIndex));
        holder.getSongLengthTextView().setText(String.valueOf(mMediaStore.getInt(songLengthIndex)));
        holder.getSongImageView().setImageResource(R.drawable.songimageicon);

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView text=(TextView)view.findViewById(R.id.send_text_image_fragment_view);
                if(!checkBox1.isChecked()) {
                    checkBox1.setChecked(false);
                    for (int i = 0; i < audioUri.size(); i++) {
                        if (audioUri.get(i).toString().equals(uri.toString())) {
                            audioUri.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(mActivity, "" + audioUri.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBox1.setChecked(true);
                    audioUri.add(uri);
                    Toast.makeText(mActivity, ""+audioUri.size(), Toast.LENGTH_SHORT).show();
                }
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


    /*
    return number of songs
     */
    @Override
    public int getItemCount() {
        return mMediaStore!=null?mMediaStore.getCount():0;
    }

    /*
    function to intiliaze all type of view in our layout by calling fndviewbyid()
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView songNameTextView,songLengthTextView;
        ImageView songImageView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View v) {
            super(v);
            songNameTextView=(TextView)v.findViewById(R.id.song_name_recycler_text_view);
            songLengthTextView=(TextView)v.findViewById(R.id.song_length_recycler_text_view);
            songImageView=(ImageView)v.findViewById(R.id.audio_image_recycler_list_view);
            checkBox=(CheckBox)v.findViewById(R.id.song_checked_recycler_view);
        }

        public ImageView getSongImageView(){
            return songImageView;
        }

        public TextView getSongNameTextView(){
            return songNameTextView;
        }

        public TextView getSongLengthTextView(){
            return songLengthTextView;
        }

        public CheckBox getCheckBox(){return checkBox;}
    }


    //function to check cursor change or not
    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStore == cursor) {
            return null;
        }
        Cursor oldCursor = mMediaStore;
        this.mMediaStore = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    /*
    if cursor chenge the we have to clode the old cursor and give new value to cursor
     */
    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }


}
