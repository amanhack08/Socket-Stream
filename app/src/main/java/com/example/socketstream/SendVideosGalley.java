package com.example.socketstream;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.socketstream.recyclerView.AboutVideo;
import com.example.socketstream.recyclerView.SendImageRecyclerView;
import com.example.socketstream.recyclerView.SendVideoRecyclerView;


import java.util.ArrayList;

public class SendVideosGalley extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    View v;
    SendVideoRecyclerView videosAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.send_videos_gallery, container, false);
        videosAdapter=new SendVideoRecyclerView(getActivity());
        getLoaderManager().initLoader(0,null,this);
        RecyclerView recyclerView=v.findViewById(R.id.video_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),3));
        recyclerView.setAdapter(videosAdapter);
        return v;
    }



//    public void loadVideosFromStorage() {
//
//        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
//                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
//        ///////////////////////////////////////////////////
//        Cursor cursor = cr.query(MediaStore.Files.getContentUri("external"), null, selection, null, null);
//        //////////////////////////////////////////////////
//        if (cursor != null) {
//            int t1=cursor.getCount();
//            cursor.moveToFirst();
//            int size1=0;
//
//            do {
//                Bitmap uri1 = MediaStore.Video.Thumbnails.getThumbnail(cr, cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)), MediaStore.Video.Thumbnails.MICRO_KIND, null);
//                int colIndex=cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE);
//                int size=cursor.getInt(colIndex);
//                size1+=size;
//                AboutVideo aboutVideo=new AboutVideo(uri1,size);
//                aboutVideos.add(aboutVideo);
//            } while (cursor.moveToNext());
//            int t=size1;
//            cursor.close();
//            RecyclerView recyclerView=v.findViewById(R.id.video_recycler_view);
//            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),3));
//            SendVideoRecyclerView sendVideoRecyclerView=new SendVideoRecyclerView(aboutVideos);
//            recyclerView.setAdapter(sendVideoRecyclerView);
//
//        }
//
//    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return new CursorLoader(getActivity().getApplicationContext(),MediaStore.Files.getContentUri("external")
        ,null
        ,selection
        ,null
        ,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        videosAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        videosAdapter.changeCursor(null);
    }
}
